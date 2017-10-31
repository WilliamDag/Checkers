package checkers;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.border.*;

public class Move
{
	public static int originX, originY;
	public static int destinationX, destinationY;
	public static Border possiblePiecesToMove = new LineBorder(Color.WHITE, 2);
	private static Border availableMove = new LineBorder(Color.GREEN, 2);
	private static Border selectedPiece = new LineBorder(Color.BLUE, 2);
	public static int possibleDoubleJumps = 0;
	
	//Change the current player.
	public static void changePlayer()
	{
		if(Checkers.gameInProgress)
		{
			if(ActionClicks.playerTurn == ActionClicks.BLACKPLAYER)
			{
				ActionClicks.playerTurn = ActionClicks.REDPLAYER;
				Checkers.message.setText("Red players turn.");
			}
			else
			{
				ActionClicks.playerTurn = ActionClicks.BLACKPLAYER;
				Checkers.message.setText("Black players turn.");
			}
		}
	}
	
	//Clicking on a piece sets the source coordinates.
	public static void clickAPiece()
	{
		if(Board.boardSquares[originX][originY].getBorder() == possiblePiecesToMove)
		{
			Board.boardSquares[originX][originY].setBorder(selectedPiece);
			ActionClicks.sourceSet = true;
		}
		else
		{
			Checkers.message.setText("You can't move that piece.");
		}
	}
	
	//Player can only click on their own pieces.
	public static boolean whosPiece(int player, int c1, int r1)
	{
		if(player == ActionClicks.REDPLAYER)
		{
			if(Board.boardSquares[c1][r1].getIcon() != Piece.REDCHECK && Board.boardSquares[c1][r1].getIcon() != Piece.REDKING)
			{
				Checkers.message.setText("Can only move Red pieces.");
				return false;
			}
		}
		else
		{
			if(Board.boardSquares[c1][r1].getIcon() != Piece.BLACKCHECK && Board.boardSquares[c1][r1].getIcon() != Piece.BLACKKING)
			{
				Checkers.message.setText("Can only move Black pieces.");
				return false;
			}
		}
		return true;
	}
	
	//If can move is true then move, include sources and destinations.
	public static boolean canMove(int player, int c1, int r1, int c2, int r2)
	{
		if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
		{
			return false; // (r2,c2) is off the board.
		}
		if (Board.boardSquares[c2][r2].getIcon() == Piece.BLACKCHECK || Board.boardSquares[c2][r2].getIcon() == Piece.REDCHECK)
		{
			return false; // (r2,c2) already contains a piece.
		}
		if (player == ActionClicks.REDPLAYER)
		{
			if (Board.boardSquares[c1][r1].getIcon() == Piece.REDCHECK && r2 < r1)
			{
				return false; // Regular red piece can only move down.
			}
			if(!(r2 == r1 + 1 && c2 == c1 - 1) && !(r2 == r1 + 1 && c2 == c1 + 1)) //Checks if its a possible move for a normal piece
			{
				if(Board.boardSquares[c1][r1].getIcon() == Piece.REDKING)
				{
					if(!(r2 == r1 - 1 && c2 == c1 + 1) && !(r2 == r1 - 1 && c2 == c1 - 1)) //Checks if its a possible move for a King
					{
						return false;
					}
					else
					{
						return true;
					}
				}
				return false;
			}
			return true; // The move is legal.
		}
		else
		{
			if (Board.boardSquares[c1][r1].getIcon() == Piece.BLACKCHECK && r2 > r1)
			{
				return false; // Regular black piece can only move up.
			}
			if (!(r2 == r1 - 1 && c2 == c1 + 1) && !(r2 == r1 - 1 && c2 == c1 - 1)) //Checks if its a possible move for a normal piece
			{
				if(Board.boardSquares[c1][r1].getIcon() == Piece.BLACKKING)
				{
					if(!(r2 == r1 + 1 && c2 == c1 - 1) && !(r2 == r1 + 1 && c2 == c1 + 1))  //Checks if its a possible move for a King
					{
						return false;
					}
					else
					{
						return true;
					}
				}
				return false;
			}
			return true; // The move is legal.
		}
	}
	
