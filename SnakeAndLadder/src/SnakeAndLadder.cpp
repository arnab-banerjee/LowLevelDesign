#include <iostream>
#include <vector>
#include <deque>
#include <cstdlib>
#include <ctime>
#include <memory>

using namespace std;

class Dice {
    int diceCount;

    public:
    Dice(int d) {
        diceCount = d;
    }
    int rollDice() {
        int total = 0;
        int currentRoll = 0;
        while (currentRoll < diceCount) {
            total += (rand() % 6 + 1);
            currentRoll++;
        }
        return total;
    }
};

class Player {
    int id;
    int currentPos;
    public:
    Player(int id, int currentPos) {
        this->id = id;
        this->currentPos = currentPos;
    }
    int getID() const {return id;}
    int getCurrentPos() const {return currentPos;}
    void setPos(int pos) {currentPos = pos;}
    void incrementPos(int pos) {currentPos += pos;}
};

class Jump {
    int start;
    int end;
    public:
    Jump(int x,int y) : start(x), end(y) {}
    int getStart() const {return start;}
    int getEnd() const {return end;}
};
class Cell {
    public:
    unique_ptr<Jump> jump = nullptr;
};

class Board {
    vector<Cell> cells;
    int size;

    public:
    const Cell& getCell(int pos_head) const {
        return cells[pos_head];
    }
    int getSize() const {
        return size;
    }
    Board(int boardsize, vector<Cell> builtCells)
        : size(boardsize), cells(std::move(builtCells)) {}
};

class BoardFactory {
public:
    static unique_ptr<Board> createRandomBoard(int size, int numSnakes, int numLadders) {
        int total_size = size * size;
        vector<Cell> cell;
        for (int i = 0; i < total_size; ++i) {
            cell.emplace_back();
        }
        int safetycounter = 0;
        while (numSnakes > 0 && safetycounter < 10000) {
            safetycounter++;
            int pos_head = 1 + rand() % (size * size - 2);
            int pos_tail = 1 + rand() % (size * size - 2);

            if (pos_head <= pos_tail) continue;

            Cell& c = cell[pos_head];
            if (c.jump != nullptr) continue;
            c.jump = make_unique<Jump>(pos_head, pos_tail);
            numSnakes--;
        }
        safetycounter = 0;
        while (numLadders > 0 && safetycounter < 10000) {
            safetycounter++;
            int pos_head = 1 + rand() % (size * size - 2);
            int pos_tail = 1 + rand() % (size * size - 2);
            
            if (pos_head >= pos_tail) continue;

            Cell& c = cell[pos_head];
            if (c.jump != nullptr) continue;
            c.jump =  make_unique<Jump>(pos_head, pos_tail);
            numLadders--;
        } 
        return make_unique<Board>(size, move(cell));     
    }
};

class Game {
    unique_ptr<Board> board;
    unique_ptr<Dice> dice;
    deque<Player> list;
    Player *winner;

    void intializeGame() {
        winner = nullptr;
        addPlayers();
    }
    void addPlayers() {
        Player player1(1, 0), player2(2, 0);
        list.push_back(player1);
        list.push_back(player2);
    }
    Player& findPlayerTurn() {
        list.push_back(list.front());
        list.pop_front();
        return list.back();
    }
    int jumpCheck(int pos) const {
        if (pos >= board->getSize() * board->getSize() - 1) {
            return pos;
        }
        const Cell& c = board->getCell(pos);
        if (c.jump != nullptr) {
            string s = (c.jump->getStart() > c.jump->getEnd()) ? "snake" : "ladder";
            cout << s << " pos: " << c.jump->getStart() << " " << c.jump->getEnd() << "\n"; 
            return c.jump->getEnd();
        }
        return pos;
    }
    public:
    // The Game asks for a Board in its constructor
    Game(unique_ptr<Board> gameBoard, unique_ptr<Dice> diceRoll) 
            : board(std::move(gameBoard)), dice(std::move(diceRoll)) {
        // initialize players and dice here...
        intializeGame();
    }
    void startGame() {
        while (winner == nullptr) {
            Player& playerTurn = findPlayerTurn();
            cout << "Player: " << playerTurn.getID() << " current pos " << playerTurn.getCurrentPos() << "\n";
            int diceThrow = dice->rollDice();
            playerTurn.incrementPos(diceThrow);
            playerTurn.setPos(jumpCheck(playerTurn.getCurrentPos()));
            cout << "Player: " << playerTurn.getID() << " new pos " << playerTurn.getCurrentPos() << "\n";
            if (playerTurn.getCurrentPos() > board->getSize() * board->getSize() - 1) {
                winner = &playerTurn;
                cout << "winner : " << winner->getID() << "\n";
            }
        }
    }
};

int main() {
    srand((unsigned int)time(NULL)); // Seed randomness once at the start of the app
    // 1. The Factory does the messy creation work
    unique_ptr<Board> myBoard = BoardFactory::createRandomBoard(10, 5, 4);
    unique_ptr<Dice> myDice = make_unique<Dice>(1);
    // 2. We inject the clean board into the Game
    Game game(std::move(myBoard), std::move(myDice));
    game.startGame();
    return 0;
}
