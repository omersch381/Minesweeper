package com.afeka.sm.Minesweeper;

import java.util.Random;

public class Board implements Finals {
    private Tile[][] grid;
    private int numOfFlags;
    private int size;
    private int level;
    private String gameStatus;
    private int numOfMines;

    public Board(int level) {
        this.level = level;
        size = 0;
        numOfFlags = 0;
        numOfMines = size * size / BOARD_MINES_RATIO;
        gameStatus = String.valueOf(EMPTY);
        this.grid = initiateBoard();
    }

    private Tile[][] initiateBoard() {
        setSize();
        Tile[][] board = new Tile[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = new Tile();
        setMinesOnBoard(board);
        setTilesStatus(board);
        return board;
    }

    private void setMinesOnBoard(Tile[][] board) {
        numOfMines = this.numOfFlags = size * size / BOARD_MINES_RATIO;
        Random rand = new Random();
        for (int i = 0; i < numOfMines; i++) {
            boolean wasMinePlaced;
            do {
                int row = rand.nextInt(size);
                int col = rand.nextInt(size);
                wasMinePlaced = board[row][col].setMine();
            } while (!wasMinePlaced);
        }
    }

    private void setTilesStatus(Tile[][] board) {
        int numOfAdjacentMines;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (!board[row][col].hasMine()) {
                    numOfAdjacentMines = getNumberOfAdjacentMines(board, row, col);
                    char status = (char) (numOfAdjacentMines + '0');
                    status = status == '0' ? EMPTY : status;
                    board[row][col].setStatus(status);
                }
    }

    private int getNumberOfAdjacentMines(Tile[][] board, int row, int col) {
        int numOfAdjacentMines = 0;
        Tile currentTile;
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++) {
                boolean isCurrentTile = i == 0 && j == 0; // if we are checking the current tile - it will not affect the adjacent number Of mines
                boolean isValid = isValid(row + i, col + j);
                if (!isValid || isCurrentTile)
                    continue;
                currentTile = board[row + i][col + j];
                if (currentTile.hasMine())
                    numOfAdjacentMines++;
            }
        return numOfAdjacentMines;
    }

    private boolean isValid(int row, int col) {
        return (row >= 0) && row < size && (col >= 0) && (col < size);
    }

    public String discoverTile(int row, int col) {
        gameStatus = GAME_STATUS_PLAY;
        Tile currentTile = this.grid[row][col];
        if (!currentTile.isDiscovered() && !currentTile.hasFlag()) {
            currentTile.setDiscovered();
            char status = currentTile.getStatus();
            int statusAsAnInteger = Character.getNumericValue(status);
            if (Character.isDigit(status) && statusAsAnInteger > 0 && statusAsAnInteger < 9)
                currentTile.setShowToUser(status); //which is a (char) number
            else if (status == EMPTY)
                discoverTilesRecursivelyFromGivenIndex(row, col);
            else  // a mine was clicked
                gameOverAndShowMines(row, col);
        }
        hasWon();
        return gameStatus;
    }

