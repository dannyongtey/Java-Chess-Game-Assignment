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

    private Position selectedCoordinate; 
    private int currentPlayer;
    private ArrayList<Position> possibleValidMoves = new ArrayList<Position>();
    private Chess currentChessSelected;
    private int turnCount;
    private boolean gameStatus;
    private int[] chessCheckStatus = {0, 0};

    private Board() {
        // Frame initialization
        frame = new JFrame("Myrmidon Chess");
        // TODO: Add icon to game; implement window listener to listen to window close
        final int minFrameWidth = 700;
        final int minFrameHeight = 600;
        frame.setSize(minFrameWidth,minFrameHeight);
        frame.setMinimumSize(new Dimension(minFrameWidth, minFrameHeight));
        frame.setLayout(new GridLayout(6, 7));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter(){
            // TODO: On close check if board is saved
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Closing window callback");
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
        // Button initialization
        for (int r = 0; r <= 5; r++){
            for (int c = 0; c <= 6; c++){
                buttons[r][c] = new JButton();
                buttons[r][c].setBackground(Color.WHITE);
                buttons[r][c].setName(Integer.toString(r) + "," + Integer.toString(c));
                buttons[r][c].addChangeListener(this);
                frame.add(buttons[r][c]);
            }
        }
    }   

    public void resetChess(){
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
        chessUIupdate();
    }

    public void chessUIupdate(){
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
            ImageIcon icon = new ImageIcon(c.getType() + "-" + c.getPlayer() + ".png");
            Image img = icon.getImage() ; 
            Image newimg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ; 
            ImageIcon newIcon = new ImageIcon(newimg);
            buttons[x][y].setIcon(newIcon);
        }
    }

    public void selectionUIupdate(){
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

    private void initVariable() {
        selectedCoordinate = null;
        currentPlayer = 0;
        possibleValidMoves.clear();
        currentChessSelected = null;
        turnCount = 0;
        gameStatus = true;

        selectionUIupdate();
    }

    public void flipBoard() {
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

    public void showMessage(String msg){
        JOptionPane.showMessageDialog(frame, msg);
    }

    public static Board getBoard(){
        return board;
    }

    public static void main(String[] args){
        board.showBoard();
        board.resetChess();
    }

    public void showBoard(){
        frame.setVisible(true);
    }

    public boolean isPossibleMove(Position movePos) {
        for(Position pos: possibleValidMoves) {
            if(movePos.x == pos.x && movePos.y == pos.y) {
                return true;
            }
        }
        return false;
    }

    private void nextPlayer() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        turnCount = currentPlayer == 0 ? (turnCount + 1) : turnCount;

        System.out.println("Current Player: " + currentPlayer);
        System.out.println("Turn Count: " + turnCount);
        //flipBoard();

        if((turnCount % 3) == 0 && currentPlayer == 0) {
            // swap chess
            System.out.println("swap");
            swapChess();
        }
    }

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
            }
        }
    }

    private void isChessCheck() {
        ArrayList<Position> vm = new ArrayList<Position>();
        vm = currentChessSelected.getValidMoves(map);
        System.out.println(vm);

        for(Position pos: vm) {
            if(map.get(pos) != null && map.get(pos).getType().equals("sun")) {
                chessCheckStatus[currentPlayer] = 1;

                System.out.println("Status: " + chessCheckStatus[0] + "Player: " + currentPlayer);
                System.out.println("Status: " + chessCheckStatus[1] + "Player: " + currentPlayer);
                return;
            }
        } 

        chessCheckStatus = new int[]{0, 0};
        
    }

    // Button Event Change listener callback
    public void stateChanged(ChangeEvent e){
		JButton tmp = (JButton)e.getSource();

		if (tmp.getModel().isPressed() && gameStatus){
            String btnName = tmp.getName();
            //System.out.println(btnName);
            String[] coor = btnName.split(",");
            Position tempPos = new Position(Integer.parseInt(coor[0]), Integer.parseInt(coor[1]));

            if(selectedCoordinate == null || !isPossibleMove(tempPos)) {
                Chess tempChess = map.get(tempPos);
                if(tempChess == null || tempChess.getPlayer() != currentPlayer) {
                    return;
                }
                currentChessSelected = tempChess;
                selectedCoordinate = tempPos;
                possibleValidMoves = currentChessSelected.getValidMoves(map);
            }
            else {
                map.remove(selectedCoordinate);
                map.put(tempPos, currentChessSelected);

                nextPlayer();
                chessUIupdate();
                isChessCheck();
                isGameEnd();

                selectedCoordinate = null;
                currentChessSelected = null;
                possibleValidMoves.clear();
            }

            selectionUIupdate();
		}
		
	}

    // Save map
    public void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("map.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            SaveObject so = new SaveObject(map, currentPlayer, turnCount, gameStatus);
            oos.writeObject(so);
            oos.close();
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Load map
    public void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("map.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SaveObject so;
            so = (SaveObject) ois.readObject();

            map = so.getMap();
            currentPlayer = so.getCurrentPlayer();
            turnCount = so.getTurnCount();
            gameStatus = so.getGameStatus();

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