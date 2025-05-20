package heuristic;

import java.util.*;
import model.*;

public class BlockingHeuristic implements HeuristicFunction {

    @Override
    public int evaluate(Board board) {
        Piece primary = board.getPieces().get('P');
        List<Position> positions = primary.getPositions();
        positions.sort((a, b) -> primary.isHorizontal() ? a.col - b.col : a.row - b.row);

        Position farthest = positions.get(positions.size() - 1);
        Position exit = board.getExitPosition();

        if (primary.isHorizontal() && exit.col == board.getCols()) { // jika di exit di sebelah kanan
            int start = farthest.col + 1;
            char[] array = board.getAllRow(farthest.row); // baris di index row
            
            return countBlock(array, start, exit.col); 
        } else if (!primary.isHorizontal() && exit.row == board.getRows()) { // jika di exit di sebelah bawah
            int start = farthest.row + 1;
            char[] array = board.getAllColumn(farthest.col); // kolom di index col
            
            return countBlock(array, start, exit.row);
        } else if (primary.isHorizontal() && exit.col == -1) { // jika di exit di sebelah kiri
            int end = farthest.col - positions.size() + 1;            
            char[] array = board.getAllRow(farthest.row); // baris di index row
            
            return countBlock(array, 0, end);
        } else { // jika di exit di sebelah atas
            int end = farthest.row  - positions.size() + 1;
            char[] array = board.getAllColumn(farthest.col); // kolom di index col
            
            return countBlock(array, 0, end);
        }
    }
    
    private static int countBlock(char[] array, int start, int end) {
        Set<Character> blockingPiece = new HashSet<>();
        for (int i = start; i < end; i++) {
            char id = array[i];
            if (id != '.') {
                if (!blockingPiece.contains(id)) blockingPiece.add(id);
            }
        }
        return blockingPiece.size();
    }
}
