package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.utils.Callback;
import lando.systems.ld51.utils.Time;

public class Explosion {
    public Vector2 position;
    public float radius;
    public float damage;

    public Explosion(GameScreen screen, float x, float y, float radius, float damage) {
        this.position = new Vector2(x, y);
        this.radius = radius;
        this.damage = damage;

        int explosionsToPlace = (int)(radius * radius * .01f);
        for (int i = 0; i < explosionsToPlace; i++) {
            float degrees = MathUtils.random(360f);
            float dist = MathUtils.random(radius);
            float eX = position.x + MathUtils.sinDeg(degrees) * dist;
            float eY = position.y + MathUtils.cosDeg(degrees) * dist;
            EffectAnims.Type effectType = EffectAnims.Type.explode_fast_blue;
            switch(MathUtils.random(4)){
                case 1:
                    effectType = EffectAnims.Type.explode_puff;
                    break;
                case 2:
                    effectType = EffectAnims.Type.explode_small;
                    break;
                case 3:
                    effectType = EffectAnims.Type.explode_fast_orange;
                    break;
                case 4:
                    effectType = EffectAnims.Type.explode_spark;
            }
            EffectAnims.Type finalEffectType = effectType;
            Time.do_after_delay(MathUtils.random(1f), new Callback() {
                @Override
                public void run(Object... params) {
                    screen.particles.explode(finalEffectType, eX, eY, MathUtils.random(30, 50));
                }
            });
        }
    }
}