	public static boolean makeJump(int fromCol, int fromRow, int toCol, int toRow)
	{
		if(Board.boardSquares[toCol][toRow].getIcon() == Piece.BLACKCHECK || Board.boardSquares[toCol][toRow].getIcon() == Piece.BLACKKING
				|| Board.boardSquares[toCol][toRow].getIcon() == Piece.REDCHECK || Board.boardSquares[toCol][toRow].getIcon() == Piece.REDKING)
		{
			return false;
		}
		if(!((toRow == fromRow + 2) && (toCol == fromCol + 2)) && !((toRow == fromRow - 2) && (toCol == fromCol - 2))
				&& !((toRow == fromRow + 2) && (toCol == fromCol - 2)) && !((toRow == fromRow - 2) && (toCol == fromCol + 2)))
		{
			ActionClicks.sourceSet = false;
			Checkers.message.setText("Invalid move.");
			return false;
		}
		else
		{
			Board.boardSquares[toCol][toRow].setIcon(Board.boardSquares[fromCol][fromRow].getIcon());
			Board.boardSquares[fromCol][fromRow].setIcon(null);
			if (fromRow - toRow == 2 || fromRow - toRow == -2)
			{
				for (int row = 0; row < 8; row++)
				{
					for (int col = 0; col < 8; col++)
					{
						Board.boardSquares[col][row].setEnabled(true);
					}
				}
				int jumpRow = (fromRow + toRow) / 2; // Row of the jumped piece.
				int jumpCol = (fromCol + toCol) / 2; // Column of the jumped piece.
				Board.boardSquares[jumpCol][jumpRow].setIcon(null);
			}
			if (toRow == 7 && Board.boardSquares[toCol][toRow].getIcon() == Piece.REDCHECK)
			{
				Board.boardSquares[toCol][toRow].setIcon(Piece.REDKING);
			}
			if (toRow == 0 && Board.boardSquares[toCol][toRow].getIcon() == Piece.BLACKCHECK)
			{
				Board.boardSquares[toCol][toRow].setIcon(Piece.BLACKKING);
			}
			ActionClicks.sourceSet = false;
			ActionClicks.resetBorderHighlights();
			Piece.checkForWin();
			if(highlightDoubleJumps(ActionClicks.playerTurn))
			{
				Checkers.message.setText("You must keep jumping!");
			}
		}
		return true;
	}
	
	public static void makeMove(int fromCol, int fromRow, int toCol, int toRow)
	{
		Board.boardSquares[toCol][toRow].setIcon(Board.boardSquares[fromCol][fromRow].getIcon());
		Board.boardSquares[fromCol][fromRow].setIcon(null);
		if (toRow == 7 && Board.boardSquares[toCol][toRow].getIcon() == Piece.REDCHECK)
		{
			Board.boardSquares[toCol][toRow].setIcon(Piece.REDKING);
		}
		if (toRow == 0 && Board.boardSquares[toCol][toRow].getIcon() == Piece.BLACKCHECK)
		{
			Board.boardSquares[toCol][toRow].setIcon(Piece.BLACKKING);
		}
		ActionClicks.sourceSet = false;
		ActionClicks.resetBorderHighlights();
		Piece.checkForWin();
	}
	
