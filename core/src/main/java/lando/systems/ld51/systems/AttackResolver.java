package lando.systems.ld51.systems;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld51.gameobjects.Enemy;
import lando.systems.ld51.gameobjects.Player;

public class AttackResolver {

    private static final Vector2 attackDir = new Vector2();

    public static void resolve(Player player, Array<Enemy> enemies) {
        if (!player.isAttacking()) return;

        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(player.attackRange, enemy.hurtCircle)) {
                if (Intersector.overlapConvexPolygons(player.attackHitShape, enemy.hurtShape)) {
                    float dx = enemy.position.x - player.position.x;
                    float dy = enemy.position.y - player.position.y;
                    attackDir.set(dx, dy).nor();
                    // TODO - amount should scale based on player class and type of enemy
                    enemy.hurt(1f, attackDir.x, attackDir.y);
                }
            }
        }
    }

}
