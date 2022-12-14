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
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.ui.Stats;
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
    private Color hurtFlashColor;

    public float health;
    public float size;

    private static final float ENEMY_SIZE = 70F;
    private static final float ENEMY_SIZE_MAX = 200F;

    public Enemy(GameScreen screen, CreatureAnims.Type type, float x, float y) {
        this.screen = screen;
        this.type = type;
        this.health = type.health;
        this.maxLinearSpeed = type.maxLinearSpeed;
        this.maxLinearAcceleration = type.maxLinearAccel;
        this.maxAngularSpeed = type.maxAngularSpeed;
        this.maxAngularAcceleration = type.maxAngularAccel;
        this.size = Math.min(this.health * ENEMY_SIZE, ENEMY_SIZE_MAX);

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

        float hurtSize = size / 5f;
        this.hurtCircle = new Circle(position.x, position.y, hurtSize);

        int numVerts = 10;
        float[] vertices = new float[2 * numVerts];
        for (int i = 0, angle = 0; i < vertices.length; i += 2) {
            vertices[i+0] = position.x + MathUtils.cosDeg(angle) * hurtSize;
            vertices[i+1] = position.y + MathUtils.sinDeg(angle) * hurtSize;
            angle += 360/numVerts;
        }
        this.hurtShape = new Polygon(vertices);
        this.isHurt = false;
        this.hurtDuration = 0.6f;
        this.hurtTimer = hurtDuration;
        this.hurtFlashColor = Color.WHITE.cpy();
    }

    public void update(float dt) {
        prevPosition.set(position);

        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            if (!isHurt) {
                applySteering(steeringOutput, dt);
            }
        }

        hurtCircle.setPosition(position);
        hurtShape.translate(position.x - prevPosition.x, position.y - prevPosition.y);
        if (isHurt) {
            hurtTimer -= dt;
            if (hurtTimer <= 0) {
                hurtTimer = hurtDuration;
                hurtFlashColor.set(Color.WHITE);
                isHurt = false;
            }
        }

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
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

        switch(type.gemColor) {
            case RED:
                batch.setColor(Color.RED);
                break;
            case GREEN:
                batch.setColor(Color.GREEN);
                break;
            case BLUE:
                batch.setColor(Color.ROYAL);
                break;
        }
        batch.draw(screen.assets.circleTex, position.x - 4, position.y +size/2f, 8, 8);
        batch.setColor(Color.WHITE);
        if (isHurt) {
            Animation<TextureRegion> flashAnimation = screen.assets.creatureAnims.getFlash(type);
            TextureRegion flashKeyframe = flashAnimation.getKeyFrame(stateTime);

            hurtFlashColor.a = 0.75f;
            batch.setColor(hurtFlashColor);
            batch.draw(flashKeyframe, position.x - (size / 2f), position.y - (size / 2f), size, size);
            batch.setColor(Color.WHITE);
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

        // wizard should always 'crit' enemies
        float wizardScale = (screen.player.isWizard) ? 2f : 1f;
        health -= amount * wizardScale;

        if (screen.player.isWizard || type.gemColor.matches(screen.player.phase)) {
            hurtFlashColor.set(type.gemColor.getColor());
            screen.particles.explode(EffectAnims.Type.x_white, position.x, position.y, size);
        }

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
        int gemsToSpawn = (int) type.health;
        for (int i = 0; i < gemsToSpawn; i++) {
            screen.gems.add(new Gem(screen, position, type.gemColor));
        }
        Stats.numEnemyKilled++;
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
