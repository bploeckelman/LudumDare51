package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld51.utils.Point;

public class EffectAnims {

    private final TextureRegion sheet;
    private final TextureRegion[][] regions;
    private final ObjectMap<Type, Animation<TextureRegion>> animations;

    public EffectAnims(Assets assets) {
        String name = "temp-effects/oryx-effects";
        this.sheet = assets.atlas.findRegion(name);
        if (this.sheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist? Did you run the 'sprites' task in gradle?");
        }
        int tileSize = 32;
        this.regions = sheet.split(tileSize, tileSize);
        this.animations = new ObjectMap<>();
        Array<TextureRegion> frames = new Array<>();
        for (Type type : Type.values()) {
            frames.clear();
            for (Point coord : type.coords) {
                TextureRegion texture = regions[coord.y][coord.x];
                frames.add(texture);
            }
            Animation<TextureRegion> animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
            animations.put(type, animation);
        }
    }

    public Animation<TextureRegion> get(Type type) {
        Animation<TextureRegion> animation = animations.get(type);
        if (animation == null) {
            throw new GdxRuntimeException("Can't get effect animation for type '" + type.name() + "', it might not have been created correctly, check EffectAnims.java");
        }
        return animation;
    }

    public enum Type {
          swipe(0,10,  1,10)
        , explode_puff(2,10,  3,10,  4,10,  5,10)
        , explode_small(3,2,  4,2,  5,2)
        , explode_fast_orange(6,9,  7,9)
        , explode_fast_blue(6,1,  7,1)
        , explode_spark(0,9,  1,9)
        , flame_red(6,0,  7,0)
        , flame_green(6,2,  7,2)
        , flame_purple(6,3,  7,3)
        , x_red(2,1,  3,1)
        , x_white(4,1,  5,1)
        , meteor(0,4)
        , fireball_red(0,5)
        , double_fireball_red(0,6)
        , fireball_green(1,5)
        , fireball_blue(0,7)
        , double_fireball_blue(0,8)
        , orb_red(0,0,  1,0)
        , orb_blue(0,1, 1,1)
        , swirl(2,2)
        ;
        final Array<Point> coords;
        Type(int... coords) {
            if (coords.length % 2 != 0) {
                throw new GdxRuntimeException("EffectAnims.Type coords not a multiple of two, did you forget a number?");
            }
            this.coords = new Array<>();
            for (int i = 0; i < coords.length; i += 2) {
                this.coords.add(new Point(coords[i], coords[i+1]));
            }
        }
        public static Type random() {
            int numTypes = values().length;
            return values()[MathUtils.random(0, numTypes - 1)];
        }
    }

}
