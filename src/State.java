import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
    private final char[][] board;
    private final List<Piece> pieces;
    private final int numRows;
    private final int numCols;
    private final Point exitPosition;
    private Piece primaryPieceCache = null;

    public State(int numRows, int numCols, List<Piece> initialPieces, Point exitPosition) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.exitPosition = exitPosition;
        this.pieces = new ArrayList<>();
        for (Piece p : initialPieces) {
            this.pieces.add(new Piece(p)); 
            if (p.isPrimary) {
                this.primaryPieceCache = this.pieces.get(this.pieces.size() - 1);
            }
        }
        this.board = new char[numRows][numCols];
        initializeBoard();
    }

    private State(State previousState, Piece movedPiece, Point newHeadPosition) {
        this.numRows = previousState.numRows;
        this.numCols = previousState.numCols;
        this.exitPosition = previousState.exitPosition;
        this.pieces = new ArrayList<>();
        for (Piece p : previousState.pieces) {
            if (p.id == movedPiece.id) {
                Piece newMovedPiece = new Piece(p);
                newMovedPiece.setHeadPosition(newHeadPosition);
                this.pieces.add(newMovedPiece);
                if (newMovedPiece.isPrimary) {
                    this.primaryPieceCache = newMovedPiece;
                }
            } else {
                this.pieces.add(new Piece(p)); 
            }
        }
        this.board = new char[numRows][numCols];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < numRows; i++) {
            Arrays.fill(board[i], '.');
        }
        for (Piece piece : pieces) {
            for (Point cell : piece.getOccupiedCells()) {
                if (isValidCoordinate(cell.row, cell.col)) {
                    board[cell.row][cell.col] = piece.id;
                }
            }
        }
    }

    public char[][] getBoard() {
        // Mengembalikan copy untuk menjaga immutability state dari luar jika diinginkan
        char[][] boardCopy = new char[numRows][numCols];
        for(int i=0; i<numRows; i++){
            boardCopy[i] = Arrays.copyOf(board[i], numCols);
        }
        return boardCopy;
    }

    public List<Piece> getPieces() {
        // Mengembalikan list yang berisi copy dari pieces untuk menjaga immutability
        List<Piece> piecesCopy = new ArrayList<>();
        for(Piece p : this.pieces){
            piecesCopy.add(new Piece(p));
        }
        return piecesCopy;
    }
    
    public Piece getPrimaryPiece() {
        if (primaryPieceCache == null) {
            for (Piece p : pieces) { // Gunakan this.pieces
                if (p.isPrimary) {
                    primaryPieceCache = p; // Tidak perlu di-copy di sini, karena ini internal cache
                    break;
                }
            }
        }
        // Jika mengembalikan keluar, sebaiknya di-copy
        return primaryPieceCache != null ? new Piece(primaryPieceCache) : null;
    }

    public Point getExitPosition() {
        return exitPosition; // Point immutable
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < numCols;
    }

    public boolean isGoal() {
        Piece primary = getPrimaryPiece(); // Ini sudah mengembalikan copy jika primaryPieceCache tidak null
        if (primary == null) return false;
        return primary.getFrontCell(exitPosition).equals(exitPosition);
    }

    public List<StateMovePair> generateSuccessors() {
        List<StateMovePair> successors = new ArrayList<>();
        // Iterasi menggunakan index agar bisa mendapatkan piece original dari this.pieces
        for (int pieceIndex = 0; pieceIndex < this.pieces.size(); pieceIndex++) {
            Piece piece = this.pieces.get(pieceIndex); // Dapatkan piece original dari state saat ini

            // Coba gerakkan ke arah "positif" (DOWN atau RIGHT)
            if (piece.isHorizontal) { // RIGHT
                for (int dist = 1; ; dist++) {
                    Point newHead = new Point(piece.getHeadPosition().row, piece.getHeadPosition().col + dist);
                    Point frontCellAfterMove = new Point(newHead.row, newHead.col + piece.length - 1);
                    if (!isValidCoordinate(frontCellAfterMove.row, frontCellAfterMove.col) || 
                        !isPathClear(piece, Direction.RIGHT, dist)) { // Modifikasi isPathClear
                        break; 
                    }
                    successors.add(new StateMovePair(
                        new State(this, piece, newHead), // piece yang dioper adalah piece original
                        new Move(piece.id, Direction.RIGHT, dist)
                    ));
                }
            } else { // DOWN
                for (int dist = 1; ; dist++) {
                    Point newHead = new Point(piece.getHeadPosition().row + dist, piece.getHeadPosition().col);
                    Point frontCellAfterMove = new Point(newHead.row + piece.length - 1, newHead.col);
                     if (!isValidCoordinate(frontCellAfterMove.row, frontCellAfterMove.col) || 
                        !isPathClear(piece, Direction.DOWN, dist)) { // Modifikasi isPathClear
                        break;
                    }
                    successors.add(new StateMovePair(
                        new State(this, piece, newHead),
                        new Move(piece.id, Direction.DOWN, dist)
                    ));
                }
            }

            // Coba gerakkan ke arah "negatif" (UP atau LEFT)
            if (piece.isHorizontal) { // LEFT
                for (int dist = 1; ; dist++) {
                    Point newHead = new Point(piece.getHeadPosition().row, piece.getHeadPosition().col - dist);
                    Point frontCellAfterMove = newHead; 
                    if (!isValidCoordinate(frontCellAfterMove.row, frontCellAfterMove.col) ||
                        !isPathClear(piece, Direction.LEFT, dist)) { // Modifikasi isPathClear
                        break;
                    }
                    successors.add(new StateMovePair(
                        new State(this, piece, newHead),
                        new Move(piece.id, Direction.LEFT, dist)
                    ));
                }
            } else { // UP
                for (int dist = 1; ; dist++) {
                    Point newHead = new Point(piece.getHeadPosition().row - dist, piece.getHeadPosition().col);
                    Point frontCellAfterMove = newHead;
                    if (!isValidCoordinate(frontCellAfterMove.row, frontCellAfterMove.col) ||
                        !isPathClear(piece, Direction.UP, dist)) { // Modifikasi isPathClear
                        break;
                    }
                    successors.add(new StateMovePair(
                        new State(this, piece, newHead),
                        new Move(piece.id, Direction.UP, dist)
                    ));
                }
            }
        }
        return successors;
    }
    
    // Modifikasi isPathClear: hanya perlu piece original, arah, dan jarak
    private boolean isPathClear(Piece pieceToMove, Direction dir, int distance) {
        Point oldHead = pieceToMove.getHeadPosition();
        List<Point> cellsToCheck = new ArrayList<>();

        // Tentukan sel-sel yang akan dilalui (antara posisi lama dan baru)
        // dan sel-sel yang akan ditempati (posisi baru)
        Point currentCell;
        for (int d = 1; d <= distance; d++) { // Untuk setiap langkah dalam pergeseran
            int checkRow = oldHead.row;
            int checkCol = oldHead.col;

            if (pieceToMove.isHorizontal) {
                if (dir == Direction.RIGHT) checkCol = oldHead.col + pieceToMove.length -1 + d; // Cek ujung depan + langkah
                else if (dir == Direction.LEFT) checkCol = oldHead.col - d; // Cek ujung depan (baru) - langkah
                 currentCell = new Point(oldHead.row, checkCol);
            } else { // Vertikal
                if (dir == Direction.DOWN) checkRow = oldHead.row + pieceToMove.length -1 + d;
                else if (dir == Direction.UP) checkRow = oldHead.row - d;
                currentCell = new Point(checkRow, oldHead.col);
            }
            
            if (!isValidCoordinate(currentCell.row, currentCell.col)) return false; // Keluar batas
            // Untuk piece horizontal yang bergerak ke kanan, sel yang dicek adalah piece.length dari head + d
            // Untuk piece horizontal yang bergerak ke kiri, sel yang dicek adalah head - d
            // Untuk piece vertikal yang bergerak ke bawah, sel yang dicek adalah piece.length dari head + d
            // Untuk piece vertikal yang bergerak ke atas, sel yang dicek adalah head - d
            if (board[currentCell.row][currentCell.col] != '.' && board[currentCell.row][currentCell.col] != 'K') {
                return false; // Dihalangi
            }
        }
        return true;
    }

    public static class StateMovePair {
        public final State state;
        public final Move move;
        public StateMovePair(State state, Move move) {
            this.state = state;
            this.move = move;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.deepEquals(this.board, state.board); // Gunakan this.board
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board); // Gunakan this.board
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sb.append(this.board[i][j]).append(" "); // Gunakan this.board
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}