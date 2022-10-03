package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.Config;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.ui.TutorialUI;

public class StoryScreen extends BaseScreen {

    private boolean exitingScreen = false;
    private int clickPhase;
    private float phaseAccum;
    private float storyAccum;
    private String subtitles;
    private Rectangle playerBounds1;
    private Rectangle playerBounds2;
    private Rectangle playerBounds3;
    private Rectangle backgroundImage;
    private TextureRegion playerTexture1;
    private TextureRegion playerTexture2;
    private TextureRegion playerTexture3;
    private Texture backgroundTexture;
    private Color whiteWithAlpha;
    private boolean isStoryOver = false;
    private boolean isTutorialShown = false;
    private TutorialUI tutorialUI;

    public StoryScreen() {
        whiteWithAlpha = new Color(Color.WHITE);
        clickPhase = 0;
        phaseAccum = 0;

        storyAccum = 0;

        subtitles = " ";
        backgroundImage = new Rectangle(0, 100, 900,  600);
        backgroundTexture = game.assets.titleBackground;

        playerBounds1 = new Rectangle(10, 200, 300, 300);
        playerTexture1 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.RED).get(Player.State.WALK).getKeyFrame(0);

        playerBounds2 = new Rectangle(220, 200, 300, 300);
        playerTexture2 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.GREEN).get(Player.State.WALK).getKeyFrame(0);

        playerBounds3 = new Rectangle(440, 200, 300, 300);
        playerTexture3 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.BLUE).get(Player.State.WALK).getKeyFrame(0);

        subtitles = "";
        playerTexture1 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.RED).get(Player.State.WALK).getKeyFrame(0);
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushScreen("game", "blend");
            }
        };
        tutorialUI = new TutorialUI(assets, skin, windowCamera, listener);
        uiStage.addActor(tutorialUI);
    }

    @Override
    public void show() {
        game.getInputMultiplexer().addProcessor(uiStage);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(uiStage);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        phaseAccum += delta;
        uiStage.setDebugAll(false);
        if (Config.Debug.general) {
            uiStage.setDebugAll(true);
        }

        playerBounds1.x += storyAccum * 2;
        playerBounds2.x += storyAccum * 2;
        playerBounds3.x += storyAccum * 2;

        if(clickPhase == 7 && phaseAccum > 4) {
            subtitles = "It's kind of a whole thing";
        }
        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 7.25F)&& !isStoryOver) {

            storyAccum += delta * 10;

            playerBounds1.x = (float) (storyAccum);
            playerBounds2.x = (float) (storyAccum + 220);
            playerBounds3.x = (float) storyAccum + 400;

            if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 6F) && !isStoryOver) {

                // todo cancel playing sounds
                game.audio.stopAllSounds();


                phaseAccum = 0;
                clickPhase++;



                switch (clickPhase) {
                    case 1:
                        game.audio.playSound(AudioManager.Sounds.intro1, 1.5F);

                        playerTexture1 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.RED).get(Player.State.WALK).getKeyFrame(0);


                        subtitles = "Inside each of us there are a great many darknesses";

//                    subtitles = "Rage... Greed... Boastfulness";
                        // audio and texture changes etc here
                        break;
                    case 2:
                        game.audio.playSound(AudioManager.Sounds.intro2, 2.5F);

//                    playerTexture1 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.BLUE).get(Player.State.WALK).getKeyFrame(0);

                        subtitles = "Rage... Greed... Boastfulness";
                        break;
                    case 3:
                        game.audio.playSound(AudioManager.Sounds.intro3, 2.5F);
                        subtitles = "If left unchecked, any one of these can become our undoing";
                        break;
                    case 4:
                        game.audio.playSound(AudioManager.Sounds.intro4, 2.5F);
                        subtitles = "But if we can accept and integrate all the parts of ourselves...";
                        break;
                    case 5:
                        game.audio.playSound(AudioManager.Sounds.intro5, 2.5F);
                        subtitles = "And also encourage them to collect the gems that correspond \n to their respective color";
                        break;
                    case 6:
                        game.audio.playSound(AudioManager.Sounds.intro6, 2.5F);
                        subtitles = "It transforms us into the most powerful version of ourselves...";
                        break;
                    case 7:
                        game.audio.playSound(AudioManager.Sounds.intro7, 2.5F);

                        subtitles = "But we only have like, ten seconds to do it each time.";
                        if(phaseAccum > 3.0F) {
                            subtitles = "It's kind of a whole thing";

                        }
                        break;
                    case 8:
                        game.audio.playSound(AudioManager.Sounds.intro8, 2.5F);
                        subtitles = "Let us begin the work!";
                        break;

                    default:
                        isStoryOver = true;
                        break;


                }
                // start new Audio
                if (isStoryOver && isTutorialShown == false) {
                    isTutorialShown = true;
                    tutorialUI.showTutorial();
                }
            }
            uiStage.act(delta);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        float alpha = MathUtils.clamp(phaseAccum * 3.0f, 0f, 1f );
        whiteWithAlpha.set(alpha, alpha, alpha, alpha);
        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            batch.setColor(whiteWithAlpha);
            batch.draw(backgroundTexture, 0, 00, 1000, 600);
            batch.draw(playerTexture1, playerBounds1.x, playerBounds1.y, playerBounds1.width, playerBounds1.height);
            batch.draw(playerTexture2, playerBounds2.x, playerBounds2.y, playerBounds2.width, playerBounds2.height);
            batch.draw(playerTexture3, playerBounds3.x, playerBounds3.y, playerBounds3.width, playerBounds3.height);

            assets.largeFont.getData().setScale(.4f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.center, false);
            assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight / 7f + assets.layout.height);
            assets.largeFont.getData().setScale(1f);
            assets.largeFont.setColor(Color.WHITE);

            batch.setColor(Color.WHITE);
        }
        batch.end();
        uiStage.draw();
    }
}
