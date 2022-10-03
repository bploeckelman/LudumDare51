package lando.systems.ld51.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import lando.systems.ld51.assets.Assets;
import lando.systems.ld51.assets.EffectAnims;
import lando.systems.ld51.gameobjects.Gem;

public class Particles implements Disposable {

    public enum Layer { background, middle, foreground }

    private static final int MAX_PARTICLES = 4000;

    private final Assets assets;
    private final ObjectMap<Layer, Array<Particle>> activeParticles;
    private final Pool<Particle> particlePool = Pools.get(Particle.class, MAX_PARTICLES);

    public Particles(Assets assets) {
        this.assets = assets;
        this.activeParticles = new ObjectMap<>();
        int particlesPerLayer = MAX_PARTICLES / Layer.values().length;
        this.activeParticles.put(Layer.background, new Array<>(false, particlesPerLayer));
        this.activeParticles.put(Layer.middle,     new Array<>(false, particlesPerLayer));
        this.activeParticles.put(Layer.foreground, new Array<>(false, particlesPerLayer));
    }

    public void clear() {
        for (Layer layer : Layer.values()) {
            particlePool.freeAll(activeParticles.get(layer));
            activeParticles.get(layer).clear();
        }
    }

    public void update(float dt) {
        for (Layer layer : Layer.values()) {
            for (int i = activeParticles.get(layer).size - 1; i >= 0; --i) {
                Particle particle = activeParticles.get(layer).get(i);
                particle.update(dt);
                if (particle.isDead()) {
                    activeParticles.get(layer).removeIndex(i);
                    particlePool.free(particle);
                }
            }
        }
    }

    public void draw(SpriteBatch batch, Layer layer) {
        activeParticles.get(layer).forEach(particle -> particle.render(batch));
    }

    @Override
    public void dispose() {
        clear();
    }

    // ------------------------------------------------------------------------
    // Helper fields for particle spawner methods
    // ------------------------------------------------------------------------
    private final Color tempColor = new Color();
    private final Vector2 tempVec2 = new Vector2();

    // ------------------------------------------------------------------------
    // Spawners for different particle effects
    // ------------------------------------------------------------------------

