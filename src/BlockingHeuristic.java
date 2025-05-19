import java.util.Arrays;
import java.util.HashSet;
import java.util.Set; 

public class BlockingHeuristic implements Heuristic {
    // ... (isi sama seperti sebelumnya, pastikan import State, Piece, Point benar)
    @Override
    public int calculate(State state) {
        Piece primary = state.getPrimaryPiece(); // Ini akan mengembalikan copy
        Point exit = state.getExitPosition();
        // Untuk mendapatkan board aktual, kita tidak bisa pakai state.getBoard() jika itu mengembalikan copy.
        // Sebaiknya state memiliki metode internal untuk akses board jika heuristik membutuhkannya,
        // atau heuristik hanya bekerja berdasarkan List<Piece> dan info P & K.
        // Untuk saat ini, asumsikan State menyediakan cara mendapatkan konfigurasi piece yang diperlukan.
        // Mari kita sederhanakan dengan membangun board sementara dari pieces di dalam state untuk heuristik ini.
        
        char[][] currentBoardConfig = new char[state.getNumRows()][state.getNumCols()];
        for(int i=0; i<state.getNumRows(); i++) Arrays.fill(currentBoardConfig[i], '.');
        for(Piece p : state.getPieces()){ // getPieces() akan mengembalikan list of copies
            for(Point cell : p.getOccupiedCells()){
                if(cell.row >=0 && cell.row < state.getNumRows() && cell.col >=0 && cell.col < state.getNumCols()){
                    currentBoardConfig[cell.row][cell.col] = p.id;
                }
            }
        }


        if (primary == null || exit == null) {
            return Integer.MAX_VALUE;
        }

        Set<Character> blockingPieces = new HashSet<>();
        Point primaryFront = primary.getFrontCell(exit);

        if (primary.isHorizontal) {
            int row = primary.getHeadPosition().row;
            // Cek ke kanan
            if (exit.col > primaryFront.col) { 
                for (int c = primaryFront.col + 1; c < exit.col; c++) { // Sel antara depan P dan K
                    if (currentBoardConfig[row][c] != '.' && currentBoardConfig[row][c] != 'K') {
                        blockingPieces.add(currentBoardConfig[row][c]);
                    }
                }
            // Cek ke kiri
            } else if (exit.col < primaryFront.col) { 
                for (int c = primaryFront.col - 1; c > exit.col; c--) {
                     if (currentBoardConfig[row][c] != '.' && currentBoardConfig[row][c] != 'K') {
                        blockingPieces.add(currentBoardConfig[row][c]);
                    }
                }
            }
        } else { // Vertikal
            int col = primary.getHeadPosition().col;
            // Cek ke bawah
            if (exit.row > primaryFront.row) { 
                for (int r = primaryFront.row + 1; r < exit.row; r++) {
                    if (currentBoardConfig[r][col] != '.' && currentBoardConfig[r][col] != 'K') {
                        blockingPieces.add(currentBoardConfig[r][col]);
                    }
                }
            // Cek ke atas
            } else if (exit.row < primaryFront.row) { 
                 for (int r = primaryFront.row - 1; r > exit.row; r--) {
                    if (currentBoardConfig[r][col] != '.' && currentBoardConfig[r][col] != 'K') {
                        blockingPieces.add(currentBoardConfig[r][col]);
                    }
                }
            }
        }
        
        int heuristicValue = blockingPieces.size();
        // Jika primary piece belum di posisi goal, tambahkan 1.
        // Ini karena minimal butuh 1 langkah lagi (menggerakkan primary piece) jika tidak ada yang menghalangi.
        if (!primary.getFrontCell(exit).equals(exit)) {
             heuristicValue++; 
        }
        return heuristicValue;
    }
}