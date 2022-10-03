package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.VectorPool;

public class Gem {

    public static float SIZE = 20;
    public static float LIFE_DURATION = 70; // seconds
    public static float FADEOUT_DURATION = 10; // seconds
    public static float AttractRange = 150;
    public static float CollectDistance = 20;

    public enum Type { RED, GREEN, BLUE }
    public enum State { IDLE, SPIN }

    public final Type type;

    private final GameScreen screen;

    private Animation<AtlasRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;
    private State state;

    public boolean collected;
    public boolean diedOfOldAge;
    public float lifeTimer;
    public float fadeoutTimer;

    public Vector2 pos;
    public Vector2 velocity;
    public Vector2 initialVelocity;
    public float spawnTimer;

    public Gem(GameScreen screen, Vector2 position, Type type) {
        this.screen = screen;
        this.type = type;
        this.state = State.IDLE;
        this.pos = VectorPool.vec2.obtain().set(position);
        this.velocity = VectorPool.vec2.obtain().set(0, 0);
        this.initialVelocity = VectorPool.vec2.obtain().set(MathUtils.random(-50f, 50f), MathUtils.random(30f, 120f));
        this.animation = screen.assets.gemAnimationByTypeByState.get(type).get(state);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = MathUtils.random(0f, 1f); // randomize starting state time for visual variety
        this.spawnTimer = MathUtils.random(1f, 2f);
        this.collected = false;
        this.diedOfOldAge = false;
        this.lifeTimer = LIFE_DURATION;
        this.fadeoutTimer = FADEOUT_DURATION;

        screen.game.audio.playSound(AudioManager.Sounds.gemDrop);
    }

    public void update(float dt) {
        spawnTimer -= dt;
        if (spawnTimer > 0) {
            initialVelocity.y -= 130 * dt;
            if (initialVelocity.y < -80){
                initialVelocity.y *= -1;
            }

            initialVelocity.x *= Math.pow(.8f, dt);
            this.pos.add(initialVelocity.x * dt, initialVelocity.y * dt);
        }

        if (spawnTimer <= 0) {
            state = State.SPIN;
            velocity.set(0, 0);
            float attract2 = AttractRange * AttractRange;
            float dist2ToPlayer = this.pos.dst2(screen.player.position);
            if (screen.player.canPickup(this)) {
                if (dist2ToPlayer < CollectDistance * CollectDistance) {
                    // Pick up and don't fade
                    screen.player.pickupGem(this);
                    fadeoutTimer = FADEOUT_DURATION;
                    collected = true;
                }
                if (dist2ToPlayer < attract2) {
                    velocity.set(screen.player.position).sub(pos).nor().scl((attract2 - dist2ToPlayer) / attract2 * 300f);
                }
            }
            this.pos.add(velocity.x * dt, velocity.y * dt);
        }

        lifeTimer -= dt;
        if (lifeTimer <= 0f) {
            diedOfOldAge = true;
        } else {
            if (lifeTimer <= FADEOUT_DURATION) {
                fadeoutTimer -= dt;
            }
        }

        stateTime += dt;
        animation = screen.assets.gemAnimationByTypeByState.get(type).get(state);
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        float alpha = fadeoutTimer / FADEOUT_DURATION;
        if (screen.player.canPickup(this)) {
            batch.setColor(1f, 1f, 1f, alpha);
        } else {
            batch.setColor(0.25f, 0.25f, 0.25f, alpha);
        }
        batch.draw(keyframe, pos.x - SIZE / 2f, pos.y - SIZE / 2f, SIZE, SIZE);
        batch.setColor(Color.WHITE);
    }

    public void free() {
        VectorPool.vec2.free(pos);
        VectorPool.vec2.free(velocity);
        VectorPool.vec2.free(initialVelocity);
    }

}
