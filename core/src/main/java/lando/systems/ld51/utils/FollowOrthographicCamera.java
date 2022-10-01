package lando.systems.ld51.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class FollowOrthographicCamera extends OrthographicCamera {

    Vector2 direction;

    public FollowOrthographicCamera() {
        direction = new Vector2();
    }

    public void update(Vector2 followPoint, float dt) {
        // TODO: add constraints for world boundary

        direction.set(followPoint).sub(position.x, position.y).scl(1.5f);

        position.add(direction.x * dt, direction.y * dt, 0);

        this.update();
    }
}
