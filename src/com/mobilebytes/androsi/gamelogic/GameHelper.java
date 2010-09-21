package com.mobilebytes.androsi.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class GameHelper {

	public static int counter = 0;

    /**
     * Gets the list of movements for the given player
     *
     * @param player
     * @param board
     * @return
     */
    public static List<Movement> getAllowedMovementsForPlayer(int player,
            Board board) {

        MatrixChecker matrixChecker = new MatrixChecker(board);
        List<Movement> list = new ArrayList<Movement>();

        // scanning the grid
        for (int col = 0; col < GameLogic.COLS; col++) {
            for (int row = 0; row < GameLogic.ROWS; row++) {
                // if player can set
                if (matrixChecker.canSet(player, col, row)) {
                    list.add(new Movement(col, row, player));
                }
            }
        }
        return list;
    }

    /**
     * Given a player, returns its opponent
     *
     * @param player
     * @return
     */
    public static int opponent(int player) {
        if (player == GameLogic.PLAYER_ONE) {
            return GameLogic.PLAYER_TWO;
        } else if (player == GameLogic.PLAYER_TWO) {
            return GameLogic.PLAYER_ONE;
        } else {
            return 0;
        }
    }

    /**
     * Calculates the power of a number
     *
     * @param base
     * @param power
     * @return
     */
    public static int pow(int base, int power) {
        if (power >= 1) {
            return base * (pow(base, power - 1));
        } else {
            return 1;
        }
    }
}