package model;

public class Move {
    private char pieceId;
    private Direction direction; // direction: orientasi piece 
    private int steps; // steps: kotak keberapa dari sebuah piece. kotak paling kiri atau atas bernilai satu dan seterusnya

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Move(char pieceId, Direction direction, int steps) {
        this.pieceId = pieceId;
        this.direction = direction;
        this.steps = steps;
    }

    public char getPieceId() {
        return pieceId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    public String toString() {
        return pieceId + " " + direction + " " + steps;
    }
}
