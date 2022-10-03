package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld51.Config;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.audio.AudioManager;
import lando.systems.ld51.screens.GameScreen;
import lando.systems.ld51.ui.Stats;
import lando.systems.ld51.utils.Calc;
import lando.systems.ld51.utils.Time;
import lombok.RequiredArgsConstructor;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static lando.systems.ld51.Main.game;

public class Player extends ObjectLocation {

    public enum Phase {
          RED    ("warrior", WeaponType.AXE)
        , GREEN  ("thief",   WeaponType.DAGGER)
        , BLUE   ("cleric",  WeaponType.CLUB)
        , WIZARD ("wizard", null) // danger
        ;
        public final String charClassImageName;
        public final WeaponType weaponType;
        Phase(String charClassImageName, WeaponType weaponType) {
            this.charClassImageName = charClassImageName;
            this.weaponType = weaponType;
        }
    }
    public enum State { WALK, SWING }
    public enum WeaponType { AXE, CLUB, DAGGER }

    @RequiredArgsConstructor
    public static class WeaponAnims {
        public final Animation<AtlasRegion> weapon;
        public final Animation<AtlasRegion> glow;
    }

    public static final float SIZE_NORMAL = 100f;
    public static final float SIZE_WIZARD = 200;
    public static float SIZE = SIZE_NORMAL;
    public static final float SPEED_NORMAL = 340f;
    public static final float SPEED_WIZARD = 450f;
    public static float SPEED = SPEED_NORMAL;
    public static final float ATTACK_COOLDOWN_NORMAL = 0.2f;
    public static final float ATTACK_COOLDOWN_WIZARD = 0.15f;

    public static int FULL_GEM_COUNT = 25;
    public static int DEFAULT_GEM_DROP_AMOUNT = 8;

    private final GameScreen screen;

    private Animation<AtlasRegion> animation;
    private Animation<AtlasRegion> flashAnimation;
    private TextureRegion keyframe;
    private TextureRegion flashKeyframe;
    private float stateTime;

    private WeaponAnims weaponAnims;
    private TextureRegion weaponKeyframe;
    private TextureRegion weaponGlowKeyframe;

    public boolean isWizard;
    public boolean lockGemsUntilWizard;

    public Circle hurtCircle;
    public Circle attackRange;
    public Polygon attackHitShape;

    private boolean isAttacking;
    private float attackStateTime;
    private float attackInterval;
    private float attackIntervalNormal = 0.33f;
    private float attackIntervalWizard = 0.01f;
    private float attackTimer;
    private float attackCooldown;

    private float timeSinceLastHurt;
    private float hurtDuration;
    private float hurtTimer;
    public boolean isHurt;

    public Phase phase;
    public State state;
    public Vector2 velocity;
    public Vector3 mousePos;
    public Vector2 facing;
    public Vector2 movementVector;
    public Vector2 tempPos;
    public Vector2 tempVec2;
    public Vector2 tempVec;
    public int redGemCount;
    public int greenGemCount;
    public int blueGemCount;
    public int gemAmountToLose = DEFAULT_GEM_DROP_AMOUNT;
    public int musicPhase = 1;
    public boolean wizardMusicIsPlaying = false;
    public float wizardTransitionTimer = 0f;

    public int walkoutCounter = 1;


