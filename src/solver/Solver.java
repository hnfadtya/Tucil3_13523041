package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import model.*;

public abstract class Solver {
    public abstract  Result solve(Board initialBoard);

    protected boolean isGoal(Board board) {
        Piece primary = board.getPieces().get('P');
        Position exit = board.getExitPosition();

        for (Position pos : primary.getPositions()) {
            if (primary.isHorizontal()) {
                if (pos.row == exit.row &&
                    (pos.col + 1 == exit.col || pos.col - 1 == exit.col)) {
                    return true;
                }
            } else {
                if (pos.col == exit.col &&
                    (pos.row + 1 == exit.row || pos.row - 1 == exit.row)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected List<Node> generateSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        Board currentBoard = node.getBoard(); // ngambil board dulu 
        Map<Character, Piece> pieces = currentBoard.getPieces(); // ngambil Map<Character, Piece>

        for (Piece piece : pieces.values()) { // ngecek tiap piece
            List<Move> possibleMoves = getValidMoves(currentBoard, piece); // list gerakan yg memungkinkan (tidak berada di pinggir papan)
            for (Move move : possibleMoves) { 
                Board newBoard = currentBoard.simulateMove(piece, move);
                if (newBoard != null) {
                    List<Move> newPath = new ArrayList<>(node.getPath());
                    newPath.add(move);
                    successors.add(new Node(newBoard, newPath, node.getCost() + 1));
                }
            }
        }

        return successors;
    }

    protected List<Move> getValidMoves(Board board, Piece piece) {
        List<Move> moves = new ArrayList<>();
        char[][] grid = board.getGrid();
        int rows = board.getRows();
        int cols = board.getCols();
        List<Position> positions = piece.getPositions(); // cari tau posisi setiap kotaknya dimana aja

        positions.sort(Comparator.comparingInt(pos -> piece.isHorizontal() ? pos.col : pos.row)); // menyesuaikan isi positions agar index pertama isinya kotak paling kiri atau paling atas

        if (piece.isHorizontal()) {
            Position leftMost = positions.get(0); 
            int r = leftMost.row; 
            int c = leftMost.col - 1;
            int steps = 0;

            while (c >= 0 && grid[r][c] == '.') { // akan dilewati jika leftMost.col = 0 (di pinggir kiri papan)
                steps++;
                moves.add(new Move(piece.getId(), Move.Direction.LEFT, steps));
                c--;
            }

            Position rightMost = positions.get(positions.size() - 1);
            r = rightMost.row;
            c = rightMost.col + 1;
            steps = 0;

            while (c < cols && grid[r][c] == '.') { // akan dilewati jika rightMost.col = jumlah kolom papan (di pinggir kanan papan)
                steps++;
                moves.add(new Move(piece.getId(), Move.Direction.RIGHT, steps));
                c++;
            }
        } else {
            Position topMost = positions.get(0);
            int r = topMost.row - 1;
            int c = topMost.col;
            int steps = 0;

            while (r >= 0 && grid[r][c] == '.') { // akan dilewati jika topMost.row = 0 (di pinggir atas papan)
                steps++;
                moves.add(new Move(piece.getId(), Move.Direction.UP, steps));
                r--;
            }

            Position bottomMost = positions.get(positions.size() - 1);
            r = bottomMost.row + 1;
            c = bottomMost.col;
            steps = 0;

            while (r < rows && grid[r][c] == '.') { // akan dilewati jika bottomMost.row = jumlah baris papan (di pinggir bawah papan)
                steps++;
                moves.add(new Move(piece.getId(), Move.Direction.DOWN, steps));
                r++;
            }
        }

        return moves;
    }

    protected String generateBoardKey(Board board) {
        StringBuilder sb = new StringBuilder(); // Constructs a string builder with no characters in it and an initial capacity of 16 characters
        List<Character> ids = new ArrayList<>(board.getPieces().keySet()); // ingat bro getPieces returns Map<Character, Piece>
        Collections.sort(ids);

        for (char id : ids) {
            Piece piece = board.getPieces().get(id); // mengambil piece berdasarkan id yang terurut
            sb.append(id);
            for (Position pos : piece.getPositions()) {
                sb.append("(").append(pos.row).append(",").append(pos.col).append(")");
            }
        }

        return sb.toString();
    }
}
