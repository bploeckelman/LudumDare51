package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.Config;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.Calc;
import lando.systems.ld51.utils.VectorPool;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static lando.systems.ld51.Main.game;

public class Enemy implements Steerable<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    private final GameScreen screen;

    public final CreatureAnims.Type type;

    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;

    // gdx-ai steering related
    private final Vector2 position;
    private final Vector2 prevPosition;
    private final Vector2 linearVelocity;
    private float orientation;
    private float angularVelocity;
    private SteeringBehavior<Vector2> steeringBehavior;
    private boolean independentFacing;
    private boolean tagged;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;

    public Circle hurtCircle;  // NOTE - this is for broad phase simple checks
    public Polygon hurtShape; // NOTE - this is for narrow phase convex poly/poly checks
    private float hurtDuration;
    private float hurtTimer;
    private boolean isHurt;

    public float health;
    public float size;

    public Enemy(GameScreen screen, CreatureAnims.Type type, float x, float y) {
        this.screen = screen;
        this.type = type;
        this.health = type.health;
        this.maxLinearSpeed = type.maxLinearSpeed;
        this.maxLinearAcceleration = type.maxLinearAccel;
        this.maxAngularSpeed = type.maxAngularSpeed;
        this.maxAngularAcceleration = type.maxAngularAccel;
        this.size = 50f;

        this.animation = screen.assets.creatureAnims.get(type);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;

        this.position = VectorPool.vec2.obtain().set(x,  y);
        this.prevPosition = VectorPool.vec2.obtain().set(x, y);
        this.linearVelocity = VectorPool.vec2.obtain().set(0, 0);
        this.orientation = 0f;
        this.angularVelocity = 0f;
        this.independentFacing = false;
        this.steeringBehavior = null;

        this.hurtCircle = new Circle(position.x, position.y, size / 2f);

        int numVerts = 10;
        float[] vertices = new float[2 * numVerts];
        for (int i = 0, angle = 0; i < vertices.length; i += 2) {
            vertices[i+0] = position.x + MathUtils.cosDeg(angle) * size / 2f;
            vertices[i+1] = position.y + MathUtils.sinDeg(angle) * size / 2f;
            angle += 360/numVerts;
        }
        this.hurtShape = new Polygon(vertices);
        this.isHurt = false;
        this.hurtDuration = 0.25f;
        this.hurtTimer = hurtDuration;
    }

    public void update(float dt) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            /*
             * Here you might want to add a motor control layer filtering steering accelerations.
             * For instance, a car in a driving game has physical constraints on its movement:
             * - it cannot turn while stationary
             * - the faster it moves, the slower it can turn (without going into a skid)
             * - it can brake much more quickly than it can accelerate
             * - it only moves in the direction it is facing (ignoring power slides)
             */
            applySteering(steeringOutput, dt);
        }

        hurtCircle.setPosition(position);
        hurtShape.translate(position.x - prevPosition.x, position.y - prevPosition.y);
        if (isHurt) {
            hurtTimer -= dt;
            if (hurtTimer <= 0) {
                hurtTimer = hurtDuration;
                isHurt = false;
            }
        }

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
        prevPosition.set(position);
        position.mulAdd(linearVelocity, delta);
        linearVelocity.mulAdd(steering.linear, delta).limit(this.getMaxLinearSpeed());

        if (independentFacing) {
            orientation += angularVelocity * delta;
            angularVelocity += steering.angular * delta;
        } else {
            // for non-independent facing, align orientation to linear velocity
            float newOrientation = vectorToAngle(linearVelocity);
            if (newOrientation != orientation) {
                angularVelocity = (newOrientation - orientation);
                orientation = newOrientation;
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, position.x - (size / 2f), position.y - (size / 2f), size, size);

        if (isHurt) {
            Animation<TextureRegion> flashAnimation = screen.assets.creatureAnims.getFlash(type);
            TextureRegion flashKeyframe = flashAnimation.getKeyFrame(stateTime);
            batch.draw(flashKeyframe, position.x - (size / 2f), position.y - (size / 2f), size, size);
        }

        if (Config.Debug.general) {
            ShapeDrawer shapes = screen.assets.shapes;
            shapes.setColor(Color.MAGENTA);
            shapes.circle(hurtCircle.x, hurtCircle.y, hurtCircle.radius, 2f);
            shapes.setColor(Color.CORAL);
            shapes.polygon(hurtShape);
            shapes.setColor(Color.WHITE);
        }
    }

    public void hurt(float amount, float dirX, float dirY) {
        if (isHurt) return;
        isHurt = true;

        if(screen.player.isWizard) {
            game.audio.playSound(AudioManager.Sounds.scorch, 0.25f);
        } else {
            game.audio.playSound(AudioManager.Sounds.impact, 0.125f);
        }

        health -= amount;

        // bounce back
        float bounceBackAmount = 50f;
        prevPosition.set(position);
        position.add(dirX * bounceBackAmount, dirY * bounceBackAmount);
        hurtCircle.setPosition(position);
        hurtShape.translate(position.x - prevPosition.x, position.y - prevPosition.y);
    }

    public boolean isDead() {
        return (health <= 0f);
    }

    public void kill() {
        screen.particles.explode(position.x, position.y, size);
        int gemsToSpawn = MathUtils.random(5, 9);
        for (int i = 0; i < gemsToSpawn; i++) {
            screen.gems.add(new Gem(screen, position, type.gemColor));
        }

        game.audio.playSound(AudioManager.Sounds.die, 0.5F);

        VectorPool.vec2.free(position);
        VectorPool.vec2.free(prevPosition);
        VectorPool.vec2.free(linearVelocity);

    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    // ------------------------------------------------------------------------
    // Steerable implementation
    // ------------------------------------------------------------------------

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return hurtCircle.radius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    // ------------------------------------------------------------------------
    // Location implementation
    // ------------------------------------------------------------------------

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Calc.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Calc.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new ObjectLocation();
    }

    // ------------------------------------------------------------------------
    // Limiter implementation
    // ------------------------------------------------------------------------

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

}
