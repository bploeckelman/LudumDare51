package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;

public class StatsUI extends VisWindow {
    private Assets assets;
    private Skin skin;
    public VisWindow statsWindow;
    public TextButton closeStatsTextButton;
    public ImageButton closeStatsButton;
    private Rectangle statsPaneBoundsVisible;
    public MoveToAction hideStatsPaneAction;
    public MoveToAction showStatsPaneAction;
    private OrthographicCamera windowCamera;
    public StatsUI(Skin skin, Assets assets, OrthographicCamera windowCamera) {
        super("");
        this.skin = skin;
        this.assets = assets;
        this.windowCamera = windowCamera;
        initializeUI(skin, assets, windowCamera);
        
    }

    public void initializeUI(Skin skin, Assets assets, OrthographicCamera windowCamera) {
        Window.WindowStyle defaultWindowStyle = skin.get("default", Window.WindowStyle.class);
        Window.WindowStyle glassWindowStyle = new Window.WindowStyle(defaultWindowStyle);
        glassWindowStyle.background = Assets.Patch.glass.drawable;
        glassWindowStyle.titleFont = assets.font;
        glassWindowStyle.titleFontColor = Color.BLACK;
        setStyle(glassWindowStyle);


        statsPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth * 4f / 5f, windowCamera.viewportHeight * 2f / 3f, windowCamera.viewportWidth* 1f /5f, windowCamera.viewportHeight * 1f / 3f);


        statsWindow = new VisWindow("", glassWindowStyle);
        statsWindow.setSize(statsPaneBoundsVisible.width, statsPaneBoundsVisible.height);
        statsWindow.setPosition(statsPaneBoundsVisible.x, statsPaneBoundsVisible.y);
        statsWindow.setMovable(false);
        statsWindow.align(Align.top | Align.center);
        statsWindow.setModal(false);
        statsWindow.setKeepWithinStage(false);
        //statsWindow.setColor(statsWindow.getColor().r, statsWindow.getColor().g, statsWindow.getColor().b, 1f);
        //statsWindow.setColor(Color.RED);

        Label settingLabel = new Label("Controls", skin, "larger");
        statsWindow.add(settingLabel);
        statsWindow.row();

        addActor(statsWindow);
        addActor(closeStatsButton);
    }

}
