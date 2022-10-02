package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

public class CreatureAnims {

    private final TextureRegion sheet;
    private final TextureRegion flashSheet;
    private final TextureRegion[][] regions;
    private final TextureRegion[][] flashRegions;
    private final ObjectMap<Type, Animation<TextureRegion>> animations;
    private final ObjectMap<Type, Animation<TextureRegion>> flashAnimations;

    public CreatureAnims(Assets assets) {
        String name = "temp-creatures/oryx-creatures";
        this.sheet = assets.atlas.findRegion(name);
        if (this.sheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist? Did you run the 'sprites' task in gradle?");
        }
        name = "temp-creatures/oryx-creatures-flash";
        this.flashSheet = assets.atlas.findRegion(name);
        if (this.flashSheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist? Did you run the 'sprites' task in gradle?");
        }

        int tileSize = 48;
        int numCreaturesWidePerSheet = 10;
        this.regions = sheet.split(tileSize, tileSize);
        this.flashRegions = flashSheet.split(tileSize, tileSize);
        this.animations = new ObjectMap<>();
        this.flashAnimations = new ObjectMap<>();
        Array<TextureRegion> frames = new Array<>();
        Array<TextureRegion> flashFrames = new Array<>();
        for (Type type : Type.values()) {
            frames.clear();
            frames.add(regions[type.y][type.x + 0 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 1 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 2 * numCreaturesWidePerSheet]);
            frames.add(regions[type.y][type.x + 3 * numCreaturesWidePerSheet]);
            Animation<TextureRegion> animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
            animations.put(type, animation);

            flashFrames.clear();
            flashFrames.add(flashRegions[type.y][type.x + 0 * numCreaturesWidePerSheet]);
            flashFrames.add(flashRegions[type.y][type.x + 1 * numCreaturesWidePerSheet]);
            flashFrames.add(flashRegions[type.y][type.x + 2 * numCreaturesWidePerSheet]);
            flashFrames.add(flashRegions[type.y][type.x + 3 * numCreaturesWidePerSheet]);
            Animation<TextureRegion> flashAnimation = new Animation<>(0.1f, flashFrames, Animation.PlayMode.LOOP);
            flashAnimations.put(type, flashAnimation);
        }
    }

    public Animation<TextureRegion> get(Type type) {
        Animation<TextureRegion> animation = animations.get(type);
        if (animation == null) {
            throw new GdxRuntimeException("Can't get creature animation for type '" + type.name() + "', it might not have been created correctly, check CreatureAnims.java");
        }
        return animation;
    }

    public Animation<TextureRegion> getFlash(Type type) {
        Animation<TextureRegion> animation = flashAnimations.get(type);
        if (animation == null) {
            throw new GdxRuntimeException("Can't get creature flash animation for type '" + type.name() + "', it might not have been created correctly, check CreatureAnims.java");
        }
        return animation;
    }

    public enum Type {
          warrior      (5, 0,  5,  4f)
        , cleric       (4, 0,  3,  1f)
        , rogue        (7, 0,  2,  1f)
        , wizard_blue  (0, 0,  2,  1f)
        , rat_small    (0, 5,  1,  0.2f)
        , rat_big      (1, 5,  3,  3f)
        ;
        public final int x;
        public final int y;
        public final float health;
        public final float avoidanceScale;
        Type(int x, int y, float health, float avoidanceScale) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.avoidanceScale = avoidanceScale;
        }
        public static Type random() {
            int numTypes = values().length;
            return values()[MathUtils.random(0, numTypes - 1)];
        }
    }

}
