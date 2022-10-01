package lando.systems.ld51.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.gameobjects.Player;

public class GameScreen extends BaseScreen {

    public Player player;

    public GameScreen(){
        this.player = new Player(this);
    }


    @Override
    public void update(float delta) {
        super.update(delta);

        player.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            player.render(batch);
        }
        batch.end();
    }
}
