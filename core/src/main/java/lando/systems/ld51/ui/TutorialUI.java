package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.InputPrompts;
import lando.systems.ld51.audio.AudioManager;

public class TutorialUI extends Group {

    private Assets assets;
    private Skin skin;
    public VisWindow tutorialWindow;
    public TextButton closeTutorialTextButton;
    public ImageButton closeTutorialButton;
    public VisWindow greyOutWindow;
    private Rectangle tutorialPaneBoundsVisible;
    private Rectangle tutorialPaneBoundsHidden;
    public boolean isTutorialShown = false;
    public MoveToAction hideTutorialPaneAction;
    public MoveToAction showTutorialPaneAction;
    public MoveToAction showCloseTutorialButtonAction;
    public MoveToAction hideCloseTutorialButtonAction;
    private AudioManager audio;
    private OrthographicCamera windowCamera;

    public TutorialUI(Assets assets, Skin skin, AudioManager audio, OrthographicCamera windowCamera) {
        super();
        this.assets = assets;
        this.skin = skin;
        this.audio = audio;
        this.windowCamera = windowCamera;
        initializeUI();

    }
    public void initializeUI() {
        Window.WindowStyle defaultWindowStyle = skin.get("default", Window.WindowStyle.class);
        Window.WindowStyle glassWindowStyle = new Window.WindowStyle(defaultWindowStyle);
        glassWindowStyle.background = Assets.Patch.metal.drawable;
        glassWindowStyle.titleFont = assets.font;
        glassWindowStyle.titleFontColor = Color.BLACK;


        tutorialPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth/4, 0, windowCamera.viewportWidth/2, windowCamera.viewportHeight);
        tutorialPaneBoundsHidden = new Rectangle(tutorialPaneBoundsVisible);
        tutorialPaneBoundsHidden.y -= tutorialPaneBoundsVisible.height;

        isTutorialShown = false;
//        Rectangle bounds = isTutorialhown ? tutorialPaneBoundsVisible : tutorialPaneBoundsHidden;

//        tutorialPane = new VisImage(Assets.Patch.glass_active.drawable);
//        tutorialPane.setSize(bounds.width, bounds.height);
//        tutorialPane.setPosition(bounds.x, bounds.y);
//        tutorialPane.setColor(Color.DARK_GRAY);

        greyOutWindow = new VisWindow("", true);
        greyOutWindow.setSize(windowCamera.viewportWidth, windowCamera.viewportHeight);
        greyOutWindow.setPosition(0f, 0f);
        greyOutWindow.setMovable(false);
        greyOutWindow.setColor(1f, 1f, 1f, .8f);
        greyOutWindow.setKeepWithinStage(false);
        greyOutWindow.setVisible(false);

        tutorialWindow = new VisWindow("", glassWindowStyle);
        tutorialWindow.setSize(tutorialPaneBoundsHidden.width, tutorialPaneBoundsHidden.height);
        tutorialWindow.setPosition(tutorialPaneBoundsHidden.x, tutorialPaneBoundsHidden.y);
        tutorialWindow.setMovable(false);
        tutorialWindow.align(Align.top | Align.center);
        tutorialWindow.setModal(false);
        tutorialWindow.setKeepWithinStage(false);
        //tutorialWindow.setColor(tutorialWindow.getColor().r, tutorialWindow.getColor().g, tutorialWindow.getColor().b, 1f);
        //tutorialWindow.setColor(Color.RED);

        Label settingLabel = new Label("Tutorial", skin);
        tutorialWindow.add(settingLabel).padBottom(40f).padTop(40f);
        tutorialWindow.row();
        Label musicVolumeLabel = new Label("Music Volume", skin);
        tutorialWindow.add(musicVolumeLabel).padBottom(10f);
        tutorialWindow.row();


