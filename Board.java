package checkers;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Board
{
	//Squares on the board are made up of a multi-dimensional array of Buttons
	public static JButton[][] boardSquares = new JButton[8][8];
	private static String Columns = "01234567";
	
	//Constructor - Setup the board and squares
	public Board()
	{
		setupBoard();
	}
	
	private static void setupBoard()
	{
		//Setup Checkers Board
		JPanel board = new JPanel(new GridLayout(0, 9))
		{
			private static final long serialVersionUID = 1L;

			//Override - The size of the board MUST be square. This will return the largest square size possible to fit window
			@Override
			public final Dimension getPreferredSize()
			{
				Dimension dimension = super.getPreferredSize();
				Dimension preferredSize = null;
				Component component = getParent();
				if (component == null)
				{
					preferredSize = new Dimension((int) dimension.getWidth(), (int) dimension.getHeight());
				}
				else if (component != null && component.getWidth() > dimension.getWidth() && component.getHeight() > dimension.getHeight())
				{
					preferredSize = component.getSize();
				}
				else
				{
					preferredSize = dimension;
				}
				int width = (int) preferredSize.getWidth();
				int height = (int) preferredSize.getHeight();
				int smallerSize = (width > height ? height : width);
				return new Dimension(smallerSize, smallerSize);
			}
		};
		board.setBackground(Color.WHITE);
		board.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 20, 20), new LineBorder(Color.black)));
		JPanel constrain = new JPanel(new GridBagLayout());
		constrain.setBackground(Color.WHITE);
		constrain.add(board);
		Checkers.guiJPanel.add(constrain);
		//Add Checkers Board squares using Insets & JButtons
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < boardSquares.length; i++)
		{
			for (int j = 0; j < boardSquares[i].length; j++)
			{
				JButton button = new JButton();
				button.setMargin(buttonMargin);
				ImageIcon icon = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
				{
					button.setBackground(Color.RED);
				}
				else
				{
					button.setBackground(Color.BLACK);
				}
				boardSquares[j][i] = button;
				boardSquares[j][i].putClientProperty("column", j);
				boardSquares[j][i].putClientProperty("row", i);
				boardSquares[j][i].setEnabled(false);
			}
		}	
		
		//Fill the Board with Labels
		board.add(new JLabel(""));
		for (int i = 0; i < 8; i++)
		{
			board.add(new JLabel(Columns.substring(i, i + 1), SwingConstants.CENTER));
		}
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				switch (j)
				{
					case 0:
						board.add(new JLabel("" + (1 + (i - 1)), SwingConstants.CENTER));
					default:
						board.add(boardSquares[j][i]);
				};
			}
		}
	}
}