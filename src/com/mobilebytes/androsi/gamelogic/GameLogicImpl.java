package com.mobilebytes.androsi.gamelogic;

public class GameLogicImpl implements GameLogic {

    // /////////////////////// PRIVATE FIELDS //////////////////////////

    /**
     * The matrix
     */
    private Board         board;

    /**
     * Just a helper.
     */
    private MatrixChecker matrixChecker;

    /**
     * The player that has to play
     */
    private int           currentPlayer = GameLogic.PLAYER_ONE;

    // /////////////////////// LIFETIME ////////////////////////////////
    /**
     * initializes the matrix
     */
    public GameLogicImpl(Board board) {
        this.board = board;
        matrixChecker = new MatrixChecker(board);

    }

    // /////////////////////// PUBLIC METHODS //////////////////////////

    @Override
    public boolean canSet(int player, int col, int row) {
        return matrixChecker.canSet(player, col, row);
    }

    /**
     * Changes all the opponent chips to the player color in the given direction
     *
     * @param player
     *            the current player
     * @param column
     *            the column where the player sets the chip (starting point of
     *            the conquer)
     * @param row
     *            the row where the player sets the chip
     * @param xDirection
     *            the direction in the x axis (left or right or none )
     * @param yDirection
     *            the direction in the y axis (up or down or none)
     */
    private void conquer(int player, int column, int row, int xDirection,
            int yDirection) {

        int x = column;
        int y = row;
        boolean ownChip = false;

        int[][] gameMatrix = board.getMatrix();

        // conquer until an own chip is found
        while (!ownChip) {
            // advancing in the given direction
            x += xDirection;
            y += yDirection;

            // if is not an own chip
            if (gameMatrix[x][y] != player) {
                setStone(player, x, y);
            } else {
                ownChip = true;
            }
        }
    }

    /**
     * Conquers the positions in all directions (if the player is enclosing
     * opponent pieces, just converting them to player color)
     */
    @Override
    public void conquerPosition(int player, int column, int row) {

        // in each direction, if we are enclosing opponent chips, conquering...
        if (matrixChecker.isEnclosingUpwards(player, column, row)) {
            conquer(player, column, row, GameDirection.X.NONE, GameDirection.Y.UP);
        }
        if (matrixChecker.isEnclosingDownwards(player, column, row)) {
            conquer(player, column, row, GameDirection.X.NONE, GameDirection.Y.DOWN);
        }
        if (matrixChecker.isEnclosingRight(player, column, row)) {
            conquer(player, column, row, GameDirection.X.RIGHT, GameDirection.Y.NONE);
        }
        if (matrixChecker.isEnclosingLeft(player, column, row)) {
            conquer(player, column, row, GameDirection.X.LEFT, GameDirection.Y.NONE);
        }
        if (matrixChecker.isEnclosingLeftAndDown(player, column, row)) {
            conquer(player, column, row, GameDirection.X.LEFT, GameDirection.Y.DOWN);
        }
        if (matrixChecker.isEnclosingLeftAndUp(player, column, row)) {
            conquer(player, column, row, GameDirection.X.LEFT, GameDirection.Y.UP);
        }
        if (matrixChecker.isEnclosingRightAndDown(player, column, row)) {
            conquer(player, column, row, GameDirection.X.RIGHT, GameDirection.Y.DOWN);
        }
        if (matrixChecker.isEnclosingRightAndUp(player, column, row)) {
            conquer(player, column, row, GameDirection.X.RIGHT, GameDirection.Y.UP);
        }

    }

    /**
     * Returns all the allowed positions for a player
     */
    @Override
    public int[][] getAllowedPositionsForPlayer(int player) {

        int[][] allowedPositions = new int[COLS][ROWS];

        // scanning the grid
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                // if player can set
                if (matrixChecker.canSet(player, col, row)) {
                    allowedPositions[col][row] = player;
                }
            }
        }
        return allowedPositions;
    }

    /**
     * Returns the board
     *
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the number of stones for a given player
     *
     * @return
     */
    @Override
    public int getCounterForPlayer(int player) {

        return board.getCounterForPlayer(player);

    }

    /**
     * Gets the current player
     *
     * @return the currentPlayer
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the game status
     */
    @Override
    public int[][] getGameMatrix() {

        return board.getMatrix();
    }

    /**
     * Gets the current movility for the given player
     */
    @Override
    public int getMovilityForPlayer(int player) {

        int movility = 0;

        // scanning the grid
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                // if player can set
                if (matrixChecker.canSet(player, col, row)) {
                    movility++;
                }
            }
        }
        return movility;
    }

    @Override
    public void initialize() {
        currentPlayer = PLAYER_ONE;
        board = new Board();
        matrixChecker = new MatrixChecker(board);
    }

    // /////////////////////// PRIVATE METHODS //////////////////////////

    /**
     * Calculates if the given player is blocked
     *
     * @param player
     * @return
     */
    @Override
    public boolean isBlockedPlayer(int player) {

        int allowed[][] = getAllowedPositionsForPlayer(player);
        int allowCount = 0;
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (allowed[col][row] == player) {
                    allowCount++;
                }
            }
        }
        return allowCount == 0;
    }

    // /////////////////////// ACCESSORS ////////////////////////////

    /**
     * Informs if the game is finished
     *
     * @return
     */
    public boolean isFinished() {

        return (isBlockedPlayer(PLAYER_ONE) && isBlockedPlayer(PLAYER_TWO));
    }

    /**
     * Sets the current player
     */
    @Override
    public void setCurrentPlayer(int player) {
        currentPlayer = player;
    }

    /**
     * Sets a chip in a given place
     */
    @Override
    public void setStone(int player, int col, int row) {

        if ((col < COLS) && (row < ROWS)) {
            board.setStone(col, row, player);
        }
    }

}