package lando.systems.ld51.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class FollowOrthographicCamera extends OrthographicCamera {

    Vector2 direction;
    Vector2 tempVec;


    public FollowOrthographicCamera() {
        direction = new Vector2();
        this.tempVec = new Vector2();
    }

    public void update(Vector2 followPoint, Rectangle bounds, float dt) {
        tempVec.set(MathUtils.clamp(followPoint.x, bounds.x + viewportWidth/2, bounds.x + bounds.width - viewportWidth/2f),
                MathUtils.clamp(followPoint.y, bounds.y + viewportHeight/2, bounds.y + bounds.height - viewportHeight/2f));

        direction.set(tempVec).sub(position.x, position.y).scl(8.5f);

        position.add(direction.x * dt, direction.y * dt, 0);

        this.update();
    }
}
