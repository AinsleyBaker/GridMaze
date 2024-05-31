import Fallin.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the GameEngine class.
 */
public class GameEngineTest {
    private GameEngine engine;
    private static GameEngine.GameMessageCallback messageCallback = message -> {
    };

    /**
     * Initialize the engine for each test.
     */
    @BeforeEach
    public void setUp() {
        engine = new GameEngine(10, messageCallback);
    }

    /**
     * Test setting a valid difficulty level.
     */
    @Test
    public void setDiff_ValidDiff_CorrectDiff() {
        engine.setDiff(5);
        assertEquals(5, engine.getDiff());
    }

    /**
     * Test that the board is initialized with the correct size.
     */
    @Test
    public void initBoard_BoardInitialized_CorrectSize() {
        engine.initBoard();
        assertNotNull(engine.getBoard());
        assertEquals(10, engine.getSize());
    }

    /**
     * Test moving the player with a valid direction.
     */
    @Test
    public void movePlayer_Up_PlayerMovesUp() {
        engine.initBoard();
        int[] initialPosition = engine.getPlayer().getPosition();
        boolean moveState = engine.movePlayer("up");
        int[] newPosition =engine.getPlayer().getPosition();
        // Ensure player has moved
        assertTrue(moveState);
        // Ensure has moved in correct direction
        assertEquals(initialPosition[0] - 1, newPosition[0]);
        assertEquals(initialPosition[1], newPosition[1]);
    }

    /**
     * Test moving the player with an invalid direction.
     */
    @Test
    public void movePlayer_InvalidDirection_NoChange() {
        engine.initBoard();
        int[] initialPosition = engine.getPlayer().getPosition();
        boolean moveState = engine.movePlayer("!@#$%^&*()");
        // Assert player has not moved
        assertFalse(moveState);
        int[] newPosition = engine.getPlayer().getPosition();
        // Ensure the players position remains the same
        assertArrayEquals(initialPosition, newPosition);
    }

    /**
     * Test moving the player into a trap cell.
     */
    @Test
    public void movePlayer_IntoTrap_PlayerHealthDecreases() {
        engine.getBoard()[0][1] = new Trap();
        engine.getPlayer().setPosition(new int[]{1, 1});
        engine.movePlayer("up");
        int newHealth = engine.getPlayer().getHealth();
        assertEquals(8, newHealth);
    }

    /**
     * Test moving the player into a gold cell.
     */
    @Test
    public void movePlayer_IntoGold_PlayerCollectsGold() {
        engine.getBoard()[0][1] = new Gold();
        engine.getPlayer().setPosition(new int[]{1, 1});
        engine.movePlayer("up");
        int newGold = engine.getPlayer().getGold();
        assertEquals(1, newGold);
    }

    /**
     * Test moving the player into a mutant cell.
     */
    @Test
    public void movePlayer_IntoMutant_PlayerHealthDecreases() {
        engine.getBoard()[0][1] = new Mutant();
        engine.getPlayer().setPosition(new int[]{1, 1});
        engine.movePlayer("up");
        int newHealth = engine.getPlayer().getHealth();
        assertEquals(6, newHealth);
    }

    /**
     * Test moving the player into a medical unit cell.
     */
    @Test
    public void movePlayer_IntoMedUnit_PlayerHealthIncreases() {
        engine.getBoard()[0][1] = new MedicalUnit();
        engine.getPlayer().setPosition(new int[]{1, 1});
        engine.getPlayer().setHealth(5);
        engine.movePlayer("up");
        int newHealth = engine.getPlayer().getHealth();
        assertEquals(8, newHealth);
    }

    /**
     * Test saving and loading the game state to ensure it is preserved.
     */
    @Test
    public void saveAndLoadGame_GameSavedAndLoaded_GameStatePreserved() {
        // Test saving and loading the game
        engine.initBoard();
        engine.saveGame("test_savegame.dat");
        GameEngine loadedEngine = GameEngine.loadGame("test_savegame.dat");
        assertNotNull(loadedEngine);
        assertEquals(engine.getDiff(), loadedEngine.getDiff());
        assertArrayEquals(engine.getPlayer().getPosition(), loadedEngine.getPlayer().getPosition());
    }

    /**
     * Test moving a mutant to a new position.
     */
    @Test
    public void moveMutant_MutantMovesToNewPosition() {
        engine.initBoard();
        engine.getBoard()[5][5] = new Mutant();
        engine.moveMutant(5, 5);
        boolean mutantMoved = false;
        for (int y = 0; y < engine.getBoard().length; y++) {
            for (int x = 0; x < engine.getBoard()[y].length; x++) {
                if (engine.getBoard()[y][x] instanceof Mutant && (y != 5 || x != 5)) {
                    mutantMoved = true;
                    break;
                }
            }
            if (mutantMoved) break;
        }
        assertTrue(mutantMoved);
        assertFalse(engine.getBoard()[5][5] instanceof Mutant);
    }

