package lando.systems.ld51.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.Config;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.gameobjects.Arena;
import lando.systems.ld51.gameobjects.Enemy;
import lando.systems.ld51.gameobjects.Gem;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.particles.Particles;
import lando.systems.ld51.ui.BossHealthUI;
import lando.systems.ld51.ui.DebugWindow;
import lando.systems.ld51.ui.PlayerGemsUI;
import lando.systems.ld51.utils.FollowOrthographicCamera;

public class GameScreen extends BaseScreen {

    public Player player;
    public Arena arena;
    public Array<Gem> gems;
    public Array<Enemy> enemies;
    public float accum;

    public final Particles particles;

    private DebugWindow debugWindow;
    private BossHealthUI bossHealthUI;
    public PlayerGemsUI playerGemsUI;

    private final EnemySpawner enemySpawner;

    private final float BOSS_HEALTH_UI_HEIGHT = 100f;
    private final float PLAYER_GEMS_UI_HEIGHT = 100f;

    public GameScreen(){
        this.player = new Player(this);
        this.arena = new Arena(this);
        this.gems = new Array<>();
        this.enemies = new Array<>();
        this.accum = 0;
        this.particles = new Particles(assets);
        this.enemySpawner = new EnemySpawner();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();

        //debug window
        debugWindow = new DebugWindow("", true, windowCamera, skin);
        uiStage.addActor(debugWindow);

        //boss health ui
        bossHealthUI = new BossHealthUI("", true, 0f, windowCamera.viewportHeight - BOSS_HEALTH_UI_HEIGHT, windowCamera.viewportWidth, BOSS_HEALTH_UI_HEIGHT, skin);
        bossHealthUI.setVisible(true);
        uiStage.addActor(bossHealthUI);

        playerGemsUI = new PlayerGemsUI("", 0f, 0f, windowCamera.viewportWidth, PLAYER_GEMS_UI_HEIGHT, skin);
        uiStage.addActor(playerGemsUI);
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
        accum += delta;

        player.setPhase((int)(accum / 10f));
        arena.update(delta);
        particles.update(delta);

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

        player.update(delta);

        for (int i = gems.size -1; i >= 0; i--) {
            Gem gem = gems.get(i);
            gem.update(delta);
            if (gem.collected){
                gems.removeIndex(i);
            }
        }

        Enemy enemy = enemySpawner.update(delta);
        if (enemy != null) {
            enemies.add(enemy);
        }
        for (int i = enemies.size - 1; i >= 0; i--) {
            enemy = enemies.get(i);
            enemy.update(delta);
            if (enemy.isDead()) {
                enemy.kill();
                enemies.removeIndex(i);
            }
        }

        // Camera follow things
        // TODO - maybe just make the worldCamera a FollowOrthoCam so we don't need to cast here
        ((FollowOrthographicCamera)worldCamera).update(player.position, delta);

        debugWindow.update();
        uiStage.act();
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
            particles.draw(batch, Particles.Layer.background);
            for (Enemy enemy : enemies) {
                enemy.render(batch);
            }
            particles.draw(batch, Particles.Layer.middle);
            player.render(batch);
            for (Gem gem : gems){
                gem.render(batch);
            }
            particles.draw(batch, Particles.Layer.foreground);
        }
        batch.end();
        uiStage.draw();
    }

    // ------------------------------------------------------------------------
    // Helper classes
    // ------------------------------------------------------------------------

    class EnemySpawner {
        private float timer = 0f;
        private float duration = 3f;
        Enemy update(float delta) {
            Enemy enemy = null;
            timer += delta;
            if (timer >= duration) {
                timer -= duration;
                enemy = spawn();
            }
            return enemy;
        }
        Enemy spawn() {
            CreatureAnims.Type type = CreatureAnims.Type.random();
            Vector2 playerPos = player.position;
            float setback = 100f;
            float angle = MathUtils.random(0, 360f);
            float xDist = worldCamera.viewportWidth / 2f + setback;
            float yDist = worldCamera.viewportHeight / 2f + setback;
            float x = MathUtils.cosDeg(angle) * xDist;
            float y = MathUtils.sinDeg(angle) * yDist;
            Enemy enemy = new Enemy(GameScreen.this, type, x, y);
            enemy.targetPos = playerPos;
            return enemy;
        }
    }

}
