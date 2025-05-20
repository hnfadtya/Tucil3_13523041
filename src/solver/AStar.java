package solver;

import heuristic.*;
import java.util.*;
import model.*;

public class AStar extends Solver {
    private HeuristicFunction heuristic;

    public AStar(HeuristicFunction heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Result solve(Board initialBoard) {
        long startTime = System.nanoTime();
        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingInt(n -> n.getCost() + heuristic.evaluate(n.getBoard()))
        );
        Set<String> visited = new HashSet<>();

        queue.add(new Node(initialBoard, new ArrayList<>(), 0));
        int visitedNodes = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedNodes++;

            Board board = current.getBoard();
            String key = generateBoardKey(board);
            if (visited.contains(key)) continue;
            visited.add(key);

            if (isGoal(board)) {
                long endTime = System.nanoTime();
                return new Result(current.getPath(), visitedNodes, (endTime - startTime) / 1_000_000);
            }

            queue.addAll(generateSuccessors(current));
        }

        return new Result(new ArrayList<>(), visitedNodes, 0);
    }
}
