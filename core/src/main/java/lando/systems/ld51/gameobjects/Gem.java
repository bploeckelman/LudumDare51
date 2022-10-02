package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.assets.ItemTextures;
import lando.systems.ld51.screens.GameScreen;

public class Gem {

    public static float AttractRange = 150;
    public static float CollectDistance = 20;

    public enum Type {
          RED(ItemTextures.Type.gem_red)
        , GREEN(ItemTextures.Type.gem_green)
        , BLUE(ItemTextures.Type.gem_blue)
        ;
        final ItemTextures.Type textureType;
        Type(ItemTextures.Type textureType) {
            this.textureType = textureType;
        }
    };

    public final Type type;

    public Vector2 pos;
    public GameScreen gameScreen;
    public boolean collected;
    public Vector2 velocity;
    public Vector2 initialVelocity;
    public TextureRegion texture;
    public float spawnTimer;

    public Gem(GameScreen screen, Vector2 position, Type type) {
        this.gameScreen = screen;
        this.type = type;
        this.collected = false;
        this.pos = new Vector2(position);
        this.velocity = new Vector2();
        this.initialVelocity = new Vector2(MathUtils.random(-50f, 50f), MathUtils.random(30f, 120f));
        this.texture = screen.assets.itemTextures.get(type.textureType);
        this.spawnTimer = MathUtils.random(2f, 4f);
    }

    public void update(float dt) {
        spawnTimer -= dt;
        if (spawnTimer > 0) {
            initialVelocity.y -= 100 * dt;
            if (initialVelocity.y < -50){
                initialVelocity.y *= -1;
            }

            initialVelocity.x *= Math.pow(.8f, dt);
            this.pos.add(initialVelocity.x * dt, initialVelocity.y * dt);
        }


        if (spawnTimer <= 0) {
            velocity.set(0, 0);
            float attract2 = AttractRange * AttractRange;
            float dist2ToPlayer = this.pos.dst2(gameScreen.player.position);
            if (gameScreen.player.canPickup(this)) {
                if (dist2ToPlayer < CollectDistance * CollectDistance) {
                    // Pick up
                    gameScreen.player.pickupGem(this);
                    collected = true;
                }
                if (dist2ToPlayer < attract2) {
                    velocity.set(gameScreen.player.position).sub(pos).nor().scl((attract2 - dist2ToPlayer) / attract2 * 300f);
                }
            }
            this.pos.add(velocity.x * dt, velocity.y * dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, pos.x - 10, pos.y - 10, 20, 20);
        batch.setColor(Color.WHITE);
    }
}
