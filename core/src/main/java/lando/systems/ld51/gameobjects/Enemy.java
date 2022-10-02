package lando.systems.ld51.gameobjects;

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
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.VectorPool;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Enemy {

    private final GameScreen screen;

    private CreatureAnims.Type type;
    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;

    public Vector2 position;
    public Vector2 velocity;
    public Vector2 targetPos;
    public Circle hurtCircle;  // NOTE - this is for broad phase simple checks
    public Polygon hurtShape; // NOTE - this is for narrow phase convex poly/poly checks
    private float hurtDuration = 0.5f;
    private float hurtTimer;
    private boolean isHurt;

    public float health;
    public float speed = 30f;
    public float size = 50f;

    public Enemy(GameScreen screen, CreatureAnims.Type type, float x, float y) {
        this.screen = screen;
        this.type = type;
        this.health = type.health;
        this.animation = screen.assets.creatureAnims.get(type);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;
        this.position = VectorPool.vec2.obtain().set(x,  y);
        this.velocity = VectorPool.vec2.obtain().set(0, 0);
        this.targetPos = null;
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
        // TODO - keep a minimum distance away from target so enemies don't all overlap with player
        if (targetPos != null) {
            velocity.set(targetPos.x - position.x, targetPos.y - position.y).nor().scl(speed);
        }
        float prevPosX = position.x;
        float prevPosY = position.y;
        position.add(velocity.x * dt, velocity.y * dt);
        hurtCircle.setPosition(position);
        hurtShape.translate(position.x - prevPosX, position.y - prevPosY);
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

        health -= amount;

        // bounce back
        float prevPosX = position.x;
        float prevPosY = position.y;
        float bounceBackAmount = 50f;
        position.add(dirX * bounceBackAmount, dirY * bounceBackAmount);
        hurtCircle.setPosition(position);
        hurtShape.translate(position.x - prevPosX, position.y - prevPosY);
    }

    public boolean isDead() {
        return (health <= 0f);
    }

    public void kill() {
        screen.particles.explode(position.x, position.y, size);
        VectorPool.vec2.free(position);
        VectorPool.vec2.free(velocity);
    }

}
