package com.mobilebytes.androsi.gamelogic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameFacadeImpl implements GameFacade {

    // /////////////////////// PRIVATE FIELDS //////////////////////////

    /**
     * access to the game operations
     */
    private GameLogic          gameLogic;

    /**
     * This is the object to be notified of the game situation
     */
    private GameEventsListener gameEventsListener;

    /**
     * Indicates if we are in 1 player mode or 2 player mode
     */
    private boolean            isMachineOpponent = true;

    // /////////////////////// PUBLIC METHODS //////////////////////////

    /**
     * makes a movement, conquer positions, update possible positions, toggles
     * player
     *
     * @param player
     * @param col
     * @param row
     * @return if the turn has to change
     */
    private boolean doMovement(int player, int col, int row) {

        boolean changePlayer = false;
        if (gameLogic.canSet(player, col, row)) {
            gameLogic.setStone(player, col, row);
            gameLogic.conquerPosition(player, col, row);
            changePlayer = togglePlayer();
        }
        notifyChanges();
        return changePlayer;
    }

    /**
     * Gets the current allowed positions for current Player
     */
    @Override
    public int[][] getAllowedPositionsForPlayer() {
        return gameLogic.getAllowedPositionsForPlayer(getCurrentPlayer());
    }

    /**
     * Gets the player that has to play
     */
    @Override
    public int getCurrentPlayer() {
        return gameLogic.getCurrentPlayer();
    }

    /**
     * @return the gameLogic
     */
    public GameLogic getGameLogic() {
        return gameLogic;
    }

    /**
     * Gets the current matrix of the game
     */
    @Override
    public int[][] getGameMatrix() {
        return gameLogic.getGameMatrix();
    }

    // /////////////////////// PRIVATE METHODS ///////////////////////

    /**
     * gets if the opponent is droid
     */
    @Override
    public boolean getMachineOpponent() {
        return isMachineOpponent;
    }

    /**
     * Gets the score for the given player
     */
    @Override
    public int getScoreForPlayer(int player) {
        return gameLogic.getCounterForPlayer(player);
    }

    /**
     * Calculates the machine movement
     *
     * @return
     */
    private Movement machinePlays() {

        AI ai = new AI(gameLogic.getBoard());
        Movement best = ai.getBestMove(GameLogic.PLAYER_TWO);
        return best;
    }

    /**
     * Notifies the listener for the changes occured in the game
     */
    private void notifyChanges() {
        if (gameEventsListener != null) {

            int p1 = gameLogic.getCounterForPlayer(PLAYER_ONE);
            int p2 = gameLogic.getCounterForPlayer(PLAYER_TWO);

            gameEventsListener.onScoreChanged(p1, p2);

            if (gameLogic.isFinished()) {
                int winner = NONE;
                if (p1 > p2) {
                    winner = GameLogic.PLAYER_ONE;
                } else if (p2 > p1) {
                    winner = GameLogic.PLAYER_TWO;
                }

                gameEventsListener.onGameFinished(winner);
            }

        }
    }

    // /////////////////////// ACCESSORS //////////////////////////

    /**
     * Starts a new game
     */
    @Override
    public void restart() {

        gameLogic.initialize();
    }

    /**
     * Sets the chip for the given player in a given position
     */
    @Override
    public void set(int player, int col, int row) {

        boolean playerHasChanged;

        playerHasChanged = doMovement(player, col, row);
        // if is the machine moment...
        if (isMachineOpponent && playerHasChanged
                && (gameLogic.getCurrentPlayer() == GameLogic.PLAYER_TWO)) {

            // TODO launch second thread

            MachineThread secondThread = new MachineThread();
            secondThread.setGameEventsListener(gameEventsListener);
            secondThread.setGameLogic(gameLogic);

            ExecutorService threadExecutor = Executors
                    .newSingleThreadExecutor();
            threadExecutor.execute(secondThread);



        }

    }

    /**
     * Sets the event listener (is going to be notified when the game situation
     * changes)
     */
    @Override
    public void setGameEventsListener(GameEventsListener listener) {
        gameEventsListener = listener;
    }

    /**
     * @param gameLogic
     *            the gameLogic to set
     */
    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    /**
     * If true is playing against droid
     */
    @Override
    public void setMachineOpponent(boolean machineOpponent) {
        isMachineOpponent = machineOpponent;
    }

    /**
     * Changes the player
     *
     * @return if the player has been toggled (if the opponent player can play)
     */
    private boolean togglePlayer() {

        int current = gameLogic.getCurrentPlayer();
        boolean toggled;
        // if the next player can play (has at least one place to put the
        // chip)
        if (!gameLogic.isBlockedPlayer(GameHelper.opponent(current))) {
            // just toggles
            gameLogic.setCurrentPlayer(GameHelper.opponent(current));
            toggled = true;
        } else {
            System.out.println(String.format(
                    "player %d cannot play!!!!!!!!!!!!!!!!!!!", GameHelper
                            .opponent(current)));
            toggled = false;
        }
        return toggled;
    }

}