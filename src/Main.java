import heuristic.*;
import java.io.*;
import java.util.*;
import model.*;
import parser.BoardReader;
import solver.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW_BG = "\u001B[43m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        while (true) {
            clearScreen();
            printHeader();
            run();
            System.out.print("\nIngin mencoba lagi? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {printFooter(); break;}
        }
    }

    private static void run() {
        Board board = chooseFile();
        Solver solver = chooseSolver();
        String outputName = askOutputFileName();

        try {
            Result result = solver.solve(board);
            displayResult(result, board, solver.getClass().getSimpleName(), outputName);
        } catch (OutOfMemoryError e) {
            System.err.println("\n Out of memory! Solusi tidak dapat ditemukan karena keterbatasan memori.");
        }
    }

    private static String askOutputFileName() {
        System.out.print("Masukkan nama file output (tanpa .txt): ");
        return scanner.nextLine().trim();
    }

    private static Board chooseFile() {
        while (true) {
            System.out.print("Masukkan nama file konfigurasi (di folder test/): ");
            String filename = "test/" + scanner.nextLine().trim();
            try {
                Board board = BoardReader.readFromFile(filename);
                System.out.println("\nKonfigurasi papan berhasil dibaca!\n");
                printBoard(board, null);
                return board;
            } catch (Exception e) {
                System.out.println("Gagal membaca file: " + e.getMessage());
            }
        }
    }

    private static Solver chooseSolver() {
        while (true) {
            System.out.println("\nPilih algoritma pencarian:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best First Search (GBFS)");
            System.out.println("3. A* Search");
            System.out.print("Pilihan [1-3]: ");
            String choice = scanner.nextLine().trim();
            HeuristicFunction heuristic = null;

            if (choice.equals("1")) return new UCS();
            if (choice.equals("2") || choice.equals("3")) {
                heuristic = chooseHeuristic();
                return choice.equals("2") ? new GreedyBFS(heuristic) : new AStar(heuristic);
            }
            System.out.println("Pilihan tidak valid. Silakan coba lagi.");
        }
    }

    private static HeuristicFunction chooseHeuristic() {
        while (true) {
            System.out.println("\nPilih heuristik:");
            System.out.println("1. Manhattan Distance");
            System.out.println("2. Blocking Pieces");
            System.out.print("Pilihan [1-2]: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) return new ManhattanDistanceHeuristic();
            if (choice.equals("2")) return new BlockingHeuristic();
            System.out.println("Pilihan tidak valid.");
        }
    }

    private static void displayResult(Result result, Board board, String solverName, String outputName) {
        List<Move> moves = result.getMoves();
        Board currentBoard = board.copyBoard();
        StringBuilder outputFile = new StringBuilder();
        String filePath = "test/" + outputName + ".txt";


        if (moves.isEmpty()) {
            System.out.println("Tidak ditemukan solusi.");
            outputFile.append("Tidak ditemukan solusi.\n");
        } else {
            System.out.println("Solusi ditemukan!");
            System.out.println("Jumlah langkah: " + moves.size());
            outputFile.append("Jumlah langkah: ").append(moves.size()).append("\n\n");

            for (int i = 0; i < moves.size(); i++) {
                Move move = moves.get(i);
                System.out.println("Langkah " + (i + 1) + ": " + move);
                outputFile.append("Langkah ").append(i + 1).append(": ").append(move).append("\n");

                currentBoard = currentBoard.simulateMove(currentBoard.getPieces().get(move.getPieceId()), move);
                printBoard(currentBoard, move.getPieceId());
                outputFile.append(boardToString(currentBoard, move.getPieceId())).append("\n");
            }
        }

        System.out.println("\nStatistik:");
        System.out.println("Node dikunjungi: " + result.getVisitedNodes());
        System.out.println("Waktu eksekusi: " + result.getExecutionTime() + " ms");

        outputFile.append("\nStatistik:\n")
                  .append("Node dikunjungi: ").append(result.getVisitedNodes()).append("\n")
                  .append("Waktu eksekusi: ").append(result.getExecutionTime()).append(" ms\n");

        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print(outputFile);
            System.out.println("\nHasil disimpan ke: " + filePath);
        } catch (IOException e) {
            System.err.println("\nGagal menyimpan hasil ke file: " + e.getMessage());
        }
    }

    private static void printBoard(Board board, Character highlight) {
        char[][] grid = board.getGrid();
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'P') System.out.print(RED + cell + RESET);
                else if (cell == 'K') System.out.print(BLUE + cell + RESET);
                else if (highlight != null && cell == highlight) System.out.print(YELLOW_BG + cell + RESET);
                else System.out.print(cell);
            }
            System.out.println();
        }
    }

    private static String boardToString(Board board, Character highlight) {
        StringBuilder sb = new StringBuilder();
        char[][] grid = board.getGrid();
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'P') sb.append('P');
                else if (cell == 'K') sb.append('K');
                else if (highlight != null && cell == highlight) sb.append('*');
                else sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void printHeader() {
        System.out.println("                                                               ");
        System.out.println("                                                               ");
        System.out.println("     ░▒█▀▀▄░▒█░▒█░▒█▀▀▀█░▒█░▒█░░░▒█░▒█░▒█▀▀▀█░▒█░▒█░▒█▀▀▄      ");
        System.out.println("     ░▒█▄▄▀░▒█░▒█░░▀▀▀▄▄░▒█▀▀█░░░▒█▀▀█░▒█░░▒█░▒█░▒█░▒█▄▄▀      ");
        System.out.println("     ░▒█░▒█░▒█▄▄█░▒█▄▄▄█░▒█░▒█░░░▒█░▒█░▒█▄▄▄█░▒█▄▄█░▒█░▒█      ");
        System.out.println("                                                               ");
        System.out.println("                 Welcome to Rush Hour Solver!                  ");
        System.out.println("                      by Hanif Kalyana Aditya                  ");
        System.out.println("                                                               ");
        System.out.println("                                                               ");
    }

    private static void printFooter() {
        System.out.println("\n┌──────────────────────────────────────────────┐");
        System.out.println("│  Terima kasih telah menggunakan program ini! │");
        System.out.println("└──────────────────────────────────────────────┘\n");
    }

    private static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033c"); // alternative clear for Unix-like systems
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n Tidak bisa membersihkan layar.");
        }
    }
}
