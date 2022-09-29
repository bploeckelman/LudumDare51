package lando.systems.ld51.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Point implements Pool.Poolable {

    public static Pool<Point> pool = Pools.get(Point.class);

    public static final Point Zero = new Point(0, 0);

    public static Point zero() {
        return new Point();
    }

    public static Point at(int x, int y) {
        return new Point(x, y);
    }

    public int x;
    public int y;

    // must be public for Json deserialization
    public Point() {
        this(0, 0);
    }

    public Point(Point other) {
        this(other.x, other.y);
    }

    private Point(int x, int y) {
        set(x, y);
    }

    public Point copy() {
        return new Point(x, y);
    }

    public Point set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point set(Point other) {
        return set(other.x, other.y);
    }

    public Point set(Vector2 other) {
        return set((int) other.x, (int) other.y);
    }

    public boolean is(int x, int y) {
        return (this.x == x && this.y == y);
    }

    public boolean is(Point point) {
        if (point == null) return false;
        return is(point.x, point.y);
    }

    public Point add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point add(Point point) {
        this.x += point.x;
        this.y += point.y;
        return this;
    }

    public Point sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Point sub(Point point) {
        this.x -= point.x;
        this.y -= point.y;
        return this;
    }

    public Point mul(int s) {
        this.x *= s;
        this.y *= s;
        return this;
    }

    public Point div(int s) {
        this.x /= s;
        this.y /= s;
        return this;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    @Override
    public void reset() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
