package com.afeka.sm.Minesweeper;


public class Game {
    private Board board;

    public Game(int level) {
        board = new Board(level);
    }


    public String playTile(int position) {
        int col = position % board.getSize();
        int row = position / board.getSize();
        String status = board.discoverTile(row, col);
        return status;
    }


    public Board getBoard() {
        return board;
    }
}
