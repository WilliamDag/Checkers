package checkers;

import javax.swing.*;

public class Piece
{
	public final static ImageIcon REDCHECK = new ImageIcon(Piece.class.getResource("/redCheck.png"));
	public final static ImageIcon BLACKCHECK = new ImageIcon(Piece.class.getResource("/blackCheck.png"));
	public final static ImageIcon REDKING = new ImageIcon(Piece.class.getResource("/redKing.png"));
	public final static ImageIcon BLACKKING = new ImageIcon(Piece.class.getResource("/blackKing.png"));
	 
	//A Piece. Has an x & y coordinate and an "Image"
	private Piece(int col, int row, ImageIcon pieceType)
	{
		Board.boardSquares[col][row].setIcon(pieceType);
	}
	
	//Create pieces on the board array
	public static void setupNewGame()
	{
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (col % 2 != row % 2)
				{
					if (row < 3)
					{
						new Piece(col, row, REDCHECK);
					}
					else if (row > 4)
					{
						new Piece(col, row, BLACKCHECK);
					}
					else if (row == 3 || row == 4)
					{
						Board.boardSquares[col][row].setIcon(null);
					}
				}
				else
				{
					Board.boardSquares[col][row].setIcon(null);
				}
				Board.boardSquares[col][row].setEnabled(true);
			}
		}
	}
	
	//Finish / quit a game & clear all the pieces from the board.
	public static void clearPieces()
	{
		for (int i = 0; i < Board.boardSquares.length; i++)
		{
			for (int j = 0; j < Board.boardSquares[i].length; j++)
			{
				Board.boardSquares[j][i].setIcon(null);
			}
		}
	}
	
	//Check for a winner
	public static void checkForWin()
	{
		//Keep track of the number of tiles that don't contain a red or black piece
		int numOfTilesWithoutBlack = 0;
		int numOfTilesWithoutRed = 0;
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if(!(Board.boardSquares[col][row].getIcon() == BLACKCHECK) && !(Board.boardSquares[col][row].getIcon() == BLACKKING))
				{
					numOfTilesWithoutBlack++;
				}
				if(!(Board.boardSquares[col][row].getIcon() == REDCHECK) && !(Board.boardSquares[col][row].getIcon() == REDKING))
				{
					numOfTilesWithoutRed++;
				}
			}
		}
		//If the entire board (64 squares) is without a specific colour, then that player loses the game.
		if (numOfTilesWithoutRed == 64)
		{
			Checkers.gameInProgress = false;
			int win = JOptionPane.showConfirmDialog(Checkers.guiJPanel, "Black Wins! Would you like to play again?");
			if (win == JOptionPane.YES_OPTION)
			{
				Checkers.gameInProgress = true;
				setupNewGame();
				ActionClicks.playerTurn = ActionClicks.BLACKPLAYER;
				ActionClicks.turnStage = ActionClicks.checkForJumps;
				Checkers.message.setText("Welcome to Checkers 1.0");
			}
			else
			{
				clearPieces();
			}
		}
		if (numOfTilesWithoutBlack == 64)
		{
			Checkers.gameInProgress = false;
			int win = JOptionPane.showConfirmDialog(Checkers.guiJPanel, "Red Wins! Would you like to play again?");
			if (win == JOptionPane.YES_OPTION)
			{
				Checkers.gameInProgress = true;
				setupNewGame();
				ActionClicks.playerTurn = ActionClicks.BLACKPLAYER;
				ActionClicks.turnStage = ActionClicks.checkForJumps;
				Checkers.message.setText("Welcome to Checkers 1.0");
			}
			else
			{
				clearPieces();
			}
		}
		//Reset the counter
		numOfTilesWithoutBlack = 0;
		numOfTilesWithoutRed = 0;
	}
}