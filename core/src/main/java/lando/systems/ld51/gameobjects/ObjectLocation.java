package lando.systems.ld51.gameobjects;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld51.utils.Calc;

public class ObjectLocation implements Location<Vector2> {

    public Vector2 position = new Vector2();
    public float orientation = 0;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Calc.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Calc.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new ObjectLocation();
    }

}
