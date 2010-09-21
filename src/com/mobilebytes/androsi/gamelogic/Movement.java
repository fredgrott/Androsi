package com.mobilebytes.androsi.gamelogic;

public class Movement {
	/**
     * The column of the movement
     */
    private int column;
    /**
     * The row of the movement
     */
    private int row;
    /**
     * The player of the movement
     */
    private int player;

    /**
     * Constructs the movement
     *
     * @param column
     * @param row
     * @param player
     */
    public Movement(int column, int row, int player) {
        this.column = column;
        this.row = row;
        this.player = player;
    }

    public int getColumn() {
        return column;
    }

    public int getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Just returns a string representation of the movement
     */
    @Override
    public String toString() {
        return String.format("(%d, %d) P%d", column, row, player);
    }
}