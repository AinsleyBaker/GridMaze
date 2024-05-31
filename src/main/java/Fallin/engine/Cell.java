package Fallin.engine;

import java.io.Serializable;
/**
 * The Cell class represents a single cell in the game grid.
 * It implements serializable to allow cells to be serialized for saving the game state.
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private char cellType;


    /**
     * Default constructor to initialize the cell type to '-'.
     */
    public Cell(){
        cellType = '-';
    }

    /**
     * Gets the type of the cell.
     *
     * @return the current type of the cell as a char.
     */
    public char getCellType() {
        return cellType;
    }


    /**
     * Sets the type of the cell.
     *
     * @param cellType - the new type of the cell as a char.
     */
    public void setCellType(char cellType) {
        this.cellType = cellType;
    }

}