package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld51.Config;
import lando.systems.ld51.Main;
import lando.systems.ld51.screens.GameScreen;

public class Player {

    public static float SIZE = 30f;
    public static float SPEED = 100f;

    public Vector2 position;
    public Vector2 velocity;
    public Vector3 mousePos;
    public Vector2 facing;
    private Texture tex;
    private GameScreen gameScreen;

    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(Config.Screen.window_width/2f, Config.Screen.window_height/2f);
        this.velocity = new Vector2();
        this.facing = new Vector2();
        this.mousePos = new Vector3();
        this.tex = Main.game.assets.pixel;

    }

    public void update(float dt) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.worldCamera.unproject(mousePos);
        facing.set(mousePos.x, mousePos.y).sub(position).nor();

        if (Gdx.input.isTouched()){
            position.add(facing.x * SPEED * dt, facing.y * SPEED * dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(tex, position.x - (SIZE/2f), position.y - (SIZE/2f), SIZE, SIZE);
    }
}
