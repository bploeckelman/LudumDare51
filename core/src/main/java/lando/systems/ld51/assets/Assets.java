package lando.systems.ld51.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.*;
import lando.systems.ld51.Config;
import lando.systems.ld51.gameobjects.Gem;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.utils.screenshake.SimplexNoise;
import space.earlygrey.shapedrawer.ShapeDrawer;
import text.formic.Stringf;

public class Assets implements Disposable {

    public enum Load { ASYNC, SYNC }

    public static TextureRegion pixelTexRegion;

    public boolean initialized;

    public SpriteBatch batch;
    public ShapeDrawer shapes;
    public GlyphLayout layout;
    public AssetManager mgr;
    public Particles particles;
    public TextureAtlas atlas;

    public I18NBundle strings;
    public InputPrompts inputPrompts;
    public ItemTextures itemTextures;
    public PixelUIs pixelUIs;
    public CreatureAnims creatureAnims;
    public EffectAnims effectAnims;

    public BitmapFont font;
    public BitmapFont smallFont;
    public BitmapFont largeFont;

    public Texture titleBackground;
    public Texture titleBlueBeam;
    public Texture titleGreenBeam;
    public Texture titleRedBeam;
    public Texture titleGradient;
    public Texture titleCleric;
    public Texture titleLight;
    public Texture titlePrismHighlight;
    public Texture titlePrismToGem;
    public Texture titleThief;
    public Texture titleWarrior;
    public Texture titleWizard;
    public Texture titleChromeEdge;
    public Texture titleChromeGradient;
    public Texture titleTrigger;
    public Texture titleHatPrism;

    public Texture pixel;
    public Texture noiseTex;
    public TextureRegion circleTex;
    public TextureRegion arrow;
    public TextureRegion pixelRegion;
    public TextureRegion redProgressBar;
    public TextureRegion blueProgressBar;
    public TextureRegion greenProgressBar;
    public TextureRegion whiteProgressBar;
    public TextureRegion bossPortraitLeft;
    public TextureRegion bossPortraitRight;

    public Animation<TextureRegion> cat;
    public Animation<TextureRegion> dog;
    public Animation<TextureRegion> kitten;
    public Animation<TextureRegion> gemBlueIdle;
    public Animation<TextureRegion> gemRedIdle;
    public Animation<TextureRegion> gemGreenIdle;
    public Animation<TextureRegion> gemBlueSpin;
    public Animation<TextureRegion> gemRedSpin;
    public Animation<TextureRegion> gemGreenSpin;

    public Array<Animation<TextureRegion>> numberParticles;
    public ObjectMap<Gem.Type, ObjectMap<Gem.State, Animation<AtlasRegion>>> gemAnimationByTypeByState;
    public ObjectMap<Player.Phase, ObjectMap<Player.State, Animation<AtlasRegion>>> playerAnimationByPhaseByState;
    public ObjectMap<Player.Phase, ObjectMap<Player.State, Animation<AtlasRegion>>> playerFlashAnimationByPhaseByState;
    public ObjectMap<Player.WeaponType, Player.WeaponAnims> weaponAnimationsByType;

    public SimplexNoise noise;

    public ShaderProgram shieldShader;
    public ShaderProgram titleShader;
    public ShaderProgram backgroundShader;

    public Music introMusicMusic;
    public Music warriorMusic1Music;

    public Music introMusicSound;
    public Music warriorMusic1;
    public Music warriorMusic2;
    public Music warriorMusic3;
    public Music rogueMusic1;
    public Music rogueMusic2;
    public Music rogueMusic3;
    public Music clericMusic1;
    public Music clericMusic2;
    public Music clericMusic3;
    public Music wizardMusic1;

    public Sound swipe1;
    public Sound swipe2;
    public Sound swipe3;
    public Sound swipe4;
    public Sound swipe5;
    public Sound impact1;
    public Sound impact2;
    public Sound impact3;
    public Sound impact4;
    public Sound impactLight;
    public Sound impactWet;


