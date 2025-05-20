package solver;

import java.util.*;
import model.*;

public class UCS extends Solver {

    @Override
    public Result solve(Board initialBoard) {
        long startTime = System.nanoTime();
        long endTime;

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.getCost()));
        Set<String> visited = new HashSet<>();

        Node startNode = new Node(initialBoard, new ArrayList<>(), 0);
        queue.add(startNode);

        int visitedNodes = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Returns: : the head of this queue, or null if this queue is empty
            visitedNodes++;

            Board currentBoard = current.getBoard();

            String stateKey = generateBoardKey(currentBoard); // contoh: A(0,2)(0,3)B(1,2)(2,2)C(4,3)(5,3)...
            if (visited.contains(stateKey)) continue;
            visited.add(stateKey);

            if (isGoal(currentBoard)) {
                endTime = System.nanoTime();
                return new Result(current.getPath(), visitedNodes, (endTime - startTime) / 1_000_000);
            }

            List<Node> successors = generateSuccessors(current);
            queue.addAll(successors);
        }

        endTime = System.nanoTime();
        return new Result(new ArrayList<>(), visitedNodes, (endTime - startTime) / 1_000_000); // no solution found brader
    }

}
