import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Piece {
    public final char id;
    public final int length;
    public final boolean isHorizontal;
    public final boolean isPrimary;
    private Point headPosition; // Titik paling atas untuk vertikal, paling kiri untuk horizontal

    public Piece(char id, int row, int col, int length, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.headPosition = new Point(row, col);
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
    }

    // Copy constructor
    public Piece(Piece other) {
        this.id = other.id;
        this.headPosition = new Point(other.headPosition.row, other.headPosition.col);
        this.length = other.length;
        this.isHorizontal = other.isHorizontal;
        this.isPrimary = other.isPrimary;
    }

    public Point getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(Point headPosition) {
        this.headPosition = headPosition;
    }
    
    public List<Point> getOccupiedCells() {
        List<Point> cells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                cells.add(new Point(headPosition.row, headPosition.col + i));
            } else {
                cells.add(new Point(headPosition.row + i, headPosition.col));
            }
        }
        return cells;
    }

    public Point getFrontCell(Point exitPosition) {
        if (isHorizontal) {
            if (exitPosition.col > headPosition.col) { 
                return new Point(headPosition.row, headPosition.col + length - 1);
            } else { 
                return headPosition;
            }
        } else { 
            if (exitPosition.row > headPosition.row) { 
                return new Point(headPosition.row + length - 1, headPosition.col);
            } else { 
                return headPosition;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return id == piece.id &&
               length == piece.length &&
               isHorizontal == piece.isHorizontal &&
               isPrimary == piece.isPrimary &&
               Objects.equals(headPosition, piece.headPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headPosition, length, isHorizontal, isPrimary);
    }

    @Override
    public String toString() {
        return "Piece{" +
               "id=" + id +
               ", head=" + headPosition +
               ", length=" + length +
               ", horiz=" + isHorizontal +
               ", primary=" + isPrimary +
               '}';
    }
}