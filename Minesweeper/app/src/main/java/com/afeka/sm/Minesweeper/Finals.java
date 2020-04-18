package com.afeka.sm.Minesweeper;

public interface Finals {
    char MINE = 'X';
    char EMPTY = ' ';
    char RED_MINE = 'R';
    char FLAG = 'F';

    String GAME_STATUS_PLAY = "play";
    String GAME_STATUS_WIN = "Win";
    String GAME_STATUS_LOSE = "Lose";

    int EASY_LEVEL = 1;
    int MEDIUM_LEVEL = 2;

    int EASY_BOARD_SIZE = 4;
    int MEDIUM_BOARD_SIZE = 6;
    int HARD_BOARD_SIZE = 8;

    int BOARD_MINES_RATIO = 7;

    String LEVEL_ACTIVITY_KEY = "level Activity";
    String GAME_RESULT = "GameResult";
}
