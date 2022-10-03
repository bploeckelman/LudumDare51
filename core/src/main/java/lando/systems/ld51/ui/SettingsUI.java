package lando.systems.ld51.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.audio.AudioManager;

public class SettingsUI extends Group {

    private Assets assets;
    private Skin skin;
    public VisWindow settingsWindow;
    public ImageButton closeSettingsButton;
    public VisWindow greyOutWindow;
    private Rectangle settingsPaneBoundsVisible;
    private Rectangle settingsPaneBoundsHidden;
    public boolean isSettingShown;
    public MoveToAction hideSettingsPaneAction;
    public MoveToAction showSettingsPaneAction;
    public MoveToAction showCloseSettingsButtonAction;
    public MoveToAction hideCloseSettingsButtonAction;
    private AudioManager audio;
    private OrthographicCamera windowCamera;

    public SettingsUI(Assets assets, Skin skin, AudioManager audio, OrthographicCamera windowCamera) {
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

        VisSlider.SliderStyle horizontalSliderStyle = skin.get("default-horizontal", VisSlider.SliderStyle.class);
        VisSlider.SliderStyle customCatSliderStyle = new VisSlider.SliderStyle(horizontalSliderStyle);
        customCatSliderStyle.knob = new TextureRegionDrawable(assets.cat.getKeyFrame(0));
        customCatSliderStyle.knobDown = customCatSliderStyle.knob;
        customCatSliderStyle.knobOver = customCatSliderStyle.knob;

        VisSlider.SliderStyle customDogSliderStyle = new VisSlider.SliderStyle(horizontalSliderStyle);
        customDogSliderStyle.knob = new TextureRegionDrawable(assets.dog.getKeyFrame(0));
        customDogSliderStyle.knobDown = customDogSliderStyle.knob;
        customDogSliderStyle.knobOver = customDogSliderStyle.knob;

        settingsPaneBoundsVisible = new Rectangle(windowCamera.viewportWidth/4, 0, windowCamera.viewportWidth/2, windowCamera.viewportHeight);
        settingsPaneBoundsHidden = new Rectangle(settingsPaneBoundsVisible);
        settingsPaneBoundsHidden.y -= settingsPaneBoundsVisible.height;

        isSettingShown = false;
//        Rectangle bounds = isSettingShown ? settingsPaneBoundsVisible : settingsPaneBoundsHidden;

//        settingsPane = new VisImage(Assets.Patch.glass_active.drawable);
//        settingsPane.setSize(bounds.width, bounds.height);
//        settingsPane.setPosition(bounds.x, bounds.y);
//        settingsPane.setColor(Color.DARK_GRAY);

        greyOutWindow = new VisWindow("", true);
        greyOutWindow.setSize(windowCamera.viewportWidth, windowCamera.viewportHeight);
        greyOutWindow.setPosition(0f, 0f);
        greyOutWindow.setMovable(false);
        greyOutWindow.setColor(1f, 1f, 1f, .8f);
        greyOutWindow.setKeepWithinStage(false);
        greyOutWindow.setVisible(false);

        settingsWindow = new VisWindow("", glassWindowStyle);
        settingsWindow.setSize(settingsPaneBoundsHidden.width, settingsPaneBoundsHidden.height);
        settingsWindow.setPosition(settingsPaneBoundsHidden.x, settingsPaneBoundsHidden.y);
        settingsWindow.setMovable(false);
        settingsWindow.align(Align.top | Align.center);
        settingsWindow.setModal(false);
        settingsWindow.setKeepWithinStage(false);
        //settingsWindow.setColor(settingsWindow.getColor().r, settingsWindow.getColor().g, settingsWindow.getColor().b, 1f);
        //settingsWindow.setColor(Color.RED);

        Label settingLabel = new Label("Settings", skin);
        settingsWindow.add(settingLabel).padBottom(40f).padTop(40f);
        settingsWindow.row();
        Label musicVolumeLabel = new Label("Music Volume", skin);
        settingsWindow.add(musicVolumeLabel).padBottom(10f);
        settingsWindow.row();
        VisSlider musicSlider = new VisSlider(0f, 1f, .01f, false, customCatSliderStyle);
        musicSlider.setValue(audio.musicVolume.floatValue());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audio.setMusicVolume(musicSlider.getValue());
            }
        });
        settingsWindow.add(musicSlider).padBottom(10f).width(settingsWindow.getWidth() - 100f);
        settingsWindow.row();
        Label soundVolumeLevel = new Label("Sound Volume", skin);
        settingsWindow.add(soundVolumeLevel).padBottom(10f);
        settingsWindow.row();
        VisSlider soundSlider = new VisSlider(0f, 1f, .01f, false, customDogSliderStyle);
        soundSlider.setValue(audio.soundVolume.floatValue());
        soundSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                audio.setSoundVolume(soundSlider.getValue());
                audio.playSound(AudioManager.Sounds.collect);
            }
        });
        settingsWindow.add(soundSlider).padBottom(10f).width(settingsWindow.getWidth() - 100f);
        settingsWindow.row();

        closeSettingsButton = new ImageButton(skin, "default");
        closeSettingsButton.setWidth(50f);
        closeSettingsButton.setHeight(50f);
        closeSettingsButton.setPosition(settingsPaneBoundsHidden.x + settingsPaneBoundsHidden.width - closeSettingsButton.getWidth(), settingsPaneBoundsHidden.y + settingsPaneBoundsHidden.height - closeSettingsButton.getHeight());
        closeSettingsButton.setClip(false);

        float showDuration = 0.2f;
        float hideDuration = 0.1f;

        hideCloseSettingsButtonAction = new MoveToAction();
        hideCloseSettingsButtonAction.setPosition(settingsPaneBoundsHidden.x + settingsPaneBoundsHidden.width - closeSettingsButton.getWidth(), settingsPaneBoundsHidden.y + settingsPaneBoundsHidden.getHeight() - closeSettingsButton.getHeight());;
        hideCloseSettingsButtonAction.setDuration(hideDuration);
        showCloseSettingsButtonAction = new MoveToAction();
        showCloseSettingsButtonAction.setPosition(settingsPaneBoundsVisible.x + settingsPaneBoundsVisible.width - closeSettingsButton.getWidth(), settingsPaneBoundsVisible.y + settingsPaneBoundsVisible.getHeight() - closeSettingsButton.getHeight());
        showCloseSettingsButtonAction.setDuration(showDuration);

        closeSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideSettings();
            }
        });

        //addActor(closeSettingsButton);


        hideSettingsPaneAction = new MoveToAction();
        hideSettingsPaneAction.setPosition(settingsPaneBoundsHidden.x, settingsPaneBoundsHidden.y);
        hideSettingsPaneAction.setDuration(hideDuration);
        //hideSettingsPaneAction.setActor(settingsWindow);

        showSettingsPaneAction = new MoveToAction();
        showSettingsPaneAction.setPosition(settingsPaneBoundsVisible.x, settingsPaneBoundsVisible.y);
        showSettingsPaneAction.setDuration(showDuration);
        //showSettingsPaneAction.setActor(settingsWindow);
        //greyOutWindow.addActor(settingsWindow);
        addActor(greyOutWindow);
        addActor(settingsWindow);
        addActor(closeSettingsButton);
    }

    public void hideSettings() {
        hideSettingsPaneAction.reset();
        hideCloseSettingsButtonAction.reset();
        settingsWindow.addAction(hideSettingsPaneAction);
        closeSettingsButton.addAction(hideCloseSettingsButtonAction);
        greyOutWindow.setVisible(false);
        isSettingShown = false;
    }
    
    public void showSettings() {
        showSettingsPaneAction.reset();
        showCloseSettingsButtonAction.reset();
        greyOutWindow.setZIndex(settingsWindow.getZIndex() + 100);
        settingsWindow.setZIndex(settingsWindow.getZIndex() + 100);
        settingsWindow.addAction(showSettingsPaneAction);
        closeSettingsButton.addAction(showCloseSettingsButtonAction);
        greyOutWindow.setVisible(true);
        isSettingShown = true;
    }


}
