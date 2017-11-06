package checkers;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.border.*;

public class Move
{
	public static int originX, originY;
	public static int destinationX, destinationY;
	public static Border possiblePiecesToMove = new LineBorder(Color.WHITE, 2);
	public static Border availableMove = new LineBorder(Color.GREEN, 2);
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
	public static boolean whosPiece(int player, int column, int row)
	{
		if(player == ActionClicks.REDPLAYER)
		{
			if(Board.boardSquares[column][row].getIcon() != Piece.REDCHECK && Board.boardSquares[column][row].getIcon() != Piece.REDKING)
			{
				Checkers.message.setText("Can only move Red pieces.");
				return false;
			}
		}
		else
		{
			if(Board.boardSquares[column][row].getIcon() != Piece.BLACKCHECK && Board.boardSquares[column][row].getIcon() != Piece.BLACKKING)
			{
				Checkers.message.setText("Can only move Black pieces.");
				return false;
			}
		}
		return true;
	}
	
	//If can move is true then move, include sources and destinations.
	public static boolean canMove(int player, int fromX, int fromY, int toX, int toY)
	{
		if (toY < 0 || toY >= 8 || toX < 0 || toX >= 8)
		{
			return false; //Move is off the board.
		}
		if (Board.boardSquares[toX][toY].getIcon() == Piece.BLACKCHECK || Board.boardSquares[toX][toY].getIcon() == Piece.REDCHECK)
		{
			return false; //Move already contains a piece.
		}
		if (player == ActionClicks.REDPLAYER)
		{
			if (Board.boardSquares[fromX][fromY].getIcon() == Piece.REDCHECK && toY < fromY)
			{
				return false; // Regular red piece can only move down.
			}
			if(!(toY == fromY + 1 && toX == fromX - 1) && !(toY == fromY + 1 && toX == fromX + 1)) //Checks if its a possible move for a normal piece
			{
				if(Board.boardSquares[fromX][fromY].getIcon() == Piece.REDKING)
				{
					if(!(toY == fromY - 1 && toX == fromX + 1) && !(toY == fromY - 1 && toX == fromX - 1)) //Checks if its a possible move for a King
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
			if (Board.boardSquares[fromX][fromY].getIcon() == Piece.BLACKCHECK && toY > fromY)
			{
				return false; // Regular black piece can only move up.
			}
			if (!(toY == fromY - 1 && toX == fromX + 1) && !(toY == fromY - 1 && toX == fromX - 1)) //Checks if its a possible move for a normal piece
			{
				if(Board.boardSquares[fromX][fromY].getIcon() == Piece.BLACKKING)
				{
					if(!(toY == fromY + 1 && toX == fromX - 1) && !(toY == fromY + 1 && toX == fromX + 1))  //Checks if its a possible move for a King
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
	
	public static boolean makeJump(int fromX, int fromY, int toX, int toY)
	{
		if(Board.boardSquares[toX][toY].getIcon() == Piece.BLACKCHECK || Board.boardSquares[toX][toY].getIcon() == Piece.BLACKKING
				|| Board.boardSquares[toX][toY].getIcon() == Piece.REDCHECK || Board.boardSquares[toX][toY].getIcon() == Piece.REDKING)
		{
			return false;
		}
		if(!((toY == fromY + 2) && (toX == fromX + 2)) && !((toY == fromY - 2) && (toX == fromX - 2))
				&& !((toY == fromY + 2) && (toX == fromX - 2)) && !((toY == fromY - 2) && (toX == fromX + 2)))
		{
			ActionClicks.sourceSet = false;
			Checkers.message.setText("Invalid move.");
			return false;
		}
		else
		{
			Board.boardSquares[toX][toY].setIcon(Board.boardSquares[fromX][fromY].getIcon());
			Board.boardSquares[fromX][fromY].setIcon(null);
			if (fromY - toY == 2 || fromY - toY == -2)
			{
				for (int row = 0; row < 8; row++)
				{
					for (int col = 0; col < 8; col++)
					{
						Board.boardSquares[col][row].setEnabled(true);
					}
				}
				int jumpRow = (fromY + toY) / 2; // Row of the jumped piece.
				int jumpCol = (fromX + toX) / 2; // Column of the jumped piece.
				Board.boardSquares[jumpCol][jumpRow].setIcon(null);
			}
			if (toY == 7 && Board.boardSquares[toX][toY].getIcon() == Piece.REDCHECK)
			{
				Board.boardSquares[toX][toY].setIcon(Piece.REDKING);
			}
			if (toY == 0 && Board.boardSquares[toX][toY].getIcon() == Piece.BLACKCHECK)
			{
				Board.boardSquares[toX][toY].setIcon(Piece.BLACKKING);
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
	
	public static void makeMove(int fromX, int fromY, int toX, int toY)
	{
		Board.boardSquares[toX][toY].setIcon(Board.boardSquares[fromX][fromY].getIcon());
		Board.boardSquares[fromX][fromY].setIcon(null);
		if (toY == 7 && Board.boardSquares[toX][toY].getIcon() == Piece.REDCHECK)
		{
			Board.boardSquares[toX][toY].setIcon(Piece.REDKING);
		}
		if (toY == 0 && Board.boardSquares[toX][toY].getIcon() == Piece.BLACKCHECK)
		{
			Board.boardSquares[toX][toY].setIcon(Piece.BLACKKING);
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
	
	public static boolean canJump(int player, int fromX, int fromY, int jumpX, int jumpY, int toX, int toY)
	{
		if (toY < 0 || toY >= 8 || toX < 0 || toX >= 8) 
		{
			return false; //Move is off the board.
		}
		if (Board.boardSquares[toX][toY].getIcon() == Piece.BLACKCHECK || Board.boardSquares[toX][toY].getIcon() == Piece.REDCHECK ||
				Board.boardSquares[toX][toY].getIcon() == Piece.BLACKKING || Board.boardSquares[toX][toY].getIcon() == Piece.REDKING)
		{
			return false; //Move already contains a piece.
		}
		if (player == ActionClicks.REDPLAYER)
		{
			if (Board.boardSquares[fromX][fromY].getIcon() == Piece.REDCHECK && toY < fromY)
			{
				return false; // Regular red piece can only move up.
			}
			if (Board.boardSquares[jumpX][jumpY].getIcon() != Piece.BLACKCHECK && Board.boardSquares[jumpX][jumpY].getIcon() != Piece.BLACKKING)
			{
				return false; // There is no black piece to jump.
			}
			return true; // The jump is legal.
		}
		else
		{
			if (Board.boardSquares[fromX][fromY].getIcon() == Piece.BLACKCHECK && toY > fromY)
			{
				return false; // Regular black piece can only move down.
			}
			if (Board.boardSquares[jumpX][jumpY].getIcon() != Piece.REDCHECK && Board.boardSquares[jumpX][jumpY].getIcon() != Piece.REDKING)
			{
				return false; // There is no red piece to jump.
			}
			return true; // The jump is legal.
		}
	}
}