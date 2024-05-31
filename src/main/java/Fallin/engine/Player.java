package Fallin.engine;

import java.io.Serializable;

/**
 * The Player class represents the player character in the game.
 * It implements Serializable to allow the player's state to be saved and restored.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int gold;
    private int steps;
    private int stepCounter;
    private int[] position;

    /**
     * Default constructor that initializes the player's stats.
     */
    public Player() {
        this.health = 10;
        this.gold = 0;
        this.steps = 100;
        this.stepCounter = 0;
        this.position = new int[2];
    }

    /**
     * Gets the player's health.
     *
     * @return the current health of the player.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the player's health.
     *
     * @param health - the new health value for the player.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the player's gold.
     *
     * @return the current gold amount of the player.
     */
    public int getGold() {
        return gold;
    }

    /**
     * Sets the player's gold.
     *
     * @param gold - the new gold amount for the player.
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Gets the player's steps.
     *
     * @return the current steps remaining for the player.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets the player's steps.
     *
     * @param steps - the new steps value for the player.
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * Gets the player's step counter.
     *
     * @return the current step counter value.
     */
    public int getStepCounter() {
        return stepCounter;
    }

    /**
     * Sets the player's step counter.
     *
     * @param stepCounter - the new step counter value.
     */
    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    /**
     * Gets the player's position.
     *
     * @return the current position of the player as an array.
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * Sets the player's position.
     *
     * @param position - the new position for the player as an array.
     */
    public void setPosition(int[] position) {
        this.position = position;
    }
}