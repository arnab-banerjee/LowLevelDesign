import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//3. THE FACTORY (Creation Logic)
public class BoardFactory {
	
	private BoardFactory() {}
	public static Board createRandomBoard(int boardSize, int numSnakes, int numLadders) {
		int totalCells = boardSize * boardSize;
	
	    // Size + 1 allows 1-based indexing
	    List<Cell> cells = new ArrayList<>(totalCells + 1);
	    for (int i = 0; i <= totalCells; i++) {
	        cells.add(new Cell());
	    }
	    placeJumps(cells, numSnakes, true, totalCells);
	    placeJumps(cells, numLadders, false, totalCells);

	    return new Board(boardSize, cells);
	}
	
	private static void placeJumps(List<Cell> cells, int count, boolean isSnake, int totalCells) {
	    int placed = 0;
	    int safetyCounter = 0; 
	    ThreadLocalRandom random = ThreadLocalRandom.current(); 

	    while (placed < count && safetyCounter < 10000) {
	        safetyCounter++;
	        int start = random.nextInt(2, totalCells); 
	        int end = random.nextInt(2, totalCells);

	        if (start == end || cells.get(start).getJump() != null) continue;
	        if (isSnake && start <= end) continue;   // Snakes go down
	        if (!isSnake && start >= end) continue;  // Ladders go up

	        cells.get(start).setJump(new Jump(start, end));
	        placed++;
	    }
	}
}