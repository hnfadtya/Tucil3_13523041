package heuristic;

import java.util.Comparator;
import java.util.List;
import model.*;

public class ManhattanDistanceHeuristic implements HeuristicFunction {

    @Override
    public int evaluate(Board board) {
        Piece primary = board.getPieces().get('P');
        List<Position> positions = primary.getPositions();
        positions.sort((a, b) -> primary.isHorizontal() ? a.col - b.col : a.row - b.row);

        Position farthest = positions.get(positions.size() - 1);
        Position exit = board.getExitPosition();

        if (primary.isHorizontal()) {
            positions.sort(Comparator.comparingInt(p -> p.col));
            int distance = exit.col - farthest.col;
            return Math.max(0, distance - 1);  // -1 karena harus berdiri 1 sebelum keluar
        } else {
            positions.sort(Comparator.comparingInt(p -> p.row));
            int distance = exit.row - farthest.row;
            return Math.max(0, distance - 1);
        }
    }
}


