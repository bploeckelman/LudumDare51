package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.ui.SettingsUI;

public class TitleScreen extends BaseScreen {

    private TextureRegion gdx;
    private float state;
    private TextButton startGameButton;
    private TextButton creditButton;
    private TextButton settingsButton;
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    private final float BUTTON_PADDING = 10f;

    @Override
    public void create() {
        gdx = assets.atlas.findRegion("libgdx");
        state = 0f;
        game.audio.playMusic(AudioManager.Musics.introMusic);
        InputMultiplexer mux = new InputMultiplexer(this, uiStage);
        Gdx.input.setInputProcessor(mux);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        state += delta;

    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            batch.draw(assets.titleBackground, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleLight, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleWizard, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleGradient, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titlePrismHighlight, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titlePrismToGem, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleHatPrism, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleThief, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleWarrior, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleCleric, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleBlueBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleGreenBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleRedBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleChromeEdge, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleChromeGradient, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleTrigger, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

        }
        batch.end();
        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();

        SettingsUI settingsUI = new SettingsUI(assets, skin, audio, windowCamera);

        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;

        startGameButton = new TextButton("Start Game", titleScreenButtonStyle);
        Gdx.app.log("startbuttonwidth&height", "width: " + startGameButton.getWidth() + " & height: " + startGameButton.getHeight());
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(windowCamera.viewportWidth / 2f - startGameButton.getWidth() / 2f, windowCamera.viewportHeight / 3f);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audio.stopAllSounds();
                game.getScreenManager().pushScreen("game", "blend");
            }
        });

        settingsButton = new TextButton("Settings", titleScreenButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(windowCamera.viewportWidth / 2f - settingsButton.getWidth() / 2f, startGameButton.getY() - startGameButton.getHeight() - BUTTON_PADDING);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsUI.showSettings();
            }
        });


        creditButton = new TextButton("Credits", titleScreenButtonStyle);
        creditButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        creditButton.setPosition(windowCamera.viewportWidth / 2f - creditButton.getWidth() / 2f, settingsButton.getY() - settingsButton.getHeight() - BUTTON_PADDING);
        creditButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushScreen("credit", "blend");
            }
        });


        uiStage.addActor(startGameButton);
        uiStage.addActor(settingsButton);
        uiStage.addActor(creditButton);
        uiStage.addActor(settingsUI);
    }

}
