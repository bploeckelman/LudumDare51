package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.screens.GameScreen;

public class Boss {

    public static float SIZE = 150;


    public float protectedRadius = 130;
    public Vector2 position;

    private float accum;

    private final GameScreen screen;
    private final Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float stateTime;

    public Boss(GameScreen screen) {
        this.screen = screen;
        this.position = new Vector2(screen.arena.bounds.x + screen.arena.bounds.width/2f,
                          screen.arena.bounds.y + screen.arena.bounds.height/2f);
        TextureRegion frame = screen.assets.atlas.findRegion("characters/wizard-glass-joe");
        this.animation = new Animation<>(0.1f, frame);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        this.keyframe = animation.getKeyFrame(0);
        this.stateTime = 0f;
    }

    public void update(float dt){
        accum += dt;

        stateTime += dt;
        keyframe = animation.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
        batch.draw(keyframe, position.x - SIZE/2f, position.y - SIZE/2f, SIZE, SIZE);
        batch.setColor(Color.WHITE);

        batch.setShader(screen.assets.shieldShader);
        screen.assets.shieldShader.setUniformf("u_time", accum);
        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(screen.assets.noiseTex, position.x - protectedRadius, position.y - protectedRadius, protectedRadius*2f, protectedRadius*2f);
        batch.setColor(Color.WHITE);
        batch.setShader(null);
    }
}
