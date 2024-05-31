package Fallin.engine;

import java.io.Serializable;
/**
 * The Gold class represents a gold cell in the game grid.
 */
public class Gold extends Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private int value;

    /**
     * The Gold class represents a gold cell in the game grid.
     */
    public Gold() {
        setCellType('G');
        value = 1;
    }

}
