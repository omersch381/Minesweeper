package com.afeka.sm.Minesweeper;

import android.graphics.Color;

import com.example.mineswipper.R;

public class Tile {
    final char MINE = 'X';
    final char EMPTY = ' ';
    final char FLAG = 'F';
    final char RED_MINE = 'R';
    private boolean isDiscovered;
    private boolean hasFlag;
    private boolean hasMine;
    private char status;
    private char showToUser;

    public Tile() {
        this.isDiscovered = false;
        this.hasFlag = false;
        this.hasMine = false;
        this.status = EMPTY;
        this.showToUser = EMPTY;
    }

    public boolean setMine() {
        if (!hasMine) {
            hasMine = true;
            setStatus(MINE);
            return true;
        } else
            return false;

    }

    public void setStatus(char status) {
        int statusAsAnInteger = Character.getNumericValue(status);
        if (status == MINE)
            this.status = MINE;
        else if (Character.isDigit(status) && statusAsAnInteger > 0 && statusAsAnInteger < 9)
            this.status = status;
        else
            this.status = EMPTY;
    }

    public boolean handleFlag() throws Exception {
        if (isDiscovered)
            throw new Exception(String.valueOf(R.string.TileAlreadyDiscoveredErrorMSG));
        else {
            if (hasFlag()) {
                hasFlag = false;
                setShowToUser(EMPTY);
            } else {
                hasFlag = true;
                setShowToUser(FLAG);
            }
        }
        return hasFlag;
    }

    public void setDiscovered() {
        if (!isDiscovered) {
            isDiscovered = true;
            setShowToUser(status);
        }
    }

    public void setShowToUser(char showToUser) {
        if (!isDiscovered || showToUser == RED_MINE) // if not discovered or bombed-clicked
            this.showToUser = showToUser;
        else //isDiscovered = true
            this.showToUser = status;
    }

    public int getBackgroundColor() {
        if (isDiscovered)
            return Color.BLACK;
        else
            return Color.GREEN;
    }

    public char getStatus() {
        return status;
    }

    public char getShowToUser() {
        return showToUser;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public boolean hasMine() {
        return hasMine;
    }

}
