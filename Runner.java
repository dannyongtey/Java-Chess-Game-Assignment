// Runner class to run the singleton board.
// By: Danny Tey
public class Runner {
    public static void main(String[] args){
        Board board = Board.getBoard();
        board.showBoard();
    }
}