    /**
     * Test placing gold/treasure objects on the board.
     */
    @Test
    public void placeObjects_PlaceGold_GoldPlacedCorrectly() {
        Cell[][] board = new Cell[10][10];
        engine.placeObjects('G', 5);
        int goldCount = 0;
        for (Cell[] row : engine.getBoard()) {
            for (Cell cell : row) {
                if (cell instanceof Gold) {
                    goldCount++;
                }
            }
        }
        assertEquals(5, goldCount);
    }

    /**
     * Test placing traps on the board.
     */
    @Test
    public void placeObjects_PlaceTraps_TrapsPlacedCorrectly() {
        Cell[][] board = new Cell[10][10];
        engine.placeObjects('T', 3);
        int trapCount = 0;
        for (Cell[] row : engine.getBoard()) {
            for (Cell cell : row) {
                if (cell instanceof Trap) {
                    trapCount++;
                }
            }
        }
        assertEquals(3, trapCount);
    }

    /**
     * Test placing medical units on the board.
     */
    @Test
    public void placeObjects_PlaceMedicalUnits_MedicalUnitsPlacedCorrectly() {
        Cell[][] board = new Cell[10][10];
        engine.placeObjects('M', 7);
        int medUnitCount = 0;
        for (Cell[] row : engine.getBoard()) {
            for (Cell cell : row) {
                if (cell instanceof MedicalUnit) {
                    medUnitCount++;
                }
            }
        }
        assertEquals(7, medUnitCount);
    }

    /**
     * Test placing mutants on the board.
     */
    @Test
    public void placeObjects_PlaceMutants_MutantsPlacedCorrectly() {
        Cell[][] board = new Cell[10][10];
        engine.placeObjects('E', 4);
        int mutantCount = 0;
        for (Cell[] row : engine.getBoard()) {
            for (Cell cell : row) {
                if (cell instanceof Mutant) {
                    mutantCount++;
                }
            }
        }
        assertEquals(4, mutantCount);
    }

    /**
     * Test initializing the board with objects placed correctly.
     */
    @Test
    public void initBoard_BoardInitialized_ObjectsPlaced() {
        engine.initBoard();
        int goldCount = 0;
        int trapCount = 0;
        int medUnitCount = 0;
        int mutantCount = 0;
        for (Cell[] row : engine.getBoard()) {
            for (Cell cell : row) {
                if (cell instanceof Gold) {
                    goldCount++;
                } else if (cell instanceof Trap) {
                    trapCount++;
                } else if (cell instanceof MedicalUnit) {
                    medUnitCount++;
                } else if (cell instanceof Mutant) {
                    mutantCount++;
                }
            }
        }
        assertEquals(8, goldCount);
        assertEquals(5, trapCount);
        assertEquals(2, medUnitCount);
        assertEquals(engine.getMutants(), mutantCount);
    }

    /**
     * Test generating random coordinates.
     */
    @Test
    public void getRandCoord_GenerateRandomCoordinates_CoordinatesInRange() {
        for (int i = 0; i < 100; i++) {
            int[] coords = engine.getRandCoord();
            assertTrue(coords[0] >= 0 && coords[0] < 10);
            assertTrue(coords[1] >= 0 && coords[1] < 10);
        }
    }

    /**
     * Test to verify that a newly created Cell has the default cell type '-'.
     */
    @Test
    public void Cell_CreateCell_CorrectCellType() {
        Cell[][] board = new Cell[10][10];
        board[0][0] = new Cell();
        char cellType = board[0][0].getCellType();
        assertEquals(cellType, '-');
    }

     /**
     * Test to verify that a newly created Mutant cell has the cell type 'M'.
     */
    @Test
    public void Mutant_CreateMutant_CorrectCellType() {
        Cell[][] board = new Cell[10][10];
        board[0][0] = new Mutant();
        char cellType = board[0][0].getCellType();
        assertEquals(cellType, 'E');
    }

    /**
     * Test to verify that a newly created Gold cell has the cell type 'G'.
     */
    @Test
    public void Gold_CreateGold_CorrectCellType() {
        Cell[][] board = new Cell[10][10];
        board[0][0] = new Gold();
        char cellType = board[0][0].getCellType();
        assertEquals(cellType, 'G');
    }

    /**
     * Test to verify that a newly created Meical unit cell has the cell type 'M'.
     */
    @Test
    public void MedicalUnit_CreateMedicalUnit_CorrectCellType() {
        Cell[][] board = new Cell[10][10];
        board[0][0] = new MedicalUnit();
        char cellType = board[0][0].getCellType();
        assertEquals(cellType, 'M');
    }

    /**
     * Test to verify that a newly created Trap cell has the cell type 'T'.
     */
    @Test
    public void Trap_CreateTrap_CorrectCellType() {
        Cell[][] board = new Cell[10][10];
        board[0][0] = new Trap();
        char cellType = board[0][0].getCellType();
        assertEquals(cellType, 'T');
    }
}