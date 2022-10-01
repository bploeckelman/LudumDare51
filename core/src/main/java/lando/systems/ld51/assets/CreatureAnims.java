package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

public class CreatureAnims {

    private final TextureRegion sheet;
    private final TextureRegion[][] regions;
    private final ObjectMap<Type, Animation<TextureRegion>> animations;

    public CreatureAnims(Assets assets) {
        String name = "creatures-temp/oryx-creatures";
        this.sheet = assets.atlas.findRegion(name);
        if (this.sheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist? Did you run the 'sprites' task in gradle?");
        }
        int tileSize = 48;
        int numCreaturesWidePerSheet = 10;
        this.regions = sheet.split(tileSize, tileSize);
        this.animations = new ObjectMap<>();
        Array<TextureRegion> frames = new Array<>();
        for (Type type : Type.values()) {
            frames.clear();
            frames.add(regions[type.y][type.x + 0 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 1 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 2 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 3 * numCreaturesWidePerSheet]);
            Animation<TextureRegion> animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
            animations.put(type, animation);
        }
    }

    public Animation<TextureRegion> get(Type type) {
        Animation<TextureRegion> animation = animations.get(type);
        if (animation == null) {
            throw new GdxRuntimeException("Can't get creature animation for type '" + type.name() + "', it might not have been created correctly, check CreatureAnims.java");
        }
        return animation;
    }

    public enum Type {
          warrior(5, 0)
        , cleric(4, 0)
        , rogue(7, 0)
        , wizard_blue(0, 0)
        , rat_small(0, 5)
        , rat_big(1, 5)
        ;
        public final int x;
        public final int y;
        Type(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public static Type random() {
            int numTypes = values().length;
            return values()[MathUtils.random(0, numTypes - 1)];
        }
    }

}
