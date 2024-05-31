package Fallin.engine;

import java.io.Serializable;

/**
 * The Medical Unit class represents a medical unit cell in the game grid.
 */
public class MedicalUnit extends Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private int healthRestore;

    /**
     * Default constructor to initialize the cell type to 'M'.
     */
    public MedicalUnit() {
        setCellType('M');
        healthRestore = 3;
    }
}
