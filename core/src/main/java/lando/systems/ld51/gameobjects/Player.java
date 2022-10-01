package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld51.Config;
import lando.systems.ld51.assets.CreatureAnims;
import lando.systems.ld51.screens.GameScreen;

public class Player {

    public static float SIZE = 30f;
    public static float SPEED = 100f;
    public static int FULL_GEM_COUNT = 100;

    private final GameScreen gameScreen;

    private TextureRegion keyframe;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public Vector2 position;
    public Vector2 velocity;
    public Vector3 mousePos;
    public Vector2 facing;
    public Vector2 tempPos;
    public Vector2 tempVec2;
    public int redGemCount;
    public int greenGemCount;
    public int blueGemCount;
    public float invulnerabilityTimer;


    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(Config.Screen.window_width/2f, Config.Screen.window_height/2f);
        this.velocity = new Vector2();
        this.facing = new Vector2();
        this.mousePos = new Vector3();
        this.tempPos = new Vector2();
        this.tempVec2 = new Vector2();
        this.animation = gameScreen.assets.creatureAnims.get(CreatureAnims.Type.warrior);
        this.keyframe = animation.getKeyFrame(0f);
        this.stateTime = 0f;
        redGemCount = 0;
        greenGemCount = 0;
        blueGemCount = 0;
        invulnerabilityTimer = 0;
    }

    public void update(float dt) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.worldCamera.unproject(mousePos);
        facing.set(mousePos.x, mousePos.y).sub(position).nor();

        invulnerabilityTimer -= dt;

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);

        if (Gdx.input.isTouched()){
            Arena arena = gameScreen.arena;
            tempVec2.set(facing);
            // collision checks
            tempPos.set(position).add(facing.x * SPEED * dt, facing.y * SPEED * dt);
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

            position.add(tempVec2.x * SPEED * dt, tempVec2.y * SPEED * dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, position.x - (SIZE/2f), position.y - (SIZE/2f), SIZE, SIZE);
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
        return true;
    }
    public void pickupGem(Gem gem) {

    }
}
