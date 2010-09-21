package com.mobilebytes.androsi.gamelogic;

public class Board {
	// /////////////////////// CONSTANTS ///////////////////////////////
    /***
     * Board dimensions
     */
    public static int   COLS       = 8;

    public static int   ROWS       = 8;

    // /////////////////////// PRIVATE FIELDS //////////////////////////

    /**
     * The matrix
     */
    private int[][]     gameMatrix = new int[COLS][ROWS];

    /**
     * The situation of all the columns and rows. The integer is an unique value
     * that identifies the column status
     */

    /**
     * the scores for both players
     */
    private final int[] scores     = new int[2];

    // /////////////////////// LIFETIME ///////////////////////////////

    public Board() {
        initializeMatrix();
    }

    // /////////////////////// PUBLIC METHODS //////////////////////////

    /**
     * Returns a new representation that is an exact clone of this board
     */
    @Override
    public Board clone() {
        Board cloned = new Board();

        int[][] clonedMatrix = new int[GameLogic.COLS][GameLogic.ROWS];

        for (int i = 0; i < GameLogic.COLS; i++) {
            for (int j = 0; j < GameLogic.ROWS; j++) {
                clonedMatrix[i][j] = gameMatrix[i][j];
            }
        }

        // cloned.setMatrix(this.gameMatrix.clone());
        cloned.setMatrix(clonedMatrix);
        cloned.setCounterForPlayer(GameLogic.PLAYER_ONE, scores[0]);
        cloned.setCounterForPlayer(GameLogic.PLAYER_TWO, scores[1]);
        // System.out.println(GameHelper.counter++);
        return cloned;
    }

    /**
     * Gets the current score for the given player
     *
     * @param player
     * @return
     */
    public int getCounterForPlayer(int player) {

        return scores[player - 1];
    }

    // /////////////////////// ACCESSORS //////////////////////////
    /**
     * Gets the matrix (read only)
     */
    public int[][] getMatrix() {
        return gameMatrix.clone();
    }

    // /////////////////////// PRIVATE METHODS //////////////////////////
    /**
     * initializes the game matrix
     */
    private void initializeMatrix() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                gameMatrix[col][row] = GameLogic.EMPTY;
            }
        }
        setStone(3, 3, GameLogic.PLAYER_ONE);
        setStone(4, 4, GameLogic.PLAYER_ONE);
        setStone(3, 4, GameLogic.PLAYER_TWO);
        setStone(4, 3, GameLogic.PLAYER_TWO);

    }

    /**
     * Stores the update position in the north west axis (\)
     *
     * @param col
     * @param row
     * @param player
     */

    /**
     * Stores the update position in the north east axis (/)
     *
     * @param col
     * @param row
     * @param player
     */

    /**
     * Sets the given score for the given player
     *
     * @param player
     * @param score
     */
    public void setCounterForPlayer(int player, int score) {
        scores[player - 1] = score;
    }

    /**
     * Sets the given matrix
     *
     * @param matrix
     */
    public void setMatrix(int[][] matrix) {
        gameMatrix = matrix;
    }

    /**
     * Sets the given stone in the given coordinate and calculates mobility and
     * location values
     */
    public void setStone(int col, int row, int player) {

        // if the position was occupied by other player,
        // reducing the score
        if (gameMatrix[col][row] != GameLogic.EMPTY) {
            scores[gameMatrix[col][row] - 1]--;
        }
        gameMatrix[col][row] = player;
        if (player != GameLogic.EMPTY) {
            scores[player - 1]++;
        }
    }

    /**
     * Returns a string representation of the matrix
     */
    @Override
    public String toString() {

        StringBuffer str = new StringBuffer();
        str.append("\n");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                str.append(String.format("|%d|", gameMatrix[col][row]));
            }
            str.append("\n");
        }

        return str.toString();
    }
}