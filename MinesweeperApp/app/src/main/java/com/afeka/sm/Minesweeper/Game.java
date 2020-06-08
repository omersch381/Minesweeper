package com.afeka.sm.Minesweeper;


public class Game {
    private Board board;

    public Game(int level) {
        board = new Board(level);
    }

    public String playTile(int position) {
        int col = position % board.getSize();
        int row = position / board.getSize();
        return board.discoverTile(row, col);
    }

    public Board getBoard() {
        return board;
    }

    public void coverARandomTile() {
        board.coverARandomTile();
    }

    public void insertARandomMine() {
        board.insertAMineOnPunishMode();
    }
}
