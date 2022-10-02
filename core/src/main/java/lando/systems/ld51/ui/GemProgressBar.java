package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import lando.systems.ld51.utils.Utils;

public class GemProgressBar extends VisProgressBar {

    public GemProgressBar(float stepSize, boolean vertical, float x, float y, float width, float height, Skin skin, TextureRegion knobTexture, TextureRegion fillUpBar) {
        super(0f, 100f, stepSize, vertical);
        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        VisProgressBar.ProgressBarStyle gemProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        gemProgressBarStyle.knob = new TextureRegionDrawable(knobTexture);

        gemProgressBarStyle.background = new TextureRegionDrawable(Utils.getColoredTextureRegion(new Color(1f, 1f, 1f, 0f)));
        gemProgressBarStyle.knobBefore = new TextureRegionDrawable(fillUpBar);
        setStyle(gemProgressBarStyle);
        setPosition(x, y);
        setValue(0f);
        setSize(width, height);
    }

    public void updateProgress(float current, float max) {
        setValue(current / max * 100f);
    }
}
