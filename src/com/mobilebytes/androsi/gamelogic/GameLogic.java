package com.mobilebytes.androsi.gamelogic;

public interface GameLogic {
	/***
     * Board dimensions
     */
    public static int COLS       = 8;

    public static int ROWS       = 8;

    /**
     * players
     */
    public static int PLAYER_ONE = 1;

    public static int PLAYER_TWO = 2;

    public static int EMPTY      = 0;

    /**
     * Informs if a given player can set a chip in a given cell
     *
     * @param player
     * @param col
     * @param row
     * @return
     */
    boolean canSet(int player, int col, int row);

    /**
     * Starting on the given column and row, conquers all possible positions (in
     * all directions), which means, changing all opponent chips to the player
     * color
     *
     * @param player
     * @param column
     * @param row
     */
    void conquerPosition(int player, int column, int row);

    /**
     * Gets an array of the allowed positions for the given player
     *
     * @param player
     * @return
     */
    int[][] getAllowedPositionsForPlayer(int player);

    /**
     * Gets the board of this game
     *
     * @return
     */
    Board getBoard();

    /**
     * Gets the counter for the given player
     *
     * @param player
     */
    int getCounterForPlayer(int player);

    /**
     * Gets the player that has to play
     *
     * @return
     */
    int getCurrentPlayer();

    /**
     * Gets the current status
     *
     * @return
     */
    int[][] getGameMatrix();

    /**
     * Gets the number of movements for the given player
     */
    int getMovilityForPlayer(int player);

    /**
     * Starts a new game
     */
    void initialize();

    /**
     * Informs if the player is blocked (no moves are allowed for him)
     *
     * @param player
     * @return
     */
    boolean isBlockedPlayer(int player);

    /**
     * Inform if the game is finished
     *
     * @return
     */
    boolean isFinished();

    /**
     * Sets the current player
     *
     * @param player
     */
    void setCurrentPlayer(int player);

    /**
     * Sets the given chip in the given cell
     *
     * @param player
     * @param col
     * @param row
     */
    void setStone(int player, int col, int row);
}