    public Sound die1;
    public Sound gemDrop1;
    public Sound gemDrop2;
    public Sound collect1;
    public Sound collect2;
    public Sound collect3;
    public Sound fireball1;
    public Sound fireball2;
    public Sound fireball3;
    public Sound fireball4;
    public Sound fireball5;
    public Sound scorch1;
    public Sound scorch2;
    public Sound scorch3;
    public Sound scorch4;
    public Sound lightning1;
    public Sound warriorGemsFull;
    public Sound rogueGemsFull;
    public Sound clericGemsFull;
    public Sound playerDropGems1;
    public Sound playerDropGems2;
    public Sound playerDropGems3;
    public Sound playerDropGems4;
    public Sound playerHit1;
    public Sound playerHit2;
    public Sound playerHit3;
    public Sound playerHit4;
    public Sound playerHit5;
    public Sound playerHit6;
    public Sound playerHit7;
    public Sound playerImpact1;
    public Sound playerImpact2;
    public Sound playerImpact3;
    public Sound playerImpact4;

    public Sound intro1;
    public Sound intro2;
    public Sound intro3;
    public Sound intro4;
    public Sound intro5;
    public Sound intro6;
    public Sound intro7;
    public Sound intro8;

    public Sound warriorWalkout1;
    public Sound warriorWalkout2;
    public Sound warriorWalkout3;
    public Sound warriorWalkout4;
    public Sound warriorWalkout5;
    public Sound thiefWalkout1;
    public Sound thiefWalkout2;
    public Sound thiefWalkout3;
    public Sound thiefWalkout4;
    public Sound thiefWalkout5;
    public Sound clericWalkout1;
    public Sound clericWalkout2;
    public Sound clericWalkout3;
    public Sound clericWalkout4;
    public Sound clericWalkout5;
    public Sound clericWalkout6;
//    public Sound collect1;
////    public Sound ;
//    public Sound ;

    public enum Patch {
        debug, panel, metal, glass,
        glass_green, glass_yellow, glass_dim, glass_active;
        public NinePatch ninePatch;
        public NinePatchDrawable drawable;
    }

    public static class Particles {
        public TextureRegion circle;
        public TextureRegion sparkle;
        public TextureRegion smoke;
        public TextureRegion ring;
        public TextureRegion dollar;
        public TextureRegion blood;
        public TextureRegion sparks;
        public TextureRegion line;
    }

    public Assets() {
        this(Load.SYNC);
    }

    public Assets(Load load) {
        initialized = false;

        // create a single pixel texture and associated region
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            pixel = new Texture(pixmap);
        }
        pixmap.dispose();
        pixelRegion = new TextureRegion(pixel);

        // set a static for
        Assets.pixelTexRegion = new TextureRegion(pixel);

        batch = new SpriteBatch();
        shapes = new ShapeDrawer(batch, pixelRegion);
        layout = new GlyphLayout();

