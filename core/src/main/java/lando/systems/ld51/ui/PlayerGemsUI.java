package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.ItemTextures;
import lando.systems.ld51.assets.PixelUIs;
import lando.systems.ld51.gameobjects.Player;

public class PlayerGemsUI extends Group {
    VisWindow redGemUI;
    VisWindow greenGemUI;
    VisWindow blueGemUI;
    public GemProgressBar redProgressBar;
    public GemProgressBar blueProgressBar;
    public GemProgressBar greenProgressBar;


    private final float INACTIVE_ALPHA_VALUE = 1f;
    private final float ACTIVE_ALPHA_VALUE = 1f;
    public PlayerGemsUI(String title, float x, float y, float width, float height, Skin skin, Assets assets) {
        //add 3 VisWindows
        VisWindow.WindowStyle defaultStyle = skin.get("default", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle gemUIStyle = new VisWindow.WindowStyle(defaultStyle);
        gemUIStyle.background = Assets.Patch.metal.drawable;

        redGemUI = new VisWindow("");
        redGemUI.setPosition(x, y);
        redGemUI.setSize(width / 3f, height);
        redGemUI.setStyle(gemUIStyle);
        redGemUI.setColor(1f, 0f, 0f, INACTIVE_ALPHA_VALUE);

        redProgressBar = new GemProgressBar(.1f, false, redGemUI.getX() + 5f, redGemUI.getY() + 2.5f, redGemUI.getWidth() - 10f, redGemUI.getHeight() - 5f, skin, assets.itemTextures.get(ItemTextures.Type.gem_red), assets.pixelUIs.get(PixelUIs.Type.narrow_red_pill_center));
//        redGemUI.addActor(redProgressBar);

        greenGemUI = new VisWindow("");
        greenGemUI.setPosition(x + width / 3f, y);
        greenGemUI.setSize(width / 3f, height);
        greenGemUI.setStyle(gemUIStyle);
        greenGemUI.setColor(Color.FOREST);
        greenProgressBar = new GemProgressBar(.1f, false, greenGemUI.getX() + 5f, greenGemUI.getY() + 2.5f, greenGemUI.getWidth() - 10f, greenGemUI.getHeight() - 5f, skin, assets.itemTextures.get(ItemTextures.Type.gem_green), assets.pixelUIs.get(PixelUIs.Type.narrow_green_pill_center));

        blueGemUI = new VisWindow("");
        blueGemUI.setPosition(x + 2f / 3f * width, y);
        blueGemUI.setSize(width / 3f, height);
        blueGemUI.setStyle(gemUIStyle);
        blueGemUI.setColor(0f, 0f, 1f, INACTIVE_ALPHA_VALUE);
        blueProgressBar = new GemProgressBar(.1f, false, blueGemUI.getX() + 5f, blueGemUI.getY() + 2.5f, blueGemUI.getWidth() - 10f, blueGemUI.getHeight() - 5f, skin, assets.itemTextures.get(ItemTextures.Type.gem_blue), assets.pixelUIs.get(PixelUIs.Type.narrow_blue_pill_center));

        addActor(redGemUI);
        addActor(blueGemUI);
        addActor(greenGemUI);

        addActor(redProgressBar);
        addActor(blueProgressBar);
        addActor(greenProgressBar);
    }

    public void update(Player.Phase phase) {
        if (phase == Player.Phase.BLUE) {
            blueGemUI.setColor(0f, 0f, 1f, ACTIVE_ALPHA_VALUE);
            redGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
        } else if (phase == Player.Phase.GREEN) {
            blueGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            redGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(Color.FOREST);
        } else if (phase == Player.Phase.RED) {
            blueGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
            redGemUI.setColor(1f, 0f, 0f, ACTIVE_ALPHA_VALUE);
            greenGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
        }
    }
}
