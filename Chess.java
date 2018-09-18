import java.util.*;
import java.lang.*;

public class Chess {
    private int player;
    private String type;
    // private Position position;
    Chess(int player, String type){
        this.player = player;
        this.type = type;
        // this.position = new Position(x, y);
    }

    public boolean isMoveValid(Position position, Position move ){
        boolean valid = false;
        int maxSteps = type == "sun" ? 1 : 2;
        int x = position.x;
        int y = position.y;

        switch(type){
            case "sun" : 
            case "plus" : {
                if ((move.x == x && Math.abs(move.y - y) <= maxSteps) || (move.y == y && Math.abs(move.x - x) <= maxSteps) ) {
                    valid = true;
                }
                if ( type == "plus" ) break;
            }
            case "triangle" : {
                if ( Math.abs(move.x - x) <= maxSteps && Math.abs(move.y - y) <= maxSteps ) {
                    valid = true;
                }
                break;
            }
            case "chevron" : {
                if ( (Math.abs(move.x - x) == 1 && Math.abs(move.y - y) == 2) || (Math.abs(move.x - x) == 2 && Math.abs(move.y - y) == 1) ) {
                    valid = true;
                }
                break;
            }
            default:  {
                break;
            }
        }

        return valid;
    }
    public int getPlayer(){
        return this.player;
    }
    public String getType(){
        return this.type;
    }
}