package solver;

import java.util.List;
import model.Move;

public class Result {
    private List<Move> moves;
    private int visitedNodes;
    private long executionTime;

    public Result(List<Move> moves, int visitedNodes, long executionTime) {
        this.moves = moves;
        this.visitedNodes = visitedNodes;
        this.executionTime = executionTime;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public int getVisitedNodes() {
        return visitedNodes;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
