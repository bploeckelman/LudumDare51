package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld51.Main;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.screens.GameScreen;

public class Boss extends ObjectLocation {

    public static float MAX_HEALTH = 1000f;

    public enum State {
          idle_a        ("characters/boss/boss-idle-a/boss-idle-a")
        , idle_b        ("characters/boss/boss-idle-b/boss-idle-b")
        , attack_spell  ("characters/boss/boss-attack-spell/boss-attack-spell")
        , attack_punch  ("characters/boss/boss-attack-punch/boss-attack-punch")
        ;
        private final String frameRegionsName;
        State(String frameRegionsName) {
            this.frameRegionsName = frameRegionsName;
        }
    }

    public enum Phase { full, three, half, quarter};

    public static float SIZE = 200;

    public float protectedRadius = 130;

    private float accum;

    private final GameScreen screen;
    public Circle hurtCircle;
    private State currentState;

    private final ObjectMap<State, Animation<AtlasRegion>> animationsByState;
    private Animation<AtlasRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;
    private float shieldState;
    public float health;
    private float attackTimer;
    private float finalAttackTimer;

    public Boss(GameScreen screen) {
        this.screen = screen;
        this.position = new Vector2(screen.arena.bounds.x + screen.arena.bounds.width/2f,
                          screen.arena.bounds.y + screen.arena.bounds.height/2f);
        this.hurtCircle = new Circle(position, SIZE/2f);
        this.animationsByState = new ObjectMap<>();
        for (State state : State.values()) {
            Array<AtlasRegion> frames = screen.assets.atlas.findRegions(state.frameRegionsName);
            Animation<AtlasRegion> animation = null;
            if (state == State.idle_a || state == State.idle_b) {
                animation = new Animation<>(0.2f, frames, Animation.PlayMode.LOOP);
            } else {
                animation = new Animation<>(0.25f, frames, Animation.PlayMode.NORMAL);
            }
            animationsByState.put(state, animation);
        }
        this.animation = animationsByState.get(State.idle_a);
        this.keyframe = animation.getKeyFrame(0);
        this.stateTime = 0f;
        this.shieldState = 1f;
        this.health = MAX_HEALTH;
    }

    int cycleCount = 0;
    public void update(float dt){
        accum += dt;
        if (screen.player.isWizard()){
            shieldState -= dt;
        } else {
            shieldState += dt;
        }
        shieldState = MathUtils.clamp(shieldState, 0f, 1f);

        // TODO - handle state changes and switch animation as needed
        stateTime += dt;

        if (!screen.player.isWizard()){
            if (Main.game.mainGameTimer % 10f > 9.15f || Main.game.mainGameTimer %10 < .25f){
                animation = animationsByState.get(State.attack_spell);
                stateTime = (Main.game.mainGameTimer + .85f) % 1;
                currentState = State.attack_spell;
            } else {
                animation = animationsByState.get(State.idle_a);
                currentState = State.idle_a;
            }
        } else {
            attackTimer -= dt;
            finalAttackTimer -= dt;
            // Fighting time
            if (health > MAX_HEALTH * .75f){
                if ((int)(Main.game.mainGameTimer % 5f) == 3){
                    if (attackTimer < 0){
                        attackTimer = .3f;
                        tempVec.set(screen.player.position).sub(position);
                        shootFireball(tempVec.angleDeg(), EffectAnims.Type.fireball_red, 100, 100);
                    }
                } else {
                    animation = animationsByState.get(State.idle_b);
                    currentState = State.idle_a;
                }

            } else if (health > MAX_HEALTH * .5f) {
                if ((int)(Main.game.mainGameTimer % 10f) == 5){
                    if (attackTimer < 0){
                        attackTimer = 2.3f;
                        fireballArc(screen.player.position, 315f, 50, true, EffectAnims.Type.fireball_green, 100);
                    }
                } else {
                    animation = animationsByState.get(State.idle_b);
                    currentState = State.idle_a;
                }
            } else if (health > MAX_HEALTH * .25f) {
                if ((int)(Main.game.mainGameTimer % 10f) == 5){
                    if (attackTimer < 0){
                        cycleCount++;
                        attackTimer = .78f;
                        tempVec.set(1, 0);
                        tempVec.rotateDeg(cycleCount * 45f);
                        tempVec.add(position);
                        fireballArc(screen.player.position, 360f, 15, false, EffectAnims.Type.fireball_blue, 50);
                    }
                } else {
                    animation = animationsByState.get(State.idle_b);
                    currentState = State.idle_a;
                }
            } else {
                // final form
                if (finalAttackTimer < 0){
                    finalAttackTimer = .5f;
                    tempVec.set(screen.player.position).sub(position);
                    shootFireball(tempVec.angleDeg(), EffectAnims.Type.fireball_red, 100, 100);
                }
                if ((int)(Main.game.mainGameTimer % 5f) == 3){
                    if (attackTimer < 0){
                        attackTimer = 2.3f;
                        if (MathUtils.randomBoolean()) {
                            fireballArc(screen.player.position, 315f, 50, true, EffectAnims.Type.fireball_green, 100);
                        } else {
                            tempVec.set(1, 0);
                            tempVec.rotateDeg(MathUtils.random(360f));
                            tempVec.add(position);
                            fireballArc(screen.player.position, 360f, 15, false, EffectAnims.Type.fireball_blue, 50);
                        }
                    }
                } else {
                    animation = animationsByState.get(State.idle_b);
                    currentState = State.idle_b;
                }
            }
        }

        keyframe = animation.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
        batch.draw(keyframe, position.x - SIZE/2f, position.y - SIZE/2f, SIZE, SIZE);
        batch.setColor(Color.WHITE);

        batch.setShader(screen.assets.shieldShader);
        screen.assets.shieldShader.setUniformf("u_time", accum);
        screen.assets.shieldShader.setUniformf("u_shield", shieldState);
        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(screen.assets.noiseTex, position.x - protectedRadius, position.y - protectedRadius, protectedRadius*2f, protectedRadius*2f);
        batch.setColor(Color.WHITE);
        batch.setShader(null);
    }

    public void getHit(float damage, float dx, float dy) {
        health -= damage;
        // TODO some particles or some shit
    }

    public boolean isAlive() {
        return health >= 0;
    }

    Vector2 tempVec = new Vector2();

    private void fireballArc(Vector2 playerPos, float degrees, int shotCount, boolean randomStart, EffectAnims.Type type, float size) {
        tempVec.set(playerPos).sub(position).nor();
        float startDir = tempVec.angleDeg();
        if (randomStart){
            startDir +=  MathUtils.random(-90f, 90f);
        }
        float dAngle = degrees / shotCount;
        for (int i = 0; i < shotCount; i++){
            shootFireball(startDir + dAngle * i, type, 100, size);
        }
    }


    private void shootFireball(float angleDeg, EffectAnims.Type fireballType, float speed, float size) {
        float degreeRad = MathUtils.degRad * angleDeg;
        Projectile proj = new Projectile(screen.assets, fireballType, position.x, position.y, degreeRad, speed, false);
        proj.size = size;
        screen.projectiles.add(proj);
    }
}
