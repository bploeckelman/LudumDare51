package lando.systems.ld51.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.gameobjects.Player;
import org.omg.PortableInterceptor.ACTIVE;

public class PlayerGemsUI extends Group {
    VisWindow redGemUI;
    VisWindow greenGemUI;
    VisWindow blueGemUI;

    private final float INACTIVE_ALPHA_VALUE = .3f;
    private final float ACTIVE_ALPHA_VALUE = 1f;
    public PlayerGemsUI(String title, float x, float y, float width, float height, Skin skin) {
        //add 3 VisWindows
        VisWindow.WindowStyle defaultStyle = skin.get("default", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle gemUIStyle = new VisWindow.WindowStyle(defaultStyle);
        gemUIStyle.background = Assets.Patch.glass.drawable;

        redGemUI = new VisWindow("");
        redGemUI.setPosition(x, y);
        redGemUI.setSize(width / 3f, height);
        redGemUI.setStyle(gemUIStyle);
        redGemUI.setColor(1f, 0f, 0f, INACTIVE_ALPHA_VALUE);

        greenGemUI = new VisWindow("");
        greenGemUI.setPosition(x + width / 3f, y);
        greenGemUI.setSize(width / 3f, height);
        greenGemUI.setStyle(gemUIStyle);
        greenGemUI.setColor(0f, 1f, 0f, INACTIVE_ALPHA_VALUE);

        blueGemUI = new VisWindow("");
        blueGemUI.setPosition(x + 2f * width, y);
        blueGemUI.setSize(width / 3f, height);
        blueGemUI.setStyle(gemUIStyle);
        blueGemUI.setColor(0f, 0f, 1f, INACTIVE_ALPHA_VALUE);

        addActor(redGemUI);
        addActor(blueGemUI);
        addActor(greenGemUI);
    }

    public void update(Player.Phase phase) {
        if (phase == Player.Phase.BLUE) {
            blueGemUI.setColor(0f, 0f, 1f, ACTIVE_ALPHA_VALUE);
            redGemUI.setColor(1f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(0f, 1f, 0f, INACTIVE_ALPHA_VALUE);
        } else if (phase == Player.Phase.GREEN) {
            blueGemUI.setColor(0f, 0f, 1f, INACTIVE_ALPHA_VALUE);
            redGemUI.setColor(1f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(0f, 1f, 0f, ACTIVE_ALPHA_VALUE);
        } else if (phase == Player.Phase.RED) {
            blueGemUI.setColor(0f, 0f, 1f, INACTIVE_ALPHA_VALUE);
            redGemUI.setColor(1f, 0f, 0f, ACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(0f, 1f, 0f, INACTIVE_ALPHA_VALUE);
        }
    }
}
