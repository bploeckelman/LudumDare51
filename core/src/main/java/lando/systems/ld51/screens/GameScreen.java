package lando.systems.ld51.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld51.Config;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.gameobjects.*;
import lando.systems.ld51.particles.Particles;
import lando.systems.ld51.systems.AttackResolver;
import lando.systems.ld51.ui.BossHealthUI;
import lando.systems.ld51.ui.CooldownTimerUI;
import lando.systems.ld51.ui.DebugWindow;
import lando.systems.ld51.ui.PlayerGemsUI;
import lando.systems.ld51.utils.FollowOrthographicCamera;
import lando.systems.ld51.utils.screenshake.ScreenShakeCameraController;

import java.util.Comparator;

public class GameScreen extends BaseScreen {

    public static float NORMAL_ZOOM = 1.2f;
    public static float WIZARD_ZOOM = 1.9f;

    public Player player;
    public Boss boss;
    public Arena arena;
    public Array<Gem> gems;
    public Array<Enemy> enemies;
    public Array<Explosion> explosions;

    public final Particles particles;
    public final Array<Spawner> spawners;
    public final Array<Projectile> projectiles;

    private DebugWindow debugWindow;
    private BossHealthUI bossHealthUI;
    public PlayerGemsUI playerGemsUI;
    public CooldownTimerUI cooldownTimerUI;

    public BossArrow bossArrow;

    public ScreenShakeCameraController screenShaker;

    private final EnemySpawner enemySpawner;

    private final float BOSS_HEALTH_UI_HEIGHT = 30f;
    private final float PLAYER_GEMS_UI_HEIGHT = 30f;
    private final Comparator<Enemy> sortPositionsByYDescending = (a, b) -> -Float.compare(a.getPosition().y, b.getPosition().y);

    public GameScreen(){
        this.arena = new Arena(this);
        this.explosions = new Array<>();
        this.player = new Player(this);
        this.boss = new Boss(this);
        this.gems = new Array<>();
        this.enemies = new Array<>();
        this.particles = Main.game.particles;
        this.spawners = new Array<>();
        this.projectiles = new Array<>();
        this.enemySpawner = new EnemySpawner();
        this.bossArrow = new BossArrow(this);
        bossHealthUI.setBoss(boss);

        populateSpawners();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();

        //debug window
        debugWindow = new DebugWindow("", true, windowCamera, skin);
        uiStage.addActor(debugWindow);

        //boss health ui
        bossHealthUI = new BossHealthUI("", true, 0f + BOSS_HEALTH_UI_HEIGHT, windowCamera.viewportHeight - BOSS_HEALTH_UI_HEIGHT, windowCamera.viewportWidth - BOSS_HEALTH_UI_HEIGHT * 2, BOSS_HEALTH_UI_HEIGHT, skin, assets, windowCamera);
        bossHealthUI.setVisible(true);
        uiStage.addActor(bossHealthUI);
        uiStage.addActor(bossHealthUI.bossHealthBar);
        uiStage.addActor(bossHealthUI.bossImageLeft);
        uiStage.addActor(bossHealthUI.bossImageRight);

        cooldownTimerUI = new CooldownTimerUI(0f, PLAYER_GEMS_UI_HEIGHT, windowCamera.viewportWidth, PLAYER_GEMS_UI_HEIGHT, skin, assets);
        uiStage.addActor(cooldownTimerUI);

        playerGemsUI = new PlayerGemsUI("", 0f, 0f, windowCamera.viewportWidth, PLAYER_GEMS_UI_HEIGHT, skin, assets);
        uiStage.addActor(playerGemsUI);
    }

