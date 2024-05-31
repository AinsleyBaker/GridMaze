package Fallin.gui;

import Fallin.engine.*;
import Fallin.engine.Cell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * The Controller class manages the GUI components and interactions of the game.
 */
public class Controller implements GameEngine.GameMessageCallback {
    // Declaring FXML variables for GUI
    @FXML
    private BorderPane root;

    @FXML
    private VBox centerBox;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextArea messageArea;

    @FXML
    private VBox difficultyBox;

    @FXML
    private TextField difficultyField;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private Label healthLabel;

    @FXML
    private Label goldLabel;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stepCounterLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label topScoresLabel;
    // Game engine instance
    private GameEngine engine;

    private static final int CELL_SIZE = 68;

    /**
     * Function to initialize GUI components of the game.
     */
    @FXML
    public void initialize() {
        // Setup background image
        Image bgImage = new Image(getClass().getResource("/images/bg.jpg").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(1200, 700, false, false, false, true);
        BackgroundImage background = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );
        root.setBackground(new Background(background));
        // Initially disable movement and save/load buttons
        disableButtons(true);
        // Ensure difficulty box is visible initially
        difficultyBox.setVisible(true);
        topScoresLabel.setPrefWidth(1200);
    }

    /**
     * Sets the movement buttons to be enabled or disabled.
     *
     * @param disabled - True to disable the buttons, false to enable.
     */
    private void disableButtons(boolean disabled) {
        upButton.setDisable(disabled);
        downButton.setDisable(disabled);
        leftButton.setDisable(disabled);
        rightButton.setDisable(disabled);
        saveButton.setDisable(disabled);
        loadButton.setDisable(disabled);
    }

    /**
     * Handles the start game action.
     */
    @FXML
    private void handleStartGame() {
        String input = difficultyField.getText();
        try {
            int difficulty = Integer.parseInt(input);
            if (difficulty >= 0 && difficulty <= 10) {
                startGame(difficulty);
            } else {
                showPopup("Invalid Number","Please Enter a Number Between 0 and 10.");
            }
        } catch (NumberFormatException ex) {
            showPopup("Invalid Number","Please Enter a Valid Number");
        }
    }

    /**
     * Displays a popup on the screen
     *
     * @param title - The title of the popup.
     * @param title - The message to display.
     */
    private void showPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        String css = getClass().getResource("/app.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("pop-up");
        alert.showAndWait();
    }

    /**
     * Starts the game with the specified difficulty.
     *
     * @param difficulty - The difficulty level.
     */
    private void startGame(int difficulty) {
        // Initialize game engine
        engine = new GameEngine(10, this);
        engine.setDiff(difficulty);
        engine.setMutants(engine.getDiff());
        engine.initBoard();
        displayTopScores();
        updateGui();

        // Disable movement and start/load buttons
        disableButtons(false);

        // Hide difficulty selection
        difficultyBox.setVisible(false);
        centerBox.getChildren().remove(difficultyBox);
    }

