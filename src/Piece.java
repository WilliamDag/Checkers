package checkers;

import javax.swing.*;

public class Piece
{
	public final static ImageIcon REDCHECK = new ImageIcon("res/redCheck.png");
	public final static ImageIcon BLACKCHECK = new ImageIcon("res/blackCheck.png");
	public final static ImageIcon REDKING = new ImageIcon("res/redKing.png");
	public final static ImageIcon BLACKKING = new ImageIcon("res/blackKing.png");
	 
	//A Piece object. Has an x & y coordinate and an "Image"
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
				}
				Board.boardSquares[col][row].setEnabled(true);
			}
		}
	}
	
	//Disable any squares on the board that don't contain pieces, so they can't be clicked on.
	public static void changeTileStates()
	{
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if(!(Board.boardSquares[col][row].getIcon() == BLACKCHECK) && !(Board.boardSquares[col][row].getIcon() == REDCHECK)
						&& !(Board.boardSquares[col][row].getIcon() == BLACKKING) && !(Board.boardSquares[col][row].getIcon() == REDKING))
				{
					Board.boardSquares[col][row].setEnabled(false);
				}
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
				Board.boardSquares[j][i].setEnabled(false);
			}
		}
	}
}