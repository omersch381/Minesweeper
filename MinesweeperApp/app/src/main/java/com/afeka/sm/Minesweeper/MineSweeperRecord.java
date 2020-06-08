package com.afeka.sm.Minesweeper;

public class MineSweeperRecord implements Finals {
    String name = String.valueOf(EMPTY);
    int time = INITIAL_RECORD_VALUE;

    public MineSweeperRecord() {
    }

    public MineSweeperRecord(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
