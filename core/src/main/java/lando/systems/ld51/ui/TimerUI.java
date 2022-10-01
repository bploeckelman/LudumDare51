package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;

public class TimerUI extends VisWindow {
    private Skin skin;
    private VisProgressBar progressBar;
    public TimerUI(String title, float x, float y, float width, float height, Skin skin) {
        super(title);
        this.skin = skin;
        VisWindow.WindowStyle defaultStyle = skin.get("default", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle upperUIStyle = new VisWindow.WindowStyle(defaultStyle);
        upperUIStyle.background = Assets.Patch.glass.drawable;
        setStyle(upperUIStyle);
        setPosition(x, y);
        setPosition(width, height);
//        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
//        VisProgressBar.ProgressBarStyle avalancheProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
//        progressBar = new VisProgressBar(0f, 100f, .1f, false, avalancheProgressBarStyle);
//        progressBar.setPosition(windowCamera.viewportWidth / 4f + 25f, windowCamera.viewportHeight * 7 / 8);
//        progressBar.setValue(0f);
//        progressBar.setWidth(windowCamera.viewportWidth / 2f - 50f);
//        progressBar.setHeight(70f);
//        addActor(progressBar);
    }

}