    public Player(GameScreen screen) {
        this.screen = screen;
        this.phase = Phase.RED;
        this.state = State.WALK;
        this.position = new Vector2(screen.arena.bounds.width/2f, screen.arena.bounds.height/3f + 80);
        this.velocity = new Vector2();
        this.facing = new Vector2();
        this.movementVector = new Vector2();
        this.mousePos = new Vector3();
        this.tempPos = new Vector2();
        this.tempVec2 = new Vector2();
        this.tempVec = new Vector2();
        this.animation = screen.assets.playerAnimationByPhaseByState.get(phase).get(state);
        this.flashAnimation = screen.assets.playerFlashAnimationByPhaseByState.get(phase).get(state);
        this.keyframe = animation.getKeyFrame(0f);
        this.flashKeyframe = flashAnimation.getKeyFrame(0f);
        this.stateTime = 0f;
        this.attackRange = new Circle(position, 2f * SIZE); // NOTE - used for broad phase collision check
        this.weaponAnims = screen.assets.weaponAnimationsByType.get(phase.weaponType);
        this.weaponKeyframe = weaponAnims.weapon.getKeyFrame(0f);
        this.weaponGlowKeyframe = weaponAnims.glow.getKeyFrame(0f);
        this.attackStateTime = 0f;
        this.isAttacking = false;
        // player gem ui switch update
        this.isWizard = false;
        this.redGemCount = 0;
        this.greenGemCount = 0;
        this.blueGemCount = 0;
        this.lockGemsUntilWizard = false;
        this.attackInterval = attackIntervalNormal;
        this.attackTimer = attackInterval;
        this.attackCooldown = ATTACK_COOLDOWN_NORMAL;
        this.hurtCircle = new Circle(position, SIZE / 4f);
        this.hurtDuration = 0.33f;
        this.hurtTimer = hurtDuration;
        this.timeSinceLastHurt = 0;
    }

    public void update(float dt) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screen.worldCamera.unproject(mousePos);
        facing.set(mousePos.x, mousePos.y).sub(position).nor();
        setOrientation(facing.angleRad());

        timeSinceLastHurt += dt;
        if (isHurt) {
            hurtTimer -= dt;
            if (hurtTimer <= 0) {
                hurtTimer = hurtDuration;
                isHurt = false;
                timeSinceLastHurt = 0f;
            }
        }

        attackTimer -= dt;
        if (canAttack()) {
            attack();
        }

        movementVector.set(0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    movementVector.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  movementVector.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) movementVector.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  movementVector.x -= 1;
        movementVector.nor();

        // save previous position so the attack hit shape can be moved if the player moves
        float prevPosX = position.x;
        float prevPosY = position.y;

//        if (!movementVector.isZero()){
            tempVec2.set(movementVector);
            tempPos.set(position).add(movementVector.x * SPEED * dt, movementVector.y * SPEED * dt);

            // collision check of arena
            Arena arena = screen.arena;
            if ((tempPos.x - SIZE/2f) < arena.bounds.x){
                tempPos.x -= ((tempPos.x -SIZE/2f) - arena.bounds.x);
            }
            if ((tempPos.x + SIZE/2f) > arena.bounds.x + arena.bounds.width){
                tempPos.x -= (tempPos.x + SIZE/2f) - (arena.bounds.x + arena.bounds.width);
            }
            if ((tempPos.y - SIZE/2f) < arena.bounds.y){
                tempPos.y -= ((tempPos.y -SIZE/2f) - arena.bounds.y);
            }
            if ((tempPos.y + SIZE/2f) > arena.bounds.y + arena.bounds.height){
                tempPos.y -= (tempPos.y + SIZE/2f) - (arena.bounds.y + arena.bounds.height);
            }

            // Boss check
            Boss boss = screen.boss;
            float distToBoss = tempPos.dst(boss.position);
            if (distToBoss < SIZE/2 + boss.protectedRadius) {
                float overlapDist = SIZE/2 + boss.protectedRadius - distToBoss;
                tempVec.set(tempPos).sub(boss.position).nor();
                tempPos.add(tempVec.x * overlapDist, tempVec.y * overlapDist);
            }

            position.set(tempPos);
//        }

        // only animate while moving or attacking, or if we're the wizard who has a floaty walk animation
        boolean looksLikeCurrentPhase = looksLikeCurrentPhase(); // fix a dumb bug
        if (isWizard || isAttacking || !looksLikeCurrentPhase || !position.epsilonEquals(prevPosX, prevPosY)) {
            stateTime += dt;

            float animStateTime = isAttacking ? attackStateTime : stateTime;
            State animState = isAttacking ? State.SWING : State.WALK;
            Phase animPhase = isWizard ? Phase.WIZARD : phase;

            animation = screen.assets.playerAnimationByPhaseByState.get(animPhase).get(animState);
            flashAnimation = screen.assets.playerFlashAnimationByPhaseByState.get(animPhase).get(animState);

            keyframe = animation.getKeyFrame(animStateTime);
            flashKeyframe = flashAnimation.getKeyFrame(animStateTime);
        }

