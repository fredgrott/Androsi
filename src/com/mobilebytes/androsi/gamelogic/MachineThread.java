package com.mobilebytes.androsi.gamelogic;

public class MachineThread implements Runnable {

    // /////////////////////// PRIVATE FIELDS //////////////////////////

    /**
     * The matrix
     */
    private GameLogic          gameLogic;

    /**
     * This is the object to be notified of the game situation
     */
    private GameEventsListener gameEventsListener;

    // /////////////////////// LIFETIME ////////////////////////////////

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

    // /////////////////////// PRIVATE METHODS //////////////////////////

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

            int p1 = gameLogic.getCounterForPlayer(GameLogic.PLAYER_ONE);
            int p2 = gameLogic.getCounterForPlayer(GameLogic.PLAYER_TWO);

            gameEventsListener.onScoreChanged(p1, p2);

            if (gameLogic.isFinished()) {
                int winner = GameLogic.EMPTY;
                if (p1 > p2) {
                    winner = GameLogic.PLAYER_ONE;
                } else if (p2 > p1) {
                    winner = GameLogic.PLAYER_TWO;
                }
                gameEventsListener.onGameFinished(winner);
            }
        }
    }

    // /////////////////////// PUBLIC METHODS //////////////////////////
    /**
     * The background process
     */
    @Override
    public void run() {

        boolean playerHasChanged;

        do {
            Movement machineMovement = machinePlays();
            if (machineMovement != null) {
                playerHasChanged = doMovement(GameLogic.PLAYER_TWO,
                        machineMovement.getColumn(), machineMovement.getRow());
            } else {
                // if machine movement is null.. machine cannot play
                playerHasChanged = true;
            }
            // it can happen that the human can not play...
        } while (!playerHasChanged);

    }

    /**
     * Sets the game event listener
     *
     * @param gameEventsListener
     */
    public void setGameEventsListener(GameEventsListener gameEventsListener) {
        this.gameEventsListener = gameEventsListener;
    }

    // /////////////////////// ACCESSORS //////////////////////////

    /**
     * Setter for the game logic
     */
    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
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