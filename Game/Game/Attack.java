package Game;

import java.util.Random;

/**
 * Class that Stores attacks.
 */
public class Attack {
    int damage;
    double hitChance;
    double variabilty;
    Random random = new Random();

    /**
     * Constructor for attacks.
     * @param damage amount of damage
     * @param hitChance probability of hitting the Opponent
     * @param variabilty variability the damage and hitChance 
     */
    public Attack(int damage, double hitChance, double variabilty) {
        this.damage = damage;
        this.hitChance = hitChance;
        this.variabilty = variabilty;
    }

    public int getDamage() {
        return damage;
    }

    public void addDamage(int change) {
        this.damage += change;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Getter for an attact that takes into consideration damage and, hitChance and variability.
     * @return the mathematical calculation of probability of hitting , damage and variability
     */
    public double getDamagePower() {
        if (hitChance > Math.random()) {
            return random.nextDouble(damage * variabilty, damage * (1 + variabilty));
        }
        return 0;
    }
}
