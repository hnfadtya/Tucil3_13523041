import heuristic.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.Board;
import model.Move;
import parser.BoardReader;
import solver.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showTitle();
    
            // === 1. PILIH FILE INPUT ===
            System.out.println("Masukkan nama file konfigurasi (di folder test/): "); // asumsikan sudah ada file di folder test/
            System.out.println("Ketik 'Exit' untuk keluar dari program...");
            System.out.print(">> ");
    
            String input = scanner.nextLine().trim();
            if (input.equals("Exit")) break;
            
            String filename = "test/" + input;
    
            Board board;
            try {
                board = BoardReader.readFromFile(filename);
                System.out.println("\nKonfigurasi papan berhasil dibaca!\n");
                board.printBoard();
                System.out.println("Koordinat pintu keluar: " + (board.getExitPosition()).toString());
            } catch (IOException e) {
                System.err.println("Gagal membaca file: " + e.getMessage());
                return;
            }
    
            // === 2. PILIH ALGORITMA ===
            System.out.println("\nPilih algoritma pencarian:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best First Search (GBFS)");
            System.out.println("3. A* Search");
            System.out.print("Pilihan [1-3]: ");
            int algChoice = scanner.nextInt();
            scanner.nextLine(); // flush newline
    
            // === 3. PILIH HEURISTIK (untuk GBFS dan A*) ===
            HeuristicFunction heuristic = null;
            if (algChoice == 2 || algChoice == 3) {
                System.out.println("\nPilih heuristik:");
                System.out.println("1. Manhattan Distance");
                System.out.println("2. Blocking Pieces");
                System.out.print("Pilihan [1-2]: ");
                int heurChoice = scanner.nextInt();
                scanner.nextLine(); // flush newline
    
                heuristic = switch (heurChoice) {
                    case 1 -> new ManhattanDistanceHeuristic();
                    case 2 -> new BlockingHeuristic();
                    default -> {
                        System.out.println("Pilihan tidak valid. Menggunakan Manhattan.");
                        yield new ManhattanDistanceHeuristic();
                    }
                };
            }
    
            // === 4. JALANKAN SOLVER ===
            Solver solver = switch (algChoice) {
                case 1 -> new UCS();
                case 2 -> new GreedyBFS(heuristic);
                case 3 -> new AStar(heuristic);
                default -> {
                    System.out.println("Pilihan tidak valid. Menggunakan UCS.");
                    yield new UCS();
                }
            };
    
            System.out.println("\nMencari solusi...\n");
            Result result = solver.solve(board);
    
            // === 5. CETAK HASIL ===
            displayResult(result, board);
        }
        scanner.close();
    }

    private static void showTitle() {
        System.out.println("=========================================");
        System.out.println("            RUSH HOUR SOLVER             ");
        System.out.println("=========================================");
    }

    private static void displayResult(Result result, Board board) {
        List<Move> moves = result.getMoves();
        if (moves.isEmpty()) {
            System.out.println("Tidak ditemukan solusi.");
        } else {
            System.out.println("Solusi ditemukan!");
            System.out.println("Jumlah langkah: " + moves.size());
            System.out.println("\nMenampilkan simulasi langkah demi langkah:\n");

            Board currentBoard = board.copyBoard();

            for (int i = 0; i < moves.size(); i++) {
                Move move = result.getMoves().get(i);
                System.out.println("Langkah " + (i + 1) + ": " + move);

                currentBoard = currentBoard.simulateMove(currentBoard.getPieces().get(move.getPieceId()), move);

                currentBoard.printBoard();
                System.out.println();
            }
        }

        System.out.println("\nStatistik:");
        System.out.println("Node dikunjungi: " + result.getVisitedNodes());
        System.out.println("Waktu eksekusi: " + result.getExecutionTime() + " ms");
    }
}
