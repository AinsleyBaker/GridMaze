package Fallin.engine;

import java.io.Serializable;

/**
 * The Trap class represents a trap cell in the game grid.
 */
public class Trap extends Cell implements Serializable {
    private int damage;
    /**
     * Default constructor to initialize the cell type to 'T'.
     */
    public Trap() {
        setCellType('T');
        damage = 2;
    }

    /**
     * Gets the damage value of the trap.
     *
     * @return the damage value.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage value of the trap.
     *
     * @param damage - the damage value.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

}
