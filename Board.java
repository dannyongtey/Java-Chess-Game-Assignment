import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;


// Singleton design pattern that returns only the chess board
public class Board implements ChangeListener{
    private JFrame frame;
    private JButton[][] buttons = new JButton[6][7];
    private Map<String, Integer> map = new HashMap<>();
    private String selectedCoordinate = "";
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
                System.out.println("New Game clicked");
            }
        });
        loadGame = new JMenuItem(new AbstractAction("Load Game"){
        
            @Override
            public void actionPerformed(ActionEvent e) {
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
                
                ImageIcon icon = new ImageIcon("plus.png");
                Image img = icon.getImage() ; 
                Image newimg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ; 
                ImageIcon newIcon = new ImageIcon(newimg);
                buttons[r][c].setIcon(newIcon);
                frame.add(buttons[r][c]);
            }
        }
    
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

    public void showMessage(String message){
        // final JPanel glass = (JPanel)frame.getGlassPane();
        // glass.setLayout(new GridBagLayout());
        // JLabel label = new JLabel(message);
        // label.setFont(label.getFont().deriveFont(Font.BOLD, 96f));
        // glass.add(label);
        // glass.setVisible(true);
        // try
        // {
        //     final int sleepTime = 1000;
        //     Thread.sleep(sleepTime);
        // }
        // catch(InterruptedException ex)
        // {
        //     Thread.currentThread().interrupt();
        // }
        // glass.setVisible(false);
    }

    // Button Event Change listener callback
    public void stateChanged(ChangeEvent e){
		JButton tmp = (JButton)e.getSource();
		if (tmp.getModel().isPressed()){
            showMessage("Pokai");
			String btnName = tmp.getName();
			if (!btnName.equals(selectedCoordinate)){
                // If clicked on a new chess, select that chess
                if (selectedCoordinate.equals("")){
                    // If previously haven't selected
                    selectedCoordinate = btnName;
                } else {
                    // If previously has selected a coordinate
                    
                    // TODO: If I am eating my chess then do nothing, just change selection

                    // TODO: Else Code to check valid move

                        // If code is valid move, Just move it, change the map to that damn chess, switch player

                        // If code is invalid move, do nothing, show message

                        // Either way, clear selection
                        selectedCoordinate = "";

                }
                
                
            } 
		}
		
	}
}