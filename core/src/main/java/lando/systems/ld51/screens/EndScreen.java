package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.Config;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.ui.StatsUI;

public class EndScreen extends BaseScreen {

    private boolean exitingScreen = false;
    private int clickPhase;
    private float phaseAccum;
    private float endAccum;
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
    private boolean isEndOver = false;
    private StatsUI statsUI;

    private boolean outroNarrationIsGoing = false;

    public EndScreen() {
        whiteWithAlpha = new Color(Color.WHITE);
        clickPhase = 0;
        phaseAccum = 0;

        endAccum = 0;

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


    }

    @Override
    public void show() {
        game.getInputMultiplexer().addProcessor(uiStage);
        statsUI = new StatsUI(skin, assets, windowCamera);
        uiStage.addActor(statsUI);
        statsUI.setVisible(false);
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

        if(!outroNarrationIsGoing) {
            game.audio.playMusic(AudioManager.Musics.outroMusic);
            game.audio.playSound(AudioManager.Sounds.outro, 1.0F);
            outroNarrationIsGoing = true;
        }

        playerBounds1.x += endAccum * 2;
        playerBounds2.x += endAccum * 2;
        playerBounds3.x += endAccum * 2;

        if (!exitingScreen && Gdx.input.justTouched() && isEndOver && statsUI.isVisible()){
            exitingScreen = true;
            game.getScreenManager().pushScreen("credit", "blend");
        }

        if (((Gdx.input.justTouched() && phaseAccum > .2f) )&& !isEndOver) {

            endAccum += delta * 10;
            System.out.println(phaseAccum);

            playerBounds1.x = (float) (endAccum);
            playerBounds2.x = (float) (endAccum + 220);
            playerBounds3.x = (float) endAccum + 440;

            if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 5.5F) && !isEndOver) {

                // todo cancel playing sounds
//                game.audio.stopAllSounds();


                phaseAccum = 0;
                clickPhase++;


                switch (clickPhase) {
                    case 1:
//                        game.audio.playSound(AudioManager.Sounds.intro1, 1.5F);


                        subtitles = "When the dust finally settles, all we are left with is our self";

//                    subtitles = "Rage... Greed... Boastfulness";
                        // audio and texture changes etc here
                        break;
                    case 2:
//                        game.audio.playSound(AudioManager.Sounds.intro2, 2.5F);

//                    playerTexture1 = game.assets.playerAnimationByPhaseByState.get(Player.Phase.BLUE).get(Player.State.WALK).getKeyFrame(0);

                        subtitles = "No matter what demons we carry within us";
                        break;
                    case 3:
//                        game.audio.playSound(AudioManager.Sounds.intro3, 2.5F);
                        subtitles = "We cannot conquer our darkness \nby refusing to acknowledge it";
                        break;
                    case 4:
//                        game.audio.playSound(AudioManager.Sounds.intro4, 2.5F);
                        subtitles = "What we resist always persists";
                        break;
                    case 5:
//                        game.audio.playSound(AudioManager.Sounds.intro5, 2.5F);
                        subtitles = "Whether it be anger, greed, or boastfulness";
                        break;
                    case 6:
//                        game.audio.playSound(AudioManager.Sounds.intro6, 2.5F);
                        subtitles = "The answer lies not in conquering the world";
                        break;
                    case 7:
//                        game.audio.playSound(AudioManager.Sounds.intro7, 2.5F);
                        subtitles = "But in taming ourselves, accepting \nwhat is in true heart with honesty and compassion";
                        break;
                    case 8:
//                        game.audio.playSound(AudioManager.Sounds.intro8, 2.5F);
                        subtitles = "Well done!";
                        break;

                    default:
                        isEndOver = true;
                        statsUI.setVisible(true);
                        break;


                }
                // start new Audio



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
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.center, true);
            assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight / 7f + assets.layout.height);
            assets.largeFont.getData().setScale(1f);
            assets.largeFont.setColor(Color.WHITE);

            batch.setColor(Color.WHITE);
        }
        batch.end();
        uiStage.draw();
    }
}
