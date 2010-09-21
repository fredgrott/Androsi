package com.mobilebytes.androsi.gamelogic;

public interface GameEventsListener {
	 /**
     * Invoked when the game has been finished
     *
     * @param winner
     *            The winner of the game (can be none in case of 'equals')
     */
    void onGameFinished(int winner);

    /**
     * Invoked when the score changes
     *
     * @param p1Score
     * @param p2Score
     */
    void onScoreChanged(int p1Score, int p2Score);
}