        // NOTE - wizard doesn't have weapon anims so this will go null when we go wizard
        weaponAnims = screen.assets.weaponAnimationsByType.get(phase.weaponType);

        attackRange.setPosition(position);
        if (isAttacking) {
            attackStateTime += dt;

            // update hit shape
            if (attackHitShape != null) {
                attackHitShape.translate(position.x - prevPosX, position.y - prevPosY);
                attackHitShape.setRotation(facing.angleDeg());
            }

            // TODO - a bunch of this shit is could break when we go wizard mode because it has no weaponanims
            if (weaponAnims != null) {
                // update attack animation
                weaponKeyframe = weaponAnims.weapon.getKeyFrame(attackStateTime);
                weaponGlowKeyframe = weaponAnims.glow.getKeyFrame(attackStateTime);

                // complete attack if appropriate
                if (attackStateTime >= weaponAnims.weapon.getAnimationDuration()) {
                    attackStateTime = 0f;
                    attackHitShape = null;
                    attackCooldown = (isWizard) ? ATTACK_COOLDOWN_WIZARD : ATTACK_COOLDOWN_NORMAL;
                    isAttacking = false;
                }
            }
        }
        attackCooldown -= dt;
        if (attackCooldown < 0f) {
            attackCooldown = 0f;
        }
//        Gdx.app.log("attack cooldown", Stringf.format("%.2f", attackCooldown));

        // use facing to decide whether to flip keyframe
        float angle = facing.angleDeg();
        if (Calc.between(angle, 0, 90) || Calc.between(angle, 270, 360)) {
            // facing right
            if (keyframe.isFlipX())      keyframe.flip(true, false);
            if (flashKeyframe.isFlipX()) flashKeyframe.flip(true, false);
        } else if (Calc.between(angle, 90, 270)) {
            // facing left
            if (!keyframe.isFlipX())      keyframe.flip(true, false);
            if (!flashKeyframe.isFlipX()) flashKeyframe.flip(true, false);
        }

        // make sure our wizardly attributes are current
        SIZE = (isWizard) ? SIZE_WIZARD : SIZE_NORMAL;
        SPEED = (isWizard) ? SPEED_WIZARD : SPEED_NORMAL;
        hurtCircle.radius = SIZE / 4f;

        screen.playerGemsUI.updatePlayerGemsUIColor(this);
        hurtCircle.setPosition(position);

        wizardTransitionTimer += dt;
//        System.out.println(wizardTransitionTimer);
    }

    private boolean canAttack() {
        float animationDuration = animation.getAnimationDuration();
        boolean attackPressed = (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE));

        // wizard attacks fast
        if (isWizard && attackPressed && attackCooldown <= 0f) {
            return true;
        }

        if (isAttacking || isHurt || attackCooldown > 0 || attackTimer > 0) return false;
