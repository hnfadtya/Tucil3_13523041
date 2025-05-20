package parser;

import java.io.*;
import java.util.*;
import model.*;

public class BoardReader {

    public static Board readFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        // 1. Baca dimensi papan
        String[] dims = br.readLine().trim().split(" ");
        int rows = Integer.parseInt(dims[0]);
        int cols = Integer.parseInt(dims[1]);

        // 2. Baca jumlah piece (tidak digunakan langsung)
        int pieceCount = Integer.parseInt(br.readLine().trim());

        // 3. Baca baris-baris konfigurasi
        List<String> rawLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                rawLines.add(line);
            }
        }
        br.close();

        // 4. Deteksi posisi pintu keluar dan bentuk grid murni
        Position exitPosition = null;
        List<String> boardLines = new ArrayList<>();

        if (rawLines.size() == rows + 1) { // K di atas atau bawah
            if (rawLines.get(0).contains("K")) { // K di atas
                int kCol = rawLines.get(0).indexOf('K');
                exitPosition = new Position(-1, kCol); // kRow = -1 (di atas) 
                boardLines = rawLines.subList(1, rawLines.size()); // membuang baris paling atas
            } else { // K di bawah
                int kCol = rawLines.get(rawLines.size() - 1).indexOf('K');
                exitPosition = new Position(rows, kCol); // kRow = rows (di bawah)
                boardLines = rawLines.subList(0, rows); // membuang baris paling bawah
            }
        } else { // K ada di kiri atau kanan (di dalam baris grid)
            if ((rawLines.get(0)).charAt(0) == ' ') { // K di kiri
                for (int r = 0; r < rows; r++) {
                    String row = rawLines.get(r);
                    if (row.charAt(0) == 'K') {
                        exitPosition = new Position(r, -1); // kCol = -1 (di kiri)
                    } 
                    boardLines.add(row.substring(1));  // membuang baris paling kiri
                }
            } else {
                for (int r = 0; r < rows; r++) {
                    String row = rawLines.get(r);
                    if (row.charAt(cols) == 'K') {
                        exitPosition = new Position(r, cols); // kCol = cols (di kanan)
                    } 
                    boardLines.add(row.substring(0, row.length() - 1)); // buang 'K'
                }
            }
        }
        
        // 5. Bangun grid dan mapping piece
        char[][] grid = new char[rows][cols];
        Map<Character, List<Position>> piecePositions = new HashMap<>();

        for (int r = 0; r < rows; r++) {
            String row = boardLines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = row.charAt(c);
                grid[r][c] = ch;
                // System.out.print(ch);
                
                if (Character.isUpperCase(ch)) {
                    piecePositions.putIfAbsent(ch, new ArrayList<>());
                    piecePositions.get(ch).add(new Position(r, c));
                }
            }
            // System.out.println();
        }

        // 6. Buat objek Piece
        Map<Character, Piece> pieces = new HashMap<>();
        for (Map.Entry<Character, List<Position>> entry : piecePositions.entrySet()) {
            char id = entry.getKey();
            pieces.put(id, new Piece(id, entry.getValue()));
        }

        // 7. Return board
        return new Board(rows, cols, grid, pieces, exitPosition);
    }
}