    public void swipe(float x, float y, float angle) {
        Animation<TextureRegion> animation = assets.effectAnims.get(EffectAnims.Type.swipe);
        activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                .animation(animation)
                .startSize(32)
                .timeToLive(animation.getAnimationDuration())
                .startPos(x, y)
                .startRotation(angle)
                .init());
    }

    public void explode(float x, float y, float size) {
        explode(EffectAnims.Type.explode_puff, x, y, size);
    }

    public void explode(EffectAnims.Type effectType, float x, float y, float size) {
        Animation<TextureRegion> animation = assets.effectAnims.get(effectType);
        activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                .animation(animation)
                .startSize(size)
                .timeToLive(animation.getAnimationDuration())
                .startPos(x, y)
                .init());
    }

    public void dropCoins(int numRed, int numGreen, int numBlue, float x, float y) {
        for (int i = 0; i < numRed; i++) {
            tempVec2.setToRandomDirection();
            float speed = MathUtils.random(100f, 200f);
            float ttl = MathUtils.random(2f, 4f);
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .animation(assets.gemRedSpin)
                    .animUnlocked(true)
                    .startSize(Gem.SIZE)
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .startPos(x, y)
                    .timeToLive(ttl)
                    .velocity(tempVec2.x * speed, tempVec2.y * speed)
                    .init()
            );
        }
        for (int i = 0; i < numGreen; i++) {
            tempVec2.setToRandomDirection();
            float speed = MathUtils.random(100f, 200f);
            float ttl = MathUtils.random(2f, 4f);
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .animation(assets.gemGreenSpin)
                    .animUnlocked(true)
                    .startSize(Gem.SIZE)
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .startPos(x, y)
                    .timeToLive(ttl)
                    .velocity(tempVec2.x * speed, tempVec2.y * speed)
                    .init()
            );
        }
        for (int i = 0; i < numBlue; i++) {
            tempVec2.setToRandomDirection();
            float speed = MathUtils.random(100f, 200f);
            float ttl = MathUtils.random(2f, 4f);
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .animation(assets.gemBlueSpin)
                    .animUnlocked(true)
                    .startSize(Gem.SIZE)
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .startPos(x, y)
                    .timeToLive(ttl)
                    .velocity(tempVec2.x * speed, tempVec2.y * speed)
                    .init()
            );
        }
    }

    Vector2 tempStart = new Vector2();
    public void lightning(Vector2 start, Vector2 end) {
        tempStart.set(start).add(10, 40);
        TextureRegion keyframe = assets.particles.line;
        float dist = tempStart.dst(end);
        tempVec2.set(end).sub(tempStart).nor();
        float angle = tempVec2.angleDeg();
        for (int i = 0; i < dist/5f; i++) {
            float size = MathUtils.random(8f, 30f);
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                            .startPos(tempStart.x + tempVec2.x * (5*i), tempStart.y + tempVec2.y * (5*i))
                            .startSize(size, 15)
                            .startColor(1f, 1f, 0, 1f)
                            .endColor(.3f, .3f, .3f, .3f)
                            .startRotation(angle + MathUtils.random(-30, 30))
                            .timeToLive(.8f * i / (dist/5f))
                    .init()
            );
        }
    }

    public void sparkle(float x, float y) {
        TextureRegion keyframe = assets.particles.sparkle;
        tempColor.set(Color.WHITE);
        int numParticles = 3;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y)
                    .velocityDirection(MathUtils.random(-5, 5) + 90, MathUtils.random(-200, -150))
                    .startSize(MathUtils.random(10, 16))
                    .endSize(MathUtils.random(2, 8))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(2f)
                    .startColor(tempColor)
                    .init());
        }
    }

    public void addSmoke(float x, float y){
        TextureRegion keyframe = assets.particles.smoke;
        float grayValue = MathUtils.random(.7f) + .3f;
        tempColor.set(grayValue, grayValue, grayValue, 1f);
        int numParticles = 10;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y)
                    .targetPos(x + MathUtils.random(-20f, 50f), y + MathUtils.random(-20f, 50f))
                    .velocityDirection(MathUtils.random(-20, 20), MathUtils.random(-20, 20f))
                    .startSize(MathUtils.random(10, 16))
                    .endSize(MathUtils.random(2, 8))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(.5f, 1.5f))
                    .startColor(tempColor)
                    .init());
        }
    }

    public void addLargeSmoke(float x, float y){
        TextureRegion keyframe = assets.particles.smoke;
        float grayValue = MathUtils.random(.7f) + .3f;
        tempColor.set(grayValue, grayValue, grayValue, 1f);
        int numParticles = 150;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x + MathUtils.random(-70f, 70f), y + MathUtils.random(-70f, 70f))
