import java.io.*;

// Indicates x and y coordinate of the chess
// By: Prev Wong and Danny Tey
public class Position implements Serializable{
    public int x;
    public int y;
    Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    // Overriding equals to determine if two positions are the same
    // By: Prev Wong
    public boolean equals(Object o){
        Position p = (Position) o;
        return (p.x == this.x) && (p.y == this.y);
    }
    
    // Overriding hashCode so that a K position (a,b) is considered the same as M position (a,b).
    // By default Java compares the reference, overriding this overrides the way Java compares objects. 
    // By: Danny Tey
    @Override
    public int hashCode(){
        return (this.x * 10) + this.y;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}