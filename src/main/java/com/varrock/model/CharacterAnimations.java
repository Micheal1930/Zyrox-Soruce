package com.varrock.model;


public class CharacterAnimations {

    /**
     * The standing animation for this player.
     */
    private int standingAnimation = -1;

    /**
     * The walking animation for this player.
     */
    private int walkingAnimation = -1;

    /**
     * The running animation for this player.
     */
    private int runningAnimation = -1;

    /**
     * The running animation for this player.
     */
    private int standTurnAnimation = 0x337;

    /**
     * The running animation for this player.
     */
    private int turn180Animation = 0x334;

    /**
     * The running animation for this player.
     */
    private int turn90CWAnimation = 0x335;

    /**
     * The running animation for this player.
     */
    private int turn90CCWAnimation = 0x336;

    /**
     * Create a new {@link CharacterAnimations}.
     *
     * @param standingAnimation the standing animation for this player.
     * @param walkingAnimation  the walking animation for this player.
     * @param runningAnimation  the running animation for this player.
     */
    public CharacterAnimations(int standingAnimation, int walkingAnimation,
                               int runningAnimation) {
        this.standingAnimation = standingAnimation;
        this.walkingAnimation = walkingAnimation;
        this.runningAnimation = runningAnimation;
    }

    /**
     * Constructor for full character animations.
     *
     * @param standingAnimation
     * @param walkingAnimation
     * @param runningAnimation
     * @param standTurnAnimation
     * @param turn180Animation
     * @param turn90CWAnimation
     * @param turn90CCWAnimation
     */
    public CharacterAnimations(int standingAnimation, int walkingAnimation, int runningAnimation, int standTurnAnimation, int turn180Animation, int turn90CWAnimation, int turn90CCWAnimation) {
        this.standingAnimation = standingAnimation;
        this.walkingAnimation = walkingAnimation;
        this.runningAnimation = runningAnimation;
        this.standTurnAnimation = standTurnAnimation;
        this.turn180Animation = turn180Animation;
        this.turn90CWAnimation = turn90CWAnimation;
        this.turn90CCWAnimation = turn90CCWAnimation;
    }

    /**
     * Create a new {@link CharacterAnimations}.
     */
    public CharacterAnimations() {

    }

    @Override
    public CharacterAnimations clone() {
        CharacterAnimations ca = new CharacterAnimations();
        ca.standingAnimation = standingAnimation;
        ca.walkingAnimation = walkingAnimation;
        ca.runningAnimation = runningAnimation;
        return ca;
    }

    @Override
    public String toString() {
        return "CHARACTER ANIMATIONS[standing= " + standingAnimation + ", walking= " + walkingAnimation + ", running= " + runningAnimation + "]";
    }

    /**
     * Resets the animation indexes.
     */
    public void reset() {
        standingAnimation = -1;
        walkingAnimation = -1;
        runningAnimation = -1;
    }

    /**
     * Gets the standing animation for this player.
     *
     * @return the standing animation.
     */
    public int getStandingAnimation() {
        return standingAnimation;
    }

    /**
     * Gets the walking animation for this player.
     *
     * @return the walking animation.
     */
    public int getWalkingAnimation() {
        return walkingAnimation;
    }

    /**
     * Gets the running animation for this player.
     *
     * @return the running animation.
     */
    public int getRunningAnimation() {
        return runningAnimation;
    }

    public int getStandTurnAnimation() {
        return standTurnAnimation;
    }

    public int getTurn90CCWAnimation() {
        return turn90CCWAnimation;
    }

    public int getTurn90CWAnimation() {
        return turn90CWAnimation;
    }

    public int getTurn180Animation() {
        return turn180Animation;
    }

    /**
     * Sets the standing animation for this player.
     *
     * @param standingAnimation the new standing animation to set.
     */
    public void setStandingAnimation(int standingAnimation) {
        this.standingAnimation = standingAnimation;
    }

    /**
     * Sets the walking animation for this player.
     *
     * @param walkingAnimation the new walking animation to set.
     */
    public void setWalkingAnimation(int walkingAnimation) {
        this.walkingAnimation = walkingAnimation;
    }

    /**
     * Sets the running animation for this player.
     *
     * @param runningAnimation the new running animation to set.
     */
    public void setRunningAnimation(int runningAnimation) {
        this.runningAnimation = runningAnimation;
    }
}
