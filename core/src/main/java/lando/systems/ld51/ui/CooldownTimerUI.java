package lando.systems.ld51.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.ItemTextures;
import lando.systems.ld51.gameobjects.Player;

public class CooldownTimerUI extends VisWindow {
    public VisProgressBar timerProgressBar;
    public float bossCooldownRemainingPercentage;
    private VisProgressBar.ProgressBarStyle timerProgressBarStyle;
    private Assets assets;
    public CooldownTimerUI(float x, float y, float width, float height, Skin skin, Assets assets) {
        super("");
        this.assets = assets;
        setPosition(x, y);
        setSize(width, height);
        setColor(getColor().r, getColor().g, getColor().b, .5f);
        setZIndex(1);
        timerProgressBar = new VisProgressBar(0f, 100f, 1f, false);
        VisProgressBar.ProgressBarStyle defaultStyle = skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        timerProgressBarStyle = new VisProgressBar.ProgressBarStyle(defaultStyle);
        timerProgressBarStyle.knob = new TextureRegionDrawable(assets.itemTextures.get(ItemTextures.Type.clock));
        timerProgressBar.setPosition(0f, 0f);
        timerProgressBar.setSize(width, height);
        timerProgressBar.setValue(100f);
        timerProgressBar.setStyle(timerProgressBarStyle);
        timerProgressBar.setZIndex(9);
        addActor(timerProgressBar);
    }

    public void updateTimerValue(Player player, float accum) {
        bossCooldownRemainingPercentage = 100f-(accum % 10f) * 10f;
        timerProgressBar.setValue(bossCooldownRemainingPercentage);
        if (player.isWizard) {
            timerProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.whiteProgressBar);
            timerProgressBar.setStyle(timerProgressBarStyle);
        }
        else {
            switch (player.getCurrentPhase()) {
                case RED:
                    timerProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.redProgressBar);
                    timerProgressBar.setStyle(timerProgressBarStyle);
                    break;
                case BLUE:
                    timerProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.blueProgressBar);
                    timerProgressBar.setStyle(timerProgressBarStyle);
                    break;
                case GREEN:
                    timerProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.greenProgressBar);
                    timerProgressBar.setStyle(timerProgressBarStyle);
                    break;
            }
        }
    }
}
