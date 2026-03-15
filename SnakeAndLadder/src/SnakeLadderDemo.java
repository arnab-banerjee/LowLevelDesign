
public class SnakeLadderDemo {
	public static void main(String args[]) {
		Board board = BoardFactory.createRandomBoard(10, 5, 4);
		Dice dice = new Dice(1);
		Game game = new Game(board, dice);
		
		game.addPlayer(new Player("Player-1", 1));
		game.addPlayer(new Player("Player-2", 2));
		System.out.println("Starting Game... \n");
		game.startGame();
	}
}
