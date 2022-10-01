package lando.systems.ld51.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
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
    protected Stage uiStage;
    protected Skin skin;
    private VisWindow debugWindow;

    public OrthographicCamera worldCamera;

    public BaseScreen() {
        this.game = Main.game;
        this.assets = game.assets;
        this.tween = game.tween;
        this.windowCamera = game.windowCamera;
        this.batch = assets.batch;
        this.pointerPos = new Vector3();
        this.audio = game.audio;
        initializeUI();
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
        uiStage.act(delta);
    }


    @Override
    public void resize(int width, int height) {
        windowCamera.setToOrtho(false, width, height);
        windowCamera.update();
    }

    @Override
    public void dispose() {
        uiStage.dispose();
    }

    protected void initializeUI() {
        // reset the stage in case it hasn't already been set to the current window camera orientation
        // NOTE - doesn't seem to be a way to directly set the stage camera as the window camera
        //  could go in the other direction, create the uiStage and set windowCam = stage.cam
        skin = VisUI.getSkin();
        StretchViewport viewport = new StretchViewport(windowCamera.viewportWidth, windowCamera.viewportHeight);
        uiStage = new Stage(viewport, batch);
    }

//    private void initializeDebugUI() {
//        debugWindow = new VisWindow("", true);
//        debugWindow.setFillParent(false);
//        debugWindow.setSize(160f, 50f);
//        debugWindow.setPosition(10f, windowCamera.viewportHeight-debugWindow.getHeight());
//        debugWindow.setColor(1f, 1f, 1f, 0.4f);
//        debugWindow.setKeepWithinStage(false);
//
//        VisLabel label;
//        Label.LabelStyle labelStyle = skin.get("outfit-medium-20px", Label.LabelStyle.class);
//
//        label = new VisLabel();
//        debugWindow.add(label).growX().row();
//        DebugElements.fpsLabel = label;
//
//        label = new VisLabel();
//        debugWindow.add(label).growX().row();
//        DebugElements.javaHeapLabel = label;
//
//        label = new VisLabel();
//        debugWindow.add(label).growX().row();
//        DebugElements.nativeHeapLabel = label;
//
//        label = new VisLabel();
//        debugWindow.add(label).growX().row();
//        DebugElements.drawCallLabel = label;
//
//        uiStage.addActor(debugWindow);
//
//        Actor rootActor = debugWindow;
//        Action transitionAction = Actions.moveTo(0, -windowCamera.viewportHeight, 0.1f, Interpolation.exp10In);
//        transitionAction.setActor(rootActor);
//        //debugWindow.addAction(transitionAction);
//    }
//
//    private void updateDebugElements() {
//        DebugElements.fpsLabel.setText(Config.getFpsString());
//        DebugElements.javaHeapLabel.setText(Config.getJavaHeapString());
//        DebugElements.nativeHeapLabel.setText(Config.getNativeHeapString());
//        DebugElements.drawCallLabel.setText(Config.getDrawCallString(batch));
//    }
//
//    private static class DebugElements {
//        public static VisLabel fpsLabel;
//        public static VisLabel javaHeapLabel;
//        public static VisLabel nativeHeapLabel;
//        public static VisLabel drawCallLabel;
//    }

}