        ImageButton.ImageButtonStyle defaultButtonStyle = skin.get("default", ImageButton.ImageButtonStyle.class);
        ImageButton.ImageButtonStyle closeButtonStyle = new ImageButton.ImageButtonStyle(defaultButtonStyle);
        closeButtonStyle.imageUp = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.button_light_power));
        closeButtonStyle.imageDown = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.button_light_power));
        closeButtonStyle.down = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.button_light_power));
        closeButtonStyle.up = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.button_light_power));
        closeButtonStyle.disabled = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.button_light_power));
        closeTutorialButton = new ImageButton(skin);
        closeTutorialButton.setStyle(closeButtonStyle);
        closeTutorialButton.setWidth(50f);
        closeTutorialButton.setHeight(50f);
        closeTutorialButton.setPosition(tutorialPaneBoundsHidden.x + tutorialPaneBoundsHidden.width - closeTutorialButton.getWidth(), tutorialPaneBoundsHidden.y + tutorialPaneBoundsHidden.height - closeTutorialButton.getHeight());
        closeTutorialButton.setClip(false);

        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle tutorialButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        tutorialButtonStyle.font = assets.smallFont;
        tutorialButtonStyle.fontColor = Color.WHITE;
        tutorialButtonStyle.up = Assets.Patch.glass.drawable;
        tutorialButtonStyle.down = Assets.Patch.glass_dim.drawable;
        tutorialButtonStyle.over = Assets.Patch.glass_dim.drawable;

        closeTutorialTextButton = new TextButton("Close Tutorial", tutorialButtonStyle);
        tutorialWindow.row();
        tutorialWindow.add(closeTutorialTextButton).padBottom(10f).width(tutorialWindow.getWidth() - 100f);

        float showDuration = 0.2f;
        float hideDuration = 0.1f;

        hideCloseTutorialButtonAction = new MoveToAction();
        hideCloseTutorialButtonAction.setPosition(tutorialPaneBoundsHidden.x + tutorialPaneBoundsHidden.width - closeTutorialButton.getWidth(), tutorialPaneBoundsHidden.y + tutorialPaneBoundsHidden.getHeight() - closeTutorialButton.getHeight());;
        hideCloseTutorialButtonAction.setDuration(hideDuration);
        showCloseTutorialButtonAction = new MoveToAction();
        showCloseTutorialButtonAction.setPosition(tutorialPaneBoundsVisible.x + tutorialPaneBoundsVisible.width - closeTutorialButton.getWidth(), tutorialPaneBoundsVisible.y + tutorialPaneBoundsVisible.getHeight() - closeTutorialButton.getHeight());
        showCloseTutorialButtonAction.setDuration(showDuration);

        closeTutorialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideTutorial();
            }
        });

        closeTutorialTextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideTutorial();
            }
        });

        //addActor(closeTutorialButton);


        hideTutorialPaneAction = new MoveToAction();
        hideTutorialPaneAction.setPosition(tutorialPaneBoundsHidden.x, tutorialPaneBoundsHidden.y);
        hideTutorialPaneAction.setDuration(hideDuration);
        //hideTutorialPaneAction.setActor(tutorialWindow);

        showTutorialPaneAction = new MoveToAction();
        showTutorialPaneAction.setPosition(tutorialPaneBoundsVisible.x, tutorialPaneBoundsVisible.y);
        showTutorialPaneAction.setDuration(showDuration);
        //showTutorialPaneAction.setActor(tutorialWindow);
        //greyOutWindow.addActor(tutorialWindow);
        addActor(greyOutWindow);
        addActor(tutorialWindow);
        addActor(closeTutorialButton);
    }

    public void hideTutorial() {
        hideTutorialPaneAction.reset();
        hideCloseTutorialButtonAction.reset();
        tutorialWindow.addAction(hideTutorialPaneAction);
        closeTutorialButton.addAction(hideCloseTutorialButtonAction);
        greyOutWindow.setVisible(false);
        isTutorialShown = false;
    }

    public void showTutorial() {
        showTutorialPaneAction.reset();
        showCloseTutorialButtonAction.reset();
        greyOutWindow.setZIndex(tutorialWindow.getZIndex() + 100);
        tutorialWindow.setZIndex(tutorialWindow.getZIndex() + 100);
        tutorialWindow.addAction(showTutorialPaneAction);
        closeTutorialButton.addAction(showCloseTutorialButtonAction);
        greyOutWindow.setVisible(true);
        isTutorialShown = true;
    }
}
