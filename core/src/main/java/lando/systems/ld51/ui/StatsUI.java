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


        statsPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth - 550f, windowCamera.viewportHeight - 210f, 550f, 210f);


        setSize(statsPaneBoundsVisible.width, statsPaneBoundsVisible.height);
        setPosition(statsPaneBoundsVisible.x, statsPaneBoundsVisible.y);
        setMovable(false);
        align(Align.top | Align.center);
        setModal(false);
        setKeepWithinStage(false);
        //setColor(getColor().r, getColor().g, getColor().b, 1f);
        //setColor(Color.RED);

        Label label = new Label("Stats", skin, "large");
        add(label);
        row();
        Table statsTable = new Table();
        label = new Label("Game duration: " + (Stats.totalGameTime) + "sec", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Gem earned: " + (Stats.gemTotalEarned), skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Gem lost: " + (Stats.gemTotalLost) + "sec", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Longest time undamaged: " + (Stats.longestTimeBetweenHits) + "sec", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("White Wizard Count: " + (Stats.numTransitionToWhiteWizard), skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Enemy Killed: " + (Stats.numEnemyKilled), skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        add(statsTable);
    }

}
