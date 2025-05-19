public class Move {
    public final char pieceId;
    public final Direction direction;
    public final int distance;

    public Move(char pieceId, Direction direction, int distance) {
        this.pieceId = pieceId;
        this.direction = direction;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Gerakkan piece '" + pieceId + "' ke arah " + direction + " sejauh " + distance + " sel.";
    }
}