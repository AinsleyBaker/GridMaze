package Fallin.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The TopScoresManager class manages adding, saving, and loading high scores from a file.
 */
public class TopScoresManager {
    private static String scoresFile = "top_scores.txt";
    private static List<Score> topScores = new ArrayList<>();

    // Load scores from file
    static {
        loadScoresFromFile();
    }

    /**
     * Adds a new score to the top scores list if it qualifies and saves to file
     *
     * @param score - the new score to add.
     */
    public static void addScore(Score score) {
        if (isQualifiedForTopScores(score)) {
            topScores.add(score);
            topScores.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
            if (topScores.size() > 5) {
                topScores = topScores.subList(0, 5);
            }
            saveScoresToFile();
        }
    }

    /**
     * Checks if a score qualifies to be in the top scores list.
     *
     * @param score - the score to check.
     * @return true if the score qualifies, else false.
     */
    public static boolean isQualifiedForTopScores(Score score) {
        return topScores.size() < 5 || score.getPoints() > topScores.get(topScores.size() - 1).getPoints();
    }

    /**
     * Gets the current top scores list.
     *
     * @return a list of top scores.
     */
    public static List<Score> getTopScores() {
        return new ArrayList<>(topScores);
    }

    /**
     * Saves the top scores list to a file.
     */
    public static void saveScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoresFile))) {
            for (Score score : topScores) {
                writer.write(score.getPoints() + "," + score.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the top scores list from a file.
     */
    public static void loadScoresFromFile() {
        topScores.clear();
        File file = new File(scoresFile);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(scoresFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int points = Integer.parseInt(parts[0]);
                String date = parts[1];
                topScores.add(new Score(points, date));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the file name for saving and loading scores.
     *
     * @param fileName - the new file name.
     */
    public static void setScoresFile(String fileName) {
        scoresFile = fileName;
        loadScoresFromFile();
    }

    /**
     * Clears the current top scores list.
     */
    public static void clearScores() {
        topScores.clear();
    }
}