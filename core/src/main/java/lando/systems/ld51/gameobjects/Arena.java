package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld51.Main;
import lando.systems.ld51.screens.GameScreen;

public class Arena {
    public Rectangle bounds = new Rectangle(0,0, 2000, 2000);
    Color accentColor = new Color();
    Color baseColor = new Color(.2f, .2f, .2f, 1f);
    float tileSize = 50f;
    private GameScreen screen;

    public Arena(GameScreen screen) {
        this.screen = screen;

    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        switch(screen.player.getCurrentPhase()){
            case RED:
                accentColor.set(.3f, 0, 0, 1f);
                break;
            case GREEN:
                accentColor.set(0, .3f, 0, 1f);
                break;
            case BLUE:
                accentColor.set(0f, 0, .3f, 1f);
                break;
        }
        if (screen.player.isWizard()){
            accentColor.set(.8f, .8f, .8f, 1f);
        }
        for (int x = 0; x < bounds.width/tileSize; x++){
            for (int y = 0; y < bounds.height/tileSize; y++) {
                Color c = ((x+y)%2 == 1) ? accentColor : baseColor;
                batch.setColor(c);
                batch.draw(Main.game.assets.pixel, x *tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        batch.setColor(Color.WHITE);

        int phase = 0;
        switch (screen.player.getCurrentPhase()){
            case RED:
                phase = 0;
                break;
            case GREEN:
                phase = 1;
                break;
            case BLUE:
                phase = 2;
                break;
            case WIZARD:
                phase = 3;
                break;
        }
        if (screen.player.isWizard()){
            phase = 3;
        }

        ShaderProgram shader = screen.assets.backgroundShader;

        batch.setShader(shader);
        shader.setUniformf("u_time", Main.game.mainGameTimer % 10f);
        shader.setUniformi("u_phase", phase);
        batch.draw(screen.assets.noiseTex, -100, -100, bounds.width + 200, bounds.height + 200);
        batch.setShader(null);
    }
}
