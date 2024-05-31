import Fallin.engine.Score;
import Fallin.engine.TopScoresManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the TopScoresManager class.
 */
public class TopScoresManagerTest {
    private static final String TEST_HS_FILE = "test_top_scores.txt";

    /**
     * Set up high scores file and clear all scores in the TopScoresManager
     */
    @BeforeEach
    public void setUp() {
        TopScoresManager.setScoresFile(TEST_HS_FILE);
        TopScoresManager.clearScores();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_HS_FILE))) {
        } catch (IOException e) {
            fail("Failed to create file.");
        }
    }

    /**
     * Delete the test scores file.
     */
    @AfterEach
    public void deleteFile() {
        File file = new File(TEST_HS_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Test adding a valid score to the top scores list.
     */
    @Test
    public void addScore_ValidScore_ScoreAdded() {
        Score score = new Score(100, "01/01/2024");
        TopScoresManager.addScore(score);
        List<Score> topScores = TopScoresManager.getTopScores();
        assertEquals(1, topScores.size());
        assertEquals(score, topScores.get(0));
    }

    /**
     * Test adding a score that is not high enough to be in the top scores list.
     */
    @Test
    public void addScore_NotTopFive_ScoreNotAdded() {
        for (int i = 1; i <= 5; i++) {
            TopScoresManager.addScore(new Score(100 - i * 10, "01/01/2024"));
        }
        Score lowScore = new Score(50, "02/01/2024");
        TopScoresManager.addScore(lowScore);
        List<Score> topScores = TopScoresManager.getTopScores();
        assertEquals(5, topScores.size());
        assertFalse(topScores.contains(lowScore));
    }

/**
     * Test writing and then loading scores from a file.
     */
    @Test
    public void loadScoresFromFile_ValidFile_ScoresLoaded() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_HS_FILE))) {
            writer.write("200,01/01/2024\n");
            writer.write("150,06/06/2024\n");
            writer.write("100,12/12/2024\n");
        } catch (IOException e) {
            fail("Failed to create test scores file");
        }

        TopScoresManager.loadScoresFromFile();
        List<Score> topScores = TopScoresManager.getTopScores();
        assertEquals(3, topScores.size());
        assertEquals(200, topScores.get(0).getPoints());
        assertEquals(150, topScores.get(1).getPoints());
        assertEquals(100, topScores.get(2).getPoints());
    }

    /**
     * Test saving scores to a file and reloading it.
     */
    @Test
    public void saveScoresToFile_ScoresSaved() {
        TopScoresManager.addScore(new Score(200, "01/01/2024"));
        TopScoresManager.addScore(new Score(100, "12/12/2024"));
        TopScoresManager.saveScoresToFile();
        TopScoresManager.clearScores();
        TopScoresManager.loadScoresFromFile();
        List<Score> topScores = TopScoresManager.getTopScores();
        assertEquals(2, topScores.size());
        assertEquals(200, topScores.get(0).getPoints());
        assertEquals(150, topScores.get(1).getPoints());
    }
}
