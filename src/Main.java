import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Penggunaan: java Main <path_ke_file_konfigurasi>");
            // Contoh: java Main test/sample_board.txt
            return;
        }
        String filePath = args[0];

        try {
            State initialState = Solver.parseBoardFromFile(filePath); // Panggil dari Solver
            System.out.println("State Awal Papan:");
            System.out.println(initialState);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Pilih algoritma (UCS, GBFS, A*):");
            String algoChoice = scanner.nextLine().toUpperCase();

            Heuristic selectedHeuristic = null;
            if (algoChoice.equals("GBFS") || algoChoice.equals("A*")) {
                System.out.println("Pilih heuristik (1 untuk Manhattan, 2 untuk Blocking):");
                String heurInput = scanner.nextLine();
                int heurChoice = -1;
                try {
                    heurChoice = Integer.parseInt(heurInput);
                } catch (NumberFormatException e) {
                    System.out.println("Input heuristik tidak valid. Harus berupa angka.");
                    scanner.close();
                    return;
                }

                if (heurChoice == 1) {
                    selectedHeuristic = new ManhattanDistanceHeuristic();
                } else if (heurChoice == 2) {
                    selectedHeuristic = new BlockingHeuristic();
                } else {
                    System.out.println("Pilihan heuristik tidak valid.");
                    scanner.close();
                    return;
                }
            }

            Solver.Solution solution = Solver.solve(initialState, algoChoice, selectedHeuristic); // Panggil dari Solver

            System.out.println("\n--- Hasil ---");
            System.out.println("Algoritma: " + algoChoice);
            if (selectedHeuristic != null) {
                 System.out.println("Heuristik: " + selectedHeuristic.getClass().getSimpleName());
            }

            if (solution.path != null && !solution.path.isEmpty()) {
                System.out.println("Solusi ditemukan dalam " + solution.path.size() + " langkah:");
                for (Move move : solution.path) { // Pastikan Move diimport jika perlu
                    System.out.println(move);
                }
            } else {
                System.out.println("Tidak ada solusi yang ditemukan.");
            }
            System.out.println("Node yang dikunjungi: " + solution.nodesVisited);
            System.out.printf("Waktu eksekusi: %.3f ms\n", solution.executionTimeMs);

            scanner.close();

        } catch (IOException e) {
            System.err.println("Error membaca file atau konfigurasi papan: " + e.getMessage());
            // e.printStackTrace(); // Aktifkan untuk debugging detail
        } catch (IllegalArgumentException e) {
            System.err.println("Error argumen: " + e.getMessage());
        } catch (Exception e) { // Tangkap Exception umum untuk masalah tak terduga
            System.err.println("Terjadi kesalahan tak terduga: " + e.getMessage());
            e.printStackTrace(); // Ini penting untuk debugging masalah tak terduga
        }
    }
}