//                    .targetPos(x + MathUtils.random(0f, 250f), y + MathUtils.random(0f, 250f))
                    .velocityDirection(MathUtils.random(-200f, 200f), MathUtils.random(-200f, 200f))
                    .startSize(MathUtils.random(100f, 200f))
                    .endSize(MathUtils.random(50f, 80f))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(1.5f, 3.5f))
                    .startColor(tempColor)
                    .init());
        }
    }

    public void addCash(float x, float y, int amount) {

        Color color = amount > 0 ? Color.GREEN : Color.RED;
        TextureRegion keyframe = assets.particles.dollar;
        int numParticles = Math.abs(amount) / 10;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y)
                    .velocityDirection(MathUtils.random(20f, 160f), MathUtils.random(100f) + 40)
                    .startSize(20)
                    .endSize(30)
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(1f, 1.5f))
                    .startColor(color)
                    .init());
        }
    }

    public void addSmokeStackSmoke(float x, float y){
        TextureRegion keyframe = assets.particles.smoke;

        int numParticles = 1;
        for (int i = 0; i < numParticles; ++i) {
            float grayValue = MathUtils.random(.95f, 1f);
            tempColor.set(grayValue, grayValue, grayValue, 1f);
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x+MathUtils.random(-20f, 20f), y + MathUtils.random(-20f, 20f)) // + MathUtils.random(-70f, 70f), y)
                    .velocityDirection(MathUtils.random(-90f, -30f), MathUtils.random(-50f, -200f))
                    .startSize(MathUtils.random(100f, 120f))
                    .endSize(MathUtils.random(10f, 80f))
                    .startColor(tempColor)
                    .startAlpha(MathUtils.random(.3f, .6f))
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(1.5f, 2.5f))
                    .init());
        }
    }

    private final Color[] projectilePistonColors = new Color[] {
            Color.ORANGE, Color.CORAL, Color.FIREBRICK, Color.YELLOW, Color.PURPLE
    };

    public void projectileBreak(float x, float y, boolean failure){
        TextureRegion keyframe = assets.particles.ring;
        int numParticles = 50;
        for (int i = 0; i < numParticles; ++i) {
            if (failure) {
                tempColor.set(Color.GRAY);
                numParticles = 30;
            } else {
                tempColor.set(projectilePistonColors[MathUtils.random(0, projectilePistonColors.length - 1)]);
            }
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x + MathUtils.random(-5f, 5f), y + MathUtils.random(-5f, 5f))
                    .velocityDirection(MathUtils.random(-300f, 300f), MathUtils.random(-300f, 300f))
                    .startSize(MathUtils.random(4f, 8f))
                    .endSize(MathUtils.random(10f, 20f))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(0.25f, 0.5f))
                    .startColor(tempColor)
                    .init());
        }
    }

    public void reactorSteam(Rectangle rect) {
        TextureRegion keyframe = assets.particles.smoke;
        int numParticles = 5;

        for (int i = 0; i < numParticles; ++i) {
            float x = rect.x + MathUtils.random(rect.width);
            float y = rect.y + MathUtils.random(rect.height);
            tempColor.set(1, 1, 1, 1f);
            activeParticles.get(Layer.background).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y) // + MathUtils.random(-70f, 70f), y)
                    .velocityDirection(MathUtils.random(0, 180), MathUtils.random(50f, 100f))
                    .startSize(MathUtils.random(30f, 80f))
                    .endSize(MathUtils.random(1f, 10f))
                    .startColor(tempColor)
                    .startAlpha(MathUtils.random(.3f, .6f))
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(.5f, 2.5f))
                    .init());
        }    }

    public void projectileTrail(float x, float y) {
        TextureRegion keyframe = assets.particles.sparkle;
        tempColor.set(Color.WHITE);
        int numParticles = 1;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.middle).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y)
                    .startSize(MathUtils.random(32, 40))
                    .endSize(MathUtils.random(3, 8))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(MathUtils.random(0.5f, 1f))
                    .startColor(tempColor)
                    .init());
        }
    }

    public void addPointsParticles(long points, float x, float y, float r, float g, float b) {
        // create a particle for each number in 'points'
        String pointsStr = Long.toString(points, 10);
        int size = MathUtils.clamp(2 * (int)Math.sqrt(points), 24, 60);
        float k = MathUtils.random(0.7f, 1f);
        for (int i = 0; i < pointsStr.length(); ++i) {

            TextureRegion texture = assets.numberParticles.get(Character.digit(pointsStr.charAt(i), 10)).getKeyFrames()[0];
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(texture)
                    .endAlpha(0f)
                    .startColor(r*k, g*k, b*k, 1.5f)
                    .startSize(size * 0.55f, size)
                    .velocity(MathUtils.random(-30f, 30f), 30)
                    .acceleration(0f, MathUtils.random(-4f, -2f))
                    .startPos(x - ((pointsStr.length()-1) * size) * 0.3f + (i * size) * 0.6f, y)
                    .timeToLive(2.5f)
                    .init());
        }
    }

    public void addParticleBurstCollect(int quantity, float[] color, float[] position, float[] target) {
        for (int i = 0; i < quantity; i++) {
            activeParticles.get(Layer.middle).add(Particle.initializer(particlePool.obtain())
                    .keyframe(assets.particles.sparks)
                    .startColor(color[0], color[1], color[1], MathUtils.random(0.5f,0.7f))
                    .targetPos(target[0], target[1])
                    .startPos(position[0] + MathUtils.random(35, 70) * MathUtils.sinDeg(i*360f/quantity), position[1] + MathUtils.random(35, 70) * MathUtils.cosDeg(i*360f/quantity))
                    .timeToLive(MathUtils.random(0.8f, 1.4f))
                    .startSize(25, 25)
                    .endSize(10, 10)
                    .startRotation(MathUtils.random(-4f * 365f, 4f * 365f))
                    .endRotation(MathUtils.random(-4f * 365f, 4f * 365f))
                    .startAlpha(0.5f)
                    .endAlpha(0.25f)
                    .init());
        }

    }

}
