package lando.systems.ld51.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.eskalon.commons.screen.ManagedScreen;
import lando.systems.ld51.Config;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.audio.AudioManager;

public abstract class BaseScreen extends ManagedScreen implements Disposable {

    public final Main game;
    public final Assets assets;
    public final TweenManager tween;
    public final SpriteBatch batch;
    public final OrthographicCamera windowCamera;
    public final Vector3 pointerPos;
    public AudioManager audio;

    public OrthographicCamera worldCamera;

    public BaseScreen() {
        this.game = Main.game;
        this.assets = game.assets;
        this.tween = game.tween;
        this.windowCamera = game.windowCamera;
        this.batch = assets.batch;
        this.pointerPos = new Vector3();
        this.audio = game.audio;
    }

    @Override
    protected void create() {
        worldCamera = new OrthographicCamera();
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
    }

    @Override
    public void hide() {

    }

    public void update(float delta) {
        windowCamera.update();
        if (worldCamera != null) {
            worldCamera.update();
        }
        audio.update(delta);
    }


    @Override
    public void resize(int width, int height) {
        windowCamera.setToOrtho(false, width, height);
        windowCamera.update();
    }

    @Override
    public void dispose() {}

}
