import java.util.Collections;
import java.util.List;

//2. THE BOARD: A strictly 1D Data Container (SRP)
public final class Board {
	private final int size;
    private final int winningPosition;
    private final List<Cell> cells;
	
	Board(int boardSize, List<Cell> cells) {
		this.size = boardSize;
	    this.winningPosition = size * size;
	    // Defensive copying ensures the Game can't accidentally alter the list
	    this.cells = Collections.unmodifiableList(cells);
	}

	// Law of Demeter Fix: Game asks the Board for the winning position, 
    // rather than calculating board.cells.length * board.cells.length
    public int getWinningPosition() { return winningPosition; }

    public Cell getCell(int position) {
        if (position < 1 || position > winningPosition) {
            throw new IllegalArgumentException("Invalid board position: " + position);
        }
        return cells.get(position);
    }
}
