package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.gameobjects.Gem;
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
        redGemUI.setZIndex(1);

        redProgressBar = new GemProgressBar(1f, false, redGemUI.getX() + 10f, redGemUI.getY() + 2.5f, redGemUI.getWidth() - 20f, redGemUI.getHeight() - 5f, skin, assets, Gem.Type.RED);
//        redGemUI.addActor(redProgressBar);

        greenGemUI = new VisWindow("");
        greenGemUI.setPosition(x + width / 3f, y);
        greenGemUI.setSize(width / 3f, height);
        greenGemUI.setStyle(gemUIStyle);
        greenGemUI.setColor(Color.FOREST);
        greenGemUI.setZIndex(1);
        greenProgressBar = new GemProgressBar(1f, false, greenGemUI.getX() + 10f, greenGemUI.getY() + 2.5f, greenGemUI.getWidth() - 20f, greenGemUI.getHeight() - 5f, skin, assets, Gem.Type.GREEN);

        blueGemUI = new VisWindow("");
        blueGemUI.setPosition(x + 2f / 3f * width, y);
        blueGemUI.setSize(width / 3f, height);
        blueGemUI.setStyle(gemUIStyle);
        blueGemUI.setColor(0f, 0f, 1f, INACTIVE_ALPHA_VALUE);
        blueGemUI.setZIndex(1);
        blueProgressBar = new GemProgressBar(1f, false, blueGemUI.getX() + 10f, blueGemUI.getY() + 2.5f, blueGemUI.getWidth() - 20f, blueGemUI.getHeight() - 5f, skin, assets, Gem.Type.BLUE);

        addActor(redGemUI);
        addActor(blueGemUI);
        addActor(greenGemUI);

        addActor(redProgressBar);
        addActor(blueProgressBar);
        addActor(greenProgressBar);
    }

    public void updatePlayerGemsUIColor(Player player) {
        if (player.isWizard) {
            blueGemUI.setColor(Color.WHITE);
            redGemUI.setColor(Color.WHITE);
            greenGemUI.setColor(Color.WHITE);
        }
        else {
            switch (player.getCurrentPhase()) {
                case RED:
                    blueGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    redGemUI.setColor(1f, 0f, 0f, ACTIVE_ALPHA_VALUE);
                    greenGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    break;
                case GREEN:
                    blueGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    redGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    greenGemUI.setColor(Color.FOREST);
                    break;
                case BLUE:
                    blueGemUI.setColor(0f, 0f, 1f, ACTIVE_ALPHA_VALUE);
                    redGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    greenGemUI.setColor(0f, 0f, 0f, INACTIVE_ALPHA_VALUE);
                    break;
            }
        }
    }
}
