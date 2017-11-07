package checkers;

import java.util.List;

import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class History
{
	private static List<String> historyOfMoves = new ArrayList<>();
	private static List<String> undoList = new ArrayList<>();
	public static String fileName;
	public static File selectedFile;
	private static int originXCounter = -4;
	private static int originYCounter = -3;
	private static int destinationXCounter = -2;
	private static int destinationYCounter = -1;
	private static int turnCounter = 0;
	
	public static void clearLists()
	{
		historyOfMoves.clear();
		undoList.clear();
	}
	
	public static void undoLastMove()
	{
		if(turnCounter > 0)
		{
			if(destinationYCounter > -1)
			{
				originXCounter = -4;
				originYCounter = -3;
				destinationXCounter = -2;
				destinationYCounter = -1;
			}
			int lastOriginX = Integer.parseInt(undoList.get(undoList.size() +originXCounter));
			int lastOriginY = Integer.parseInt(undoList.get(undoList.size() +originYCounter));
			int lastDestinationX = Integer.parseInt(undoList.get(undoList.size() +destinationXCounter));
			int lastDestinationY = Integer.parseInt(undoList.get(undoList.size() +destinationYCounter));
			if((lastDestinationX == lastOriginX + 2) || (lastDestinationX == lastOriginX - 2))
			{
				Checkers.message.setText("Can't undo a jump!");
			}
			else
			{
				if(ActionClicks.gameType == ActionClicks.AIGAME)
				{
					Checkers.message.setText("Can't use undo in an AI game!");
				}
				else
				{
					turnCounter--;
					Board.boardSquares[lastOriginX][lastOriginY].setIcon(Board.boardSquares[lastDestinationX][lastDestinationY].getIcon());
					Board.boardSquares[lastDestinationX][lastDestinationY].setIcon(null);
					Move.changePlayer();
					if(turnCounter > 0)
					{
						originXCounter = originXCounter - 4;
						originYCounter = originYCounter - 4;
						destinationXCounter = destinationXCounter - 4;
						destinationYCounter = destinationYCounter - 4;
					}
					historyOfMoves.remove(historyOfMoves.size() -1);
					historyOfMoves.remove(historyOfMoves.size() -1);
					historyOfMoves.remove(historyOfMoves.size() -1);
					historyOfMoves.remove(historyOfMoves.size() -1);
					historyOfMoves.remove(historyOfMoves.size() -1);
				}
			}
		}
		else
		{
			Checkers.message.setText("No move to undo!");
		}
	}
	
	public static void redoLastMove()
	{
		if(originXCounter <= -4 && !undoList.isEmpty())
		{
			int lastOriginX = Integer.parseInt(undoList.get(undoList.size() +originXCounter));
			int lastOriginY = Integer.parseInt(undoList.get(undoList.size() +originYCounter));
			int lastDestinationX = Integer.parseInt(undoList.get(undoList.size() +destinationXCounter));
			int lastDestinationY = Integer.parseInt(undoList.get(undoList.size() +destinationYCounter));
			if((lastDestinationX == lastOriginX + 2) || (lastDestinationX == lastOriginX - 2))
			{
				Checkers.message.setText("Can't redo a jump!");
			}
			else
			{
				if(ActionClicks.gameType == ActionClicks.AIGAME)
				{
					Checkers.message.setText("Can't use undo in an AI game!");
				}
				else
				{
					turnCounter++;
					Board.boardSquares[lastDestinationX][lastDestinationY].setIcon(Board.boardSquares[lastOriginX][lastOriginY].getIcon());
					Board.boardSquares[lastOriginX][lastOriginY].setIcon(null);
					historyOfMoves.add(Integer.toString(lastOriginX));
					historyOfMoves.add(Integer.toString(lastOriginY));
					historyOfMoves.add(Integer.toString(lastDestinationX));
					historyOfMoves.add(Integer.toString(lastDestinationY));
					historyOfMoves.add(System.lineSeparator());
					Move.changePlayer();
					originXCounter = originXCounter + 4;
					originYCounter = originYCounter + 4;
					destinationXCounter = destinationXCounter + 4;
					destinationYCounter = destinationYCounter + 4;
				}
			}
		}
		else
		{
			Checkers.message.setText("No move to redo!");
		}
	}
	
	public static void storeHistory() throws FileNotFoundException
	{
		PrintWriter writer = new PrintWriter(fileName + ".txt");
		for(String str: historyOfMoves)
			{
				writer.write(str);
			}
		writer.close();
	}
	
	public static void addToHistory()
	{
		historyOfMoves.add(Integer.toString(Move.originX));
		historyOfMoves.add(Integer.toString(Move.originY));
		historyOfMoves.add(Integer.toString(Move.destinationX));
		historyOfMoves.add(Integer.toString(Move.destinationY));
		historyOfMoves.add(System.lineSeparator());
		addToUndo();
	}
	
	private static void addToUndo()
	{
		try
		{
			if(destinationYCounter != -1)
			{
				undoList.subList(undoList.size() + destinationYCounter + 1, undoList.size()).clear();
				originXCounter = -4;
				originYCounter = -3;
				destinationXCounter = -2;
				destinationYCounter = -1;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		turnCounter++;
		undoList.add(Integer.toString(Move.originX));
		undoList.add(Integer.toString(Move.originY));
		undoList.add(Integer.toString(Move.destinationX));
		undoList.add(Integer.toString(Move.destinationY));
	}
	
	public static void loadSavedGame()
	{
		if(Checkers.gameInProgress)
		{
			JOptionPane.showMessageDialog(Checkers.guiJPanel, "You must finish or quit this game first!");
		}
		else
		{
			Checkers.newGame("Loading saved game.");
		    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile)))
		    {
		        String line;
		        while ((line = br.readLine()) != null)
		        {
	        		int originX = Integer.parseInt(line.substring(0,1));
	        		int originY = Integer.parseInt(line.substring(1,2));
	        		int destinationX = Integer.parseInt(line.substring(2,3));
	        		int destinationY = Integer.parseInt(line.substring(3));
	    			if((destinationX == originX + 2) || (destinationX == originX - 2))
	    			{
	        			Move.makeJump(originX, originY, destinationX, destinationY);
	        			Move.changePlayer();
	    			}
	    			else
	    			{
	        			Move.makeMove(originX, originY, destinationX, destinationY);
	        			Move.changePlayer();
	    			}
			    }
				History.clearLists();
				Checkers.gameInProgress = true;
	   		}
	   		catch (FileNotFoundException e)
	   		{
	   			e.printStackTrace();
				System.err.println("Error: File can't be read.");
			}
	    	catch (IOException e)
	    	{
	    		e.printStackTrace();
	    		System.err.println("Error: File can't be read.");
	    	}
		}
	}
	
	public static void openHistoryThread()
	{
	    new Thread(new Runnable()
	    {
			@Override
			public void run() {
				try
				{
					readFromHistory();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
	    }).start();
	}
	
	private static void readFromHistory() throws InterruptedException
	{
		Piece.setupNewGame();
		Thread.sleep(1000);
	    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile)))
	    {
	        String line;
	        while ((line = br.readLine()) != null)
	        {
        		int originX = Integer.parseInt(line.substring(0,1));
        		int originY = Integer.parseInt(line.substring(1,2));
        		int destinationX = Integer.parseInt(line.substring(2,3));
        		int destinationY = Integer.parseInt(line.substring(3));
    			if((destinationX == originX + 2) || (destinationX == originX - 2))
    			{
        			Move.makeJump(originX, originY, destinationX, destinationY);
        			ActionClicks.resetBorderHighlights();
        			Thread.sleep(1000);
    			}
    			else
    			{
        			Move.makeMove(originX, originY, destinationX, destinationY);
        			ActionClicks.resetBorderHighlights();
        			Thread.sleep(1000);
    			}
		    }
   		}
   		catch (FileNotFoundException e)
   		{
   			e.printStackTrace();
			System.err.println("Error: File can't be read.");
		}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    		System.err.println("Error: File can't be read.");
    	}
	}
}
