package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.assets.ItemTextures;
import lando.systems.ld51.screens.GameScreen;

public class Gem {

    public static float AttractRange = 250;

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
    public TextureRegion texture;

    public Gem(GameScreen screen, Vector2 position, Type type) {
        this.gameScreen = screen;
        this.type = type;
        this.collected = false;
        this.pos = new Vector2(position);
        this.velocity = new Vector2();
        this.texture = screen.assets.itemTextures.get(type.textureType);
    }

    public void update(float dt) {
        // TODO: some sort of bounce when they spawn
        velocity.set(0, 0);
        float attract2 = AttractRange * AttractRange;
        float dist2ToPlayer = this.pos.dst2(gameScreen.player.position);
        if (gameScreen.player.canPickup(this)) {
            if (dist2ToPlayer < 30 * 30) {
                // Pick up
                gameScreen.player.pickupGem(this);
                collected = true;
            }
            if (dist2ToPlayer < attract2) {
                velocity.set(gameScreen.player.position).sub(pos).nor().scl((attract2 - dist2ToPlayer)/attract2 * 200f);
            }
        }
        this.pos.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, pos.x - 10, pos.y - 10, 20, 20);
        batch.setColor(Color.WHITE);
    }
}
