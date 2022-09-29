package lando.systems.ld51.utils;

@FunctionalInterface
public interface Callback {
    void run(Object... params);
}
