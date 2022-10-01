package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.screens.GameScreen;

public class Boss {

    public static float SIZE = 150;
    public float protectedRadius = 110;
    public Vector2 position;
    public GameScreen screen;

    public Boss(GameScreen screen) {
        this.screen = screen;
        this.position = new Vector2(screen.arena.bounds.x + screen.arena.bounds.width/2f,
                          screen.arena.bounds.y + screen.arena.bounds.height/2f);

    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch) {
        batch.setColor(0, 0, 0,1f);
        batch.draw(screen.assets.pixel, position.x - SIZE/2f, position.y - SIZE/2f, SIZE, SIZE);
        batch.setColor(Color.WHITE);
    }
}
