public class Node {
    public final State state;
    public final Node parent;
    public final Move move; 
    public final int g; 
    public final int h; 

    public Node(State state, Node parent, Move move, int g, int h) {
        this.state = state;
        this.parent = parent;
        this.move = move;
        this.g = g;
        this.h = h;
    }

    public int getF() {
        return g + h; 
    }
}