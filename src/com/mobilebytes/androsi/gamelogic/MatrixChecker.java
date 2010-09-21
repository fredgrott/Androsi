package com.mobilebytes.androsi.gamelogic;

public class MatrixChecker {
// /////////////////////// PRIVATE FIELDS //////////////////////////

    /**
     * The game matrix
     */
    private final int[][] gameMatrix;

    // /////////////////////// PUBLIC METHODS //////////////////////////

    /**
     * Constructs the checker
     */
    public MatrixChecker(Board board) {

        gameMatrix = board.getMatrix();
    }

    /**
     * Verifies if a user can set in a given cell
     *
     * @param player
     * @param col
     * @param row
     */
    public boolean canSet(int player, int col, int row) {
        boolean canPut;

        canPut = isEmpty(col, row)
                && (isEnclosingUpwards(player, col, row)
                        || isEnclosingDownwards(player, col, row)
                        || isEnclosingRight(player, col, row)
                        || isEnclosingLeft(player, col, row)
                        || isEnclosingLeftAndUp(player, col, row)
                        || isEnclosingLeftAndDown(player, col, row)
                        || isEnclosingRightAndDown(player, col, row) || isEnclosingRightAndUp(
                        player, col, row));

        return canPut;

    }

    /**
     * Returns the value of first different position in the given direction
     * starting in the given initial coordinate. If no different position is
     * found, returns the player value. If the different position is empty, then
     * returns EMPTY If is an opponent position returns its value.
     *
     * @param player
     * @param initialColumn
     * @param initialRow
     * @param directionX
     * @param directionY
     * @return
     */
    private int getFirstPositionDifferentThan(int player, int initialColumn,
            int initialRow, int directionX, int directionY) {

        // stores the value of the first position with a value different than
        // the player value
        int differentPosition = player;

        boolean resolved = false;

        int x = initialColumn;
        int y = initialRow;

        while (!resolved) {

            x += directionX;
            y += directionY;
            // if we are inside the bounds of the board game
            if ((x >= 0) && (x < GameLogic.COLS) && (y >= 0)
                    && (y < GameLogic.COLS)) {

                if (gameMatrix[x][y] != player) {
                    resolved = true;
                    differentPosition = gameMatrix[x][y];
                }
            } else {
                // out of bounds means resolved with no different position
                resolved = true;
            }
        }

        return differentPosition;

    }

    /**
     * Checks if a given position is empty
     */
    private boolean isEmpty(int col, int row) {

        return gameMatrix[col][row] == 0;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingDownwards(int player, int col, int row) {

        boolean enclosing = false;

        // impossible enclose downwards if row is more or equals Max rows - 1
        if (row < GameLogicImpl.ROWS - 1) {

            // impossible enclose upwards if adjacent chip is not
            // opponent
            if (gameMatrix[col][row + 1] == opponent(player)) {

                int value = getFirstPositionDifferentThan(opponent(player),
                        col, row + 1, GameDirection.X.NONE, GameDirection.Y.DOWN);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingLeft(int player, int col, int row) {

        boolean enclosing = false;

        // impossible enclose left if col is 1 or less
        if (col > 1) {

            // impossible enclose if adjacent chip is not
            // opponent
            if (gameMatrix[col - 1][row] == opponent(player)) {

                int value = getFirstPositionDifferentThan(opponent(player),
                        col - 1, row, GameDirection.X.LEFT, GameDirection.Y.NONE);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingLeftAndDown(int player, int col, int row) {

        boolean enclosing = false;

        // checking if is possible to enclose left & down
        if ((col > 1) && (row < GameLogic.ROWS - 1)) {

            // impossible enclose if adjacent chip is not
            // opponent
            if (gameMatrix[col - 1][row + 1] == opponent(player)) {

                // searching left and up for an own chip
                int value = getFirstPositionDifferentThan(opponent(player),
                        col - 1, row + 1, GameDirection.X.LEFT, GameDirection.Y.DOWN);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingLeftAndUp(int player, int col, int row) {

        boolean enclosing = false;

        // checking if is possible to enclose left & up
        if ((col > 1) && (row > 1)) {

            // impossible enclose if adjacent chip is not
            // opponent
            if (gameMatrix[col - 1][row - 1] == opponent(player)) {

                // searching left and up for an own chip
                int value = getFirstPositionDifferentThan(opponent(player),
                        col - 1, row - 1, GameDirection.X.LEFT, GameDirection.Y.UP);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingRight(int player, int col, int row) {

        boolean enclosing = false;

        // impossible enclose right if col is more or equals Max cols - 1
        if (col < GameLogicImpl.COLS - 1) {

            // impossible enclose right if adjacent chip is not
            // opponent
            if (gameMatrix[col + 1][row] == opponent(player)) {

                int value = getFirstPositionDifferentThan(opponent(player),
                        col + 1, row, GameDirection.X.RIGHT, GameDirection.Y.NONE);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingRightAndDown(int player, int col, int row) {

        boolean enclosing = false;

        // checking if is possible to enclose left & down
        if ((col < GameLogic.COLS - 1) && (row < GameLogic.ROWS - 1)) {

            // impossible enclose if adjacent chip is not
            // opponent
            if (gameMatrix[col + 1][row + 1] == opponent(player)) {

                // searching left and up for an own chip
                int value = getFirstPositionDifferentThan(opponent(player),
                        col + 1, row + 1, GameDirection.X.RIGHT, GameDirection.Y.DOWN);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    // /////////////////////// PRIVATE METHODS ///////////////////////

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingRightAndUp(int player, int col, int row) {

        boolean enclosing = false;

        // checking if is possible to enclose left & down
        if ((col < GameLogic.COLS - 1) && (row > 1)) {

            // impossible enclose if adjacent chip is not
            // opponent
            if (gameMatrix[col + 1][row - 1] == opponent(player)) {

                // searching left and up for an own chip
                int value = getFirstPositionDifferentThan(opponent(player),
                        col + 1, row - 1, GameDirection.X.RIGHT, GameDirection.Y.UP);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Checks if from a given position player are enclosing an opponent chip
     */
    public boolean isEnclosingUpwards(int player, int col, int row) {

        boolean enclosing = false;

        // impossible enclose upwards if row is less than 2
        if (row > 1) {

            // impossible enclose upwards if adjacent upwards chip is not
            // opponent
            if (gameMatrix[col][row - 1] == opponent(player)) {

                int value = getFirstPositionDifferentThan(opponent(player),
                        col, row - 1, GameDirection.X.NONE, GameDirection.Y.UP);
                // only encloses if the first different than opponent is an own
                // chip
                enclosing = value == player;
            }
        }
        return enclosing;
    }

    /**
     * Returns the opponent of the given player or zero if zero is passed
     *
     * @param player
     * @return
     */
    private int opponent(int player) {

        int opp = GameLogicImpl.EMPTY;

        if (player == GameLogic.PLAYER_ONE) {
            opp = GameLogic.PLAYER_TWO;
        } else if (player == GameLogic.PLAYER_TWO) {
            opp = GameLogic.PLAYER_ONE;
        }
        return opp;
    }
}