import java.util.LinkedList;
import java.util.Objects;
import java.util.Deque;


public class Game {
	private final Board board;
	private final Dice dice;
	private final Deque<Player> players;
	Player winner;
	
	public Game(Board board, Dice dice) {
		this.board = Objects.requireNonNull(board, "Board cannot be null");
        this.dice = Objects.requireNonNull(dice, "Dice cannot be null");
        this.players = new LinkedList<>();
        this.winner = null;	
    }
	public void addPlayer(Player player) {
		players.add(Objects.requireNonNull(player));
	}
	public void startGame() {
		if (players.size() < 2) {
			throw new IllegalStateException("At least 2 players are required to start.");
		}
		while (winner == null) {
			Player playerTurn = findPlayerTurn();
			
			System.out.println("Current turn: " + playerTurn.getId() + " pos: " 
						+ playerTurn.getCurrentPos());
			int steps = dice.rollDice();
			playerTurn.move(steps);
			int resolvedPosition = jumpCheck(playerTurn.getCurrentPos());
			playerTurn.setCurrentPos(resolvedPosition);
	
            System.out.println("Player turn:" + playerTurn.getId() + " new Position is: " + playerTurn.getCurrentPos());
            //check for winning condition
            if (playerTurn.getCurrentPos() >= board.getWinningPosition()) {
                winner = playerTurn;
                playerTurn.setCurrentPos(board.getWinningPosition()); // to handle exceed size case
            }
		}
        System.out.println("\n===> The Winner is:" + winner.getId());
	}
	
	int jumpCheck(int playerNewPos) {
		if (playerNewPos >= board.getWinningPosition()) {
			return playerNewPos;
		}
		Cell cell = board.getCell(playerNewPos);
		Jump jump = cell.getJump();
		
		if (jump != null) {
            String jumpBy = (jump.getStart() < jump.getEnd()) ? "Ladder" : "Snake";
            System.out.println("[+] Jump done by: " + jumpBy);
			return jump.getEnd();
		}
		return playerNewPos;
	}
	
	Player findPlayerTurn() {
		Player playerChance = players.removeFirst();
		players.addLast(playerChance);
		return playerChance;
	}
}
