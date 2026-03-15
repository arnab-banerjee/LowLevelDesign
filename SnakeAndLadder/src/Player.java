// 1. IMMUTABLE DATA CLASSES
// 'final' class prevents inheritance. 'final' fields prevent modification.
public class Player {
	private final String id;
	private int currentPos;
	
	Player(String id, int pos) {
		this.id = id;
		currentPos = pos;
	}
	public String getId() {
		return id;
	}
	public int getCurrentPos() {
		return currentPos;
	}
	public void setCurrentPos(int pos) {
		this.currentPos = pos;
	}
	public void move(int steps) {
		this.currentPos += steps;
	}
}
