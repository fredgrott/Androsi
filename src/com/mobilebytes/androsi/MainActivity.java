package com.mobilebytes.androsi;

import com.mobilebytes.androsi.gamelogic.Board;
import com.mobilebytes.androsi.gamelogic.GameEventsListener;
import com.mobilebytes.androsi.gamelogic.GameFacade;
import com.mobilebytes.androsi.gamelogic.GameFacadeImpl;
import com.mobilebytes.androsi.gamelogic.GameLogic;
import com.mobilebytes.androsi.gamelogic.GameLogicImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity  implements GameEventsListener,
        android.content.DialogInterface.OnClickListener {

    /**
     * The game board.
     */
    private GameFacade    gameFacade = null;

    /**
     * Used to invoke the GUI operations (UI thread).
     */
    private final Handler handler;

    // ///////////////////////// LIFETIME /////////////////////////////////

    /**
     * Constructor.
     */
    public MainActivity() {
        handler = new Handler();
    }

    /**
     * Occurs when the user closes a dialog.
     *
     * @param dialog the dialog
     * @param which the which
     */
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            restart();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main);

        // retrieving the old facade if any
        // trying to recover the last version
        gameFacade = (GameFacade) getLastNonConfigurationInstance();
        // if is the first time...
        if (gameFacade == null) {
            gameFacade = new GameFacadeImpl();
            gameFacade.setMachineOpponent(Settings
                    .getIsDroidOpponent(getBaseContext()));
            gameFacade.setGameLogic(new GameLogicImpl(new Board()));
        } else {
            refreshCounters();
        }
        // caution. "this" has been re-constructed after an orientation
        // change... so need to
        // subscribe as listener again
        gameFacade.setGameEventsListener(this);

        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
        gameBoard.setGameFacade(gameFacade);







    }

    /**
     * Occurs when the user presses the menu key.
     *
     * @param menu the menu
     * @return true, if successful
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Occurs when the game has finished.
     *
     * @param winner the winner
     */
    public void onGameFinished(int winner) {

        String playerName;
        if (winner == GameLogic.PLAYER_ONE) {
            playerName = getResources().getString(R.string.p1);
        } else {
            playerName = getResources().getString(R.string.p2);
        }

        handler.post(new MessageBoxShower(String.format(super.getResources()
                .getString(R.string.game_finished, playerName)), this, this));
    }

    /**
     * Occurs when the user clicks in a menu option.
     *
     * @param featureId the feature id
     * @param item the item
     * @return true, if successful
     */
    @Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                super.startActivity(new Intent(this, Settings.class));
                break;
            case R.id.restart:
                showNewGameConfirmation(super.getResources().getString(
                        R.string.new_game_msg));
            default:
                return false;
        }
        return true;
    }

    /**
     * Provides the data to remember (if the screen changes orientation, the
     * full class is rebuilt).
     *
     * @return the object
     */
    @Override
    public Object onRetainNonConfigurationInstance() {

        return gameFacade;
    }

    /**
     * Occurs when the score has changed... so refreshing counters.
     *
     * @param p1Score the p1 score
     * @param p2Score the p2 score
     */
    public void onScoreChanged(int p1Score, int p2Score) {

        GuiUpdater updater = new GuiUpdater(p1Score, p2Score, this);
        handler.post(updater);
    }

    // ///////////////////////// PRIVATE METHODS ///////////////////////////////

    /**
     * Refreshes the counters after an orientation changing.
     */
    private void refreshCounters() {

        int p1 = gameFacade.getScoreForPlayer(GameFacade.PLAYER_ONE);
        int p2 = gameFacade.getScoreForPlayer(GameFacade.PLAYER_TWO);
        setPlayersCounters(p1, p2);
    }

    /**
     * Restarts the facade and the graphics.
     */
    private void restart() {
        gameFacade.restart();
        gameFacade.setMachineOpponent(Settings
                .getIsDroidOpponent(getBaseContext()));
        refreshCounters();
        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
        gameBoard.invalidate();
    }

    /**
     * Draws the scores.
     *
     * @param p1Score the p1 score
     * @param p2Score the p2 score
     */
    private void setPlayersCounters(int p1Score, int p2Score) {

        TextView txtP1 = (TextView) findViewById(R.id.txtPlayer1Counter);
        txtP1.setText(String.format(" %d", p1Score));
        TextView txtP2 = (TextView) findViewById(R.id.txtPlayer2Counter);
        txtP2.setText(String.format(" %d", p2Score));

    }

    /**
     * shows the dialog for confirmation of the restart.
     *
     * @param message the message
     */
    private void showNewGameConfirmation(String message) {

        ConfirmationDialog cd = new ConfirmationDialog(this);
        cd.showConfirmation(this, message);
    }
}
