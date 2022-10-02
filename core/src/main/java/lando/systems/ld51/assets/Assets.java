package lando.systems.ld51.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.*;
import lando.systems.ld51.Config;
import lando.systems.ld51.gameobjects.Gem;
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

    public Texture pixel;
    public Texture noiseTex;
    public TextureRegion pixelRegion;
    public TextureRegion redProgressBar;
    public TextureRegion blueProgressBar;
    public TextureRegion greenProgressBar;
    public TextureRegion whiteProgressBar;

    public Animation<TextureRegion> cat;
    public Animation<TextureRegion> dog;
    public Animation<TextureRegion> gemBlueIdle;
    public Animation<TextureRegion> gemRedIdle;
    public Animation<TextureRegion> gemGreenIdle;
    public Animation<TextureRegion> gemBlueSpin;
    public Animation<TextureRegion> gemRedSpin;
    public Animation<TextureRegion> gemGreenSpin;

    public Array<Animation<TextureRegion>> numberParticles;
    public ObjectMap<Gem.Type, ObjectMap<Gem.State, Animation<AtlasRegion>>> gemAnimationByTypeByState;

    public SimplexNoise noise;

    public ShaderProgram shieldShader;

    public Music introMusicMusic;

    public Sound introMusicSound;
    public Sound warriorMusic1;
    public Sound warriorMusic2;
    public Sound warriorMusic3;
    public Sound rogueMusic1;
    public Sound rogueMusic2;
    public Sound rogueMusic3;
    public Sound clericMusic1;
    public Sound clericMusic2;
    public Sound clericMusic3;
    public Sound wizardMusic1;

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


            mgr.load("audio/sound/introMusic.ogg", Sound.class);
            mgr.load("audio/sound/wizardMusic1.ogg", Sound.class);

            mgr.load("audio/sound/warriorMusicA.ogg", Sound.class);
            mgr.load("audio/sound/warriorMusicB.ogg", Sound.class);
            mgr.load("audio/sound/warriorMusicC.ogg", Sound.class);
            mgr.load("audio/sound/rogueMusicA.ogg", Sound.class);
            mgr.load("audio/sound/rogueMusicB.ogg", Sound.class);
            mgr.load("audio/sound/rogueMusicC.ogg", Sound.class);
            mgr.load("audio/sound/clericMusicA.ogg", Sound.class);
            mgr.load("audio/sound/clericMusicB.ogg", Sound.class);
            mgr.load("audio/sound/clericMusicC.ogg", Sound.class);
//            mgr.load("audio/sound/introMusic.ogg", Sound.class);

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

        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");

        cat = new Animation<>(0.1f, atlas.findRegions("pets/cat"), Animation.PlayMode.LOOP);
        dog = new Animation<>(0.1f, atlas.findRegions("pets/dog"), Animation.PlayMode.LOOP);
        gemBlueIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-blue/gem-blue-idle/gem-blue-idle"), Animation.PlayMode.LOOP);
        gemBlueSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-blue/gem-blue-spin/gem-blue-spin"), Animation.PlayMode.LOOP);
        gemRedIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-red/gem-red-idle/gem-red-idle"), Animation.PlayMode.LOOP);
        gemRedSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-red/gem-red-spin/gem-red-spin"), Animation.PlayMode.LOOP);
        gemGreenIdle = new Animation<>(0.1f, atlas.findRegions("gems/gem-green/gem-green-idle/gem-green-idle"), Animation.PlayMode.LOOP);
        gemGreenSpin = new Animation<>(0.1f, atlas.findRegions("gems/gem-green/gem-green-spin/gem-green-spin"), Animation.PlayMode.LOOP);

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
                Animation<AtlasRegion> animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
                animationByState.put(state, animation);
            }
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
        introMusicSound = mgr.get("audio/sound/introMusic.ogg", Sound.class);
        wizardMusic1 = mgr.get("audio/sound/wizardMusic1.ogg", Sound.class);

        warriorMusic1 = mgr.get("audio/sound/warriorMusicB.ogg", Sound.class);
        warriorMusic2 = mgr.get("audio/sound/warriorMusicA.ogg", Sound.class);
        warriorMusic3 = mgr.get("audio/sound/warriorMusicC.ogg", Sound.class);

        rogueMusic1 = mgr.get("audio/sound/rogueMusicA.ogg", Sound.class);
        rogueMusic2 = mgr.get("audio/sound/rogueMusicC.ogg", Sound.class);
        rogueMusic3 = mgr.get("audio/sound/rogueMusicB.ogg", Sound.class);

        clericMusic1 = mgr.get("audio/sound/clericMusicC.ogg", Sound.class);
        clericMusic2 = mgr.get("audio/sound/clericMusicB.ogg", Sound.class);
        clericMusic3 = mgr.get("audio/sound/clericMusicA.ogg", Sound.class);

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
