package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        if (screen.player.getIsWizard()){
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
    }
}
