package Fallin.engine;

/**
 * The Score class represents a score upon game completion.
 */
public class Score {
    private int points;
    private String date;

    /**
     * Constructor for Score.
     *
     * @param points - the number of points for the score.
     * @param date - the date the score was achieved.
     */
    public Score(int points, String date) {
        this.points = points;
        this.date = date;
    }

    /**
     * Gets the points of the score.
     *
     * @return the current points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points of the score.
     *
     * @param points - the new points value.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the date of the score.
     *
     * @return the current date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the score.
     *
     * @param date- the new date value.
     */
    public void setDate(String date) {
        this.date = date;
    }
}
