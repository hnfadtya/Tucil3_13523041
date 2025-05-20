package model;

import java.util.List;

public class Piece {
    private char id;
    private List<Position> positions;
    private boolean isHorizontal;

    public Piece(char id, List<Position> positions) {
        this.id = id;
        this.positions = positions;
        this.isHorizontal = determineOrientation();
    }

    private boolean determineOrientation() {
        if (positions.size() < 2) return true; // default horizontal kalo cm satu kotak
        Position first = positions.get(0);
        Position second = positions.get(1);
        return first.row == second.row;
    }

    public char getId() {
        return id;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public int getLength() {
        return positions.size();
    }

    public void setPositions(List<Position> newPositions) {
        this.positions = newPositions;
    }
}
