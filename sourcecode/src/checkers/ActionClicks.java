package checkers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ActionClicks implements ActionListener
{
	private static Border normalBorder = LineBorder.createGrayLineBorder();
	public static boolean sourceSet = false;
	public static int playerTurn;
	public final static int BLACKPLAYER = 1;
	public final static int REDPLAYER = 2;
	public static int gameType;
	public final static int HUMANGAME = 1;
	public final static int AIGAME = 2;
	public static int turnStage;
	public static int checkForJumps = 1;
	public static int checkForDoubleJumps = 2;
	public static int checkForMoves = 3;

	//Add ActionListener
	public ActionClicks()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				Board.boardSquares[j][i].addActionListener(this);
			}
		}
	}
	
	//Method to remove the possible move highlights from the board
	public static void resetBorderHighlights()
	{
		for (int i = 0; i < Board.boardSquares.length; i++)
		{
			for (int j = 0; j < Board.boardSquares[i].length; j++)
			{
				Board.boardSquares[j][i].setBorder(normalBorder);
			}
		}
	}

	//Make the AI move after 1 second
	private static void waitThenMove()
	{
	    new Thread(new Runnable()
	    {
			@Override
			public void run() {
				try
				{
					Thread.sleep(1000);
					AIPlayer.makeAIMove();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
	    }).start();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(gameType == HUMANGAME)
		{
			if(turnStage == checkForJumps) //Check for any jumps at the start of a move and highlight them, making the available move squares active.
			{
				if(Move.highlightJumps(playerTurn))
				{
					Checkers.message.setText("You must jump!");
					if(!sourceSet) //If no piece has been selected, get the user to select one.
					{
						JButton btn = (JButton) e.getSource(); //Get X & Y coordinates from the ActionListener source "Click".
						Move.originX = (Integer) btn.getClientProperty("column");
						Move.originY = (Integer) btn.getClientProperty("row");
						if(Move.whosPiece(playerTurn, Move.originX, Move.originY)) //Check the piece clicked on can be moved by the current player.
						{
							Move.clickAPiece(); //Set the X & Y origin coords to the Move variables and set "sourceSet" to TRUE.
						}
					}
					else //If a source has been set, have the next click set the destination coords.
					{
						JButton btn = (JButton) e.getSource();
						Move.destinationX = (Integer) btn.getClientProperty("column");
						Move.destinationY = (Integer) btn.getClientProperty("row");
						if(Move.makeJump(Move.originX, Move.originY, Move.destinationX, Move.destinationY)) //Make the jump
						{
							History.addToHistory();
							turnStage = checkForDoubleJumps;
						}
						else //If the destination clicked on wasn't valid, reset the move coordinates.
						{
							Checkers.message.setText("Invalid move.");
							sourceSet = false;
						}
					}
				}
				else
				{
					turnStage = checkForMoves;
				}
			}
			if(turnStage == checkForDoubleJumps)
			{
				if(Move.possibleDoubleJumps > 0)
				{
					Checkers.message.setText("You must keep jumping!");
					JButton btn = (JButton) e.getSource();
					Move.destinationX = (Integer) btn.getClientProperty("column");
					Move.destinationY = (Integer) btn.getClientProperty("row");
					if(Move.makeJump(Move.originX, Move.originY, Move.destinationX, Move.destinationY)) //Make the jump
					{
						History.addToHistory();
						Move.possibleDoubleJumps = 0;
						if(!Move.highlightDoubleJumps(playerTurn))
						{
							resetBorderHighlights();
							Move.changePlayer();
							turnStage = checkForMoves;
						}
					}
					else //If the destination clicked on wasn't valid, reset the move coordinates.
					{
						//resetBorderHighlights();
						Checkers.message.setText("Invalid move.");
					}
				}
				else
				{
					Move.changePlayer();
					turnStage = checkForJumps;
				}
			}
			if(turnStage == checkForMoves)
			{
				if(Move.highlightMoves(playerTurn)) //Highlight any possible moves for current player
				{
					if(!sourceSet) //Get X & Y coordinates from the ActionListener source "Click".
					{
						JButton btn = (JButton) e.getSource();
						Move.originX = (Integer) btn.getClientProperty("column");
						Move.originY = (Integer) btn.getClientProperty("row");
						if(Move.whosPiece(playerTurn, Move.originX, Move.originY))
						{
							Move.clickAPiece();
						}
					}
					else //If source is set then get the destination coords on next click.
					{
						JButton btn = (JButton) e.getSource();
						Move.destinationX = (Integer) btn.getClientProperty("column");
						Move.destinationY = (Integer) btn.getClientProperty("row");
						if(Move.canMove(playerTurn,Move.originX,Move.originY,Move.destinationX,Move.destinationY)) //If the piece can move
						{
							Move.makeMove(Move.originX, Move.originY, Move.destinationX, Move.destinationY); //Then make move
							History.addToHistory();
							Move.changePlayer(); //Change players
							resetBorderHighlights(); //Remove the highlights from the board
							turnStage = checkForJumps;
						}
						else //If the destination clicked on wasn't valid, reset the move coordinates.
						{
							Checkers.message.setText("Invalid move.");
							sourceSet = false;
						}
					}
				}
			}
		}
		if(gameType == AIGAME)
		{
			if(playerTurn == BLACKPLAYER)
			{
				if(turnStage == checkForJumps) //Check for any jumps at the start of a move and highlight them, making the available move squares active.
				{
					if(Move.highlightJumps(BLACKPLAYER))
					{
						Checkers.message.setText("You must jump!");
						if(!sourceSet) //If no piece has been selected, get the user to select one.
						{
							JButton btn = (JButton) e.getSource(); //Get X & Y coordinates from the ActionListener source "Click".
							Move.originX = (Integer) btn.getClientProperty("column");
							Move.originY = (Integer) btn.getClientProperty("row");
							if(Move.whosPiece(BLACKPLAYER, Move.originX, Move.originY)) //Check the piece clicked on can be moved by the current player.
							{
								Move.clickAPiece(); //Set the X & Y origin coords to the Move variables and set "sourceSet" to TRUE.
							}
						}
						else //If a source has been set, have the next click set the destination coords.
						{
							JButton btn = (JButton) e.getSource();
							Move.destinationX = (Integer) btn.getClientProperty("column");
							Move.destinationY = (Integer) btn.getClientProperty("row");
							if(Move.makeJump(Move.originX, Move.originY, Move.destinationX, Move.destinationY)) //Make the jump
							{
								History.addToHistory();
								turnStage = checkForDoubleJumps;
							}
							else //If the destination clicked on wasn't valid, reset the move coordinates.
							{
								Checkers.message.setText("Invalid move.");
								sourceSet = false;
							}
						}
					}
					else
					{
						turnStage = checkForMoves;
					}
				}
				if(turnStage == checkForDoubleJumps)
				{
					if(Move.possibleDoubleJumps > 0)
					{
						Checkers.message.setText("You must keep jumping!");
						JButton btn = (JButton) e.getSource();
						Move.destinationX = (Integer) btn.getClientProperty("column");
						Move.destinationY = (Integer) btn.getClientProperty("row");
						if(Move.makeJump(Move.originX, Move.originY, Move.destinationX, Move.destinationY)) //Make the jump
						{
							History.addToHistory();
							Move.possibleDoubleJumps = 0;
							if(!Move.highlightDoubleJumps(playerTurn))
							{
								resetBorderHighlights();
								Move.changePlayer();
								AIPlayer.getRandomPieceDest();
								waitThenMove();
								turnStage = checkForMoves;
							}
						}
						else //If the destination clicked on wasn't valid, reset the move coordinates.
						{
							//resetBorderHighlights();
							Checkers.message.setText("Invalid move.");
						}
					}
					else
					{
						Move.changePlayer();
						AIPlayer.getRandomPieceDest();
						waitThenMove();
						turnStage = checkForJumps;
					}
				}
				if(turnStage == checkForMoves)
				{
					if(Move.highlightMoves(playerTurn)) //Highlight any possible moves for current player
					{
						if(!sourceSet) //Get X & Y coordinates from the ActionListener source "Click".
						{
							JButton btn = (JButton) e.getSource();
							Move.originX = (Integer) btn.getClientProperty("column");
							Move.originY = (Integer) btn.getClientProperty("row");
							if(Move.whosPiece(playerTurn, Move.originX, Move.originY))
							{
								Move.clickAPiece();
							}
						}
						else //If source is set then get the destination coords on next click.
						{
							JButton btn = (JButton) e.getSource();
							Move.destinationX = (Integer) btn.getClientProperty("column");
							Move.destinationY = (Integer) btn.getClientProperty("row");
							if(Move.canMove(playerTurn,Move.originX,Move.originY,Move.destinationX,Move.destinationY)) //If the piece can move
							{
								Move.makeMove(Move.originX, Move.originY, Move.destinationX, Move.destinationY); //Then make move
								History.addToHistory();
								Move.changePlayer(); //Change players
								AIPlayer.getRandomPieceDest();
								waitThenMove();
								resetBorderHighlights(); //Remove the highlights from the board
								turnStage = checkForJumps;
							}
							else //If the destination clicked on wasn't valid, reset the move coordinates.
							{
								Checkers.message.setText("Invalid move.");
								sourceSet = false;
							}
						}
					}
				}
			}
		}
	}
}