        mgr = new AssetManager();
        {
            mgr.load("images/noise.png", Texture.class);
            mgr.load("sprites/sprites.atlas", TextureAtlas.class);
            mgr.load("ui/uiskin.json", Skin.class);

            mgr.load("i18n/strings", I18NBundle.class);

            mgr.load("fonts/outfit-medium-20px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-40px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-80px.fnt", BitmapFont.class);

            mgr.load("audio/music/introMusic.ogg", Music.class);
            mgr.load("audio/music/warriorMusicA.ogg", Music.class);


            mgr.load("audio/music/introMusic.ogg", Music.class);
            mgr.load("audio/music/wizardMusic1.ogg", Music.class);

            mgr.load("audio/music/warriorMusicA.ogg", Music.class);
            mgr.load("audio/music/warriorMusicB.ogg", Music.class);
            mgr.load("audio/music/warriorMusicC.ogg", Music.class);
            mgr.load("audio/music/rogueMusicA.ogg", Music.class);
            mgr.load("audio/music/rogueMusicB.ogg", Music.class);
            mgr.load("audio/music/rogueMusicC.ogg", Music.class);
            mgr.load("audio/music/clericMusicA.ogg", Music.class);
            mgr.load("audio/music/clericMusicB.ogg", Music.class);
            mgr.load("audio/music/clericMusicC.ogg", Music.class);


            mgr.load("audio/sound/impact1.ogg", Sound.class);
            mgr.load("audio/sound/impact2.ogg", Sound.class);
            mgr.load("audio/sound/impact3.ogg", Sound.class);
            mgr.load("audio/sound/impact4.ogg", Sound.class);
            mgr.load("audio/sound/impactLight1.ogg", Sound.class);
            mgr.load("audio/sound/impactWet.ogg", Sound.class);
            mgr.load("audio/sound/swipe1.ogg", Sound.class);
            mgr.load("audio/sound/swipe2.ogg", Sound.class);
            mgr.load("audio/sound/swipe3.ogg", Sound.class);
            mgr.load("audio/sound/swipe4.ogg", Sound.class);
            mgr.load("audio/sound/swipe5.ogg", Sound.class);
            mgr.load("audio/sound/die1.ogg", Sound.class);
            mgr.load("audio/sound/gemDrop1.ogg", Sound.class);
            mgr.load("audio/sound/gemDrop2.ogg", Sound.class);
            mgr.load("audio/sound/collect1.ogg", Sound.class);
            mgr.load("audio/sound/collect2.ogg", Sound.class);
            mgr.load("audio/sound/collect3.ogg", Sound.class);
            mgr.load("audio/sound/fireball1.ogg", Sound.class);
            mgr.load("audio/sound/fireball2.ogg", Sound.class);
            mgr.load("audio/sound/fireball3.ogg", Sound.class);
            mgr.load("audio/sound/fireball4.ogg", Sound.class);
            mgr.load("audio/sound/fireball5.ogg", Sound.class);
            mgr.load("audio/sound/scorch1.ogg", Sound.class);
            mgr.load("audio/sound/scorch2.ogg", Sound.class);
            mgr.load("audio/sound/scorch3.ogg", Sound.class);
            mgr.load("audio/sound/scorch4.ogg", Sound.class);
            mgr.load("audio/sound/lightning1.ogg", Sound.class);
            mgr.load("audio/sound/warriorGemsFull.ogg", Sound.class);
            mgr.load("audio/sound/rogueGemsFull.ogg", Sound.class);
            mgr.load("audio/sound/clericGemsFull.ogg", Sound.class);
            mgr.load("audio/sound/playerDropGems1.ogg", Sound.class);
            mgr.load("audio/sound/playerDropGems2.ogg", Sound.class);
            mgr.load("audio/sound/playerDropGems3.ogg", Sound.class);
            mgr.load("audio/sound/playerDropGems4.ogg", Sound.class);
            mgr.load("audio/sound/playerHit1.ogg", Sound.class);
            mgr.load("audio/sound/playerHit2.ogg", Sound.class);
            mgr.load("audio/sound/playerHit3.ogg", Sound.class);
            mgr.load("audio/sound/playerHit4.ogg", Sound.class);
            mgr.load("audio/sound/playerHit5.ogg", Sound.class);
            mgr.load("audio/sound/playerHit6.ogg", Sound.class);
            mgr.load("audio/sound/playerHit7.ogg", Sound.class);
            mgr.load("audio/sound/playerImpact1.ogg", Sound.class);
            mgr.load("audio/sound/playerImpact2.ogg", Sound.class);
            mgr.load("audio/sound/playerImpact3.ogg", Sound.class);
            mgr.load("audio/sound/playerImpact4.ogg", Sound.class);

            mgr.load("audio/sound/intro1.ogg", Sound.class);
            mgr.load("audio/sound/intro2.ogg", Sound.class);
            mgr.load("audio/sound/intro3.ogg", Sound.class);
            mgr.load("audio/sound/intro4.ogg", Sound.class);
            mgr.load("audio/sound/intro5.ogg", Sound.class);
            mgr.load("audio/sound/intro6.ogg", Sound.class);
            mgr.load("audio/sound/intro7.ogg", Sound.class);
            mgr.load("audio/sound/intro8.ogg", Sound.class);

            mgr.load("audio/sound/warriorWalkout1.ogg", Sound.class);
            mgr.load("audio/sound/warriorWalkout2.ogg", Sound.class);
            mgr.load("audio/sound/warriorWalkout3.ogg", Sound.class);
            mgr.load("audio/sound/warriorWalkout4.ogg", Sound.class);
            mgr.load("audio/sound/warriorWalkout5.ogg", Sound.class);
            mgr.load("audio/sound/thiefWalkout1.ogg", Sound.class);
            mgr.load("audio/sound/thiefWalkout2.ogg", Sound.class);
            mgr.load("audio/sound/thiefWalkout3.ogg", Sound.class);
            mgr.load("audio/sound/thiefWalkout4.ogg", Sound.class);
            mgr.load("audio/sound/thiefWalkout5.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout1.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout2.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout3.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout4.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout5.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout5.ogg", Sound.class);
            mgr.load("audio/sound/clericWalkout6.ogg", Sound.class);
//            mgr.load("audio/sound/.ogg", Sound.class);
//            mgr.load("audio/sound/.ogg", Sound.class);

            mgr.load("images/title-screen-elements/title-background_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-char-beam-blue_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-char-beam-green_01.png", Texture.class);
            mgr.load("images/title-screen-elements/title-char-beam-red_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-char-gradient_01.png", Texture.class);
            mgr.load("images/title-screen-elements/title-cleric_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-gem-light_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-prism-right-highlight_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-prism-to-gem_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-thief_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-warrior_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-wizard-prism_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-word-chrome-edge_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-word-chrome-gradient.png", Texture.class);
            mgr.load("images/title-screen-elements/title-word-trigger-blank_00.png", Texture.class);
            mgr.load("images/title-screen-elements/title-hat-prism_00.png", Texture.class);

        }

