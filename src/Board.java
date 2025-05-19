import java.io.*;
import java.util.*;

public class Board {
    public int rows, cols;
    public char[][] grid;
    public Map<Character, Piece> pieces = new HashMap<>();
    public int exitRow = -1, exitCol = -1;

    public Board(int rows, int cols, char[][] grid, Map<Character, Piece> pieces, int exitRow, int exitCol) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.pieces = pieces;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
    }

    public static Board fromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        String[] dim = reader.readLine().split(" ");
        int rows = Integer.parseInt(dim[0]);
        int cols = Integer.parseInt(dim[1]);

        int pieceCount = Integer.parseInt(reader.readLine());
        char[][] grid = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = reader.readLine();
            for (int c = 0; c < cols; c++) {
                grid[r][c] = line.charAt(c);
            }
        }

        Map<Character, List<int[]>> locations = new HashMap<>();
        int exitRow = -1, exitCol = -1;

        // Catat lokasi huruf-huruf kendaraan
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = grid[r][c];
                if (ch == '.') continue;
                if (ch == 'K') {
                    exitRow = r;
                    exitCol = c;
                    continue;
                }
                locations.putIfAbsent(ch, new ArrayList<>());
                locations.get(ch).add(new int[]{r, c});
            }
        }

        // Bangun objek Piece dari lokasi-lokasi
        Map<Character, Piece> pieces = new HashMap<>();
        for (Map.Entry<Character, List<int[]>> entry : locations.entrySet()) {
            char id = entry.getKey();
            List<int[]> pos = entry.getValue();
            int row0 = pos.get(0)[0], col0 = pos.get(0)[1];
            int row1 = pos.get(1)[0], col1 = pos.get(1)[1];
            boolean horizontal = row0 == row1;
            int length = pos.size();

            pieces.put(id, new Piece(id, row0, col0, length, horizontal, false));
        }

        return new Board(rows, cols, grid, pieces, exitRow, exitCol);
    }

    public void printBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
        System.out.println("Exit at: (" + exitRow + ", " + exitCol + ")");
    }
}
