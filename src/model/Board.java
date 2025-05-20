package model;

import java.util.*;

public class Board {
    private int rows;
    private int cols;
    private char[][] grid;
    private Map<Character, Piece> pieces;
    private Position exitPosition;

    public Board(int rows, int cols, char[][] grid, Map<Character, Piece> pieces, Position exitPosition) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.pieces = pieces;
        this.exitPosition = exitPosition;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public char[][] getGrid() {
        return grid;
    }

    public Map<Character, Piece> getPieces() {
        return pieces;
    }

    public Position getExitPosition() {
        return exitPosition;
    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }        
    }
}
