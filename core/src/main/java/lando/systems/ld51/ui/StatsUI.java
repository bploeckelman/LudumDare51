package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;

public class StatsUI extends VisWindow {
    private Assets assets;
    private Skin skin;
    private Rectangle statsPaneBoundsVisible;
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


        statsPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth / 6f, windowCamera.viewportHeight / 4f, windowCamera.viewportWidth * 2f / 3f, windowCamera.viewportHeight / 2f);


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
        label = new Label("Game duration: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label((Stats.totalGameTime) + "sec", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Gem earned: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label((Stats.gemTotalEarned) + " gems", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Gem lost: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label((Stats.gemTotalLost) + " gems", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Longest time undamaged: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label((Stats.longestTimeBetweenHits) + "sec", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("White Wizard Count: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label((Stats.numTransitionToWhiteWizard) + "times", skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        label = new Label("Enemy Killed: ", skin);
        statsTable.add(label).align(Align.left);
        label = new Label(String.valueOf(Stats.numEnemyKilled), skin);
        statsTable.add(label).align(Align.left);
        statsTable.row();
        add(statsTable);
    }

}
