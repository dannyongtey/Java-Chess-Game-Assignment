import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;


// Singleton design pattern that returns only the chess board
public class Board implements ChangeListener{
    private JFrame frame;
    private JButton[][] buttons = new JButton[6][7];
    private Map<Position, Chess> map = new HashMap<>();
    private static Board board = new Board();
    private Position selectedCoordinate; // the chess coordinate player selected to be moved
    private int currentPlayer; // indicate which player in turn to play
    private ArrayList<Position> possibleValidMoves = new ArrayList<Position>(); // all possible moves of currently selected chess
    private Chess currentChessSelected; // chess currently selected
    private int turnCount; // number of turns have played
    private boolean gameStatus; // game still playable (false when sun is captured)
    private int[] chessCheckStatus = {0, 0}; // whether player 0 or player 1 is being checked
    private boolean saveStatus = true; // indicates whether game has been saved
    private JLabel topMessage; // JLabel for top of the screen message
    private String message = "";

    // Private constructor To initialize the UI
    // By: Danny Tey
    private Board() {  
        // Main Frame initialization
        final int minFrameWidth = 700;
        final int minFrameHeight = 600;
        frame = new JFrame("Myrmidon Chess");  
        frame.setSize(minFrameWidth,minFrameHeight);
        frame.setMinimumSize(new Dimension(minFrameWidth, minFrameHeight));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Closing window callback");

                if(saveStatus == false && gameStatus) {
                    showMessage("Please save before leaving");
                }
                else {
                    System.exit(0);
                }
            }
        });;
        // Menubar initialization
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem newGame, loadGame, saveGame;
        menuBar = new JMenuBar();
        menu = new JMenu("Game");
        newGame = new JMenuItem(new AbstractAction("New Game"){
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game is still playable and not yet saved
                if(gameStatus && !saveStatus) {
                    showMessage("Please save the game before you start a new game");
                    return;
                }

                initVariable();
                resetChess();
            }
        });
        saveGame = new JMenuItem(new AbstractAction("Save Game"){
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
        loadGame = new JMenuItem(new AbstractAction("Load Game"){
            @Override
            public void actionPerformed(ActionEvent e) {
                initVariable();
                loadGame();
                chessUIupdate();
            }
        });
        menu.add(newGame);
        menu.add(saveGame);
        menu.add(loadGame);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        JPanel chessPanel = new JPanel();
        chessPanel.setLayout(new GridLayout(6,7));
        // Button initialization
        for (int r = 0; r <= 5; r++){
            for (int c = 0; c <= 6; c++){
                buttons[r][c] = new JButton();
                buttons[r][c].setBackground(Color.WHITE);
                buttons[r][c].setName(Integer.toString(r) + "," + Integer.toString(c));
                buttons[r][c].addChangeListener(this);
                chessPanel.add(buttons[r][c]);
            }
        }
        frame.add(chessPanel, BorderLayout.CENTER);

        // Top level message initialization
        message = "READY (CREATE NEW GAME OR LOAD GAME)";
        topMessage = new JLabel(message);
        topMessage.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(topMessage, BorderLayout.PAGE_START);

    }   

    // Put all chessses into the board map in their initial position
    // By: Danny Tey
    private void resetChess(){
        map = new HashMap<Position, Chess>();
        map.put(new Position(0,0), new Chess(1, "plus"));
        map.put(new Position(0,1), new Chess(1, "triangle"));
        map.put(new Position(0,2), new Chess(1, "chevron"));
        map.put(new Position(0,3), new Chess(1, "sun"));
        map.put(new Position(0,4), new Chess(1, "chevron"));
        map.put(new Position(0,5), new Chess(1, "triangle"));
        map.put(new Position(0,6), new Chess(1, "plus"));
        map.put(new Position(5,0), new Chess(0, "plus"));
        map.put(new Position(5,1), new Chess(0, "triangle"));
        map.put(new Position(5,2), new Chess(0, "chevron"));
        map.put(new Position(5,3), new Chess(0, "sun"));
        map.put(new Position(5,4), new Chess(0, "chevron"));
        map.put(new Position(5,5), new Chess(0, "triangle"));
        map.put(new Position(5,6), new Chess(0, "plus"));
        message = "Player 1's turn";
        chessUIupdate();
    }

    // Update the chess location on chess board based on map, and also the top level message
    // By: Danny Tey
    private void chessUIupdate(){
        topMessage.setText(message);
        for (int r = 0; r <= 5; r++){
            for (int c = 0; c <= 6; c++){
                buttons[r][c].setIcon(null);
            }
        }
        for (Map.Entry<Position, Chess> entry : map.entrySet()){
            Position pos = entry.getKey();
            Chess c = entry.getValue();
            int x = pos.x;
            int y = pos.y;
            ImageIcon icon = new ImageIcon("chessImage/" + c.getType() + "-" + c.getPlayer() + ".png");
            Image img = icon.getImage() ; 
            Image newimg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ; 
            ImageIcon newIcon = new ImageIcon(newimg);
            buttons[x][y].setIcon(newIcon);
        }
    }

    // Update background color of selected chess as blue and yellow for possible moves
    // By: Danny Tey and Yeo Yong Yaw
    private void selectionUIupdate(){
        for (int r = 0; r <= 5; r++){
            for (int c = 0; c <= 6; c++){
                buttons[r][c].setBackground(Color.WHITE);
            }
        }
        System.out.println("UI selection update'");
        if(selectedCoordinate != null){
            int x = selectedCoordinate.x;
            int y = selectedCoordinate.y;
            buttons[x][y].setBackground(new Color(7,149,240));
            System.out.println("Updated UI");
        }     

        for(Position pos: possibleValidMoves) {
            buttons[pos.x][pos.y].setBackground(new Color(250, 187, 2));
        }

        System.out.println("----------------");
    }

    // Reset all variable 
    // By: Yeo Yong Yaw
    private void initVariable() {
        selectedCoordinate = null;
        currentPlayer = 0;
        possibleValidMoves.clear();
        currentChessSelected = null;
        turnCount = 0;
        gameStatus = true;

        selectionUIupdate();
    }   

    // Turn the board position when switching turn
    // By: Danny Tey
    private void flipBoard() {
        Map<Position, Chess> temp = map;
        map = new HashMap<Position, Chess>();
        for (Map.Entry<Position, Chess> entry : temp.entrySet()){
            Position pos = entry.getKey();
            Chess c = entry.getValue();
            int x = 5 - pos.x;
            int y = 6 - pos.y;
            map.put(new Position(x,y), c);
        }
        chessUIupdate();
    }

    // Pop up a dialog box
    // By: Danny Tey
    private void showMessage(String msg){
        JOptionPane.showMessageDialog(frame, msg);
    }

    // Return the Board (Singleton)
    // By: Danny Tey
    public static Board getBoard(){
        return board;
    }

    // By: Danny Tey
    public void showBoard(){
        frame.setVisible(true);
    }

    // Check if the move is in the array list of possibleValidMoves
    // By: Yeo Yong Yaw
    private boolean isPossibleMove(Position movePos) {
        for(Position pos: possibleValidMoves) {
            if(movePos.x == pos.x && movePos.y == pos.y) {
                return true;
            }
        }
        return false;
    }

    // Change game state for next player
    // By: Yeo Yong Yaw
    private void nextPlayer() {
        
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        turnCount = currentPlayer == 0 ? (turnCount + 1) : turnCount;

        System.out.println("Current Player: " + currentPlayer);
        System.out.println("Turn Count: " + turnCount);
        flipBoard();
        if((turnCount % 3) == 0 && currentPlayer == 0) {
            // swap chess
            swapChess();
        }
        message = "Player " + (currentPlayer+1) + "'s turn";
    }

    // Swap 3 types of chess after each 3 rounds
    // By: Yeo Yong Yaw
    private void swapChess() {
        for(Map.Entry<Position, Chess> entry: map.entrySet()) {
            Chess tempChess = entry.getValue();
            switch(tempChess.getType()) {
                case "plus" :
                    tempChess.setType("triangle");
                    break;

                case "triangle" :
                    tempChess.setType("chevron");
                    break;

                case "chevron" :
                    tempChess.setType("plus");
                    break;

                default:
                    break;
            }
        }
    }

    // Check if game has ended by checking existance of "sun" chess in the map
    // By: Yeo Yong Yaw
    private void isGameEnd() {
        int[] status = {0, 0};
        Chess tempChess;
        for(Map.Entry<Position, Chess> entry: map.entrySet()) {
            tempChess = entry.getValue();
            if(tempChess.getType().equals("sun")) {
                status[tempChess.getPlayer()] = 1;
            }
        }

        for(int i=0; i<status.length; i++) {
            if(status[i] == 0) {
                int player = i == 0 ? 2 : 1;
                showMessage("Player " + player + " win!");
                gameStatus = false;
                message = "Player " + player + " win!";
                chessUIupdate();
            }
        }
    }

    // Check if current player move will check his/her opponent
    // By: Yeo Yong Yaw
    private void isChessCheck() {
        
        ArrayList<Position> vm = new ArrayList<Position>();
        vm = currentChessSelected.getValidMoves(map);

        for(Position pos: vm) {
            if(map.get(pos) != null && map.get(pos).getType().equals("sun")) {
                chessCheckStatus[currentPlayer] = 1;
                System.out.println("Check");
                message += " (CHECK)"; 
                return;
            }
        } 

        chessCheckStatus = new int[]{0, 0};
    }

    // Button Event Change listener callback
    public void stateChanged(ChangeEvent e){
		JButton tmp = (JButton)e.getSource();

        // If each chess is being pressed and the game is still playable (no one has won)
		if (tmp.getModel().isPressed() && gameStatus){
            String btnName = tmp.getName();
            String[] coor = btnName.split(",");
            Position tempPos = new Position(Integer.parseInt(coor[0]), Integer.parseInt(coor[1]));

            // If previously have not selected one chess to be moved or the player changed his mind to choose another chess to be moved
            if(selectedCoordinate == null || !isPossibleMove(tempPos)) {
                Chess tempChess = map.get(tempPos);
                if(tempChess == null || tempChess.getPlayer() != currentPlayer) {
                    return;
                }
                currentChessSelected = tempChess;
                selectedCoordinate = tempPos;
                possibleValidMoves = currentChessSelected.getValidMoves(map);
            }
            // Selected a move coordinate
            else {
                map.remove(selectedCoordinate); // remove current chess from map
                map.put(tempPos, currentChessSelected); // put the selected chess into desire location

                nextPlayer();
                isChessCheck();
                chessUIupdate();
                isGameEnd();

                selectedCoordinate = null;
                currentChessSelected = null;
                possibleValidMoves.clear(); // clear all possible moves of previous chess
            }

            saveStatus = false;
            selectionUIupdate();
		}
		
	}

    // Save the map, which player turns, total turn counts and game status (whether game has ended)
    // By: Yeo Yong Yaw
    private void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("map.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            SaveObject so = new SaveObject(map, currentPlayer, turnCount, gameStatus);
            oos.writeObject(so);
            oos.close();
            fos.close();

            saveStatus = true;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Load the map, which player turns, total turn counts and game status
    // By: Yeo Yong Yaw
    private void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("map.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SaveObject so;
            so = (SaveObject) ois.readObject();

            map = so.getMap();
            currentPlayer = so.getCurrentPlayer();
            turnCount = so.getTurnCount();
            gameStatus = so.getGameStatus();
            saveStatus = true;

            ois.close();
            fis.close();
        }
        catch(IOException ie) {
            ie.printStackTrace();
            return;
        }
        catch(ClassNotFoundException ce) {
            System.out.println("Class not found");
            return;
        }
    }
}
