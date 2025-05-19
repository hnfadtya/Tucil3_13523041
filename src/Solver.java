import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays; // Tambahkan import ini
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Solver { // Ganti nama kelas

    // parseBoardFromFile dipindahkan ke sini
    public static State parseBoardFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        int numRows = 0;
        int numCols = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Dimensi
            if (line == null) throw new IOException("File konfigurasi kosong atau format dimensi salah.");
            String[] dims = line.split(" ");
            if (dims.length < 2) throw new IOException("Format dimensi salah. Harusnya 'baris kolom'.");
            numRows = Integer.parseInt(dims[0]);
            numCols = Integer.parseInt(dims[1]);

            reader.readLine(); // N, kita abaikan dan identifikasi pieces dari board

            char[][] rawBoard = new char[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                String boardLine = reader.readLine();
                if (boardLine == null) throw new IOException("Konfigurasi papan tidak lengkap. Baris diharapkan: " + numRows);
                
                // Inisialisasi baris dengan titik (sel kosong)
                Arrays.fill(rawBoard[i], '.');
                // Isi dengan karakter dari file, tangani baris yang lebih pendek
                for (int j = 0; j < boardLine.length() && j < numCols; j++) {
                    rawBoard[i][j] = boardLine.charAt(j);
                }
            }

            List<Piece> pieces = new ArrayList<>();
            Point exitPosition = null;
            Map<Character, List<Point>> pieceCells = new HashMap<>();
            Set<Character> processedPieceChars = new HashSet<>(); // Untuk menandai piece yang sudah diidentifikasi

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    char cellChar = rawBoard[r][c];
                    if (cellChar == 'K') {
                        exitPosition = new Point(r, c);
                    } else if (cellChar != '.' && !processedPieceChars.contains(cellChar)) {
                        // Karakter piece baru ditemukan, identifikasi seluruh piece
                        List<Point> currentPiecePoints = new ArrayList<>();
                        boolean isHorizontal = false;
                        int length = 0;

                        // Cek perluasan horizontal
                        if (c + 1 < numCols && rawBoard[r][c+1] == cellChar) {
                            isHorizontal = true;
                            int currentC = c;
                            while(currentC < numCols && rawBoard[r][currentC] == cellChar) {
                                currentPiecePoints.add(new Point(r, currentC));
                                length++;
                                currentC++;
                            }
                        } 
                        // Cek perluasan vertikal
                        else if (r + 1 < numRows && rawBoard[r+1][c] == cellChar) {
                            isHorizontal = false;
                            int currentR = r;
                             while(currentR < numRows && rawBoard[currentR][c] == cellChar) {
                                currentPiecePoints.add(new Point(currentR, c));
                                length++;
                                currentR++;
                            }
                        }
                        // Piece dengan panjang 1
                        else {
                            isHorizontal = true; // Asumsi default untuk piece panjang 1
                            currentPiecePoints.add(new Point(r,c));
                            length = 1;
                        }
                        
                        if (!currentPiecePoints.isEmpty()) {
                            pieces.add(new Piece(cellChar, currentPiecePoints.get(0).row, currentPiecePoints.get(0).col, length, isHorizontal, cellChar == 'P'));
                            processedPieceChars.add(cellChar);
                        }
                    }
                }
            }


            if (exitPosition == null) {
                throw new IOException("Pintu keluar 'K' tidak ditemukan di file konfigurasi.");
            }
            if (pieces.stream().noneMatch(p -> p.isPrimary)) {
                throw new IOException("Primary piece 'P' tidak ditemukan di file konfigurasi.");
            }
            
            return new State(numRows, numCols, pieces, exitPosition);
        }
    }

    // reconstructPath tetap di sini
    private static List<Move> reconstructPath(Node goalNode) {
        LinkedList<Move> path = new LinkedList<>();
        Node current = goalNode;
        while (current != null && current.move != null) {
            path.addFirst(current.move);
            current = current.parent;
        }
        return path;
    }

    // solve tetap di sini
    public static Solution solve(State initialState, String algorithmType, Heuristic heuristic) {
        PriorityQueue<Node> openSet;
        long startTime = System.nanoTime();
        int nodesVisited = 0;

        Node startNode = new Node(initialState, null, null, 0, heuristic != null ? heuristic.calculate(initialState) : 0);

        switch (algorithmType.toUpperCase()) {
            case "UCS":
                openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.g));
                break;
            case "GBFS":
                if (heuristic == null) throw new IllegalArgumentException("GBFS memerlukan heuristik.");
                openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.h));
                break;
            case "A*":
                if (heuristic == null) throw new IllegalArgumentException("A* memerlukan heuristik.");
                openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
                break;
            default:
                throw new IllegalArgumentException("Algoritma tidak dikenal: " + algorithmType);
        }

        openSet.add(startNode);
        Set<State> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            
            if (closedSet.contains(currentNode.state)) { // Cek di sini lebih efisien
                continue;
            }
            closedSet.add(currentNode.state);
            nodesVisited++; // Hanya hitung node yang diexpand (dikeluarkan dari openSet dan belum di closedSet)

            if (currentNode.state.isGoal()) {
                long endTime = System.nanoTime();
                List<Move> path = reconstructPath(currentNode);
                return new Solution(path, nodesVisited, (endTime - startTime) / 1_000_000.0); // ms
            }

            for (State.StateMovePair pair : currentNode.state.generateSuccessors()) {
                State successorState = pair.state;
                Move move = pair.move;

                if (closedSet.contains(successorState)) { // Jika sudah pernah di-expand, abaikan
                    continue;
                }

                int newG = currentNode.g + 1; 
                int newH = heuristic != null ? heuristic.calculate(successorState) : 0;
                Node successorNode = new Node(successorState, currentNode, move, newG, newH);
                
                openSet.add(successorNode);
            }
        }
        long endTime = System.nanoTime();
        return new Solution(null, nodesVisited, (endTime - startTime) / 1_000_000.0); 
    }

    // Kelas Solution tetap di sini sebagai inner class atau bisa juga file terpisah
    public static class Solution {
        public final List<Move> path;
        public final int nodesVisited;
        public final double executionTimeMs;

        public Solution(List<Move> path, int nodesVisited, double executionTimeMs) {
            this.path = path;
            this.nodesVisited = nodesVisited;
            this.executionTimeMs = executionTimeMs;
        }
    }

    // main method dipindah ke Main.java
}