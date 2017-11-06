package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Checkers extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final static String VERSION = "1.0";
	public static boolean gameInProgress;
	public static JPanel guiJPanel = new JPanel(new BorderLayout(2, 1));
	public static String messageText;
	public static JLabel message = new JLabel("Welcome to Checkers " + VERSION);
	private static ActionClicks actions;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;
	
	//Constructor
	private Checkers()
	{
		setFocusable(true);
		//Initialise GUI
		gui();
		//Add JFrame for Main Window
		JFrame guiJFrame = new JFrame("Checkers " + VERSION);
		guiJFrame.setSize(500, 500);
		guiJFrame.setMinimumSize(guiJFrame.getSize());
		guiJFrame.pack();
		guiJFrame.setResizable(false);
		guiJFrame.setVisible(true);
		guiJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Add GUI to JFrame
		guiJFrame.add(guiJPanel);
	}
	
	//Setup GUI
	private void gui()
	{
		repaint();
		//Create the menu bar.
		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		guiJPanel.add(menuBar, BorderLayout.PAGE_START);
		//Build the first menu.
		menu = new JMenu("Game");
		menuBar.add(menu);
		Action newGameAction = new AbstractAction("Player v Player")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				newGame("Let's begin! Black moves first.");
			}
		};
		menu.add(newGameAction);
		Action newAIGameAction = new AbstractAction("Player v Computer")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				newAIGame("Let's begin! Black moves first.");
			}
		};
		menu.add(newAIGameAction);
		Action quitGameAction = new AbstractAction("Quit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				quitGame("Game Quit. Shall we play again?");
			}
		};
		menu.add(quitGameAction);
		
		//Build second menu in the menu bar.
		menu = new JMenu("Options");
		menuBar.add(menu);
		//Undo the last move
		Action undoMoveAction = new AbstractAction("Undo   'U'")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				History.undoLastMove();
			}
		};
		menu.add(undoMoveAction);
        menu.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U,0), "U_pressed");
        menu.getActionMap().put("U_pressed", undoMoveAction);
        
		//Redo the last move
		Action redoMoveAction = new AbstractAction("Redo   'R'")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				History.redoLastMove();
			}
		};
		menu.add(redoMoveAction);
        menu.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,0), "R_pressed");
        menu.getActionMap().put("R_pressed", redoMoveAction);
        
		//Save the game
		Action saveGameAction = new AbstractAction("Save")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
		        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir") + "/history/"));
		        int returnValue = fileChooser.showSaveDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION)
		        {
					try
					{
						History.fileName = fileChooser.getSelectedFile().toString();
						History.storeHistory();
					}
					catch (FileNotFoundException ex)
					{
						ex.printStackTrace();
					}
		        }
			}
		};
		menu.add(saveGameAction);
		//Load a saved game
		Action loadGameAction = new AbstractAction("Load Saved Game")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
		        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir") + "/history/"));
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION)
		        {
		        	History.selectedFile = fileChooser.getSelectedFile();
		        	History.loadSavedGame();
		        }
			}
		};
		menu.add(loadGameAction);
		//Watch a replay of a saved game
		Action watchGameAction = new AbstractAction("Watch Replay")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
		        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir") + "/history/"));
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION)
		        {
		        	History.selectedFile = fileChooser.getSelectedFile();
					History.openHistoryThread();
		        }
			}
		};
		menu.add(watchGameAction);
		menu = new JMenu("----------");
		menuBar.add(menu);
		menu.setEnabled(false);
		menuBar.add(message);
		message.setFont (message.getFont ().deriveFont (20.0f));
		Board board = new Board();
	}
	
	//Start a new PvP game
	public static void newGame(String messageText)
	{
		if(gameInProgress)
		{
			JOptionPane.showMessageDialog(guiJPanel, "You must finish or quit this game first!");
		}
		else
		{
			ActionClicks.gameType = ActionClicks.HUMANGAME;
			System.out.println("Human Game Started.");
			message.setText(messageText);
			ActionClicks.playerTurn = ActionClicks.BLACKPLAYER;
			Piece.setupNewGame();
			ActionClicks.sourceSet = false;
			ActionClicks.resetBorderHighlights();
			ActionClicks.turnStage = ActionClicks.checkForJumps;
			gameInProgress = true;
			actions = new ActionClicks();
			History.clearLists();
		}
	}
	
	//Start a new AI Game
	private static void newAIGame(String messageText)
	{
		if(gameInProgress)
		{
			JOptionPane.showMessageDialog(guiJPanel, "You must finish or quit this game first!");
		}
		else
		{
			ActionClicks.gameType = ActionClicks.AIGAME;
			System.out.println("AI Game Started.");
			message.setText(messageText);
			ActionClicks.playerTurn = ActionClicks.BLACKPLAYER;
			Piece.setupNewGame();
			ActionClicks.sourceSet = false;
			ActionClicks.resetBorderHighlights();
			ActionClicks.turnStage = ActionClicks.checkForJumps;
			AIPlayer.getAIPieces();
			gameInProgress = true;
			actions = new ActionClicks();
			History.clearLists();
		}
	}
	
	//Quit the current game
	private static void quitGame(String messageText)
	{
		int quit = JOptionPane.showConfirmDialog(guiJPanel, "Are you sure you want to quit this game?");
		if (quit == JOptionPane.YES_OPTION)
		{
			Piece.clearPieces();
			gameInProgress = false;
			actions = new ActionClicks();
			ActionClicks.resetBorderHighlights();
			message.setText(messageText);
			History.clearLists();
		}
		if (quit == JOptionPane.NO_OPTION || quit == JOptionPane.CANCEL_OPTION)
		{
			message.setText("Let's get back to the game!");
		}
	}
	
	//Main Method
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				//Run main class Constructor
				new Checkers();
				try
				{
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				}
				catch (Exception e)
				{
				    e.printStackTrace();
				}
			}
		});
	}
}