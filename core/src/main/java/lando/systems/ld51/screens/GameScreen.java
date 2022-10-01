package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.Config;
import lando.systems.ld51.gameobjects.Arena;
import lando.systems.ld51.gameobjects.Gem;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.utils.FollowOrthographicCamera;

public class GameScreen extends BaseScreen {

    public Player player;
    public Arena arena;
    public float accum;
    public Array<Gem> gems;


    public GameScreen(){
        this.player = new Player(this);
        this.accum = 0;
        this.gems = new Array<>();
        this.arena = new Arena();
    }

    @Override
    protected void create() {
        worldCamera = new FollowOrthographicCamera();
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        arena.update(delta);
        if (MathUtils.random(1f) > .97f){ // THIS IS PLACEHOLDER
            int randType = MathUtils.random(2);
            Gem.Type type = Gem.Type.RED;
            switch(randType){
                case 0:
                    type = Gem.Type.GREEN;
                    break;
                case 1:
                    type = Gem.Type.BLUE;
                    break;
            }
            gems.add(new Gem(this, new Vector2(MathUtils.random(Config.Screen.window_width), MathUtils.random(Config.Screen.window_height)), type));
        }
        accum += delta;
        player.update(delta);
        for (int i = gems.size -1; i >= 0; i--) {
            Gem gem = gems.get(i);
            gem.update(delta);
            if (gem.collected){
                gems.removeIndex(i);
            }
        }

        // Camera follow things
        ((FollowOrthographicCamera)worldCamera).update(player.position, delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        OrthographicCamera camera = worldCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            arena.render(batch);
            player.render(batch);
            for (Gem gem : gems){
                gem.render(batch);
            }
        }
        batch.end();
    }
}
