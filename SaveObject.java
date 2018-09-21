import java.util.*;
import java.io.*;

// A class object that used for storing important details of the game then serialize it before saving.
// By: Yeo Yong Yaw
public class SaveObject implements Serializable{
	private Map<Position, Chess> map;
	private int currentPlayer;
	private int turnCount;
	private boolean gameStatus;

	SaveObject(Map<Position, Chess> map, int currentPlayer, int turnCount, boolean gameStatus) {
		this.map = map;
		this.currentPlayer = currentPlayer;
		this.turnCount = turnCount;
		this.gameStatus = gameStatus;
	}

	public Map<Position, Chess> getMap() {
		return this.map;
	}

	public int getCurrentPlayer() {
		return this.currentPlayer;
	}

	public int getTurnCount() {
		return this.turnCount;
	}

	public boolean getGameStatus() {
		return this.gameStatus;
	}
}