    @Override
    protected void create() {
        worldCamera = new FollowOrthographicCamera();
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.zoom = 2f;
        setZoom(NORMAL_ZOOM);
        worldCamera.position.set(player.position.x, player.position.y, 0);
        worldCamera.update();
        screenShaker = new ScreenShakeCameraController(worldCamera);
        game.audio.playMusic(AudioManager.Musics.warriorMusic1);

        // start the phase change timer
        Main.game.mainGameTimer = 0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        /// DEBUG SHIT
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(mouse);
            explosions.add(new Explosion(this, mouse.x, mouse.y, 200, 100));
        }

        /// END DEBUG SHIT

        if (!boss.isAlive()) {
            // Game Over dude...
            // TODO: some exposition
            Main.game.getScreenManager().pushScreen("endScreen", "blend");
            return;
        }
        screenShaker.update(delta);

        player.setPhase((int)(Main.game.mainGameTimer / 10f));
        arena.update(delta);
        bossArrow.update(delta);

        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);

            // remove projectiles that leave the arena
            Circle bounds = projectile.bounds;
            if (bounds.x + bounds.radius < arena.bounds.x || bounds.x - bounds.radius > arena.bounds.x + arena.bounds.width
             || bounds.y + bounds.radius < arena.bounds.y || bounds.y - bounds.radius > arena.bounds.y + arena.bounds.height) {
                projectiles.removeIndex(i);
            }
        }

        player.update(delta);
        boss.update(delta);
        AttackResolver.resolve(player, enemies, boss, projectiles, explosions);

        for (int i = gems.size -1; i >= 0; i--) {
            Gem gem = gems.get(i);
            gem.update(delta);
            if (gem.collected || gem.diedOfOldAge){
                gem.free();
                gems.removeIndex(i);
            }
        }

        // spawn enemies if appropriate
        Array<Enemy> spawnedEnemies = enemySpawner.update(delta);
        if (!spawnedEnemies.isEmpty()) {
            for (Enemy spawnedEnemy : spawnedEnemies) {
                // add a group steering behavior in addition to the default behaviors
                if (spawnedEnemy.getSteeringBehavior() instanceof BlendedSteering) {
                    float radius = spawnedEnemy.size * spawnedEnemy.type.avoidanceScale;
                    BlendedSteering<Vector2> blendedSteering = (BlendedSteering<Vector2>) spawnedEnemy.getSteeringBehavior();
                    RadiusProximity<Vector2> radiusProximity = new RadiusProximity<>(spawnedEnemy, enemies, radius);
                    blendedSteering.add(new CollisionAvoidance<>(spawnedEnemy, radiusProximity), 2f);
                }
                enemies.add(spawnedEnemy);
            }
        }

        // update and remove dead enemies
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta);
            if (enemy.isDead()) {
                enemy.kill();
                enemies.removeIndex(i);
            }
        }
        enemies.sort(sortPositionsByYDescending);

        // Camera follow things
        // TODO - maybe just make the worldCamera a FollowOrthoCam so we don't need to cast here
        ((FollowOrthographicCamera)worldCamera).update(player.position, arena.bounds, delta);

        debugWindow.update();
        playerGemsUI.redProgressBar.update(delta, player.redGemCount, player.FULL_GEM_COUNT, player.isWizard);
        playerGemsUI.blueProgressBar.update(delta, player.blueGemCount, player.FULL_GEM_COUNT, player.isWizard);
        playerGemsUI.greenProgressBar.update(delta, player.greenGemCount, player.FULL_GEM_COUNT, player.isWizard);
        cooldownTimerUI.updateTimerValue(player, Main.game.mainGameTimer);
        bossHealthUI.update(delta);
        uiStage.act();

        debugWindow.setVisible(false);
        if (Config.Debug.general) {
            debugWindow.setVisible(true);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            arena.render(batch);
            particles.draw(batch, Particles.Layer.background);
            for (Spawner spawner : spawners) {
                spawner.render(batch);
            }
            for (Gem gem : gems){
                gem.render(batch);
            }
            for (Enemy enemy : enemies) {
                enemy.render(batch);
            }
            boss.render(batch);
            particles.draw(batch, Particles.Layer.middle);
            player.render(batch);
            for (Projectile projectile : projectiles) {
                projectile.render(batch);
            }
            particles.draw(batch, Particles.Layer.foreground);
        }
        batch.end();
        uiStage.draw();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            // other UI things
            bossArrow.render(batch);
        }
        batch.end();
    }

