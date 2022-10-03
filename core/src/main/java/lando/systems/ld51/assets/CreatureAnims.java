package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld51.gameobjects.Gem;

import static lando.systems.ld51.assets.CreatureAnims.Type.*;
import static lando.systems.ld51.gameobjects.Gem.Type.*;

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

    public enum CreatureGroups {
          reds   (rat_small, rat_big, beetle_red_small, beetle_red_big, spider_black_small, spider_black_big, minotaur, skeleton, fairy, flame_person, blob_red, goblin_dagger, golem_red, elemental_red, dragon_red)
        , greens (snake_small, snake_big, wolf_normal, wolf_dire, spider_brown_small, spider_brown_big, toad, bird, orc, reptile, blob_green, goblin_shield, golem_green, elemental_green, dragon_green)
        , blues  (bat_small, bat_big, worm_small, worm_big, beetle_black_small, beetle_black_big, mimic, zombie, dwarf, beholder, blob_blue, goblin_mage, golem_blue, elemental_blue, dragon_blue)

        , babies  (rat_small, snake_small, bat_small, beetle_red_small, wolf_normal, worm_small, spider_black_small, spider_brown_small, beetle_black_small)
        , mediums (orc, reptile, dwarf, flame_person, beholder, golem_red, golem_green, golem_blue)
        , biggest (devil_red, devil_green, devil_blue, dragon_red, dragon_green, dragon_blue)

        // TODO - families
        ;
        public final Array<Type> types = new Array<>();
        CreatureGroups(Type... types) {
            this.types.addAll(types);
        }
        public Type getRandomType() {
            int numTypes = types.size;
            return types.get(MathUtils.random(0, numTypes - 1));
        }
        // TODO - getRandomFamily()
    }

    public enum Type {
        // old ......................
//          warrior      (5,0,  5,  4f,    30f,  60f, 0f, 10f, RED)
//        , cleric       (4,0,  3,  1f,    80f, 100f, 0f, 30f, BLUE)
//        , rogue        (7,0,  2,  1f,   100f, 200f, 0f, 30f, GREEN)
//        , king_red     (0,10, 10, 10,    60f, 120f, 0f, 50f, RED)
//        , wizard_blue  (0,0,  2,  1f,    80f, 100f, 0f, 20f, RED)

        // small and fast, tier 1
          rat_small    (0,5,  1,  0.8f,  150, 200 , 0,  80,  RED)
        , rat_big      (1,5,  2,  1.2f,   50,  80,  0,  40,  RED)
        , snake_small  (5,5,  1,  0.8f,  150, 200,  0,  80,  GREEN)
        , snake_big    (4,5,  2,  1.2f,   50,  80,  0,  40,  GREEN)
        , bat_small    (5,4,  1,  0.8f,  150, 200,  0,  80,  BLUE)
        , bat_big      (4,4,  2,  1.2f,   50,  80,  0,  40,  BLUE)

        // small and fast tier 2
        , beetle_red_small (7,10,  1,  0.8f,  120,  200,  0,  100,   RED)
        , beetle_red_big   (5,10,  2,  1.2f,   70,  100,  0,   60,   RED)
        , wolf_normal      (1, 6,  1,  0.8f,  120,  200,  0,  100,   GREEN)
        , wolf_dire        (0, 6,  2,  1.2f,   70,  100,  0,   60,   GREEN)
        , worm_small       (5,11,  1,  0.8f,  120,  200,  0,  100,   BLUE)
        , worm_big         (6,11,  2,  1.2f,   70,  100,  0,   60,   BLUE)

        // small and fast tier 3
        , spider_black_small (9, 4,  1,  0.8f,  100,  220,  0,  200,   RED)
        , spider_black_big   (8, 4,  2,  1.2f,   50,  110,  0,  100,   RED)
        , spider_brown_small (7, 4,  1,  0.8f,  100,  220,  0,  200,   GREEN)
        , spider_brown_big   (6, 4,  2,  1.2f,   50,  110,  0,  100,   GREEN)
        , beetle_black_small (6,10,  1,  0.8f,  100,  220,  0,  200,   BLUE)
        , beetle_black_big   (4,10,  2,  1.2f,   50,  110,  0,  100,   BLUE)

        // random one offs tier 1
        , minotaur (6, 5,  2,  1.5f,   80,   80,  0,   50,   RED)
        , skeleton (0, 3,  2,  0.8f,   50,  100,  0,   50,   RED)
        , toad     (3,12,  2,  0.2f,  100,  100,  0,   80,   GREEN)
        , bird     (1, 9,  2,  0.2f,  150,  200,  0,  200,   GREEN)
        , mimic    (7,12,  2,  1.0f,   50,   50,  0,   80,   BLUE)
        , zombie   (2, 5,  2,  0.1f,   30,   50,  0,   40,   BLUE)

        // random one offs tier 2
        , fairy        (4,7,  3,  0.8f,  150,  250,  0,  200,   RED)
        , flame_person (1,7,  5,  2.0f,  100,  130,  0,   80,   RED)
        , orc          (7,3,  4,  1.5f,   80,   80,  0,   60,   GREEN)
        , reptile      (2,6,  4,  1.0f,   80,  120,  0,   90,   GREEN)
        , dwarf        (0,4,  4,  1.0f,   50,   50,  0,   60,   BLUE)
        , beholder     (8,6,  5,  3.0f,   60,  150,  0,  100,   BLUE)

        // family tier 1
        , blob_red   (9,11,  3,  0.3f,  100,  100,  0,  100,   RED)
        , blob_green (7,11,  3,  0.3f,  100,  100,  0,  100,   GREEN)
        , blob_blue  (8,11,  3,  0.3f,  100,  100,  0,  100,   BLUE)

        // family tier 2
        , goblin_dagger (4,3,  3,  1.0f,  120,  150,  0,  120,   RED)
        , goblin_shield (5,3,  3,  1.2f,   80,  150,  0,   80,   GREEN)
        , goblin_mage   (6,3,  3,  0.8f,  100,  150,  0,  100,   BLUE)

        // family tier 3
        , golem_red   (6,9,  5,  1.2f,  80,  100,  0,  80,   RED)
        , golem_green (9,9,  5,  1.2f,  80,  100,  0,  80,   GREEN)
        , golem_blue  (8,9,  5,  1.2f,  80,  100,  0,  80,   BLUE)

        // family tier 4
        , elemental_red   (2,10,  7,  1,  150,  50,  0,  140,   RED)
        , elemental_green (3,10,  7,  1,  150,  50,  0,  140,   GREEN)
        , elemental_blue  (1,10,  7,  1,  150,  50,  0,  140,   BLUE)

        // family tier 5
        , devil_red   (5,7,  8,  1,  100,  100,  0,  100,   RED)
        , devil_green (7,7,  8,  1,  100,  100,  0,  100,   GREEN)
        , devil_blue  (6,7,  8,  1,  100,  100,  0,  100,   BLUE)

        // family tier 6
        , dragon_red   (7,8,  10,  4,  30, 200,  0,  80,  RED)
        , dragon_green (9,8,  10,  4,  30, 200,  0,  80,  GREEN)
        , dragon_blue  (8,8,  10,  4,  30, 200,  0,  80,  BLUE)

        // TODO - these would be good as large mini-bosses that drop all gem types
//        , dragon_black (6,8,  20,  5,  30, 20,  0,   5,  ALL)
//        , dark_knight  ()
        ;
        public final int x;
        public final int y;
        public final float health;
        public final float avoidanceScale;
        public final float maxLinearSpeed;
        public final float maxLinearAccel;
        public final float maxAngularSpeed;
        public final float maxAngularAccel;
        public final Gem.Type gemColor;
        Type(int x, int y,
             float health,
             float avoidanceScale,
             float maxLinearSpeed,
             float maxLinearAccel,
             float maxAngularSpeed,
             float maxAngularAccel,
             Gem.Type gemColor) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.avoidanceScale = avoidanceScale;
            this.maxLinearSpeed = maxLinearSpeed;
            this.maxLinearAccel = maxLinearAccel;
            this.maxAngularSpeed = maxAngularSpeed;
            this.maxAngularAccel = maxAngularAccel;
            this.gemColor = gemColor;
        }
        public static Type random() {
            int numTypes = values().length;
            return values()[MathUtils.random(0, numTypes - 1)];
        }
    }

}
