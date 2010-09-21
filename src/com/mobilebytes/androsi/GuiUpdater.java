package com.mobilebytes.androsi;

import android.app.Activity;
import android.widget.TextView;

public class GuiUpdater implements Runnable {

    private final int      score1;
    private final int      score2;
    private final Activity view;

    /**
     * Constructs the updater
     *
     * @param score1
     * @param score2
     * @param view
     */
    public GuiUpdater(int score1, int score2, Activity view) {

        this.score1 = score1;
        this.score2 = score2;
        this.view = view;
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        setPlayersCounters(score1, score2);

        GameBoard board = (GameBoard) view.findViewById(R.id.gameBoard);
        board.drawPositions();
        board.invalidate();
    }

    /**
     * Draws the scores
     */
    private void setPlayersCounters(int p1Score, int p2Score) {

        TextView txtP1 = (TextView) view.findViewById(R.id.txtPlayer1Counter);
        txtP1.setText(String.format(" %d", p1Score));
        TextView txtP2 = (TextView) view.findViewById(R.id.txtPlayer2Counter);
        txtP2.setText(String.format(" %d", p2Score));

    }
}