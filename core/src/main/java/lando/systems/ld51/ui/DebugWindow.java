package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.Config;

public class DebugWindow extends VisWindow {

    OrthographicCamera windowCamera;
    Skin skin;
    public DebugWindow(String title, boolean showWindowBorder, OrthographicCamera windowCamera, Skin skin) {
        super(title, showWindowBorder);
        this.windowCamera = windowCamera;
        this.skin = skin;
        initializeDebugUI();
    }

    private void initializeDebugUI() {
        //debugWindow = new VisWindow("", true);
        setFillParent(false);
        setSize(160f, 40f);
        setPosition(10f, windowCamera.viewportHeight - getHeight());
        setColor(1f, 1f, 1f, 0.4f);
        setKeepWithinStage(false);

        VisLabel label;
        Label.LabelStyle labelStyle = skin.get("outfit-medium-20px", Label.LabelStyle.class);

        label = new VisLabel();
        add(label).growX().row();
        DebugElements.fpsLabel = label;

        label = new VisLabel();
        add(label).growX().row();
        DebugElements.javaHeapLabel = label;

        label = new VisLabel();
        add(label).growX().row();
        DebugElements.nativeHeapLabel = label;
    }

    private static class DebugElements {
        public static VisLabel fpsLabel;
        public static VisLabel javaHeapLabel;
        public static VisLabel nativeHeapLabel;
    }

    public void update() {
        DebugElements.fpsLabel.setText(Config.getFpsString());
        DebugElements.javaHeapLabel.setText(Config.getJavaHeapString());
        DebugElements.nativeHeapLabel.setText(Config.getNativeHeapString());
    }
}
