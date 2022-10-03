package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.gameobjects.Player;


public class StoryScreen extends BaseScreen {

    private boolean exitingScreen = false;
    private int clickPhase;
    private float phaseAccum;
    private String subtitles;
    private Rectangle playerBounds;
    private TextureRegion playerTexture;
    private Color whiteWithAlpha;

    public StoryScreen() {
        whiteWithAlpha = new Color(Color.WHITE);
        clickPhase = 0;
        phaseAccum = 0;
        subtitles = "This is the first string";
        playerBounds = new Rectangle(100, 200, 100, 100);
        playerTexture = game.assets.playerAnimationByPhaseByState.get(Player.Phase.RED).get(Player.State.WALK).getKeyFrame(0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        phaseAccum += delta;

        if (Gdx.input.justTouched() && phaseAccum > .5f) {
            // todo cancel playing sounds
            phaseAccum = 0;
            clickPhase++;

            switch(clickPhase){
                case 1:
                    subtitles = "phase 2";
                    // audio and texture changes etc here
                    break;
                case 2:
                    subtitles = "phase 3";
                    break;
                case 3:
                    subtitles = "phase 4";
                    break;
                case 4 :
                    subtitles = "phase 5";
                    break;
                case 5:
                    subtitles = "phase 6";
                    break;
                case 6:
                    subtitles = "phase 7";
                    break;
                case 7:
                    subtitles = "phase 8";
                    break;
                case 8:
                    subtitles = "phase 9";
                    break;
            }
            // start new Audio

            if (!exitingScreen && clickPhase > 8) {
                exitingScreen = true;
                game.getScreenManager().pushScreen("game", "blend");
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        float alpha = MathUtils.clamp(phaseAccum * 1.0f, 0f, 1f );
        whiteWithAlpha.set(alpha, alpha, alpha, alpha);
        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            batch.setColor(whiteWithAlpha);
            batch.draw(playerTexture, playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

            assets.largeFont.getData().setScale(.4f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.center, false);
            assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight / 7f + assets.layout.height);
            assets.largeFont.getData().setScale(1f);
            assets.largeFont.setColor(Color.WHITE);

            batch.setColor(Color.WHITE);
        }
        batch.end();
    }
}
