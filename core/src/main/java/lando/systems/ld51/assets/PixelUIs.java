package lando.systems.ld51.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

public class PixelUIs {

    private final TextureRegion sheet;
    private final TextureRegion[][] regions;

    public PixelUIs(Assets assets) {
        String name = "icons/input-prompts";
        this.sheet = assets.atlas.findRegion(name);
        if (this.sheet == null) {
            throw new GdxRuntimeException("Unable to find '" + name + "' region in texture atlas. Does sprites/" + name + " exist?");
        }

        this.regions = sheet.split(30, 33);
    }

    public TextureRegion get(PixelUIs.Type type) {
        if (type.x < 0 || type.x >= regions[0].length
                || type.y < 0 || type.y >= regions.length) {
            throw new GdxRuntimeException("Can't get pixeul ui for type '" + type + "', invalid tilesheet coordinates: (" + type.x + ", " + type.y + ")");
        }
        return regions[type.y][type.x];
    }

    public enum Type {
        // TODO: add more entries as needed or when bored
        // pills
        big_yellow_pill_left(0, 28)
        , wide_yellow_pill_center(1, 28)
        , wide_yellow_pill_right(2, 28)
        , narrow_green_pill_left(19, 16)
        , narrow_green_pill_center(20, 16)
        , narrow_green_pill_right(21, 16)
        , narrow_red_pill_left(22, 16)
        , narrow_red_pill_center(23, 16)
        , narrow_red_pill_right(24, 16)
        ;

        public final int x;
        public final int y;
        Type(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
