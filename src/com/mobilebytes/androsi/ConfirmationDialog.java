package com.mobilebytes.androsi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class ConfirmationDialog {
	// ///////////////////////// CONSTANTS /////////////////////////////////
    /**
     * The yes button
     */
    public static int       YES_BUTTON = 0;

    /**
     * The no button
     */
    public static int       NO_BUTTON  = 1;

    // ///////////////////////// FIELDS ///////////////////////////////////

    /**
     * The listener of the response
     */
    private OnClickListener listener   = null;

    // ///////////////////////// LIFETIME ///////////////////////////////////

    /**
     * The constructor
     *
     * @param listener
     */
    public ConfirmationDialog(OnClickListener listener) {
        this.listener = listener;
    }

    // ///////////////////////// PUBLIC METHODS ////////////////////////////

    /**
     * Opens a confirmation message
     *
     * @param context
     * @param msg
     * @return
     */
    public void showConfirmation(Context context, String msg) {

        Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirm_title);
        builder.setPositiveButton(R.string.yes_caption, listener);
        builder.setNegativeButton(R.string.no_caption, listener);

        builder.setMessage(msg);
        builder.show();
    }

}
