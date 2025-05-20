package heuristic;

import model.Board;

public interface HeuristicFunction {
    int evaluate(Board board);
}
