package checkers;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class AIPlayer extends TimerTask
{
	//List to store integer coordinates of the AI controlled (red) pieces. List used as it is mutable.
	public static ArrayList<String> computerPieces = new ArrayList<String>();
	public static ArrayList<String> computerJumps = new ArrayList<String>();
	private static String randomSource;
	private static String randomJump;
	private static int randomDest;
	private static int randomSourceX, randomSourceY;

	//Make the AI move after 1 second
	@Override
	public void run()
	{
		makeAIMove();
	}
	
	public static void getAIPieces()
	{
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(Board.boardSquares[j][i].getIcon() == Piece.REDCHECK || Board.boardSquares[j][i].getIcon() == Piece.REDKING)
				{
					//Search for pieces that are red and save their X & Y coords to list
					computerPieces.add(Integer.toString(j) + "" + Integer.toString(i));
				}
			}
		}
	}
	
	public static void getAIJumps()
	{
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(Board.boardSquares[j][i].getBorder() == Move.possiblePiecesToMove)
				{
					//Search for pieces that can jump and save their X & Y coords to list
					computerJumps.add(Integer.toString(j) + "" + Integer.toString(i));
				}
			}
		}
	}
	
	private static void getRandomPieceSource()
	{
		//Get a random x & y source coordinate from the list of controlled pieces
		Random randomPieceSource = new Random();
		randomSource =  computerPieces.get(randomPieceSource.nextInt(computerPieces.size()));
	}
	
	private static void getRandomJumpSource()
	{
		//Get a random x & y source coordinate from the list of pieces that can jump
		Random randomJumpSource = new Random();
		randomJump = computerJumps.get(randomJumpSource.nextInt(computerJumps.size()));
		randomSourceX = Integer.parseInt(randomJump.substring(0,1));
		System.out.println(randomSourceX);
		randomSourceY = Integer.parseInt(randomJump.substring(1,2));
		System.out.println(randomSourceY);
		Move.originX = randomSourceX;
		Move.originY = randomSourceY;
	}
	
	public static void getRandomPieceDest()
	{
		//Random int from 0-3
		Random randomPieceDest = new Random();
		randomDest = randomPieceDest.nextInt(4) + 0;
	}
	
	//Update the lists after each move
	private static void updateAIPieces()
	{
		computerPieces.clear();
		getAIPieces();
		computerJumps.clear();
		getAIJumps();
	}
	
	private static void checkJumps()
	{
		//Depending on the random destination int, try and make a move.
		//If the move is not possible or there is another jump available
		//then increment the counter by 1 and try and jump again.
		if(randomDest == 0)
		{
			try
			{
				if(Move.canJump(ActionClicks.REDPLAYER, Move.originX, Move.originY, Move.originX + 1, Move.originY + 1, Move.originX + 2, Move.originY + 2))
				{
					Move.destinationX = Move.originX + 2;
					Move.destinationY = Move.originY + 2;
					Move.makeJump(Move.originX, Move.originY, Move.originX + 2, Move.originY + 2);
					History.addToHistory();
					System.out.println("Trying to make move..+2+2");
					if(Move.possibleDoubleJumps > 0)
					{
						Move.possibleDoubleJumps = 0;
						updateAIPieces();
						randomDest++;
						checkJumps();
					}
					else
					{
						Move.changePlayer();
						System.out.println("No more double jumps");
					}
				}
				else
				{
					randomDest++;
					checkJumps();
				}
			}
			catch(ArrayIndexOutOfBoundsException | StackOverflowError e)
			{
				randomDest++;
				checkJumps();
			}
		}
		if(randomDest == 1)
		{
			try
			{
				if(Move.canJump(ActionClicks.REDPLAYER, Move.originX, Move.originY, Move.originX + 1, Move.originY - 1, Move.originX + 2, Move.originY - 2))
				{
					Move.destinationX = Move.originX + 2;
					Move.destinationY = Move.originY - 2;
					Move.makeJump(Move.originX, Move.originY, Move.originX + 2, Move.originY - 2);
					History.addToHistory();
					System.out.println("Trying to make move..+2-2");
					if(Move.possibleDoubleJumps > 0)
					{
						Move.possibleDoubleJumps = 0;
						updateAIPieces();
						randomDest++;
						checkJumps();
					}
					else
					{
						Move.changePlayer();
						System.out.println("No more double jumps");
					}
				}
				else
				{
					randomDest++;
					checkJumps();
				}
			}
			catch(ArrayIndexOutOfBoundsException | StackOverflowError e)
			{
				randomDest++;
				checkJumps();
			}
		}
		if(randomDest == 2)
		{
			try
			{
				if(Move.canJump(ActionClicks.REDPLAYER, Move.originX, Move.originY, Move.originX - 1, Move.originY + 1, Move.originX - 2, Move.originY + 2))
				{
					Move.destinationX = Move.originX - 2;
					Move.destinationY = Move.originY + 2;
					Move.makeJump(Move.originX, Move.originY, Move.originX - 2, Move.originY + 2);
					History.addToHistory();
					System.out.println("Trying to make move..-2+2");
					if(Move.possibleDoubleJumps > 0)
					{
						Move.possibleDoubleJumps = 0;
						updateAIPieces();
						randomDest++;
						checkJumps();
					}
					else
					{
						Move.changePlayer();
						System.out.println("No more double jumps");
					}
				}
				else
				{
					randomDest++;
					checkJumps();
				}
			}
			catch(ArrayIndexOutOfBoundsException | StackOverflowError e)
			{
				randomDest++;
				checkJumps();
			}
		}
		if(randomDest == 3)
		{
			try
			{
				if(Move.canJump(ActionClicks.REDPLAYER, Move.originX, Move.originY, Move.originX - 1, Move.originY - 1, Move.originX - 2, Move.originY - 2))
				{
					Move.destinationX = Move.originX - 2;
					Move.destinationY = Move.originY - 2;
					Move.makeJump(Move.originX, Move.originY, Move.originX - 2, Move.originY - 2);
					History.addToHistory();
					System.out.println("Trying to make move..-2-2");
					if(Move.possibleDoubleJumps > 0)
					{
						Move.possibleDoubleJumps = 0;
						updateAIPieces();
						randomDest = 0;
						checkJumps();
					}
					else
					{
						Move.changePlayer();
						System.out.println("No more double jumps");
					}
				}
				else
				{
					randomDest = 0;
					checkJumps();
				}
			}
			catch(ArrayIndexOutOfBoundsException | StackOverflowError e)
			{
				randomDest = 0;
				checkJumps();
			}
		}
	}
	
	//Make an AI move.
	private static void makeAIMove()
	{
		//Check for jumps first.
		if(Move.highlightJumps(ActionClicks.REDPLAYER))
		{
			updateAIPieces();
			getRandomJumpSource();
			checkJumps();
		}
		//If no jumps then make a random move.
		//If the move is not possible then increment the counter by 1 and try and move again.
		else
		{
			getRandomPieceSource();
			randomSourceX = Integer.parseInt(randomSource.substring(0,1));
			randomSourceY = Integer.parseInt(randomSource.substring(1,2));

			if(randomDest == 0)
			{
				if(Move.canMove(ActionClicks.REDPLAYER, randomSourceX, randomSourceY, randomSourceX + 1, randomSourceY + 1))
				{
					Move.makeMove(randomSourceX, randomSourceY, randomSourceX + 1, randomSourceY + 1);
					History.addToHistory();
					updateAIPieces();
					Move.changePlayer();
				}
				else
				{
					randomDest++;
					makeAIMove();
				}
			}
			else
			if(randomDest == 1)
			{
				if(Move.canMove(ActionClicks.REDPLAYER, randomSourceX, randomSourceY, randomSourceX + 1, randomSourceY - 1))
				{
					Move.makeMove(randomSourceX, randomSourceY, randomSourceX + 1, randomSourceY - 1);
					History.addToHistory();
					updateAIPieces();
					Move.changePlayer();
				}
				else
				{
					randomDest++;
					makeAIMove();
				}
			}
			else
			if(randomDest == 2)
			{
				if(Move.canMove(ActionClicks.REDPLAYER, randomSourceX, randomSourceY, randomSourceX - 1, randomSourceY + 1))
				{
					Move.makeMove(randomSourceX, randomSourceY, randomSourceX - 1, randomSourceY + 1);
					History.addToHistory();
					updateAIPieces();
					Move.changePlayer();
				}
				else
				{
					randomDest++;
					makeAIMove();
				}
			}
			else
			if(randomDest == 3)
			{
				if(Move.canMove(ActionClicks.REDPLAYER, randomSourceX, randomSourceY, randomSourceX - 1, randomSourceY - 1))
				{
					Move.makeMove(randomSourceX, randomSourceY, randomSourceX - 1, randomSourceY - 1);
					History.addToHistory();
					updateAIPieces();
					Move.changePlayer();
				}
				else
				{
					randomDest = 0;
					makeAIMove();
				}
			}
		}
	}
}
