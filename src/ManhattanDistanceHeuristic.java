public class ManhattanDistanceHeuristic implements Heuristic {
    @Override
    public int calculate(State state) {
        Piece primary = state.getPrimaryPiece(); // Ini akan mengembalikan copy
        Point exit = state.getExitPosition();

        if (primary == null || exit == null) {
            return Integer.MAX_VALUE;
        }

        Point primaryFront = primary.getFrontCell(exit);
        
        return Math.abs(primaryFront.row - exit.row) + Math.abs(primaryFront.col - exit.col);
    }
}