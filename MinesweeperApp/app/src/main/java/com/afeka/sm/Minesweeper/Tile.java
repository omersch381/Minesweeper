package com.afeka.sm.Minesweeper;


public class Tile implements Finals {
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

    public boolean handleFlag() {
        if (hasFlag()) {
            hasFlag = false;
            setShowToUser(EMPTY);
        } else {
            hasFlag = true;
            setShowToUser(FLAG);
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

    public void setCovered() {
        this.isDiscovered = false;
        setShowToUser(EMPTY);
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
