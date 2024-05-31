package Fallin.engine;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameEngine implements Serializable {

    private static final long serialVersionUID = 1L;
    private Cell[][] board;
    private Player player;
    private static GameMessageCallback messageCallback;
    public boolean isEnd = false;
    public boolean winState = false;
    private int d = 5;
    private int score = 0;
    private int mutants = 5;

    // Constructor
    public GameEngine(int size, GameMessageCallback callback) {
        messageCallback = callback;
        board = new Cell[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                board[y][x] = new Cell();
            }
        }
        // Initialize the player
        player = new Player();
    }

    /**
     * The GameMessageCallback interface defines a callback method for displaying messages.
     */
    public interface GameMessageCallback {
        /**
         * Displays a message.
         *
         * @param message The message to be displayed.
         */
        void showMessage(String message);
    }

    // Getters and Setters...
    public void setMessageCallback(GameMessageCallback callback) {
        messageCallback = callback;
    }

    public int getSize() {
        return board.length;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public int getDiff() {
        return d;
    }

    public void setDiff(int d) {
        this.d = d;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMutants() {
        return mutants;
    }

    public void setMutants(int mutants) {
        this.mutants = mutants;
    }

    /**
     * Function to place objects on the board
     *
     * @param object - Char representing object to be placed
     * @param amount - The quantity of objects to be placed
     */
    public void placeObjects(char object, int amount) {
        int[] coord;
        int count = 0;
        while (count < amount) {
            coord = getRandCoord();
            if (board[coord[0]][coord[1]].getCellType() == '-') {
                switch (object) {
                    case 'E':
                        if (board[coord[0]][coord[1]].getCellType() == 'E') {
                            ((Mutant) board[coord[0]][coord[1]]).setMutantCount(((Mutant) board[coord[0]][coord[1]]).getMutantCount() + 1);
                        } else {
                            board[coord[0]][coord[1]] = new Mutant();
                        }
                        break;
                    case 'G':
                        board[coord[0]][coord[1]] = new Gold();
                        break;
                    case 'T':
                        board[coord[0]][coord[1]] = new Trap();
                        break;
                    case 'M':
                        board[coord[0]][coord[1]] = new MedicalUnit();
                        break;
                }
                count++;
            } else if (object == 'E' && board[coord[0]][coord[1]].getCellType() == 'E') {
                ((Mutant) board[coord[0]][coord[1]]).setMutantCount(((Mutant) board[coord[0]][coord[1]]).getMutantCount() + 1);
                count++;
            }
        }
    }

    /**
     * Initializes a 2D array with cell elements and places objects
     */
    public void initBoard() {
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                board[y][x] = new Cell();
            }
        }
        // Set 'Finish' cell
        board[0][9].setCellType('F');
        board[9][0].setCellType('P');
        player.setPosition(new int[]{9, 0});

        // Place treasures (gold)
        placeObjects('G', 8);
        // Place traps
        placeObjects('T', 5);
        // Place medical units
        placeObjects('M', 2);
        // Place mutants (enemies)
        placeObjects('E', d);
    }

    /**
     * Prints the current game board and cell types
     * Also prints player position on the board as 'P'
     * and the start cell as 'S'
     */
    public void printBoard() {
        String[] stats = new String[]{"------------------",
                "------------------",
                "------------------",
                "Player Health: " + player.getHealth(),
                "Player Gold: " + player.getGold(),
                "Steps Left: " + player.getSteps(),
                "Player Steps: " + player.getStepCounter(),
                "------------------",
                "------------------",
                "------------------"
        };

        int[] playerPos = player.getPosition();
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                // Check if cell is equal to the player's position
                if (playerPos[0] == y && playerPos[1] == x) {
                    System.out.print("P" + "\t");
                } else if (y == 9 && x == 0) {
                    System.out.print("S" + "\t");
                } else {
                    if (board[y][x].getCellType() == 'E' && ((Mutant) board[y][x]).getMutantCount() > 1) {
                        String mutantCount = Integer.toString(((Mutant) board[y][x]).getMutantCount());
                        System.out.print(board[y][x].getCellType() + mutantCount + "\t");
                    } else {
                        System.out.print(board[y][x].getCellType() + "\t");
                    }
                }
            }
            System.out.println(stats[y]);
        }
        System.out.println();
    }

    /**
     * Generates two random y and x coordinates between 0 and 9 inclusive
     *
     * @return an array of size two containing coordinates
     */
    public int[] getRandCoord() {
        Random rand = new Random();
        int[] coords = new int[2];
        coords[0] = rand.nextInt(getSize());
        coords[1] = rand.nextInt(getSize());
        return coords;
    }

    /**
     * Retrieves the positions of mutants on the game board and moves each mutant
     */
    private void getMutant() {
        int[][] mutantPositions = new int[mutants + 1][2];
        int mutantCount = 0;
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                if (board[y][x].getCellType() == 'E') {
                    // includes multiple mutants in one cell in count
                    for (int i = 0; i < ((Mutant) board[y][x]).getMutantCount(); i++) {
                        mutantPositions[mutantCount][0] = y;
                        mutantPositions[mutantCount][1] = x;
                        mutantCount++;
                    }
                }
            }
        }
        mutants = mutantCount;
        for (int i = 0; i < mutantCount; i++) {
            moveMutant(mutantPositions[i][0], mutantPositions[i][1]);
        }
    }

    /**
     * Moves a mutant to a random adjacent cell or stays in place.
     * If the target cell is empty or another mutant cell, the mutant moves to that cell.
     * If the target cell contains a player, the mutant deals damage to the player.
     *
     * @param y - The y-coordinate of the mutant's current position.
     * @param x - The x-coordinate of the mutant's current position.
     */
    public void moveMutant(int y, int x) {
        // Randomly generate a number from 0-4
        Random random = new Random();
        int move = random.nextInt(5);

        int newY = y;
        int newX = x;

        // Make a random move or stay
        switch (move) {
            case 0:
                newX--;
                break;
            case 1:
                newX++;
                break;
            case 2:
                newY--;
                break;
            case 3:
                newY++;
                break;
        }
        if (!((9 < newY || newY < 0) || (9 < newX || newX < 0))) {
            if (board[newY][newX].getCellType() == 'P') {
                player.setHealth(player.getHealth() - 4);
                ((Mutant)board[y][x]).setMutantCount(((Mutant)board[y][x]).getMutantCount() - 1);
                if (((Mutant)board[y][x]).getMutantCount() == 0) {
                    board[y][x] = new Cell();
                }
            }else if ((board[newY][newX].getCellType() == 'E' || board[newY][newX].getCellType() == '-')) {
                ((Mutant)board[y][x]).setMutantCount(((Mutant)board[y][x]).getMutantCount() - 1);
                if (((Mutant)board[y][x]).getMutantCount() == 0) {
                    board[y][x] = new Cell();
                }
                if (!((board[newY][newX].getCellType() == 'E'))) {
                    board[newY][newX] = new Mutant();
                }else {
                    ((Mutant)board[newY][newX]).setMutantCount(((Mutant)board[newY][newX]).getMutantCount() + 1);
                }
            }
        }
    }

    /**
     * Handles movement of the player and interactions with objects
     *
     * @param move - user's direction to move
     */
    public boolean movePlayer(String move) {
        int playerY = player.getPosition()[0];
        int playerX = player.getPosition()[1];
        int newPlayerY = playerY;
        int newPlayerX = playerX;
        // Update coordinates based on move
        switch (move) {
            case "up":
                newPlayerY--;
                break;
            case "down":
                newPlayerY++;
                break;
            case "left":
                newPlayerX--;
                break;
            case "right":
                newPlayerX++;
                break;
            default:
                messageCallback.showMessage("Invalid Move");
                return false;
        }
        // Check if it is an invalid move
        if ((newPlayerY > 9 || newPlayerY < 0) || (newPlayerX > 9 || newPlayerX < 0)) {
            messageCallback.showMessage("Invalid Move");
            return false;
        }

        char nextCell = board[newPlayerY][newPlayerX].getCellType();

        // Checking the cell type of the next cell and handling interactions with objects
        switch (nextCell) {
            // Mutant interaction
            case 'E':
                Mutant mutant = (Mutant) board[newPlayerY][newPlayerX];
                int totalDmg = mutant.getMutantCount() * mutant.getDamage();
                player.setHealth(player.getHealth() - totalDmg);
                mutant.setMutantCount(0);
                messageCallback.showMessage("Damaged By Mutant (-" + totalDmg + ")" );
                break;
            // Treasure interaction
            case 'G':
                player.setGold(player.getGold() + 1);
                messageCallback.showMessage("Collected Gold (1)");
                break;
            // Trap interaction
            case 'T':
                player.setHealth(player.getHealth() - 2);
                messageCallback.showMessage("Damaged By Trap (-2)");
                break;
            // Medicine Unit interaction
            case 'M':
                if (player.getHealth() > 7) {
                    player.setHealth(10);
                } else {
                    player.setHealth(player.getHealth() + 3);
                }
                messageCallback.showMessage("Found Medicine (+3)");
                break;
            // Finish Cell interaction
            case 'F':
                messageCallback.showMessage("Found The Finish!");
                isEnd = true;
                winState = true;
        }
        // Clear the old position of the player
        board[playerY][playerX].setCellType('-');
        // Set the new position of the player
        board[newPlayerY][newPlayerX].setCellType('P');
        player.setPosition(new int[]{newPlayerY, newPlayerX});

        player.setStepCounter(player.getStepCounter() + 1);
        player.setSteps(player.getSteps() - 1);

        if (player.getSteps() == 0 && !winState) {
            isEnd = true;
            messageCallback.showMessage("You Ran Out Of Steps");
            return false;
        }
        getMutant();
        if (player.getHealth() <= 0) {
            isEnd = true;
            messageCallback.showMessage("You Have No Health");
            return false;
        }
        return true;
    }

    /**
     * Prompts the player for a move choice
     * Incorporates error handling
     *
     * @return The validated move choice.
     */
    public String getMoveChoice() {
        Scanner scanner = new Scanner(System.in);
        String move;
        do {
            System.out.println("Select a move: 'up' | 'down' | 'left' | 'right'");
            move = scanner.nextLine().toLowerCase();

            if (!(move.equals("up") || move.equals("down") || move.equals("left") || move.equals("right"))) {
                System.err.println("Please select a valid move.");
            }
        } while (!(move.equals("up") || move.equals("down") || move.equals("left") || move.equals("right")));
        return move;
    }

    /**
     * Reads in difficulty value between 1 and 10 from user
     * Scanner code nested within a do-while loop for correct error handling
     *
     * @return an integer value corresponding to the difficulty
     */
    public int getDifficulty() {
        Scanner scanner = new Scanner(System.in);
        int difficulty = 0;
        do {
            try {
                System.out.println("Enter a difficulty from 0 to 10: ");
                difficulty = scanner.nextInt();

                // If difficulty not between 0 and 10
                if (!(difficulty >= 0 && difficulty <= 10)) {
                    System.err.println("Please enter a number between 0 and 10.");
                }
            } catch (InputMismatchException e) {
                System.err.println("You must enter an integer.");
                scanner.nextLine();
            }
        }
        while (!(difficulty >= 0 && difficulty <= 10));
        System.out.println();
        return difficulty;
    }

    /**
     * The main method that handles management and initialization of the game engine.
     * sets up the game board, and manages the game loop where the player makes moves
     * until the game ends.
     *
     * @param args - command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10, message -> System.out.println(message));
        engine.setDiff(engine.getDifficulty());
        engine.mutants = engine.d;
        String move;
        engine.initBoard();

        Scanner scanner = new Scanner(System.in);

        while (!engine.isEnd) {
            engine.setBoard(engine.board);
            engine.printBoard();
            messageCallback.showMessage("Make a move: ");
            move = scanner.nextLine().toLowerCase();

            if (move.equals("save")) {
                engine.saveGame("savegame.dat");
            } else if (move.equals("load")) {
                engine = GameEngine.loadGame("savegame.dat");
                if (engine == null) {
                    System.out.println("Failed to load game");
                }
            }else if (move.equals("help")) {
                System.out.println("- Player can set difficulty from 0 to 10 which determines mutant count." +
                        "\n- The player can type 'up', 'down', 'left', 'right' to move." +
                        "\n- The player can type 'save' to save the game." +
                        "\n- The player can type 'load' to load the game." +
                        "\n- The player must reach the finish door to win the game." +
                        "\n- The player loses the game if they run out of steps." +
                        "\n- The player loses the game if they don't have health." +
                        "\n- Each move reduces player's steps by one." +
                        "\n- Medical Unit heals player for 3." +
                        "\n- Traps damage player by 2." +
                        "\n- Mutants make a random move after each player move." +
                        "\n- Mutants damage player by 4." +
                        "\n- Gold coins increases player treasure by 1.");
            } else {
                if (!engine.movePlayer(move) && !engine.isEnd) {
                    System.out.println("Invalid move or ran out of steps/health.");
                }
            }
        }

        if (engine.winState) {
            engine.score = 20 * engine.player.getGold() + (engine.player.getStepCounter() - engine.player.getSteps());
            messageCallback.showMessage("Congratulations You Won With a Score of " + engine.score + '!');
        } else {
            engine.score = -1;
            messageCallback.showMessage("You Lost. Your Score was " + engine.score + '.');
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = currentDate.format(formatter);
        Score score = new Score(engine.getScore(), date);
        TopScoresManager.addScore(score);
        List<Score> topScores = TopScoresManager.getTopScores();
        System.out.println("Top Scores");
        int rank = 1;
        for (Score entry : topScores) {
            System.out.println("#" + rank + " " + entry.getPoints() + " " + entry.getDate());
            rank++;
        }
    }

    public void saveGame(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            messageCallback.showMessage("Saved Game");
        } catch (IOException e) {
            messageCallback.showMessage("Failed to save game");
            e.printStackTrace();
        }
    }

    public static GameEngine loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            GameEngine loadedEngine = (GameEngine) ois.readObject();
            messageCallback.showMessage("Loaded Game");
            return loadedEngine;
        } catch (IOException | ClassNotFoundException e) {
            messageCallback.showMessage("Failed to load game.");
            e.printStackTrace();
            return null;
        }
    }
}
