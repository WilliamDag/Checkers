package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Checkers extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final static String VERSION = "1.0";
	public static JPanel guiJPanel = new JPanel(new BorderLayout(2, 1));
	public static String messageText;
	public static JLabel message = new JLabel("Welcome to Checkers 1.0");
	
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
		//Setup ToolBar for Main Window on GUI
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(Color.WHITE);
		guiJPanel.add(toolBar, BorderLayout.PAGE_START);
		//Add ToolBar buttons with JButtons
		Action newGameAction = new AbstractAction("New")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				newGame("Let's begin! Black moves first.");
			}
		};
		toolBar.add(newGameAction);
		toolBar.add(new JButton("Save")); //TODO - Add Functionality
		toolBar.add(new JButton("Load")); //TODO - Add Functionality
		toolBar.add(new JButton("Undo")); //TODO - Add Functionality
		Action quitGameAction = new AbstractAction("Quit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				quitGame("Game Quit. Shall we play again?");
			}
		};
		toolBar.add(quitGameAction);
		toolBar.addSeparator();
		message.setFont (message.getFont ().deriveFont (20.0f));
		toolBar.add(message);
		Board board = new Board();
	}
	
	private static void newGame(String messageText)
	{
		message.setText(messageText);

	}
	
	private static void quitGame(String messageText)
	{
		message.setText(messageText);
	}
	
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