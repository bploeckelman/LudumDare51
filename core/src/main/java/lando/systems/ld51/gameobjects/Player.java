package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld51.Config;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.screens.GameScreen;

public class Player {

    public enum Phase {RED, GREEN, BLUE};

    public static float SIZE = 30f;
    public static float SPEED = 100f;
    public static int FULL_GEM_COUNT = 100;

    private final GameScreen gameScreen;

    private TextureRegion keyframe;
    private Animation<TextureRegion> animation;
    private float stateTime;

    private Animation<TextureRegion> attackAnimation;
    private TextureRegion attackKeyframe;
    private float attackStateTime;
    private boolean isAttacking;

    public Vector2 position;
    public Vector2 velocity;
    public Vector3 mousePos;
    public Vector2 facing;
    public Vector2 tempPos;
    public Vector2 tempVec2;
    public Vector2 tempVec;
    public int redGemCount;
    public int greenGemCount;
    public int blueGemCount;
    public float invulnerabilityTimer;
    public Phase currentPhase;
    public boolean isWizard;
    public float attackInterval;
    public float attackTimer;


    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(Config.Screen.window_width/2f, Config.Screen.window_height/2f);
        this.velocity = new Vector2();
        this.facing = new Vector2();
        this.mousePos = new Vector3();
        this.tempPos = new Vector2();
        this.tempVec2 = new Vector2();
        this.tempVec = new Vector2();
        this.animation = gameScreen.assets.creatureAnims.get(CreatureAnims.Type.warrior);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;
        this.attackAnimation = gameScreen.assets.effectAnims.get(EffectAnims.Type.swipe);
        this.attackKeyframe = attackAnimation.getKeyFrame(0f);
        this.attackStateTime = 0f;
        this.isAttacking = false;
        this.redGemCount = 0;
        this.greenGemCount = 0;
        this.blueGemCount = 0;
        this.invulnerabilityTimer = 0;
        this.currentPhase = Phase.RED;
        // player gem ui switch update
        this.gameScreen.playerGemsUI.update(this.currentPhase);
        this.isWizard = false;
        this.redGemCount = 0;
        this.greenGemCount = 0;
        this.blueGemCount = 0;
        this.invulnerabilityTimer = 0;
        this.attackInterval = 1f;
        this.attackTimer = attackInterval;
    }

    public void update(float dt) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.worldCamera.unproject(mousePos);
        facing.set(mousePos.x, mousePos.y).sub(position).nor();

        invulnerabilityTimer -= dt;

        attackTimer -= dt;
        if (attackTimer <= 0f) {
            attackTimer = attackInterval;
            attack();
        }

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);

        if (isAttacking) {
            attackKeyframe = attackAnimation.getKeyFrame(attackStateTime);
            attackStateTime += dt;
            if (attackStateTime >= attackAnimation.getAnimationDuration()) {
                attackStateTime = 0f;
                isAttacking = false;
            }
        }

        if (Gdx.input.isTouched()){
            tempVec2.set(facing);
            tempPos.set(position).add(facing.x * SPEED * dt, facing.y * SPEED * dt);

            // collision check of arena
            Arena arena = gameScreen.arena;
            if ((tempPos.x - SIZE/2f) < arena.bounds.x){
                tempVec2.x -= ((tempPos.x -SIZE/2f) - arena.bounds.x);
            }
            if ((tempPos.x + SIZE/2f) > arena.bounds.x + arena.bounds.width){
                tempVec2.x -= (tempPos.x + SIZE/2f) - (arena.bounds.x + arena.bounds.width);
            }
            if ((tempPos.y - SIZE/2f) < arena.bounds.y){
                tempVec2.y -= ((tempPos.y -SIZE/2f) - arena.bounds.y);
            }
            if ((tempPos.y + SIZE/2f) > arena.bounds.y + arena.bounds.height){
                tempVec2.y -= (tempPos.y + SIZE/2f) - (arena.bounds.y + arena.bounds.height);
            }

            // Boss check
            Boss boss = gameScreen.boss;
            float distToBoss = tempPos.dst(boss.position);
            if (distToBoss < SIZE/2 + boss.protectedRadius) {
                float overlapDist = SIZE/2 + boss.protectedRadius - distToBoss;
                tempVec.set(tempPos).sub(boss.position).nor();
                tempVec2.add(tempVec.x * overlapDist, tempVec.y * overlapDist);
            }

            position.add(tempVec2.x * SPEED * dt, tempVec2.y * SPEED * dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, position.x - (SIZE/2f), position.y - (SIZE/2f), SIZE, SIZE);

        if (isAttacking) {
            float keyframeWidth = attackKeyframe.getRegionWidth();
            float keyframeHeight = attackKeyframe.getRegionHeight();
            batch.draw(attackKeyframe,
                    position.x - (SIZE / 2f),
                    position.y - (SIZE / 2f),
                    keyframeWidth / 2f,
                    keyframeHeight / 2f,
                    keyframeWidth, keyframeHeight,
                    1f, 1f,
                    facing.angleDeg()
            );
        }
    }

    public void getHit(){
        if (invulnerabilityTimer > 0) return; // Can't be hit

        // Lose gems when you get hit. minimum of some value
        // TODO make this more robust with minimums and such
        int redToLose = redGemCount / 4;
        int greenToLose = greenGemCount / 4;
        int blueToLose = blueGemCount / 4;
        int totalLost = redToLose + blueToLose + greenToLose;
        if (totalLost == 0){
            // Kill them?
        }
        invulnerabilityTimer = 1f;
    }

    public boolean canPickup(Gem gem){
        if (isWizard) return false;
        switch (gem.type){
            case RED:
                return (redGemCount < FULL_GEM_COUNT);
            case GREEN:
                return (greenGemCount < FULL_GEM_COUNT);
            case BLUE:
                return (blueGemCount < FULL_GEM_COUNT);
        }
        // Shouldn't get here
        return false;
    }

    public void pickupGem(Gem gem) {
        switch (gem.type){
            case RED:
                redGemCount++;
                break;
            case GREEN:
                greenGemCount++;
                break;
            case BLUE:
                blueGemCount++;
                break;
        }
    }

    public void setPhase(int phase){
        Phase nextPhase = null;
        switch(phase % 3) {
            case 0: nextPhase = Phase.RED;
            break;
            case 1: nextPhase = Phase.GREEN;
            break;
            case 2: nextPhase = Phase.BLUE;
        }
        if (currentPhase == nextPhase) {
            return;
        }

        // TODO: anything that needs to happen on the phase change
        // Particle effects etc
        currentPhase = nextPhase;
        switch (currentPhase){
            case RED:
                this.animation = gameScreen.assets.creatureAnims.get(CreatureAnims.Type.warrior);
                gameScreen.audio.playSound(AudioManager.Sounds.warriorMusic1, 1.0f);
                break;
            case GREEN:
                this.animation = gameScreen.assets.creatureAnims.get(CreatureAnims.Type.rogue);
                gameScreen.audio.playSound(AudioManager.Sounds.rogueMusic1, 1.0f);
                break;
            case BLUE:
                this.animation = gameScreen.assets.creatureAnims.get(CreatureAnims.Type.cleric);
                gameScreen.audio.playSound(AudioManager.Sounds.clericMusic1, 1.0f);
                break;
        }
        gameScreen.particles.lightning(gameScreen.boss.position, position);
        gameScreen.playerGemsUI.update(currentPhase);
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public boolean getIsWizard() {
        return isWizard;
    }
    public void attack() {
        isAttacking = true;
        attackStateTime = 0f;
    }
}
