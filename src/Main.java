import java.util.List;
import java.util.Scanner;
import model.Board;
import model.Move;
import parser.BoardReader;
import solver.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Rush Hour Puzzle Solver ===");

        // 1. Input file konfigurasi
        System.out.print("Masukkan nama file konfigurasi (misal: test/input1.txt): ");
        String filename = scanner.nextLine();

        Board board;
        try {
            board = BoardReader.readFromFile(filename);
            System.out.println("Exit at: " + board.getExitPosition());
        } catch (Exception e) {
            System.err.println("Gagal membaca file: " + e.getMessage());
            return;
        }

        // 2. Pilih algoritma
        System.out.println("\nPilih algoritma pencarian:");
        System.out.println("1. Uniform Cost Search (UCS)");
        System.out.println("2. Greedy Best First Search (COMING SOON)");
        System.out.println("3. A* Search (COMING SOON)");
        System.out.print("Pilihan [1-3]: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // flush newline

        Solver solver = null;
        switch (choice) {
            case 1:
                solver = new UCSolver();
                break;
            case 2:
            case 3:
                System.out.println("Belum diimplementasikan. Silakan pilih UCS terlebih dahulu.");
                return;
            default:
                System.out.println("Pilihan tidak valid.");
                return;
        }

        // 3. Jalankan Solver
        System.out.println("\nMemulai pencarian solusi...");
        Result result = solver.solve(board);

        // 4. Tampilkan hasil
        System.out.println("\n=== HASIL ===");
        List<Move> moves = result.getMoves();
        if (moves.isEmpty()) {
            System.out.println("Tidak ditemukan solusi.");
        } else {
            System.out.println("Jumlah langkah: " + moves.size());
            System.out.println("Langkah-langkah:");
            for (int i = 0; i < moves.size(); i++) {
                System.out.println((i + 1) + ". " + moves.get(i));
            }
        }

        System.out.println("\nJumlah node dikunjungi: " + result.getVisitedNodes());
        System.out.println("Waktu eksekusi: " + result.getExecutionTime() + " ms");
        
        scanner.close();
    }
}
