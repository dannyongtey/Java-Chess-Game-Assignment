import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;


// Singleton design pattern that returns only the chess board
public class Board implements ChangeListener{
    private JFrame frame;
    private JButton[][] buttons = new JButton[6][7];
    private Map<Position, Chess> map = new HashMap<>();
    private Position selectedCoordinate = new Position(99,99); // Initialize with dummy coordinates
    private String currentPlayer = "";
    private static Board board = new Board();

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
        JMenuItem newGame, loadGame;
        menuBar = new JMenuBar();
        menu = new JMenu("Game");
        newGame = new JMenuItem(new AbstractAction("New Game"){
            @Override
            public void actionPerformed(ActionEvent e) {
                resetChess();
                System.out.println("New Game clicked");
            }
        });
        loadGame = new JMenuItem(new AbstractAction("Load Game"){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                flipBoard();
                System.out.println("Load game clicked");
            }
        });
        menu.add(newGame);
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
            System.out.println(x);
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
        int x = selectedCoordinate.x;
        int y = selectedCoordinate.y;
        if (x != 99 && y != 99){
            buttons[x][y].setBackground(Color.BLUE);
            System.out.println("Updated UI");
        }     
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
            System.out.println("Goign");
        }
        System.out.println(map);
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
    }

    public void showBoard(){
        frame.setVisible(true);
    }

    // Button Event Change listener callback
    public void stateChanged(ChangeEvent e){
		JButton tmp = (JButton)e.getSource();
		if (tmp.getModel().isPressed()){
            String btnName = tmp.getName();
            String[] coor = btnName.split(",");
            Position tempPos = new Position(Integer.parseInt(coor[0]), Integer.parseInt(coor[1]));
			if (!selectedCoordinate.equals(tempPos)){
                // If clicked on a new chess, select that chess
                if (selectedCoordinate.equals(new Position(99,99))){
                    // If previously haven't selected
                    
                } else {
                    // If previously has selected a coordinate
                    
                    // TODO: If I am eating my chess then do nothing, just change selection
                    // Chess oldChess = map.get(selectedCoordinate);
                    // Chess newChess = map.get(tempPos);
                    


                        
                        

                }
                
                
            } 
            selectionUIupdate();
		}
		
	}
}