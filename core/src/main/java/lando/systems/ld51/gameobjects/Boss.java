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

    public static float SIZE = 200;

    public float protectedRadius = 130;

    private float accum;

    private final GameScreen screen;
    public Circle hurtCircle;

    private final ObjectMap<State, Animation<AtlasRegion>> animationsByState;
    private Animation<AtlasRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;
    private float shieldState;
    public float health;

    public Boss(GameScreen screen) {
        this.screen = screen;
        this.position = new Vector2(screen.arena.bounds.x + screen.arena.bounds.width/2f,
                          screen.arena.bounds.y + screen.arena.bounds.height/2f);
        this.hurtCircle = new Circle(position, SIZE/2f);
        this.animationsByState = new ObjectMap<>();
        for (State state : State.values()) {
            Array<AtlasRegion> frames = screen.assets.atlas.findRegions(state.frameRegionsName);
            Animation<AtlasRegion> animation = new Animation<>(0.2f, frames, Animation.PlayMode.LOOP);
            animationsByState.put(state, animation);
        }
        this.animation = animationsByState.get(State.idle_a);
        this.keyframe = animation.getKeyFrame(0);
        this.stateTime = 0f;
        this.shieldState = 1f;
        this.health = MAX_HEALTH;
    }

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
}