        if (load == Load.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        noise = new SimplexNoise(16, .8f, 12);
        shieldShader = loadShader("shaders/default.vert", "shaders/shield.frag");
        titleShader = loadShader("shaders/default.vert", "shaders/title-fade.frag");
        backgroundShader = loadShader("shaders/default.vert", "shaders/background.frag");

        atlas = mgr.get("sprites/sprites.atlas");
        strings = mgr.get("i18n/strings", I18NBundle.class);
        noiseTex = mgr.get("images/noise.png", Texture.class);
        noiseTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        inputPrompts = new InputPrompts(this);
        itemTextures = new ItemTextures(this);
        pixelUIs = new PixelUIs(this);
        creatureAnims = new CreatureAnims(this);
        effectAnims = new EffectAnims(this);

        blueProgressBar = atlas.findRegion("pixel-ui-pack/blue-progress-bar");
        redProgressBar = atlas.findRegion("pixel-ui-pack/red-progress-bar");
        greenProgressBar = atlas.findRegion("pixel-ui-pack/green-progress-bar");
        whiteProgressBar = atlas.findRegion("pixel-ui-pack/white-progress-bar");
        bossPortraitLeft = atlas.findRegion("characters/boss/boss-portrait/boss-portrait-facing-left");
        bossPortraitRight = atlas.findRegion("characters/boss/boss-portrait/boss-portrait-facing-right");
        circleTex = atlas.findRegion("particles/circle");
        arrow = atlas.findRegion("arrow");


        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");

        cat = new Animation<>(0.1f, atlas.findRegions("pets/cat"), PlayMode.LOOP);
        dog = new Animation<>(0.1f, atlas.findRegions("pets/dog"), PlayMode.LOOP);
        kitten = new Animation<>(.1f, atlas.findRegions("pets/kitten"), PlayMode.LOOP);
        gemBlueIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-blue/gem-blue-idle/gem-blue-idle"), PlayMode.LOOP);
        gemBlueSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-blue/gem-blue-spin/gem-blue-spin"), PlayMode.LOOP);
        gemRedIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-red/gem-red-idle/gem-red-idle"), PlayMode.LOOP);
        gemRedSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-red/gem-red-spin/gem-red-spin"), PlayMode.LOOP);
        gemGreenIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-green/gem-green-idle/gem-green-idle"), PlayMode.LOOP);
        gemGreenSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-green/gem-green-spin/gem-green-spin"), PlayMode.LOOP);

        // initialize particle images
        particles = new Particles();
        particles.circle  = atlas.findRegion("particles/circle");
        particles.ring    = atlas.findRegion("particles/ring");
        particles.smoke   = atlas.findRegion("particles/smoke");
        particles.sparkle = atlas.findRegion("particles/sparkle");
        particles.dollar  = atlas.findRegion("particles/dollars");
        particles.blood   = atlas.findRegion("characters/blood-stain");
        particles.sparks  = atlas.findRegion("particles/sparks");
        particles.line    = atlas.findRegion("particles/line");
        numberParticles = new Array<>();
        for (int i = 0; i <= 9; ++i) {
            numberParticles.add(new Animation<>(0.1f, atlas.findRegions("particles/font-points-" + i)));
        }
        gemAnimationByTypeByState = new ObjectMap<>();
        for (Gem.Type type : Gem.Type.values()) {
            ObjectMap<Gem.State, Animation<AtlasRegion>> animationByState = new ObjectMap<>();
            gemAnimationByTypeByState.put(type, animationByState);
            String typeName = type.name().toLowerCase();
            for (Gem.State state : Gem.State.values()) {
                String stateName = state.name().toLowerCase();
                String regionsName = Stringf.format("gems/gem-%1$s/gem-%1$s-%2$s/gem-%1$s-%2$s", typeName, stateName);
                Array<AtlasRegion> frames = atlas.findRegions(regionsName);
                Animation<AtlasRegion> animation = new Animation<>(0.1f, frames, PlayMode.LOOP);
                animationByState.put(state, animation);
            }
        }

        playerAnimationByPhaseByState = new ObjectMap<>();
        playerFlashAnimationByPhaseByState = new ObjectMap<>();
        for (Player.Phase phase : Player.Phase.values()) {
            ObjectMap<Player.State, Animation<AtlasRegion>> animationByState = new ObjectMap<>();
            ObjectMap<Player.State, Animation<AtlasRegion>> flashAnimationByState = new ObjectMap<>();
            playerAnimationByPhaseByState.put(phase, animationByState);
            playerFlashAnimationByPhaseByState.put(phase, flashAnimationByState);

            String phaseName = phase.charClassImageName;
            for (Player.State state : Player.State.values()) {
                String stateName = state.name().toLowerCase();

                String regionsName = Stringf.format("characters/%1$s/%1$s-%2$s/%1$s-%2$s", phaseName, stateName);
                Array<AtlasRegion> frames = atlas.findRegions(regionsName);

                float frameDuration = 0.1f;
                Animation.PlayMode playMode = (state == Player.State.WALK || phase == Player.Phase.WIZARD)
                        ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL;

                Animation<AtlasRegion> animation = new Animation<>(frameDuration, frames, playMode);
                animationByState.put(state, animation);

                String flashRegionsName = Stringf.format("characters/%1$s/%1$s-%2$s/flash/%1$s-%2$s-flash", phaseName, stateName);
                Array<AtlasRegion> flashFrames = atlas.findRegions(flashRegionsName);

                Animation<AtlasRegion> flashAnimation = new Animation<>(frameDuration, flashFrames, playMode);
                flashAnimationByState.put(state, flashAnimation);
            }
        }
        weaponAnimationsByType = new ObjectMap<>();
        for (Player.WeaponType type : Player.WeaponType.values()) {
            String typeName = type.name().toLowerCase();

            String weaponRegions = Stringf.format("weapons/%1$s/%1$s", typeName);
            String glowRegions = Stringf.format("weapons/%1$s-glow/%1$s-glow", typeName);
            Array<AtlasRegion> weaponFrames = atlas.findRegions(weaponRegions);
            Array<AtlasRegion> glowFrames = atlas.findRegions(glowRegions);

            float frameDuration = 0.05f;
            Animation<AtlasRegion> weaponAnim = new Animation<>(frameDuration, weaponFrames, PlayMode.NORMAL);
            Animation<AtlasRegion> glowAnim = new Animation<>(frameDuration, glowFrames, PlayMode.NORMAL);

            Player.WeaponAnims anims = new Player.WeaponAnims(weaponAnim, glowAnim);
            weaponAnimationsByType.put(type, anims);
        }

        // initialize patch values
        Patch.debug.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/debug"), 2, 2, 2, 2);
        Patch.panel.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/panel"), 15, 15, 15, 15);
        Patch.glass.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/glass"), 8, 8, 8, 8);
        Patch.glass_green.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-green"), 8, 8, 8, 8);
        Patch.glass_yellow.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"), 8, 8, 8, 8);
        Patch.glass_dim.ninePatch    = new NinePatch(atlas.findRegion("ninepatch/glass-dim"), 8, 8, 8, 8);
        Patch.glass_active.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-active"), 8, 8, 8, 8);
        Patch.metal.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/metal"), 12, 12, 12, 12);

        Patch.debug.drawable        = new NinePatchDrawable(Patch.debug.ninePatch);
        Patch.panel.drawable        = new NinePatchDrawable(Patch.panel.ninePatch);
        Patch.glass.drawable        = new NinePatchDrawable(Patch.glass.ninePatch);
        Patch.glass_green.drawable  = new NinePatchDrawable(Patch.glass_green.ninePatch);
        Patch.glass_yellow.drawable = new NinePatchDrawable(Patch.glass_yellow.ninePatch);
        Patch.glass_dim.drawable    = new NinePatchDrawable(Patch.glass_dim.ninePatch);
        Patch.glass_active.drawable = new NinePatchDrawable(Patch.glass_active.ninePatch);
        Patch.metal.drawable        = new NinePatchDrawable(Patch.metal.ninePatch);




        // Music clips as Music objects
        introMusicMusic = mgr.get("audio/music/introMusic.ogg", Music.class);


        // Music clips as Sound objects
        introMusicSound = mgr.get("audio/music/introMusic.ogg", Music.class);
        wizardMusic1 = mgr.get("audio/music/wizardMusic1.ogg", Music.class);

        warriorMusic1 = mgr.get("audio/music/warriorMusicB.ogg", Music.class);
        warriorMusic2 = mgr.get("audio/music/warriorMusicA.ogg", Music.class);
        warriorMusic3 = mgr.get("audio/music/warriorMusicC.ogg", Music.class);

        rogueMusic1 = mgr.get("audio/music/rogueMusicA.ogg", Music.class);
        rogueMusic2 = mgr.get("audio/music/rogueMusicC.ogg", Music.class);
        rogueMusic3 = mgr.get("audio/music/rogueMusicB.ogg", Music.class);

        clericMusic1 = mgr.get("audio/music/clericMusicC.ogg", Music.class);
        clericMusic2 = mgr.get("audio/music/clericMusicB.ogg", Music.class);
        clericMusic3 = mgr.get("audio/music/clericMusicA.ogg", Music.class);

        // Sound effects
         swipe1 = mgr.get("audio/sound/swipe1.ogg", Sound.class);
         swipe2 = mgr.get("audio/sound/swipe2.ogg", Sound.class);
         swipe3 = mgr.get("audio/sound/swipe3.ogg", Sound.class);
         swipe4 = mgr.get("audio/sound/swipe4.ogg", Sound.class);
         swipe5 = mgr.get("audio/sound/swipe5.ogg", Sound.class);
         impact1 = mgr.get("audio/sound/impact1.ogg", Sound.class);
         impact2 = mgr.get("audio/sound/impact2.ogg", Sound.class);
         impact3 = mgr.get("audio/sound/impact3.ogg", Sound.class);
         impact4 = mgr.get("audio/sound/impact4.ogg", Sound.class);
         impactWet = mgr.get("audio/sound/impactWet.ogg", Sound.class);
         impactLight = mgr.get("audio/sound/impactLight1.ogg", Sound.class);
         collect1 = mgr.get("audio/sound/collect1.ogg", Sound.class);
         collect2 = mgr.get("audio/sound/collect2.ogg", Sound.class);
         collect3 = mgr.get("audio/sound/collect3.ogg", Sound.class);
         die1 = mgr.get("audio/sound/die1.ogg", Sound.class);
         gemDrop1 = mgr.get("audio/sound/gemDrop1.ogg", Sound.class);
         gemDrop2 = mgr.get("audio/sound/gemDrop2.ogg", Sound.class);
         fireball1 = mgr.get("audio/sound/fireball1.ogg", Sound.class);
         fireball2 = mgr.get("audio/sound/fireball2.ogg", Sound.class);
         fireball3 = mgr.get("audio/sound/fireball3.ogg", Sound.class);
         fireball4 = mgr.get("audio/sound/fireball4.ogg", Sound.class);
         fireball5 = mgr.get("audio/sound/fireball5.ogg", Sound.class);
         scorch1 = mgr.get("audio/sound/scorch1.ogg", Sound.class);
         scorch2 = mgr.get("audio/sound/scorch2.ogg", Sound.class);
         scorch3 = mgr.get("audio/sound/scorch3.ogg", Sound.class);
         scorch4 = mgr.get("audio/sound/scorch4.ogg", Sound.class);
         lightning1 = mgr.get("audio/sound/lightning1.ogg", Sound.class);
         warriorGemsFull = mgr.get("audio/sound/warriorGemsFull.ogg", Sound.class);
         rogueGemsFull = mgr.get("audio/sound/rogueGemsFull.ogg", Sound.class);
         clericGemsFull = mgr.get("audio/sound/clericGemsFull.ogg", Sound.class);
         playerHit1 = mgr.get("audio/sound/playerHit1.ogg", Sound.class);
         playerHit2 = mgr.get("audio/sound/playerHit2.ogg", Sound.class);
         playerHit3 = mgr.get("audio/sound/playerHit3.ogg", Sound.class);
         playerHit4 = mgr.get("audio/sound/playerHit4.ogg", Sound.class);
         playerHit5 = mgr.get("audio/sound/playerHit5.ogg", Sound.class);
         playerHit6 = mgr.get("audio/sound/playerHit6.ogg", Sound.class);
         playerHit7 = mgr.get("audio/sound/playerHit7.ogg", Sound.class);
         playerDropGems1 = mgr.get("audio/sound/playerDropGems1.ogg", Sound.class);
         playerDropGems2 = mgr.get("audio/sound/playerDropGems2.ogg", Sound.class);
         playerDropGems3 = mgr.get("audio/sound/playerDropGems3.ogg", Sound.class);
         playerDropGems4 = mgr.get("audio/sound/playerDropGems4.ogg", Sound.class);
         playerImpact1 = mgr.get("audio/sound/playerImpact1.ogg", Sound.class);
         playerImpact2 = mgr.get("audio/sound/playerImpact2.ogg", Sound.class);
         playerImpact3 = mgr.get("audio/sound/playerImpact3.ogg", Sound.class);
         playerImpact4 = mgr.get("audio/sound/playerImpact4.ogg", Sound.class);

         intro1 = mgr.get("audio/sound/intro1.ogg", Sound.class);
         intro2 = mgr.get("audio/sound/intro2.ogg", Sound.class);
         intro3 = mgr.get("audio/sound/intro3.ogg", Sound.class);
         intro4 = mgr.get("audio/sound/intro4.ogg", Sound.class);
         intro5 = mgr.get("audio/sound/intro5.ogg", Sound.class);
         intro6 = mgr.get("audio/sound/intro6.ogg", Sound.class);
         intro7 = mgr.get("audio/sound/intro7.ogg", Sound.class);
         intro8 = mgr.get("audio/sound/intro8.ogg", Sound.class);

         warriorWalkout1 = mgr.get("audio/sound/warriorWalkout1.ogg", Sound.class);
         warriorWalkout2 = mgr.get("audio/sound/warriorWalkout2.ogg", Sound.class);
         warriorWalkout3 = mgr.get("audio/sound/warriorWalkout3.ogg", Sound.class);
         warriorWalkout4 = mgr.get("audio/sound/warriorWalkout4.ogg", Sound.class);
         warriorWalkout5 = mgr.get("audio/sound/warriorWalkout5.ogg", Sound.class);

         thiefWalkout1 = mgr.get("audio/sound/thiefWalkout1.ogg", Sound.class);
         thiefWalkout2 = mgr.get("audio/sound/thiefWalkout2.ogg", Sound.class);
         thiefWalkout3 = mgr.get("audio/sound/thiefWalkout3.ogg", Sound.class);
         thiefWalkout4 = mgr.get("audio/sound/thiefWalkout4.ogg", Sound.class);
         thiefWalkout5 = mgr.get("audio/sound/thiefWalkout5.ogg", Sound.class);

         clericWalkout1 = mgr.get("audio/sound/clericWalkout1.ogg", Sound.class);
         clericWalkout2 = mgr.get("audio/sound/clericWalkout2.ogg", Sound.class);
         clericWalkout3 = mgr.get("audio/sound/clericWalkout3.ogg", Sound.class);
         clericWalkout4 = mgr.get("audio/sound/clericWalkout4.ogg", Sound.class);
         clericWalkout5 = mgr.get("audio/sound/clericWalkout5.ogg", Sound.class);
         clericWalkout6 = mgr.get("audio/sound/clericWalkout6.ogg", Sound.class);
//         warriorMusic1Music = mgr.get("audio/music/warriorMusicA.ogg", Music.class);
//         = mgr.get("audio/sound/.ogg", Sound.class);
//         = mgr.get("audio/sound/.ogg", Sound.class);

        // Title screen stuff
        titleBackground = mgr.get("images/title-screen-elements/title-background_00.png");
        titleBlueBeam = mgr.get("images/title-screen-elements/title-char-beam-blue_00.png");
        titleGreenBeam = mgr.get("images/title-screen-elements/title-char-beam-green_01.png");
        titleRedBeam = mgr.get("images/title-screen-elements/title-char-beam-red_00.png");
        titleGradient = mgr.get("images/title-screen-elements/title-char-gradient_01.png");
        titleCleric = mgr.get("images/title-screen-elements/title-cleric_00.png");
        titleLight = mgr.get("images/title-screen-elements/title-gem-light_00.png");
        titlePrismHighlight = mgr.get("images/title-screen-elements/title-prism-right-highlight_00.png");
        titlePrismToGem = mgr.get("images/title-screen-elements/title-prism-to-gem_00.png");
        titleThief = mgr.get("images/title-screen-elements/title-thief_00.png");
        titleWarrior = mgr.get("images/title-screen-elements/title-warrior_00.png");
        titleWizard = mgr.get("images/title-screen-elements/title-wizard-prism_00.png");
        titleChromeEdge = mgr.get("images/title-screen-elements/title-word-chrome-edge_00.png");
        titleChromeGradient = mgr.get("images/title-screen-elements/title-word-chrome-gradient.png");
        titleTrigger = mgr.get("images/title-screen-elements/title-word-trigger-blank_00.png");
        titleHatPrism = mgr.get("images/title-screen-elements/title-hat-prism_00.png");

        initialized = true;
        return 1;
    }

    @Override
    public void dispose() {
        mgr.dispose();
        batch.dispose();
        pixel.dispose();
        font.dispose();
        smallFont.dispose();
        largeFont.dispose();
    }

    // ------------------------------------------------------------------------

    public static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal(vertSourcePath),
                Gdx.files.internal(fragSourcePath));
        String log = shaderProgram.getLog();

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + log);
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + log);
        } else if (Config.Debug.shader){
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log: " + log);
        }

        return shaderProgram;
    }

}
