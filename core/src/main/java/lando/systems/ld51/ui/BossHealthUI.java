package lando.systems.ld51.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;

public class BossHealthUI extends VisWindow {

    public BossHealthUI(String title, boolean showWindowBorder, float x, float y, float width, float height, Skin skin) {
        super(title, showWindowBorder);
        VisWindow.WindowStyle defaultStyle = skin.get("default", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle upperUIStyle = new VisWindow.WindowStyle(defaultStyle);
        upperUIStyle.background = Assets.Patch.glass.drawable;
        setStyle(upperUIStyle);
        setPosition(x, y);
        setSize(width, height);
    }

}
