package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.screens.GameScreen;

public class BossArrow {

    public static float SIZE = 60;

    private GameScreen gameScreen;
    private float accum;
    Vector2 position;
    Vector2 direction;
    Rectangle cameraBounds;

    public BossArrow(GameScreen screen){
        this.gameScreen = screen;
        this.position = new Vector2();
        this.direction = new Vector2();
        this.cameraBounds = new Rectangle(0,0, 100, 100);
    }

    public void update(float dt) {
        accum += dt;
        direction.set(gameScreen.boss.position).sub(gameScreen.player.position).nor();
        position.set(gameScreen.windowCamera.viewportWidth/2f, gameScreen.windowCamera.viewportHeight/2f).mulAdd(direction, 180f + MathUtils.sin(accum*4f) * 40);
        OrthographicCamera worldCamera = gameScreen.worldCamera;
        cameraBounds.set(worldCamera.position.x - (worldCamera.viewportWidth/2 * worldCamera.zoom),
                         worldCamera.position.y - (worldCamera.viewportHeight/2 * worldCamera.zoom),
                         worldCamera.viewportWidth * worldCamera.zoom,
                          worldCamera.viewportHeight * worldCamera.zoom);
    }

    public void render(SpriteBatch batch) {
        Vector2 bossPos = gameScreen.boss.position;

        boolean bossOnScreen = cameraBounds.contains(bossPos);
        if (gameScreen.player.isWizard() && !bossOnScreen) {
            batch.draw(gameScreen.assets.arrow, position.x - SIZE / 2f, position.y - SIZE / 2f, SIZE / 2f, SIZE / 2f, SIZE, SIZE, 1f, 1f, direction.angleDeg());
        }
    }
}
