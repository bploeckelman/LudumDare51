package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.Config;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.utils.Calc;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Projectile extends ObjectLocation {

    private final Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;

    private float speed;

    public float size;
    public float damageAmount = 1f;

    public final Circle bounds;
    public final Vector2 direction;

    public Projectile(Assets assets, EffectAnims.Type type, float x, float y, float angleRadians, float speed) {
        this.animation = assets.effectAnims.get(type);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;
        this.position.set(x, y);
        this.orientation = angleRadians;
        this.speed = speed;
        this.size = Calc.max(keyframe.getRegionWidth(), keyframe.getRegionHeight());
        this.bounds = new Circle(x, y, size / 2f);
        this.direction = new Vector2(Vector2.X).setAngleRad(orientation).nor();
    }

    public void update(float dt) {
        position.mulAdd(direction, speed * dt);
        bounds.set(position, size / 2f);

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe,
                position.x - size / 2f,
                position.y - size / 2f,
                size / 2f,
                size / 2f,
                size, size,
                1f, 1f,
                orientation * MathUtils.radiansToDegrees
        );

        if (Config.Debug.general) {
            ShapeDrawer shapes = Main.game.assets.shapes;
            shapes.setColor(Color.MAGENTA);
            shapes.circle(bounds.x, bounds.y, bounds.radius, 2f);
            shapes.setColor(Color.WHITE);
        }
    }

    public void kill() {
        Main.game.particles.explode(EffectAnims.Type.explode_fast_orange, bounds.x, bounds.y, bounds.radius * 2);
    }
}
