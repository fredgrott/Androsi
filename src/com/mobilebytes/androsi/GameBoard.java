package com.mobilebytes.androsi;

import com.mobilebytes.androsi.gamelogic.GameFacade;
import com.mobilebytes.androsi.gamelogic.GameLogic;
import com.mobilebytes.androsi.Settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameBoard extends View {

    // //////////////////// CONSTANTS ///////////////////////////////////

    /**
     * The number of columns of this board
     */
    private static int NUMBER_OF_COLUMNS        = 8;

    /**
     * The number of rows of this board
     */
    private static int NUMBER_OF_ROWS           = 8;

    /**
     * The top margin
     */
    private static int TOP_MARGIN               = 0;

    /**
     * Vertical margin
     */
    private static int LEFT_MARGIN              = 0;

    // //////////////////// FIELDS ///////////////////////////////////

    /**
     * Access to the game facade. Interface
     */
    private GameFacade gameFacade;

    /**
     * access to the graphic system
     */
    private Canvas     canvas;

    /**
     * dimensions
     */
    private int        width;

    private int        height;

    private int        cellWidth;

    private int        cellHeight;

    /**
     * The color used by the player 1
     */
    private final int  playerOneColor           = Color.BLUE;
    private final int  playerOneInsideColor     = Color.rgb(0, 0, 150);

    /**
     * Color used by player 2
     */
    private final int  playerTwoColor           = Color.RED;
    private final int  playerTwoInsideColor     = Color.rgb(150, 0, 0);

    /**
     * The first time parameters are not calculated
     */
    private boolean    isNotCalulatedParameters = true;

    // ///////////////////////////////// LIFETIME //////////////////////////////
    /**
     * Default constructor
     */
    public GameBoard(Context context, AttributeSet attr) {
        super(context, attr);

    }

    // /////////////////////// EVENTS ////////////////////////////////////////

    /**
     * Calculates the graphic parameters such as cell width, cell height, etc
     */
    private void calculateGraphicParameters() {

        View gameBoard = findViewById(R.id.gameBoard);

        width = gameBoard.getWidth();
        height = gameBoard.getHeight();

        // getting the minor (the board is a square)
        if (height > width) {
            height = width;
        } else {
            width = height;
        }

        // converting the dimensions to get them divisible by 8
        while (width % 8 != 0) {
            width--;
            height--;
        }

        cellWidth = (width - LEFT_MARGIN * 2) / NUMBER_OF_COLUMNS;
        cellHeight = (height - TOP_MARGIN * 2) / NUMBER_OF_ROWS;
    }

    /**
     * Draws the initial lines that forms the board
     */
    private void drawBoard() {

        drawGrid();
        drawPositions();
    }

    /**
     * Just draws a circle with the given style
     *
     * @param borderColor
     *            color of the chip
     * @param isSolid
     *            if is solid or just the border (transparent)
     * @param cx
     *            center x
     * @param cy
     *            center y
     * @param radius
     *            size of the chip
     */
    private void drawCircle(int borderColor, int insideColor, boolean isSolid,
            int cx, int cy, int radius) {

        Paint paint = new Paint();

        if (!isSolid) {
            // if the shadow is being painted, changing alpha channel
            borderColor = Color.argb(77, Color.red(borderColor), Color
                    .green(borderColor), Color.blue(borderColor));

            insideColor = Color.argb(77, Color.red(insideColor), Color
                    .green(insideColor), Color.blue(insideColor));
        }
        paint.setColor(borderColor);
        paint.setAntiAlias(true);

        canvas.drawCircle(cx, cy, radius, paint);
        paint.setColor(insideColor);
        canvas.drawCircle(cx, cy, radius - 4, paint);
    };

    // ////////////////////// PUBLIC METHODS /////////////////////////////

    // ////////////////////// PRIVATE METHODS ////////////////////////////

    /**
     * Draws the grid of the board
     *
     * @param paint
     */
    private void drawGrid() {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        for (int col = 0; col <= NUMBER_OF_COLUMNS; col++) {
            // vertical lines
            int x = col * cellWidth + LEFT_MARGIN;
            canvas.drawLine(x, TOP_MARGIN, x, height - TOP_MARGIN * 1, paint);
        }

        for (int row = 0; row < NUMBER_OF_ROWS + 1; row++) {

            int y = row * cellHeight + TOP_MARGIN;
            // horizontal lines
            canvas
                    .drawLine(LEFT_MARGIN, y, width - (LEFT_MARGIN * 1), y,
                            paint);
        }
    }

    /**
     * draws a circle in the column and row given having in mind the player
     * color
     */
    private void drawPosition(int col, int row, int player, boolean fill) {

        // calculating the center of the cell
        int cellMediumX = (col * cellWidth + (col + 1) * cellWidth) / 2;
        int cellMediumY = (row * cellHeight + (row + 1) * cellHeight) / 2;

        // applying the margins
        int cx = cellMediumX + LEFT_MARGIN;
        int cy = cellMediumY + TOP_MARGIN;
        // now the radius
        int radius = (cellWidth - 2) / 2 - 2;

        // just painting a circle of the player's color
        int borderColor = getColorForPlayer(player);
        int insideColor = getInsideColorForPlayer(player);
        drawCircle(borderColor, insideColor, fill, cx, cy, radius);

    }

    /**
     * Draws all the chips
     */
    public void drawPositions() {

        int[][] gameMatrix = gameFacade.getGameMatrix();

        for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
            for (int row = 0; row < NUMBER_OF_ROWS; row++) {
                if (gameMatrix[col][row] != GameLogic.EMPTY) {
                    drawPosition(col, row, gameMatrix[col][row], true);
                }
            }
        }

    }

    /**
     * Gets the color for the given player
     */
    private int getColorForPlayer(int player) {
        if (player == GameLogic.PLAYER_ONE) {
            return playerOneColor;
        } else {
            return playerTwoColor;
        }
    }

    /**
     * @return the gameFacade
     */
    public GameFacade getGameLogic() {
        return gameFacade;
    }

    /**
     * Gets the color for the inside of the circle of the given player
     */
    private int getInsideColorForPlayer(int player) {
        if (player == GameLogic.PLAYER_ONE) {
            return playerOneInsideColor;
        } else {
            return playerTwoInsideColor;
        }
    }

    /**
     * Marks in the screen the allowed positions
     */
    private void markAllowedPositions() {
        if (Settings.getShowAllowedPositions(getContext())) {

            // if we are in 2 player mode or we are in 1 player mode and player
            // is one
            if (!gameFacade.getMachineOpponent()
                    || (gameFacade.getCurrentPlayer() == GameFacade.PLAYER_ONE)) {

                // getting the positions to mark
                int[][] allowedPos = gameFacade.getAllowedPositionsForPlayer();

                for (int i = 0; i < GameLogic.COLS; i++) {
                    for (int j = 0; j < GameLogic.ROWS; j++) {
                        // if there is a position of a player to draw
                        if (allowedPos[i][j] != GameLogic.EMPTY) {
                            // drawing hypothetic positions
                            drawPosition(i, j, allowedPos[i][j], false);
                        }
                    }
                }
            }
        }

    }

    /**
     * Occurs when drawing the board
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {

        this.canvas = canvas;

        if (isNotCalulatedParameters) {
            calculateGraphicParameters();
            isNotCalulatedParameters = false;
        }

        this.setBackgroundColor(Color.DKGRAY);
        // drawing the game board
        drawBoard();

        // setting all possible position
        markAllowedPositions();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting width and height equally
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
        if (h < w) {
            w = h;
        } else {
            h = w;
        }
        // reducing to be 8 multiple + 1 (to have room for the line)
        if (h % 8 != 0) {
            int intPart = (h / 8) * 8;
            h = intPart + 1;
        }
        this.setMeasuredDimension(h, h);
    }

    /**
     * Occurs when user touches the screen
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        // is this a click?
        if (action == MotionEvent.ACTION_DOWN) {

            float x = event.getX();
            float y = event.getY();
            // getting column and row where the click was perfomed
            int col = transformCoordinateXInColumn(x);
            int row = transformCoordinateYInRow(y);

            // if we are playing against human, just play
            if (!gameFacade.getMachineOpponent()) {
                setPosition(col, row, gameFacade.getCurrentPlayer());
            } else {
                // if we are playing against droid, checking if we are player 1
                if (gameFacade.getCurrentPlayer() == GameFacade.PLAYER_ONE) {
                    setPosition(col, row, gameFacade.getCurrentPlayer());
                }
            }

            // this.invalidate();

            return true;
        } else {
            return false;
        }

    }

    /**
     * @param gameFacade
     *            the gameFacade to set
     */
    public void setGameFacade(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }

    /**
     * draws a fill circle in the column and row given having in mind the player
     * color
     */
    private void setPosition(int col, int row, int player) {

        gameFacade.set(player, col, row);
    }

    /**
     * converts a coordinate into a column
     *
     * @param x
     * @return
     */
    private int transformCoordinateXInColumn(float x) {

        int col = (int) ((x - LEFT_MARGIN) / cellWidth);

        // if tapped outside the board
        if ((col < 0) || (col >= GameLogic.COLS)) {
            col = -1;
        }

        return col;
    }

    /**
     * converts a coordinate into a row
     *
     * @param y
     * @return
     */
    private int transformCoordinateYInRow(float y) {

        int row = (int) ((y - TOP_MARGIN) / cellHeight);

        // if tapped outside the board
        if ((row < 0) || (row >= GameLogic.ROWS)) {
            row = -1;
        }

        return row;
    }

}