    /**
     * Updates the GUI based on the game state.
     */
    private void updateGui() {
        gridPane.getChildren().clear();

        // Get image icon for each cell
        for (int y = 0; y < engine.getSize(); y++) {
            for (int x = 0; x < engine.getSize(); x++) {
                Cell cell = engine.getBoard()[y][x];
                Image icon = getCellIcon(cell);
                if (icon != null) {
                    ImageView imageView = new ImageView(icon);
                    imageView.setFitWidth(CELL_SIZE);
                    imageView.setFitHeight(CELL_SIZE);
                    gridPane.add(imageView, x, y);
                } else {
                    System.out.println("Skipping cell (" + y + ", " + x + ") due to missing image.");
                }
            }
        }
        gridPane.setGridLinesVisible(true);

        // Update stats labels
        healthLabel.setText("Player Health: " + engine.getPlayer().getHealth());
        goldLabel.setText("Player Gold: " + engine.getPlayer().getGold());
        stepsLabel.setText("Steps Left: " + engine.getPlayer().getSteps());
        stepCounterLabel.setText("Player Steps: " + engine.getPlayer().getStepCounter());
        positionLabel.setText("Player Position: " + Arrays.toString(engine.getPlayer().getPosition()));

        // Check if the game has ended
        if (engine.isEnd) {
            if (engine.winState) {
                engine.setScore(20 * engine.getPlayer().getGold() + (engine.getPlayer().getSteps()));
                System.out.println("Congratulations! You won with a score of " + engine.getScore());
            } else {
                engine.setScore(-1);
                System.out.println("You Lost. Your Score was " + engine.getScore());
            }
            TopScoresManager.addScore(new Score(engine.getScore(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            displayTopScores();
            gameOver();
        } else if (engine.getPlayer().getHealth() <= 0 || engine.getPlayer().getSteps() <= 0) {
            System.out.println("Game Over!");
            gameOver();
        }

    }

    /**
     * Displays the top 5 high scores to the GUI.
     */
    private void displayTopScores() {
        // Retrieve top plays from manager
        List<Score> topScores = TopScoresManager.getTopScores();

        // Build top scores string
        StringBuilder topScoresString = new StringBuilder("Top Scores\n");
        int rank = 1;
        for (Score score : topScores) {
            int points = score.getPoints(); // Access points from Score object
            String date = score.getDate(); // Access date from Score object

            String scoreEntry = String.format("#%d: %d - %s\n", rank, points, date);
            topScoresString.append(scoreEntry).append('\n');
            rank++;
        }

        // Update top scores label text
        topScoresLabel.setText(topScoresString.toString());
    }

    /**
     * Ends game and handles re-playing the game.
     */
    private void gameOver() {

        showPopup("Game Over", "Game Over! Your score was " + engine.getScore() + '!');
        // Disable movement buttons and save/load buttons
        disableButtons(true);

        // Create and add a "Start New Game" button with an ID
        Button newGameButton = new Button("Start New Game");
        newGameButton.setId("newGameButton");
        newGameButton.setOnAction(event -> restartGame());

        // Position the button in the center of the gridPane
        StackPane stackPane = new StackPane(newGameButton);
        stackPane.setPrefSize(gridPane.getWidth(), gridPane.getHeight());
        stackPane.setAlignment(Pos.CENTER);
        root.setCenter(stackPane);
    }

    /**
     * Quits the game.
     */
    @FXML
    private void quitGame() {
        Platform.exit(); // Exit the application
    }

    /**
     * Displays help information to the user.
     */
    @FXML
    private void getHelp() {
        showPopup("Help", "- Player can set difficulty from 0 to 10 which determines mutant count." +
                "\n- The player can move up, down, left, right using buttons." +
                "\n- The player must reach the finish door to win the game." +
                "\n- The player loses the game if they run out of steps." +
                "\n- The player loses the game if they don't have health." +
                "\n- Each move reduces player's steps by one." +
                "\n- Medical Unit heals player for 3." +
                "\n- Traps damage player by 2." +
                "\n- Mutants make a random move after each player move." +
                "\n- Mutants damage player by 4." +
                "\n- Gold coins increases player treasure by 1.");
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        // Re-initialize game engine
        engine = new GameEngine(10, this);
        // Re-initialize player stats
        engine.setPlayer(new Player());
        // Reset GUI
        root.setCenter(centerBox);
        difficultyBox.setVisible(true);
        centerBox.getChildren().add(0, difficultyBox);
    }

    /**
     * Gets the icon for a cell.
     *
     * @param cell - The cell.
     * @return The icon image.
     */
    private Image getCellIcon(Cell cell) {
        char cellType = cell.getCellType();
        try {
            switch (cellType) {
                case 'P':
                    // Returns image of the player
                    return new Image(getClass().getResourceAsStream("/images/player.png"));
                case 'E':
                    // Returns image of mutant
                    if (((Mutant) cell).getMutantCount() == 1) {
                        return new Image(getClass().getResourceAsStream("/images/mutant.png"));
                    }
                    // Returns image of two mutants within a cell
                    return new Image(getClass().getResourceAsStream("/images/2mutants.png"));
                case 'T':
                    // Returns image of trap
                    return new Image(getClass().getResourceAsStream("/images/trap.png"));
                case 'G':
                    // Returns image of gold/treasure
                    return new Image(getClass().getResourceAsStream("/images/treasure.png"));
                case 'F':
                    // Returns image of exit
                    return new Image(getClass().getResourceAsStream("/images/exit.png"));
                case 'S':
                    // Returns image of entrance
                    return new Image(getClass().getResourceAsStream("/images/entrance.png"));
                case 'M':
                    // Returns image of medical unit
                    return new Image(getClass().getResourceAsStream("/images/medunit.png"));
                default:
                    // Returns image of default cell
                    return new Image(getClass().getResourceAsStream("/images/default.png"));
            }
        } catch (Exception e) {
            System.out.println("Image file not found.");
            return null;
        }
    }

    /**
     * Moves the player up.
     */
    @FXML
    private void moveUp() {
        engine.movePlayer("up");
        updateGui();
    }

    /**
     * Moves the player down.
     */
    @FXML
    private void moveDown() {
        engine.movePlayer("down");
        updateGui();
    }


    /**
     * Moves the player left.
     */
    @FXML
    private void moveLeft() {
        engine.movePlayer("left");
        updateGui();
    }

    /**
     * Moves the player right.
     */
    @FXML
    private void moveRight() {
        engine.movePlayer("right");
        updateGui();
    }

    /**
     * Saves the board and players game state.
     */
    @FXML
    private void saveGame() {
        if (engine != null) {
            engine.saveGame("savegame.dat");
        } else {
            System.err.println("Engine is not initialized.");
        }
    }

    /**
     * Loads the last previously saved game.
     */
    @FXML
    private void loadGame() {
        GameEngine loadedEngine = GameEngine.loadGame("savegame.dat");
        if (loadedEngine != null) {
            engine = loadedEngine;
            updateGui();
            disableButtons(false);
        }
    }

    /**
     * Shows a message in the text area.
     *
     * @param message - The message.
     */
    @Override
    public void showMessage(String message) {
        messageArea.appendText(message + "\n");
    }
}

