package lando.systems.ld51.systems;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld51.gameobjects.Boss;
import lando.systems.ld51.gameobjects.Enemy;
import lando.systems.ld51.gameobjects.Player;
import lando.systems.ld51.gameobjects.Projectile;

public class AttackResolver {

    private static float BASE_WEAPON_DAMAGE = 1f;
    private static float SAME_TYPE_MULTIPLIER = 3f;
    private static final Vector2 attackDir = new Vector2();

    public static void resolve(Player player, Array<Enemy> enemies, Boss boss, Array<Projectile> projectiles) {
        // hurt enemies and the boss with projectiles
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            for (Enemy enemy : enemies) {
                if (projectile.playerShot && projectile.alive && projectile.bounds.overlaps(enemy.hurtCircle)) {
                    float amount = projectile.damageAmount * SAME_TYPE_MULTIPLIER;
                    float dx = projectile.direction.x;
                    float dy = projectile.direction.y;
                    enemy.hurt(amount, dx, dy);

                    projectile.kill();
                    projectiles.removeIndex(i);
                }
            }

            if (projectile.playerShot && projectile.alive && Intersector.overlaps(projectile.bounds, boss.hurtBox)) {
                float amount = projectile.damageAmount;
                float dx = projectile.direction.x;
                float dy = projectile.direction.y;
                boss.getHit(amount, projectile.position.x, projectile.position.y, dx, dy);

                projectile.kill();
                projectiles.removeIndex(i);
            }

            if (!projectile.playerShot && projectile.alive && projectile.bounds.overlaps(player.hurtCircle)) {
                float amount = projectile.damageAmount;
                float dx = projectile.direction.x;
                float dy = projectile.direction.y;
                player.hurt(amount, dx, dy);

                projectile.kill();
                projectiles.removeIndex(i);
            }
        }

        // hurt the player with touching
        if (!player.isHurt) {
            for (Enemy enemy : enemies) {
                if (enemy.isDead()) continue;
                if (player.hurtCircle.overlaps(enemy.hurtCircle)) {
                    float dx = player.position.x - enemy.getPosition().x;
                    float dy = player.position.y - enemy.getPosition().y;
                    attackDir.set(dx, dy).nor();
                    player.hurt(1f, attackDir.x, attackDir.y);
                    break;
                }
            }
        }

        if (!player.isAttacking()) return;
        if (player.isWizard()) return;

        // hurt enemies with melee attacks
        for (Enemy enemy : enemies) {
            if (Intersector.overlaps(player.attackRange, enemy.hurtCircle)) {
                if (player.attackHitShape != null && enemy.hurtShape != null) {
                    if (Intersector.overlapConvexPolygons(player.attackHitShape, enemy.hurtShape)) {
                        float dx = enemy.getPosition().x - player.position.x;
                        float dy = enemy.getPosition().y - player.position.y;
                        attackDir.set(dx, dy).nor();
                        // TODO - amount should scale based on player class and type of enemy
                        float multiplier = 1f;
                        switch (enemy.type.gemColor) {
                            case RED:
                                if (player.getCurrentPhase() == Player.Phase.RED) multiplier = SAME_TYPE_MULTIPLIER;
                                break;
                            case GREEN:
                                if (player.getCurrentPhase() == Player.Phase.GREEN) multiplier = SAME_TYPE_MULTIPLIER;
                                break;
                            case BLUE:
                                if (player.getCurrentPhase() == Player.Phase.BLUE) multiplier = SAME_TYPE_MULTIPLIER;
                                break;
                        }
                        enemy.hurt(BASE_WEAPON_DAMAGE * multiplier, attackDir.x, attackDir.y);
                    }
                }
            }
        }
    }

}