//    private void updateTimer() {

//    }

    public void setZoom(float amount) {
        ((FollowOrthographicCamera)worldCamera).targetZoom = amount;
    }

    private void populateSpawners() {
        int numCols = 8;
        int numRows = 8;
        float margin = 200f;
        float xSpacing = (arena.bounds.width  - (2f * margin)) / (numCols - 1);
        float ySpacing = (arena.bounds.height - (2f * margin)) / (numRows - 1);
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                float x = arena.bounds.x + margin + col * xSpacing;
                float y = arena.bounds.y + margin + row * ySpacing;
                Spawner spawner = new Spawner(this, x, y);
                spawners.add(spawner);
            }
        }
    }

    // ------------------------------------------------------------------------
    // Helper classes
    // ------------------------------------------------------------------------

    // TODO - change this to be a spawner controller,
    //  it keeps the time table for spawning
    //  and if it's time to spawn picks a source spawner
    //  and picks a random Spawner.SpawnType to spawn from there
    //  needs to be able to spawn one or more enemies so should maintain an array that it populates and returns from update
    class EnemySpawner {

        private final Array<Enemy> spawnedEnemies = new Array<>();
        private final Vector2 dist = new Vector2();

        private static final int MAX_NUM_LIVE_ENEMIES = 100;

        private static final float SPAWN_INTERVAL_WIZARD = 0.75f;
        private static final float SPAWN_INTERVAL_NORMAL = 2f;

        private float interval = SPAWN_INTERVAL_NORMAL;
        private float timer = 0f;

        Array<Enemy> update(float delta) {
            spawnedEnemies.clear();
            Enemy enemy = null;

            interval = (player.isWizard) ? SPAWN_INTERVAL_WIZARD : SPAWN_INTERVAL_NORMAL;

            timer += delta;
            if (timer >= interval) {
                timer -= interval;

                // if there's 'too many' live enemies don't spawn more
                if (enemies.size < MAX_NUM_LIVE_ENEMIES) {
                    enemy = spawn();
                }
            }

            if (enemy != null) {
                spawnedEnemies.add(enemy);
            }
            return spawnedEnemies;
        }

        Spawner findClosestOffscreenSpawner() {
            Spawner closest = null;
            float minDistance = Float.MAX_VALUE;
            for (Spawner spawner : spawners) {
                if (!spawner.isOffscreen()) continue;
                float distance = dist.set(player.position).dst(spawner.position);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = spawner;
                }
            }
            return closest;
        }

        Enemy spawn() {
            CreatureAnims.Type type = CreatureAnims.Type.random();

            // slightly prefer spawning creatures of the gem type the player has the least of
            boolean coinFlip = MathUtils.randomBoolean(0.75f);
            if (coinFlip) {
                Gem.Type leastGemType = player.getLeastGemsType();
                switch (leastGemType) {
                    case RED:   type = CreatureAnims.CreatureGroups.reds.getRandomType(); break;
                    case GREEN: type = CreatureAnims.CreatureGroups.greens.getRandomType(); break;
                    case BLUE:  type = CreatureAnims.CreatureGroups.blues.getRandomType(); break;
                }
            }

            Spawner spawner = findClosestOffscreenSpawner();
            Enemy enemy = new Enemy(GameScreen.this, type, spawner.position.x, spawner.position.y);

            // TODO - add in special steering behaviors for certain types of enemies
            BlendedSteering<Vector2> steering = new BlendedSteering<>(enemy);
            steering.add(new Seek<>(enemy, player), 1f);
            enemy.setSteeringBehavior(steering);

            return enemy;
        }
    }

}
