package lando.systems.ld51;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import de.damios.guacamole.gdx.graphics.NestableFrameBuffer;
import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import de.eskalon.commons.screen.transition.impl.PushTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.particles.Particles;
import lando.systems.ld51.screens.*;
import lando.systems.ld51.utils.Time;
import lando.systems.ld51.utils.accessors.*;

public class Main extends ManagedGame<BaseScreen, ScreenTransition> {

	public static Main game;

	public Assets assets;
	public TweenManager tween;
	public AudioManager audio;
	public Particles particles;
	public NestableFrameBuffer frameBuffer;
	public TextureRegion frameBufferRegion;
	public OrthographicCamera windowCamera;

	public float mainGameTimer = 0f;

	@Override
	public void create() {
		Main.game = this;

		Time.init();

		assets = new Assets();
		audio = new AudioManager(assets, tween);
		VisUI.load(game.assets.mgr.get("ui/uiskin.json", Skin.class));

		tween = new TweenManager();
		{
			Tween.setWaypointsLimit(4);
			Tween.setCombinedAttributesLimit(4);
			Tween.registerAccessor(Color.class, new ColorAccessor());
			Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
			Tween.registerAccessor(Vector2.class, new Vector2Accessor());
			Tween.registerAccessor(Vector3.class, new Vector3Accessor());
			Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
		};

		particles = new Particles(assets);

		Pixmap.Format format = Pixmap.Format.RGBA8888;
		int width = Config.Screen.framebuffer_width;
		int height = Config.Screen.framebuffer_height;
		boolean hasDepth = false;
		frameBuffer = new NestableFrameBuffer(format, width, height, hasDepth);
		Texture frameBufferTexture = frameBuffer.getColorBufferTexture();
		frameBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		frameBufferRegion = new TextureRegion(frameBufferTexture);
		frameBufferRegion.flip(false, true);

		windowCamera = new OrthographicCamera();
		windowCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
		windowCamera.update();

		BasicInputMultiplexer inputMux = getInputMultiplexer();//new BasicInputMultiplexer();
//		Gdx.input.setInputProcessor(inputMux);

		screenManager.initialize(inputMux, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		screenManager.addScreen("launch", new LaunchScreen());
		screenManager.addScreen("title", new TitleScreen());
		screenManager.addScreen("story", new StoryScreen());
		screenManager.addScreen("game", new GameScreen());
		screenManager.addScreen("endScreen", new EndScreen());
		screenManager.addScreen("credit", new CreditScreen());
		screenManager.addScreenTransition("blend", new BlendingTransition(assets.batch, 0.25f));
		screenManager.addScreenTransition("push", new PushTransition(assets.batch, SlidingDirection.UP, 0.25f));

		Gdx.input.setInputProcessor(game.getInputMultiplexer());

		if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
			screenManager.pushScreen("launch", "blend");
		} else {
			screenManager.pushScreen("title", "blend");
		}
	}

	public void update(float delta) {
		// handle top level input
		{
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				Gdx.app.exit();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) Config.Debug.general = !Config.Debug.general;
		}

		// update things that must update every tick
		{
			Time.update();
			GdxAI.getTimepiece().update(Time.delta);
			tween.update(Time.delta);
			mainGameTimer += Time.delta;
		}

		// handle a pause
		{
			if (Time.pause_timer > 0) {
				Time.pause_timer -= Time.delta;
				if (Time.pause_timer <= -0.0001f) {
					Time.delta = -Time.pause_timer;
				} else {
					// skip updates if we're paused
					return;
				}
			}
			Time.millis += Time.delta;
			Time.previous_elapsed = Time.elapsed_millis();
		}

		// update global systems
		{
			particles.update(delta);
			BaseScreen screen = screenManager.getCurrentScreen();
			if (screen != null) {
				screen.update(Time.delta);
			}
		}
	}

	@Override
	public void render() {
		update(Time.delta);
		screenManager.render(Time.delta);
	}

	@Override
	public void dispose() {
		VisUI.dispose(false);
		screenManager.getScreens().forEach(BaseScreen::dispose);
		frameBuffer.dispose();
		particles.dispose();
		if (assets.initialized) {
			assets.dispose();
		}
		Gdx.app.exit();
	}

}
