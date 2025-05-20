package solver;

import java.util.List;
import model.Board;
import model.Move;

public class Node implements Comparable<Node> {
    private Board board;
    private List<Move> path;
    private int cost; // g(n)

    public Node(Board board, List<Move> path, int cost) {
        this.board = board;
        this.path = path;
        this.cost = cost;
    }

    public Board getBoard() {
        return board;
    }

    public List<Move> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.cost, other.cost);
    }
}
