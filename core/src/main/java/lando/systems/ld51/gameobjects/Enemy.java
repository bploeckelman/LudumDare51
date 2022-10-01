package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.VectorPool;

public class Enemy {

    private final GameScreen screen;

    private CreatureAnims.Type type;
    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;

    public Vector2 position;
    public Vector2 velocity;
    public Vector2 targetPos;

    public float health = 10f;
    public float speed = 30f;
    public float size = 30f;

    public Enemy(GameScreen screen, CreatureAnims.Type type, float x, float y) {
        this.screen = screen;
        this.type = type;
        this.animation = screen.assets.creatureAnims.get(type);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;
        this.position = VectorPool.vec2.obtain().set(x,  y);
        this.velocity = VectorPool.vec2.obtain().set(0, 0);
        this.targetPos = null;
    }

    public void update(float dt) {
        // TODO - keep a minimum distance away from target so enemies don't all overlap with player
        if (targetPos != null) {
            velocity.set(targetPos.x - position.x, targetPos.y - position.y).nor().scl(speed);
        }
        position.add(velocity.x * dt, velocity.y * dt);

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, position.x - (size / 2f), position.y - (size / 2f), size, size);
    }

    public void hurt(float amount) {
        // TODO - trigger 'hurt' animation/effect, start counting iframes, don't allow another hurt until iframes are expired
        health -= amount;
    }

    public boolean isDead() {
        return (health <= 0f);
    }

    public void kill() {
        VectorPool.vec2.free(position);
        VectorPool.vec2.free(velocity);
    }

}
