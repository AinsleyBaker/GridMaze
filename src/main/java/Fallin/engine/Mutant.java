package Fallin.engine;

import java.io.Serializable;

/**
 * The Mutant class represents a mutant cell in the game grid.
 */
public class Mutant extends Cell implements Serializable {
    private static final long serialVersionUID = 1L;

    private int mutantCount;
    private int damage = 4;

    /**
     * Default constructor to initialize the cell type to 'E'.
     */
    public Mutant() {
        super.setCellType('E');
        this.mutantCount = 1;
    }

    /**
     * Gets the number of mutants.
     *
     * @return the current number of mutants.
     */
    public int getMutantCount() {
        return mutantCount;
    }

    /**
     * Sets the number of mutants.
     *
     * @param mutantCount - the new number of mutants.
     */
    public void setMutantCount(int mutantCount) {
        this.mutantCount = mutantCount;
    }

    /**
     * Gets the damage value of the mutant.
     *
     * @return the damage value.
     */
    public int getDamage() {
        return damage;
    }
}
