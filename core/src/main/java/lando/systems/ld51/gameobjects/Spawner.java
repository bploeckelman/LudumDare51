package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld51.Config;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.FollowOrthographicCamera;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Spawner extends ObjectLocation {

    private static final float DEBUG_RADIUS = 5f;

    public enum SpawnType {
          single_easy
        , single_med
        , single_tough
        , swarm_easy_small
        , swarm_easy_large
    }

    private final GameScreen screen;

    public Spawner(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.position.set(x, y);
    }

    public void render(SpriteBatch batch) {
        if (Config.Debug.general) {
            ShapeDrawer shapes = screen.assets.shapes;
            shapes.setColor(Color.PINK);
            shapes.filledCircle(position, DEBUG_RADIUS);
            shapes.setColor(Color.WHITE);
        }
    }

    public boolean isOffscreen() {
        FollowOrthographicCamera camera = (FollowOrthographicCamera) screen.worldCamera;
        return (position.x < (camera.position.x - camera.viewportWidth  / 2f) || position.x > (camera.position.x + camera.viewportWidth  / 2f))
            || (position.y < (camera.position.y - camera.viewportHeight / 2f) || position.y > (camera.position.y + camera.viewportHeight / 2f));
    }

}
