package lando.systems.ld51.screens;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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

public class GameScreen extends BaseScreen {

    public Player player;
    public Boss boss;
    public Arena arena;
    public Array<Gem> gems;
    public Array<Enemy> enemies;
    public float accum;

    public final Particles particles;
    public final Array<Projectile> projectiles;

    private DebugWindow debugWindow;
    private BossHealthUI bossHealthUI;
    public PlayerGemsUI playerGemsUI;
    public CooldownTimerUI cooldownTimerUI;

    public ScreenShakeCameraController screenShaker;

    private final EnemySpawner enemySpawner;

    private final float BOSS_HEALTH_UI_HEIGHT = 30f;
    private final float PLAYER_GEMS_UI_HEIGHT = 30f;

    public GameScreen(){
        this.arena = new Arena(this);
        this.player = new Player(this);
        this.boss = new Boss(this);
        this.gems = new Array<>();
        this.enemies = new Array<>();
        this.accum = 0;
        this.particles = Main.game.particles;
        this.projectiles = new Array<>();
        this.enemySpawner = new EnemySpawner();
        bossHealthUI.setBoss(boss);
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

        uiStage.addActor(bossHealthUI.bossHealthBar);

        cooldownTimerUI = new CooldownTimerUI(0f, PLAYER_GEMS_UI_HEIGHT, windowCamera.viewportWidth, PLAYER_GEMS_UI_HEIGHT, skin, assets);
        uiStage.addActor(cooldownTimerUI);

        playerGemsUI = new PlayerGemsUI("", 0f, 0f, windowCamera.viewportWidth, PLAYER_GEMS_UI_HEIGHT, skin, assets);
        uiStage.addActor(playerGemsUI);

    }

    @Override
    protected void create() {
        worldCamera = new FollowOrthographicCamera();
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
        game.audio.playMusic(AudioManager.Musics.warriorMusic1);
        this.screenShaker = new ScreenShakeCameraController(worldCamera);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!boss.isAlive()) {
            // Game Over dude...
            // TODO: some exposition
            Main.game.getScreenManager().pushScreen("endScreen", "blend");
            return;
        }
        screenShaker.update(delta);
        accum += delta;

        player.setPhase((int)(accum / 10f));
        arena.update(delta);

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

//        if (MathUtils.random(1f) > .97f){ // THIS IS PLACEHOLDER
//            int randType = MathUtils.random(2);
//            Gem.Type type = Gem.Type.RED;
//            switch(randType){
//                case 0:
//                    type = Gem.Type.GREEN;
//                    break;
//                case 1:
//                    type = Gem.Type.BLUE;
//                    break;
//            }
//            gems.add(new Gem(this, new Vector2(MathUtils.random(Config.Screen.window_width), MathUtils.random(Config.Screen.window_height)), type));
//        }

        player.update(delta);
        boss.update(delta);
        AttackResolver.resolve(player, enemies, boss, projectiles);

        for (int i = gems.size -1; i >= 0; i--) {
            Gem gem = gems.get(i);
            gem.update(delta);
            if (gem.collected){
                gem.free();
                gems.removeIndex(i);
            }
        }

        Enemy enemy = enemySpawner.update(delta);
        if (enemy != null) {
            // add a group steering behavior in addition to the default behaviors
            SteeringBehavior<Vector2> steeringBehavior = enemy.getSteeringBehavior();
            if (enemy.getSteeringBehavior() instanceof BlendedSteering) {
                float radius = enemy.size * enemy.type.avoidanceScale;
                BlendedSteering<Vector2> blendedSteering = (BlendedSteering<Vector2>) enemy.getSteeringBehavior();
                RadiusProximity<Vector2> radiusProximity = new RadiusProximity<>(enemy, enemies, radius);
                blendedSteering.add(new CollisionAvoidance<>(enemy, radiusProximity), 2f);
            }
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
        ((FollowOrthographicCamera)worldCamera).update(player.position, arena.bounds, delta);

        debugWindow.update();
        playerGemsUI.redProgressBar.update(delta, player.redGemCount, player.FULL_GEM_COUNT, player.isWizard);
        playerGemsUI.blueProgressBar.update(delta, player.blueGemCount, player.FULL_GEM_COUNT, player.isWizard);
        playerGemsUI.greenProgressBar.update(delta, player.greenGemCount, player.FULL_GEM_COUNT, player.isWizard);
        cooldownTimerUI.updateTimerValue(player, accum);
        bossHealthUI.update(delta);
        uiStage.act();
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            arena.render(batch);
            particles.draw(batch, Particles.Layer.background);
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
    }

//    private void updateTimer() {

//    }

    // ------------------------------------------------------------------------
    // Helper classes
    // ------------------------------------------------------------------------

    class EnemySpawner {
        private float timer = 0f;
        private float duration = 2f;
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
            float setback = 100f;
            float angle = MathUtils.random(0, 360f);
            float xDist = worldCamera.viewportWidth / 2f + setback;
            float yDist = worldCamera.viewportHeight / 2f + setback;
            float x = MathUtils.cosDeg(angle) * xDist;
            float y = MathUtils.sinDeg(angle) * yDist;
            Enemy enemy = new Enemy(GameScreen.this, type, x, y);
            // TODO - set enemy speed values based on type
            // TODO - add in special steering behaviors for certain types of enemies
            BlendedSteering<Vector2> steering = new BlendedSteering<>(enemy);
            steering.add(new Seek<>(enemy, player), 1f);
            enemy.setSteeringBehavior(steering);
            return enemy;
        }
    }

}
