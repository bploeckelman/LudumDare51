package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.InputPrompts;

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
    private OrthographicCamera windowCamera;

    public TutorialUI(Assets assets, Skin skin, OrthographicCamera windowCamera, ChangeListener listener) {
        super();
        this.assets = assets;
        this.skin = skin;
        this.windowCamera = windowCamera;
        initializeUI(listener);

    }
    public void initializeUI(ChangeListener listener) {
        Window.WindowStyle defaultWindowStyle = skin.get("default", Window.WindowStyle.class);
        Window.WindowStyle glassWindowStyle = new Window.WindowStyle(defaultWindowStyle);
        glassWindowStyle.background = Assets.Patch.metal.drawable;
        glassWindowStyle.titleFont = assets.font;
        glassWindowStyle.titleFontColor = Color.BLACK;


        tutorialPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth/6f, 0, windowCamera.viewportWidth* 2f /3f, windowCamera.viewportHeight);
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
        greyOutWindow.setTouchable(Touchable.disabled);

        tutorialWindow = new VisWindow("", glassWindowStyle);
        tutorialWindow.setSize(tutorialPaneBoundsHidden.width, tutorialPaneBoundsHidden.height);
        tutorialWindow.setPosition(tutorialPaneBoundsHidden.x, tutorialPaneBoundsHidden.y);
        tutorialWindow.setMovable(false);
        tutorialWindow.align(Align.top | Align.center);
        tutorialWindow.setModal(false);
        tutorialWindow.setKeepWithinStage(false);
        //tutorialWindow.setColor(tutorialWindow.getColor().r, tutorialWindow.getColor().g, tutorialWindow.getColor().b, 1f);
        //tutorialWindow.setColor(Color.RED);

        Label settingLabel = new Label("Controls", skin, "larger");
        settingLabel.setColor(Color.ORANGE);
        tutorialWindow.add(settingLabel).padBottom(20f).row();

        Label controlLabel = new Label("Movement", skin, "default");
        controlLabel.setColor(Color.LIGHT_GRAY);
        tutorialWindow.add(controlLabel).padBottom(10f).row();

        Table movementControl = new Table();
        Table tableWasd = new Table();
        Table tableArrow = new Table();
        Image image;
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_key_w)));
        tableWasd.add(image).colspan(3);
        tableWasd.row();
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_key_a)));
        tableWasd.add(image);
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_key_s)));
        tableWasd.add(image);
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_key_d)));
        tableWasd.add(image);
        movementControl.add(tableWasd).padRight(20f);
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_arrow_up)));
        tableArrow.add(image).colspan(3);
        tableArrow.row();
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_arrow_left)));
        tableArrow.add(image);
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_arrow_down)));
        tableArrow.add(image);
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_arrow_right)));
        tableArrow.add(image);
        movementControl.add(tableArrow).padLeft(20f);
        movementControl.row();
        Label controlDescLabel1 = new Label("WASD", skin);
        controlDescLabel1.setColor(Color.DARK_GRAY);
        movementControl.add(controlDescLabel1).padRight(20f);
        controlDescLabel1 = new Label("Arrow", skin);
        movementControl.add(controlDescLabel1).padLeft(20f);
        controlDescLabel1.setColor(Color.DARK_GRAY);
        movementControl.row().padBottom(20f);
        controlDescLabel1 = new Label("Aim / Attack", skin, "default");
        controlDescLabel1.setColor(Color.LIGHT_GRAY);
        movementControl.add(controlDescLabel1).padTop(20f).colspan(2).padBottom(10f);
        movementControl.row();
        image = new Image(new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.mouse_light_left)));
        movementControl.add(image).colspan(2);
        movementControl.row();
        controlDescLabel1 = new Label("Left Mouse", skin);
        controlDescLabel1.setColor(Color.DARK_GRAY);
        movementControl.add(controlDescLabel1).colspan(2);
        tutorialWindow.add(movementControl);
        tutorialWindow.row();
        Label objectiveLabel = new Label("Objective", skin, "large");
        objectiveLabel.setColor(Color.FOREST);
        tutorialWindow.add(objectiveLabel).padTop(20f).padBottom(5f);
        tutorialWindow.row();

        Label objectiveDescLabel;
        Table objectiveTable = new Table();
        objectiveTable.padTop(10f);
        objectiveTable.align(Align.top | Align.left);
        objectiveDescLabel = new Label("- Kill the other wizard", skin);
        objectiveDescLabel.setColor(Color.RED);
        objectiveTable.add(objectiveDescLabel).align(Align.top | Align.left).padBottom(5f).row();
        objectiveDescLabel = new Label("- Collect all 3 gems to become a wizard", skin);
        objectiveDescLabel.setColor(Color.GREEN);
        objectiveTable.add(objectiveDescLabel).align(Align.top | Align.left).padBottom(5f).row();
        objectiveDescLabel = new Label("- Critical damage against same colors", skin);
        objectiveDescLabel.setColor(Color.ROYAL);
        objectiveTable.add(objectiveDescLabel).align(Align.top | Align.left).padBottom(5f).row();
        tutorialWindow.add(objectiveTable);
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

        closeTutorialTextButton = new TextButton("Start Game!", tutorialButtonStyle);
        closeTutorialTextButton.setColor(Color.LIME);
        tutorialWindow.row();
        tutorialWindow.add(closeTutorialTextButton).padTop(5f).padBottom(5f).width(tutorialWindow.getWidth() - 100f).height(40f);

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

        closeTutorialTextButton.addListener(listener);

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
