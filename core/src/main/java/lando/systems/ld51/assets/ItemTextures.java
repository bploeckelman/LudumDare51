package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ItemTextures {

    private final TextureRegion sheet;
    private final TextureRegion[][] regions;

    public ItemTextures(Assets assets) {
        String name = "temp-items/oryx-items";
        this.sheet = assets.atlas.findRegion(name);
        if (this.sheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist? Did you run the 'sprites' task in gradle?");
        }
        int tileSize = 47;  // 46.8-ish ... blah
        this.regions = sheet.split(tileSize, tileSize);
    }

    public TextureRegion get(Type type) {
        TextureRegion texture = regions[type.y][type.x];
        if (texture == null) {
            throw new GdxRuntimeException("Can't get item texture for type '" + type.name() + "', it might not have been created correctly, check ItemTextures.java");
        }
        return texture;
    }

    public enum Type {
          gem_red(6, 1)
        , gem_green(8, 1)
        , gem_blue(7, 1)
        ;
        public final int x;
        public final int y;
        Type(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
