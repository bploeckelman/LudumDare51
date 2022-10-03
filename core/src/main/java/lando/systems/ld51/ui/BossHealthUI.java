package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.gameobjects.Boss;
import lando.systems.ld51.utils.Utils;

public class BossHealthUI extends VisWindow {

    private Boss boss;
    private VisProgressBar.ProgressBarStyle bossProgressBarStyle;
    public VisProgressBar bossHealthBar;
    public Image bossImageLeft;
    public Image bossImageRight;
    public Assets assets;
    private OrthographicCamera windowCamera;

    public BossHealthUI(String title, boolean showWindowBorder, float x, float y, float width, float height, Skin skin, Assets assets, OrthographicCamera windowCamera) {
        super(title, showWindowBorder);
        this.assets = assets;
        this.windowCamera = windowCamera;
        VisWindow.WindowStyle defaultStyle = skin.get("default", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle upperUIStyle = new VisWindow.WindowStyle(defaultStyle);
        upperUIStyle.background = Assets.Patch.glass.drawable;
        setStyle(upperUIStyle);
        setPosition(x, y);
        setSize(width, height);

        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        bossProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        bossProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
        bossProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));
        bossHealthBar = new VisProgressBar(75f, 100f, .1f, false);
        bossHealthBar.setValue(100f);
        bossHealthBar.setStyle(bossProgressBarStyle);
        bossHealthBar.setSize(width - 10f, height -5f);
        bossHealthBar.setPosition(x + 5f, y + 2.5f);

        bossImageLeft = new Image(new TextureRegionDrawable(assets.bossPortraitRight));
        bossImageLeft.setPosition(0f, windowCamera.viewportHeight - bossImageLeft.getHeight());

        bossImageRight = new Image(new TextureRegionDrawable(assets.bossPortraitLeft));
        bossImageRight.setPosition(windowCamera.viewportWidth - bossImageRight.getWidth(), windowCamera.viewportHeight - bossImageRight.getHeight());
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public void update(float delta) {
        if (boss == null) {
            return;
        }
        float bossHealthPercentage = boss.health / boss.MAX_HEALTH * 100f;
        bossHealthBar.setValue(bossHealthPercentage);
        if (bossHealthPercentage >= 75f) {
            bossProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
            bossProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));
            bossHealthBar.setRange(75f, 100f);
            bossHealthBar.setStyle(bossProgressBarStyle);
        } else if (bossHealthPercentage >= 50f) {
            bossProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.ORANGE));
            bossProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
            bossHealthBar.setRange(50f, 75f);
            bossHealthBar.setStyle(bossProgressBarStyle);
        } else if (bossHealthPercentage >= 25f) {
            bossProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.RED));
            bossProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.ORANGE));
            bossHealthBar.setRange(25f, 50f);
            bossHealthBar.setStyle(bossProgressBarStyle);
        } else {
            bossProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.BLACK));
            bossProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.RED));
            bossHealthBar.setRange(0f, 25f);
            bossHealthBar.setStyle(bossProgressBarStyle);
        }
    }

}