	public static boolean highlightMoves(int player)
	{
		int possibleMoves = 0;
		ImageIcon playerPiece;
		if (player == ActionClicks.REDPLAYER)
		{
			playerPiece = Piece.REDCHECK;
		}
		else
		{
			playerPiece = Piece.BLACKCHECK;
		}
		ImageIcon playerKing;
		if (player == ActionClicks.REDPLAYER)
		{
			playerKing = Piece.REDKING;
		}
		else
		{
			playerKing = Piece.BLACKKING;
		}
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (Board.boardSquares[col][row].getIcon() == playerPiece || Board.boardSquares[col][row].getIcon() == playerKing)
				{
					if(Move.canMove(player, col, row, col + 1, row - 1))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col + 1][row - 1].setEnabled(true);
						Board.boardSquares[col + 1][row - 1].setBorder(availableMove);
						possibleMoves++;
					}
					if(Move.canMove(player, col, row, col - 1, row - 1))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col - 1][row - 1].setEnabled(true);
						Board.boardSquares[col - 1][row - 1].setBorder(availableMove);
						possibleMoves++;
					}
					if(Move.canMove(player, col, row, col - 1, row + 1))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col - 1][row + 1].setEnabled(true);
						Board.boardSquares[col - 1][row + 1].setBorder(availableMove);
						possibleMoves++;
					}
					if(Move.canMove(player, col, row, col + 1, row + 1))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col + 1][row + 1].setEnabled(true);
						Board.boardSquares[col + 1][row + 1].setBorder(availableMove);
						possibleMoves++;
					}
				}
			}	
		}
		if(possibleMoves > 0)
		{
			return true;
		}
		else
		return false;
	}	
	
	public static boolean highlightJumps(int player)
	{
		int possibleJumps = 0;
		ImageIcon playerPiece;
		if (player == ActionClicks.REDPLAYER)
		{
			playerPiece = Piece.REDCHECK;
		}
		else
		{
			playerPiece = Piece.BLACKCHECK;
		}
		ImageIcon playerKing;
		if (player == ActionClicks.REDPLAYER)
		{
			playerKing = Piece.REDKING;
		}
		else
		{
			playerKing = Piece.BLACKKING;
		}
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (Board.boardSquares[col][row].getIcon() == playerPiece || Board.boardSquares[col][row].getIcon() == playerKing)
				{
					if(Move.canJump(player, col, row, col + 1, row + 1, col + 2, row + 2))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col + 2][row + 2].setEnabled(true);
						Board.boardSquares[col + 2][row + 2].setBorder(availableMove);
						possibleJumps++;
					}
					if(Move.canJump(player, col, row, col - 1, row - 1, col - 2, row - 2))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col - 2][row - 2].setEnabled(true);
						Board.boardSquares[col - 2][row - 2].setBorder(availableMove);
						possibleJumps++;
					}
					if(Move.canJump(player, col, row, col + 1, row - 1, col + 2, row - 2))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col + 2][row - 2].setEnabled(true);
						Board.boardSquares[col + 2][row - 2].setBorder(availableMove);
						possibleJumps++;
					}
					if(Move.canJump(player, col, row, col - 1, row + 1, col - 2, row + 2))
					{
						Board.boardSquares[col][row].setEnabled(true);
						Board.boardSquares[col][row].setBorder(possiblePiecesToMove);
						Board.boardSquares[col - 2][row + 2].setEnabled(true);
						Board.boardSquares[col - 2][row + 2].setBorder(availableMove);
						possibleJumps++;
					}
				}
			}	
		}
		if(possibleJumps > 0)
		{
			return true;
		}
		else
		return false;
	}
	
	public static boolean highlightDoubleJumps(int player)
	{
		if(Move.canJump(player, destinationX, destinationY, destinationX + 1, destinationY + 1, destinationX + 2, destinationY + 2))
		{
			Board.boardSquares[destinationX + 2][destinationY + 2].setEnabled(true);
			Board.boardSquares[destinationX + 2][destinationY + 2].setBorder(availableMove);
			possibleDoubleJumps++;
		}
		if(Move.canJump(player, destinationX, destinationY, destinationX - 1, destinationY - 1, destinationX - 2, destinationY - 2))
		{
			Board.boardSquares[destinationX - 2][destinationY - 2].setEnabled(true);
			Board.boardSquares[destinationX - 2][destinationY - 2].setBorder(availableMove);
			possibleDoubleJumps++;
		}
		if(Move.canJump(player, destinationX, destinationY, destinationX + 1, destinationY - 1, destinationX + 2, destinationY - 2))
		{
			Board.boardSquares[destinationX + 2][destinationY - 2].setEnabled(true);
			Board.boardSquares[destinationX + 2][destinationY - 2].setBorder(availableMove);
			possibleDoubleJumps++;
		}
		if(Move.canJump(player, destinationX, destinationY, destinationX - 1, destinationY + 1, destinationX - 2, destinationY + 2))
		{
			Board.boardSquares[destinationX - 2][destinationY + 2].setEnabled(true);
			Board.boardSquares[destinationX - 2][destinationY + 2].setBorder(availableMove);
			possibleDoubleJumps++;
		}
		if(possibleDoubleJumps > 0)
		{
			originX = destinationX;
			originY = destinationY;
			Board.boardSquares[originX][originY].setEnabled(true);
			Board.boardSquares[originX][originY].setBorder(selectedPiece);
			return true;
		}
		else
		return false;
	}
	
	public static boolean canJump(int player, int c1, int r1, int c2, int r2, int c3, int r3)
	{
		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) 
		{
			return false; // (r3,c3) is off the board.
		}
		if (Board.boardSquares[c3][r3].getIcon() == Piece.BLACKCHECK || Board.boardSquares[c3][r3].getIcon() == Piece.REDCHECK ||
				Board.boardSquares[c3][r3].getIcon() == Piece.BLACKKING || Board.boardSquares[c3][r3].getIcon() == Piece.REDKING)
		{
			return false; // (r3,c3) already contains a piece.
		}
		if (player == ActionClicks.REDPLAYER)
		{
			if (Board.boardSquares[c1][r1].getIcon() == Piece.REDCHECK && r3 < r1)
			{
				return false; // Regular red piece can only move  up.
			}
			if (Board.boardSquares[c2][r2].getIcon() != Piece.BLACKCHECK && Board.boardSquares[c2][r2].getIcon() != Piece.BLACKKING)
			{
				return false; // There is no black piece to jump.
			}
			return true; // The jump is legal.
		}
		else
		{
			if (Board.boardSquares[c1][r1].getIcon() == Piece.BLACKCHECK && r3 > r1)
			{
				return false; // Regular black piece can only move down.
			}
			if (Board.boardSquares[c2][r2].getIcon() != Piece.REDCHECK && Board.boardSquares[c2][r2].getIcon() != Piece.REDKING)
			{
				return false; // There is no red piece to jump.
			}
			return true; // The jump is legal.
		}
	} // end canJump()
}