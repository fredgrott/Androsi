package com.mobilebytes.androsi.gamelogic;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class AI.
 */
public class AI {
// ///////////////////// CONSTANTS ///////////////////////////////////

    /** The importance of the movility in the analysis of a position. */
    private static final int MOVILITY_COEFF  = 10;

    /** The importance of the positions values in the analysis of the global
     * position.
     */
    private static final int POSITIONS_COEFF = 6;

    // ///////////////////// PRIVATE FIELDS //////////////////////////////

    /** The values of the initial board. */
    private final int[][]    values          = new int[][] {
            { 50, -1, 5, 2, 2, 5, -1, 50 }, { -1, -10, 1, 1, 1, 1, -10, -1 },
            { 5, 1, 1, 1, 1, 1, 1, 5 }, { 2, 1, 1, 0, 0, 1, 1, 2 },
            { 2, 1, 1, 0, 0, 1, 1, 2 }, { 5, 1, 1, 1, 1, 1, 1, 5 },
            { -1, -10, 1, 1, 1, 1, -10, -1 }, { 50, -1, 5, 2, 2, 5, -1, 50 } };

    /** The number of movements the machine will go further. */
    private final int        depth           = 2;

    /** The best move. */
    private Movement         bestMove        = null;

    /** The board (initially a clone of the real one). */
    private final Board      board;

    /** The sign. */
    private final int[]      sign            = new int[] { 1, -1 };

    /**
     * Instantiates a new aI.
     *
     * @param board the board
     */
    public AI(Board board) {
        this.board = board;
        System.out.println("***************************** RANDOM "
                + POSITIONS_COEFF);
    }

    // /////////////////////// PUBLIC METHODS //////////////////////////

    /**
     * Analyze the situation regarding mobility.
     *
     * @param board the board
     * @param color the color
     * @return the int
     */
    private int analysis(Board board, int color) {

        int player = color + 1;
        int points;

        GameLogic logic = new GameLogicImpl(board);
        int movility = logic.getMovilityForPlayer(player);
        int positions = evaluateStrategicPosition(board, player);

        points = movility * MOVILITY_COEFF + positions * POSITIONS_COEFF;

        return points;

    }

    /**
     * Returns the addition of the values of each position from the perspective
     * of the player.
     *
     * @param board the board
     * @param player the player
     * @return the int
     */
    private int evaluateStrategicPosition(Board board, int player) {

        int total = 0;
        for (int i = 0; i < GameLogic.COLS; i++) {
            for (int j = 0; j < GameLogic.ROWS; j++) {
                if (board.getMatrix()[i][j] == player) {
                    total += values[i][j];
                }
            }
        }

        return total;
    }

    /**
     * Return the best movement for the given player.
     *
     * @param player the player
     * @return the best move
     */
    public Movement getBestMove(int player) {

        int color = player - 1;
        int currDepth = 0;
        negaMax(board, currDepth, color);
        return bestMove;

    }

    /**
     * Negamax algorithm to look forward.
     *
     * @param board the board
     * @param currentDepth the current depth
     * @param color the color
     * @return the int
     */
    private int negaMax(Board board, int currentDepth, int color) {

        int player = color + 1;
        boolean isFinished = false;

        if (isFinished || (currentDepth > depth)) {
            return sign[color] * analysis(board, color);
        }
        // max play value of all movements
        int max = Integer.MIN_VALUE + 1;

        List<Movement> movements = GameHelper.getAllowedMovementsForPlayer(
                player, board);
        if (movements.size() == 0) {
            isFinished = true;
        }
        for (Movement movement : movements) {

            Board newMovementBoard = board.clone();
            GameLogic gl = new GameLogicImpl(newMovementBoard);

            gl.setStone(movement.getPlayer(), movement.getColumn(), movement
                    .getRow());
            gl.conquerPosition(movement.getPlayer(), movement.getColumn(),
                    movement.getRow());
            int x = -negaMax(newMovementBoard, currentDepth + 1, 1 - color);
            if (x > max) {
                max = x;
                // it only must return "depth 0" movements
                if (currentDepth == 0) {
                    bestMove = movement;
                }
            }
        }
        return max;
    }

}