    private void hasWon() {
        int numOfDiscoveredTiles = 0;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++) {
                Tile currentTile = this.grid[row][col];
                if (currentTile.getShowToUser() != MINE && currentTile.isDiscovered())
                    numOfDiscoveredTiles++;
            }
        if (numOfDiscoveredTiles == getBoardSize() - getNumOfMines())
            gameStatus = GAME_STATUS_WIN;
    }

    private void setGameStatus(String status) {
        gameStatus = status;
    }

    private boolean isEmpty(Tile currentTile) {
        return currentTile.getStatus() == EMPTY;
    }

    private void discoverTilesRecursivelyFromGivenIndex(int row, int col) {
        Tile currentTile = this.grid[row][col];
        if (currentTile.hasFlag())
            numOfFlags++; // We discover the Tile so we return the flag
        currentTile.setDiscovered();
        if (!isEmpty(currentTile))
            return;
        if (row != 0 && !this.grid[row - 1][col].isDiscovered()) // up
            discoverTilesRecursivelyFromGivenIndex(row - 1, col);
        if (row != size - 1 && !this.grid[row + 1][col].isDiscovered()) // down
            discoverTilesRecursivelyFromGivenIndex(row + 1, col);
        if (col != 0 && !this.grid[row][col - 1].isDiscovered()) // left
            discoverTilesRecursivelyFromGivenIndex(row, col - 1);
        if (col != size - 1 && !this.grid[row][col + 1].isDiscovered()) // right
            discoverTilesRecursivelyFromGivenIndex(row, col + 1);
    }

    public void gameOverAndShowMines(int row, int col) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (grid[i][j].hasMine())
                    grid[i][j].setDiscovered();

        grid[row][col].setShowToUser(RED_MINE); // paint the clicked mine in red
        setGameStatus(GAME_STATUS_LOSE);
    }

    public int getNumberOfFlags() {
        return numOfFlags;
    }

    public void updateNumberOfFlags(int position) {
        int col = position % size;
        int row = position / size;
        Tile currentTile = this.grid[row][col];
        if (!currentTile.isDiscovered()) {
            boolean wasFlagPlaced = currentTile.handleFlag();
            numOfFlags = wasFlagPlaced ? numOfFlags - 1 : numOfFlags + 1;
            if (numOfFlags < 0) {
                wasFlagPlaced = currentTile.handleFlag();
                numOfFlags = wasFlagPlaced ? numOfFlags - 1 : numOfFlags + 1;
            }
        }
    }

    private void setSize() {
        switch (this.level) {
            case EASY_LEVEL:
                this.size = EASY_BOARD_SIZE;
                break;
            case MEDIUM_LEVEL:
                this.size = MEDIUM_BOARD_SIZE;
                break;
            default: //HARD_LEVEL
                this.size = HARD_BOARD_SIZE;
                break;
        }
    }

    public int getBoardSize() {
        return size * size;
    }

    public Tile getTile(int position) {
        int col = position % size;
        int row = position / size;
        return grid[row][col];
    }

    public int getLevel() {
        return this.level;
    }

    public int getSize() {
        return this.size;
    }

    public int getNumOfMines() {
        return size * size / BOARD_MINES_RATIO;
    }

    public void coverARandomTile() {
        Tile currentTile;
        if ((currentTile = getNextEmptyTile()) != null)
            currentTile.setCovered();
    }

    private Tile getNextEmptyTile() {
        int counter = 0; // We made counter in case there is no empty tile
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(size);
            col = rand.nextInt(size);
            counter++;
        } while (!grid[row][col].isDiscovered() && counter < 100000);
        if (counter >= 100000)
            return null;
        return grid[row][col];
    }

    //insert a Mine in a tile after x seconds since a phone's position was changed
    public void insertAMineOnPunishMode() {
        Tile currentTile = getNextTileToInsertAMine();
        if (currentTile != null) {
            currentTile.setMine();
            numOfFlags++;
            numOfMines++;
            setTilesStatus(grid);
            updateTilesShowToUser(grid);
        }
    }

    public Tile getNextTileToInsertAMine() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(size);
            col = rand.nextInt(size);
        } while (grid[row][col].isDiscovered() || grid[row][col].hasMine());
        return grid[row][col];
    }

    public void updateTilesShowToUser(Tile[][] gridToUpdate) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                Tile currentTile = gridToUpdate[i][j];
                if (Character.isDigit(currentTile.getStatus()) && currentTile.isDiscovered())
                    currentTile.setShowToUser(currentTile.getStatus());
            }
    }

    public boolean hasLost() { // In this method we check if the whole board is full of bombs => the user has lost.
        // We called it "hasLost" and not "isTheBoardFullOfMines" because maybe other changes in the future will also be
        // considered as if the user has lost
        return numOfMines == getBoardSize();
    }

    public void finishGame() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                grid[i][j].setShowToUser(MINE);
    }

    public String getGameStatus() {
        return gameStatus;
    }
}