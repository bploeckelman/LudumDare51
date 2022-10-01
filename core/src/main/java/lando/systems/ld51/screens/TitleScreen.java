package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class TitleScreen extends BaseScreen {

    private TextureRegion gdx;
    private float state;

    @Override
    public void create() {
        gdx = assets.atlas.findRegion("libgdx");
        state = 0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        state += delta;

        if (Gdx.input.justTouched()){
            game.getScreenManager().pushScreen("game", "blend");
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.SKY);

        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            float left = (camera.viewportWidth  - gdx.getRegionWidth())  / 2f;
            float bottom = (camera.viewportHeight - gdx.getRegionHeight()) / 2f;
            TextureRegion dog = assets.dog.getKeyFrame(state);
            TextureRegion cat = assets.cat.getKeyFrame(state);
            batch.draw(dog, left - dog.getRegionWidth() * 2, bottom, dog.getRegionWidth() * 2, dog.getRegionHeight() * 2);
            batch.draw(gdx, left, bottom);
            batch.draw(cat, left + gdx.getRegionWidth(), bottom, cat.getRegionWidth() * 2, cat.getRegionHeight() * 2);
        }
        batch.end();
    }

}
