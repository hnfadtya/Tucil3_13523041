package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public char[] getAllColumn(int col) {
        char[] column = new char[getRows()];
        for (int i = 0; i < getRows(); i++) {
            column[i] = grid[i][col];
        }
        return column;
    }

    public char[] getAllRow(int row) {
        return grid[row];
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }        
    }
    
    public Board simulateMove(Piece piece, Move move) {
        int steps = move.getSteps();
        Move.Direction dir = move.getDirection();

        Board copiedBoard = this.copyBoard();
        Map<Character, Piece> newPieces = copiedBoard.getPieces();
        Piece targetPiece = newPieces.get(piece.getId());
        List<Position> oldPositions = targetPiece.getPositions();
        char[][] newGrid = copiedBoard.getGrid();

        for (Position pos : oldPositions) {
            newGrid[pos.row][pos.col] = '.';
        }

        List<Position> newPositions = new ArrayList<>();
        for (Position pos : oldPositions) {
            int newRow = pos.row;
            int newCol = pos.col;

            switch (dir) {
                case UP -> newRow -= steps;
                case DOWN -> newRow += steps;
                case LEFT -> newCol -= steps;
                case RIGHT -> newCol += steps;
            }

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return null;
            }

            if (newGrid[newRow][newCol] != '.') {
                return null;
            }

            newPositions.add(new Position(newRow, newCol));
        }

        for (Position pos : newPositions) {
            newGrid[pos.row][pos.col] = piece.getId();
        }

        targetPiece.setPositions(newPositions);
        return copiedBoard;
    }

    public Board copyBoard() {
        // mengcopy grid lama (deep copy)
        char[][] newGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newGrid[i][j] = grid[i][j]; 
            }
        }        

        // mengcopy pieces lama
        Map<Character, Piece> newPieces = new HashMap<>();
        for (Map.Entry<Character, Piece> entry : pieces.entrySet()) {
            List<Position> newPositions = new ArrayList<>();
            for (Position pos : entry.getValue().getPositions()) {
                newPositions.add(new Position(pos.row, pos.col));
            }
            newPieces.put(entry.getKey(), new Piece(entry.getKey(), newPositions));
        }

        return new Board(rows, cols, newGrid, newPieces, new Position(exitPosition.row, exitPosition.col));
    }
}
