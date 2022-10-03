package lando.systems.ld51.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.ui.SettingsUI;
import lando.systems.ld51.utils.accessors.ColorAccessor;
import lando.systems.ld51.utils.accessors.Vector2Accessor;

public class TitleScreen extends BaseScreen {

    private TextButton startGameButton;
    private TextButton creditButton;
    private TextButton settingsButton;
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    private final float BUTTON_PADDING = 10f;

    public Vector2 wizardPos;
    public Vector2 charPos;
    public Color beamFade;
    public MutableFloat chromeAlpha;
    public MutableFloat triggerAlpha;
    public Color gradient;
    public Color light;
    public Color triggerColor;

    public boolean drawUI;
    public boolean drawGradient;

    public float accum;

    @Override
    public void create() {
        drawUI = false;
        drawGradient = false;
        triggerColor = new Color(Color.WHITE);
        wizardPos = new Vector2(0, -500);
        charPos = new Vector2(200, 0);
        beamFade = new Color(1.1f, 1.1f, 0f, 1f);
        chromeAlpha = new MutableFloat(0);
        triggerAlpha = new MutableFloat(0);
        gradient = new Color(1.51f, 1.51f, 0f, 1f);
        light = new Color(0, 1f, .6f, .6f);
        game.audio.playMusic(AudioManager.Musics.introMusic);

        Timeline.createSequence()
                .delay(.5f)
                .push(Tween.to(wizardPos, Vector2Accessor.Y, .5f)
                        .target(0))
                .push(Tween.to(charPos, Vector2Accessor.X, .5f)
                        .target(0))
                .push(Tween.call((type, source) -> {
                    drawGradient = true;
                }))
                .push(Tween.to(gradient, ColorAccessor.R, 2.f)
                        .target(0f))
                .push(Tween.to(light, ColorAccessor.B, .5f)
                        .target(0f).ease(Linear.INOUT))
                .push(Tween.to(chromeAlpha, 1, .5f)
                        .target(1f))
                .push(Tween.to(triggerAlpha, 1, .5f)
                        .target(1f))
                .pushPause(1f)
                .push(Tween.call((type, source) -> {
                    drawUI = true;
                    game.getInputMultiplexer().addProcessor(uiStage);
                }))
                .start(tween);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(uiStage);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        accum += delta;
        triggerColor.fromHsv(accum*30f, 1f, 1f);
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

            batch.setShader(assets.titleShader);
            batch.setColor(light);
            batch.draw(assets.titleLight, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.setShader(null);

            batch.setColor(Color.WHITE);

            batch.draw(assets.titleWizard, 0, wizardPos.y, windowCamera.viewportWidth, windowCamera.viewportHeight);



            batch.setShader(assets.titleShader);
            {
                if (drawGradient) {
                    batch.setColor(gradient);
                    batch.draw(assets.titleGradient, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

                    batch.draw(assets.titlePrismHighlight, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
                    batch.draw(assets.titlePrismToGem, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
                    batch.draw(assets.titleHatPrism, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

                    batch.draw(assets.titleBlueBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
                    batch.draw(assets.titleGreenBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
                    batch.draw(assets.titleRedBeam, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
                }
            }
            batch.setShader(null);
            batch.setColor(Color.WHITE);

            batch.draw(assets.titleThief, charPos.x, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleWarrior, charPos.x, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleCleric, charPos.x, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

            batch.setColor(1, 1, 1, chromeAlpha.floatValue());
            batch.draw(assets.titleChromeEdge, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.titleChromeGradient, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.setColor(triggerColor.r, triggerColor.g, triggerColor.b, triggerAlpha.floatValue());
            batch.draw(assets.titleTrigger, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

            batch.setColor(Color.WHITE);

        }
        batch.end();
        if (drawUI) {
            uiStage.draw();
        }
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

        float left = windowCamera.viewportWidth * (5f / 8f);
        float top = windowCamera.viewportHeight * (1f / 2f);

        startGameButton = new TextButton("Start Game", titleScreenButtonStyle);
        Gdx.app.log("startbuttonwidth&height", "width: " + startGameButton.getWidth() + " & height: " + startGameButton.getHeight());
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(left, top);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audio.stopAllSounds();
                game.getScreenManager().pushScreen("story", "blend");
            }
        });

        settingsButton = new TextButton("Settings", titleScreenButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(left, startGameButton.getY() - startGameButton.getHeight() - BUTTON_PADDING);
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsUI.showSettings();
            }
        });


        creditButton = new TextButton("Credits", titleScreenButtonStyle);
        creditButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        creditButton.setPosition(left, settingsButton.getY() - settingsButton.getHeight() - BUTTON_PADDING);
        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenManager().pushScreen("credit", "blend");
            }
        });


        uiStage.addActor(startGameButton);
        uiStage.addActor(settingsButton);
        uiStage.addActor(creditButton);
        uiStage.addActor(settingsUI);
    }

}
