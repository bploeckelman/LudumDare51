package lando.systems.ld51;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import text.formic.Stringf;

public class Config {

    public static final String title = "Ludum Dare 51";

    public static class Screen {
        public static final int window_width = 948;
        public static final int window_height = 533;
        public static final int framebuffer_width = 948;
        public static final int framebuffer_height = 533;
    }

    public static class Debug {
        public static boolean general = false;
        public static boolean shader = false;
    }

    public static String getFpsString() {
        return Stringf.format("[FPS] %d", Gdx.graphics.getFramesPerSecond());
    }

    public static String getJavaHeapString() {
        return Stringf.format("[Heap (java)] %,d kb", Gdx.app.getJavaHeap() / 1024);
    }

    public static String getNativeHeapString() {
        return Stringf.format("[Heap (native)] %,d kb", Gdx.app.getNativeHeap() / 1024);
    }

    public static String getDrawCallString(SpriteBatch batch) {
        return Stringf.format("[Render Calls] %d", batch.renderCalls);
    }

}
