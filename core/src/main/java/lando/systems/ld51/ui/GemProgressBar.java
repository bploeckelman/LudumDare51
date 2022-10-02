package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.gameobjects.Gem;
import lando.systems.ld51.utils.Utils;

public class GemProgressBar extends VisProgressBar {

    private float knobAnimDuration = .5f;
    private float spinAnimCounter = 0f;
    public boolean isFlashing = false;
    private Gem.Type gemType;
    private Assets assets;
    private VisProgressBar.ProgressBarStyle gemProgressBarStyle;
    public GemProgressBar(float stepSize, boolean vertical, float x, float y, float width, float height, Skin skin, Assets assets, Gem.Type gemType) {
        super(0f, 100f, stepSize, vertical);
        this.assets = assets;
        this.gemType = gemType;
        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        gemProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);

        gemProgressBarStyle.background = new TextureRegionDrawable(Utils.getColoredTextureRegion(new Color(1f, 1f, 1f, 0f)));
        setStyle(gemProgressBarStyle);
        setPosition(x, y);
        setValue(0f);
        setSize(width, height);
        setZIndex(9);
    }

    public void flashIt() {
        isFlashing = true;
        knobAnimDuration = .5f;
    }

    public void update(float delta, float current, float max, boolean isWizard) {
        setValue(current / max * 100f);
        if (current >= max || isWizard) {
            spinAnimCounter += delta;
            knobAnimDuration = 0.5f;
            isFlashing = false;
            switch(gemType) {
                case RED:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedSpin.getKeyFrame(spinAnimCounter));
                    gemProgressBarStyle.knob.setMinWidth(40f);
                    gemProgressBarStyle.knob.setMinHeight(40f);
                    break;
                case BLUE:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemBlueSpin.getKeyFrame(spinAnimCounter));
                    gemProgressBarStyle.knob.setMinWidth(40f);
                    gemProgressBarStyle.knob.setMinHeight(40f);
                    break;
                case GREEN:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemGreenSpin.getKeyFrame(spinAnimCounter));
                    gemProgressBarStyle.knob.setMinWidth(40f);
                    gemProgressBarStyle.knob.setMinHeight(40f);
                    break;
                default:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedSpin.getKeyFrame(spinAnimCounter));
                    gemProgressBarStyle.knob.setMinWidth(40f);
                    gemProgressBarStyle.knob.setMinHeight(40f);
                    break;
            }
        }
        else if (isFlashing) {
            knobAnimDuration-=delta;
            switch(gemType) {
                case RED:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedIdle.getKeyFrame(knobAnimDuration));
                    break;
                case BLUE:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemBlueIdle.getKeyFrame(knobAnimDuration));
                    break;
                case GREEN:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemGreenIdle.getKeyFrame(knobAnimDuration));
                    break;
                default:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedIdle.getKeyFrame(0));
                    break;
            }
            gemProgressBarStyle.knob.setMinWidth(25f);
            gemProgressBarStyle.knob.setMinHeight(25f);
            setStyle(gemProgressBarStyle);
        } else {
            switch(gemType) {
                case RED:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedIdle.getKeyFrame(0f));
                    gemProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.redProgressBar);
                    break;
                case BLUE:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemBlueIdle.getKeyFrame(0f));
                    gemProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.blueProgressBar);
                    break;
                case GREEN:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemGreenIdle.getKeyFrame(0f));
                    gemProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.greenProgressBar);
                    break;
                default:
                    gemProgressBarStyle.knob = new TextureRegionDrawable(assets.gemRedIdle.getKeyFrame(0f));
                    gemProgressBarStyle.knobBefore = new TextureRegionDrawable(assets.whiteProgressBar);
                    break;
            }
            gemProgressBarStyle.knob.setMinWidth(25f);
            gemProgressBarStyle.knob.setMinHeight(25f);
        }
        if (knobAnimDuration < 0) {
            knobAnimDuration = .5f;
            isFlashing = false;
        }

    }
}
