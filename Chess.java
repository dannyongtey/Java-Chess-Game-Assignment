import java.util.*;
import java.lang.*;
import java.io.*;

public class Chess implements Serializable{
    private int player;
    private String type;
    // private Position position;
    Chess(int player, String type){
        this.player = player;
        this.type = type;
        // this.position = new Position(x, y);
    }

    public ArrayList<Position> getValidMoves(Map<Position, Chess> map) {
        ArrayList<Position> validMove = new ArrayList<Position>();
        ArrayList<Position> possiblePositionList = new ArrayList<Position>();
        Position chessPosition = null;

        int[] possibleXMove;
        int[] possibleYMove;
        int stepCount;
        final int DIRECTION_COUNT = 2; // positive: 1 and negative: -1
        final int MAX_STEP_COUNT = 2; // maximum step 
        final int FORMULA_TYPE_COUNT = 2; // addig to x or adding to y

        for(Map.Entry<Position, Chess> entry: map.entrySet()) {
            if(this == entry.getValue()) {
                chessPosition = entry.getKey();
            }
        }

        switch(this.type) {
            case "sun" : 
                possibleXMove = new int[]{-1, 0, 1};
                possibleYMove = new int[]{-1, 0, 1};

                for(int i=0; i<possibleXMove.length; i++) {
                    for(int j=0; j<possibleYMove.length; j++) {
                        Position possiblePosition = new Position(chessPosition.x + possibleXMove[i], chessPosition.y + possibleYMove[j]);

                        possiblePositionList.add(possiblePosition);
                    }
                }
                break;

            case "plus" : 
                int direction = 1; // go positive, 1 or go negative, -1
                stepCount = 1; // go 1 step or 2 step
                
                // formulaType 0 refers as x move and formulaType 1 refers as y move
                for(int formulaType=0; formulaType<FORMULA_TYPE_COUNT; formulaType++) {
                    for(int i=0; i<DIRECTION_COUNT; i++) {
                        for(int j=0; j<MAX_STEP_COUNT; j++) {
                            Position possiblePosition = null;
                            if(formulaType == 0) {
                                possiblePosition = new Position(chessPosition.x + (direction * stepCount), chessPosition.y);
                            }
                            else {
                                possiblePosition = new Position(chessPosition.x, chessPosition.y + (direction * stepCount));
                            }

                            if(map.get(possiblePosition) == null || map.get(possiblePosition).getPlayer() != this.player) {
                                possiblePositionList.add(possiblePosition);

                                if(map.get(possiblePosition) != null) {
                                    break;
                                }

                                stepCount++;
                            }
                            else {
                                break;
                            }
                        }
                        stepCount = 1;
                        direction = direction * (-1);
                    }
                }
                break;

            case "triangle" :
                int directionX = 1; // x go positive, 1 or go negative, -1
                int directionY = 1; // y go positive, 1 or go negative, -1
                stepCount = 1; // go 1 step or 2 step

                // loop for directionX
                for(int i=0; i<DIRECTION_COUNT; i++) {
                    //loop for directionY
                    for(int j=0; j<DIRECTION_COUNT; j++) {
                        // loop for how many step to go
                        for(int k=0; k<MAX_STEP_COUNT; k++) {
                            Position possiblePosition = new Position(chessPosition.x + (directionX * stepCount), chessPosition.y + (directionY * stepCount));

                            if(map.get(possiblePosition) == null || map.get(possiblePosition).getPlayer() != this.player) {
                                possiblePositionList.add(possiblePosition);

                                if(map.get(possiblePosition) != null) {
                                    break;
                                }

                                stepCount++;
                            }
                            else {
                                break;
                            }
                        }
                        stepCount = 1;
                        directionY = directionY * (-1);
                    }
                    stepCount = 1;
                    directionX = directionX * (-1);
                }

                break; 

            case "chevron" :
                possibleXMove = new int[]{2, -2};
                possibleYMove = new int[]{2, -2};

                int moveLeftRight = 1;

                // x move 2 steps first
                for(int i=0; i<possibleXMove.length; i++) {
                    for(int j=0; j<DIRECTION_COUNT; j++) {
                        Position possiblePosition = new Position(chessPosition.x + possibleXMove[i], chessPosition.y + moveLeftRight);
                        possiblePositionList.add(possiblePosition);

                        moveLeftRight = moveLeftRight * -1;
                    }
                };

                // y move 2 steps first
                for(int i=0; i<possibleYMove.length; i++) {
                    for(int j=0; j<DIRECTION_COUNT; j++) {
                        Position possiblePosition = new Position(chessPosition.x + moveLeftRight, chessPosition.y + possibleYMove[i]);
                        possiblePositionList.add(possiblePosition);

                        moveLeftRight = moveLeftRight * -1;
                    }
                };
                break; 
            
            default: 
                break;
        }

        for(Position pos: possiblePositionList) {
            // Check within grid position
            if(pos.x >= 0 && pos.x <= 5 && pos.y >= 0 && pos.y <= 6) {
                Chess tempChess = map.get(pos);
                // if no chess in that grid or opponent chess is in that grid
                if(tempChess == null || this.player != tempChess.player ) {
                    validMove.add(pos);
                };
            }
        };

        return validMove;
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

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Chess: " + this.type + " Player: " + this.player;
    }
}