//        Gdx.app.log("can attack", "pressed: " + attackPressed + ", anim ready: " + attackAnimComplete);
        boolean attackAnimComplete = (attackStateTime == 0 || attackStateTime >= animationDuration);
        return (attackPressed && attackAnimComplete);
    }

    public void render(SpriteBatch batch) {
        // draw melee attack
        if (isAttacking && !isWizard && weaponKeyframe != null && weaponGlowKeyframe != null) {
            float targetSize = SIZE;
            float weaponWidth = weaponKeyframe.getRegionWidth();
            float weaponHeight = weaponKeyframe.getRegionHeight();
            float widthRatio = targetSize / weaponWidth;
            float heightRatio = targetSize / weaponHeight;
            float ratio = Calc.max(widthRatio, heightRatio);
            float attackWidth = weaponWidth * ratio;
            float attackHeight = weaponHeight * ratio;

            batch.draw(weaponGlowKeyframe,
                    position.x + (attackWidth / 2f),
                    position.y - (attackHeight / 2f),
                    -attackWidth / 2f,
                    attackHeight / 2f,
                    attackWidth, attackHeight,
                    1f, 1f,
                    facing.angleDeg()
            );
            batch.draw(weaponKeyframe,
                    position.x + (attackWidth / 2f),
                    position.y - (attackHeight / 2f),
                    -attackWidth / 2f,
                    attackHeight / 2f,
                    attackWidth, attackHeight,
                    1f, 1f,
                    facing.angleDeg()
            );
        }

        // draw character
        batch.draw(keyframe, position.x - (SIZE/2f), position.y - (SIZE/2f), SIZE, SIZE);

        // draw flash frame if hurt
        if (isHurt) {
            batch.draw(flashKeyframe, position.x - (SIZE/2f), position.y - (SIZE/2f), SIZE, SIZE);
        }

        // draw debug shapes
        if (Config.Debug.general) {
            ShapeDrawer shapes = screen.assets.shapes;
            shapes.setColor(Color.MAGENTA);
            shapes.circle(attackRange.x, attackRange.y, attackRange.radius, 1f);
            if (isAttacking && attackHitShape != null) {
                shapes.setColor(Color.CORAL);
                shapes.polygon(attackHitShape);
            }
            shapes.setColor(Color.YELLOW);
            shapes.circle(hurtCircle.x, hurtCircle.y, hurtCircle.radius, 3f);
            shapes.setColor(Color.WHITE);
        }
    }

    public void hurt(float amount, float dirX, float dirY) {
        if (isHurt) return;
        isHurt = true;

        Time.pause_for(0.1f);
        screen.audio.playSound(AudioManager.Sounds.playerImpact, .75F);
        screen.audio.playSound(AudioManager.Sounds.playerHit, 1.0F);

        // bounce back
        float bounceBackAmount = 20f;
        float prevPosX = position.x;
        float prevPosY = position.y;
        position.add(dirX * bounceBackAmount, dirY * bounceBackAmount);
        hurtCircle.setPosition(position);
        if (attackHitShape != null) {
            attackHitShape.translate(position.x - prevPosX, position.y - prevPosY);
        }

        // lose gems
        if (!lockGemsUntilWizard || isWizard) {
            lockGemsUntilWizard = false;

            // if multiple gem drops in one cycle, reduce number of dropped each time to a max of 1 for ea in cycle
            // TODO - track 'longest time between hits' as stat
            if (timeSinceLastHurt > Stats.longestTimeBetweenHits) {
                Stats.longestTimeBetweenHits = timeSinceLastHurt;
            }
            if (timeSinceLastHurt <= 10f) {
                // hurt more than once in one cycle, reduce number of gems lost each time
                gemAmountToLose = Calc.max(1, gemAmountToLose / 2);
            } else {
                gemAmountToLose = DEFAULT_GEM_DROP_AMOUNT;
            }

            int redToLose   = Calc.min(redGemCount, gemAmountToLose);
            int greenToLose = Calc.min(greenGemCount, gemAmountToLose);
            int blueToLose  = Calc.min(blueGemCount, gemAmountToLose);

            // TODO - track as stat
            int totalLost = redToLose + blueToLose + greenToLose;
            Stats.gemTotalLost += totalLost;
//            Gdx.app.log("", "last hurt " + timeSinceLastHurt + " : dropped = " + totalLost);
            timeSinceLastHurt = 0f;

            if (totalLost > 0) {
                screen.audio.playSound(AudioManager.Sounds.playerDropGems, 0.35F);
            }

            redGemCount   -= redToLose;
            greenGemCount -= greenToLose;
            blueGemCount  -= blueToLose;
            redGemCount   = Calc.max(redGemCount, 0);
            greenGemCount = Calc.max(greenGemCount, 0);
            blueGemCount  = Calc.max(blueGemCount, 0);

            if (redGemCount == 0 && greenGemCount == 0 && blueGemCount == 0) {
                // TODO - kill player?
//                Gdx.app.log("Player Hurt", "Ouch, you lost all your gems dog! How you supposed to be a magic now?");
            }

            screen.particles.dropGems(redToLose, greenToLose,  blueToLose, position.x, position.y);
//            screen.audio.playSound(AudioManager.Sounds.playerDropGems, 0.35F);
        }
    }

    public boolean canPickup(Gem gem){
        if (isWizard) return false;
        switch (gem.type){
            case RED:
                return (getCurrentPhase() == Phase.RED && redGemCount < FULL_GEM_COUNT);
            case GREEN:
                return (getCurrentPhase() == Phase.GREEN && greenGemCount < FULL_GEM_COUNT);
            case BLUE:
                return (getCurrentPhase() == Phase.BLUE && blueGemCount < FULL_GEM_COUNT);
        }
        // Shouldn't get here
        return false;
    }

    public boolean isFullOfGems() {
        return (redGemCount   == FULL_GEM_COUNT
             && greenGemCount == FULL_GEM_COUNT
             && blueGemCount  == FULL_GEM_COUNT);
    }

    public void pickupGem(Gem gem) {
        boolean wasFullOfGems = isFullOfGems();
        screen.game.audio.playSound(AudioManager.Sounds.collect, 0.125F);
        Stats.gemTotalEarned++;
        switch (gem.type){
            case RED:
                redGemCount++;
                screen.playerGemsUI.redProgressBar.flashIt();
                if(redGemCount == FULL_GEM_COUNT) {
                    screen.audio.playSound((AudioManager.Sounds.warriorGemsFull));
                }

                break;
            case GREEN:
                greenGemCount++;
                screen.playerGemsUI.greenProgressBar.flashIt();
                if(greenGemCount == FULL_GEM_COUNT) {
                    screen.audio.playSound((AudioManager.Sounds.rogueGemsFull), 0.25F);
                }

                break;
            case BLUE:
                blueGemCount++;
                screen.playerGemsUI.blueProgressBar.flashIt();
                if(blueGemCount == FULL_GEM_COUNT) {
                    screen.audio.playSound((AudioManager.Sounds.clericGemsFull));
                }
                break;
        }
        boolean isFullOfGems = isFullOfGems();
        if (!wasFullOfGems && isFullOfGems) {
            lockGemsUntilWizard = true;
        }
    }

    public void setPhase(int phase){
        Phase nextPhase = null;
        switch(phase % 3) {
            case 0: nextPhase = Phase.RED; break;
            case 1: nextPhase = Phase.GREEN; break;
            case 2: nextPhase = Phase.BLUE; break;
        }
        if (this.phase == nextPhase) {
            return;
        }

        // anything that needs to happen on the phase change, particle effects etc
        screen.audio.playSound(AudioManager.Sounds.lightning, 0.25f);
        Time.pause_for(0.15f);

        this.phase = nextPhase;
        switch (this.phase){
            case RED:
//                screen.audio.stopMusic();
                if(!wizardMusicIsPlaying) {
                    screen.audio.stopMusic();
                    screen.audio.playMusic(AudioManager.Musics.valueOf("warriorMusic" + musicPhase));
                    if(walkoutCounter < 6) {
                        screen.audio.playSound(AudioManager.Sounds.valueOf("warriorWalkout"+walkoutCounter), 1.0F);
                    }
                }
                break;
            case GREEN:
                if(!wizardMusicIsPlaying) {
                    screen.audio.stopMusic();
                    screen.audio.playMusic(AudioManager.Musics.valueOf("rogueMusic" + musicPhase));
                    if(walkoutCounter < 6) {
                        screen.audio.playSound(AudioManager.Sounds.valueOf("thiefWalkout"+walkoutCounter),1.0F);
                    }
                }
                break;
            case BLUE:
                if(!wizardMusicIsPlaying) {
                    screen.audio.stopMusic();
                    screen.audio.playMusic(AudioManager.Musics.valueOf("clericMusic" + musicPhase));
                    if(walkoutCounter <= 6) {
                        screen.audio.playSound(AudioManager.Sounds.valueOf("clericWalkout"+walkoutCounter), 1.0F);
                    }
                }
                if (musicPhase == 3) {
                    musicPhase = 1;
                } else {
                    musicPhase++;
                }

//                if(walkoutCounter < 6) {
//                    walkoutCounter = 7;
//                }
                walkoutCounter++;

                break;
        }

        // handle wizard transition
        if (!isWizard) {
            screen.screenShaker.addDamage(.8f);
            screen.particles.lightning(screen.boss.position, position);
            if (redGemCount >= FULL_GEM_COUNT && greenGemCount >= FULL_GEM_COUNT && blueGemCount >= FULL_GEM_COUNT){
                isWizard = true;
                Stats.numTransitionToWhiteWizard++;
                screen.explosions.add(new Explosion(screen, position.x, position.y, SIZE_WIZARD * 1.5f, 100));
                screen.setZoom(GameScreen.WIZARD_ZOOM);
                if(!wizardMusicIsPlaying) {
                    screen.audio.playMusic(AudioManager.Musics.wizardMusic1);
                }
                wizardMusicIsPlaying = true;


            }
        } else {
//            wizardMusicIsPlaying = false;
            if (redGemCount < FULL_GEM_COUNT /2f || greenGemCount < FULL_GEM_COUNT /2f || blueGemCount < FULL_GEM_COUNT /2f) {
                isWizard = false;
                screen.setZoom(GameScreen.NORMAL_ZOOM);
                screen.audio.stopMusic();
                 wizardMusicIsPlaying = false;
            }
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void attack() {
        if (isAttacking && !isWizard) return; // wizard attacks fast
        isAttacking = true;
        attackStateTime = 0f;

        attackInterval = (isWizard) ? attackIntervalWizard : attackIntervalNormal;
        attackTimer = attackInterval;

        if (isWizard) {
            float size = 64f;
            float x = position.x + facing.x * size * (1f / 2f);
            float y = position.y + facing.y * size * (1f / 2f);
            float angle = facing.angleRad();
            float speed = Calc.max(SPEED + 60f, 250f);
            Projectile projectile = new Projectile(screen.assets, EffectAnims.Type.meteor, x, y, angle, speed, true);
            projectile.size = size;
            screen.projectiles.add(projectile);
            game.audio.playSound(AudioManager.Sounds.fireball, 0.25F);
            attackCooldown = ATTACK_COOLDOWN_WIZARD;
        } else {
            float range = attackRange.radius;
            float[] vertices = new float[]{
                    position.x,
                    position.y,

                    position.x + range * (1f / 5f),
                    position.y - range * (1f / 4f),

                    position.x + range * (1f / 2f),
                    position.y - range * (1f / 2f),

                    position.x + range * (4f / 5f),
                    position.y,

                    position.x + range * (1f / 2f),
                    position.y + range * (1f / 2f),

                    position.x + range * (1f / 5f),
                    position.y + range * (1f / 4f)
            };
            attackHitShape = new Polygon(vertices);
            attackHitShape.setOrigin(position.x, position.y);
            game.audio.playSound(AudioManager.Sounds.swipe, 0.25F);
        }
    }

    public Phase getCurrentPhase() {
        return phase;
    }

    public boolean isWizard() {
        return isWizard;
    }

    public Gem.Type getLeastGemsType() {
        if      (redGemCount   < greenGemCount && redGemCount   < blueGemCount)  return Gem.Type.RED;
        else if (greenGemCount < redGemCount   && greenGemCount < blueGemCount)  return Gem.Type.GREEN;
        else if (blueGemCount  < redGemCount   && blueGemCount  < greenGemCount) return Gem.Type.BLUE;
        else return Gem.Type.random();
    }

    private boolean looksLikeCurrentPhase() {
        ObjectMap<State, Animation<AtlasRegion>> animationsByState = screen.assets.playerAnimationByPhaseByState.get(phase);
        for (Animation<AtlasRegion> anim : animationsByState.values()) {
            if (anim == null) continue;
            if (animation == anim) {
                return true;
            }
        }
        return false;
    }

}
