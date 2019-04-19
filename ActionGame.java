/**
 * 
 * @author Adam D
 * Date:    11/16/2018
 * 
 * Class Name: ActionGame
 * Class Purpose:
 *       This is the class which controls game functionality and 
 *       game play. It instantiates the game board, which places the 
 *       ships randomly on the game board based on three difficulty 
 *       levels.  The player is then asked to enter the row and 
 *       column combination.  If the player hits a ship, a 'X' 
 *       will appear on the game board.  If the player misses a 
 *       '-' will appear.  Quitting the game, 'Q', displays all 
 *       of the ships and their symbol to see how they were laid 
 *       out on the game board.
 *        
 * ***********************************************************************
 * ************************************ CHANGE LOG ***********************
 * Date: 01/01/2000 | Name:                        | ID:
 * -----------------------------------------------------------------------
 * Change Description:
 * 
 * ***********************************************************************
 *
 */
import java.util.InputMismatchException;
import java.util.Scanner;

public class ActionGame
{
	public static final int GAME_SIZE_SMALL = 6;
	public static final int GAME_SIZE_MED   = 9;
	public static final int GAME_SIZE_LARGE = 12;

	public static final int GAME_SIZE_SMALL_MISSILES = 30;
	public static final int GAME_SIZE_MED_MISSILES   = 50;
	public static final int GAME_SIZE_LARGE_MISSILES = 75;
	
	static String[] difficult_levels = {"B","S","A"};  
	
	static int missiles_remaining = 0;
	static boolean suppress_ships = true;
	static boolean game_over = false;
	static boolean player_quit_game = false;
	static boolean player_won_game = false;
	
	static String input_choice = "";
	static String input_row = "";
	static int input_col = 0;
	static boolean invalid_input = true;
	
	static Scanner input = new Scanner(System.in);
	
	public void playActionGame() 
	{
		displayWelcomeScreen();
		inputGameLevel();
		
		setNumberOfMissiles(input_choice);
		
		GameBoard battleshipGameBoard = new GameBoard();
		GameBoard.setGameBoard(determineBoardSize(input_choice));		
		GameBoard.getGameBoard(!suppress_ships);
	
		playGame();
	}
	
		
	/**
	 * displayWelcomeScreen method displays welcome screen to user
	 */
	private static void displayWelcomeScreen()
	{		
		System.out.println("************************************************");
		System.out.println("*                 Welcome to                   *");
		System.out.println("*                 BATTLESHIP                   *");
		System.out.println("************************************************");
		System.out.println(" ");			
	}
	
	
	/**
	 * inputGameLevel method inputs a valid game difficulty level
	 */
	private static void inputGameLevel()
	{			
		do 
		{
			System.out.println(" Please enter game level difficulty: ");
			System.out.println("   (B)eginner    |  6x6  | 30 Missiles ");
			System.out.println("   (S)tandard    |  9x9  | 50 Missiles ");
			System.out.println("   (A)dvanced    | 12x12 | 75 Missiles ");
			System.out.println(" Choice (B,S,A): ");
			input_choice =  input.nextLine();
			
		} while (!isInputGameLevelValid(input_choice));
	}
	
	
	/**
	 * validateInputGameLevel method verifies if the game level is valid
	 * @param level		Difficulty level 
	 * @return			boolean: if game level valid or not 
	 */
	private static boolean isInputGameLevelValid(String level)
	{		
		for (int i = 0; i < 3; i++)
		{
			if (level.equalsIgnoreCase(difficult_levels[i]))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * setNumberOfMissiles method sets the number of missiles remaining value
	 * to the game size default.  
	 * @param level		Difficulty level 
	 */
	private static void setNumberOfMissiles(String level)
	{
		if (level.equalsIgnoreCase(difficult_levels[0]))
		{
			missiles_remaining = GAME_SIZE_SMALL_MISSILES;
		} else if (level.equalsIgnoreCase(difficult_levels[1]))
		{
			missiles_remaining = GAME_SIZE_MED_MISSILES;
		} else
		{
			missiles_remaining = GAME_SIZE_LARGE_MISSILES;
		}
	}
	
	
	/**
	 * determineBoardSize method returns the size of the game.
	 * @param level		Difficulty level
	 * @return			int: size of game
	 */
	private static int determineBoardSize(String level)
	{
		if (level.equalsIgnoreCase(difficult_levels[0]))
		{
		 	return GAME_SIZE_SMALL;
		} else if (level.equalsIgnoreCase(difficult_levels[1]))
		{
			return GAME_SIZE_MED;
		} else
		{
			return GAME_SIZE_LARGE;
		}
	}
	
	
	/**
	 * playGame method is user game board interaction of the game.  By now the user 
	 * has a blank game board presented to them, even though the game board has the 
	 * ships positioned on it, all of the ships are suppressed.  The user enters a 
	 * letter / number combination which is checked against the game board to 
	 * determine if the coordinates are a hit, miss or reused coordinate.  The game 
	 * will then reveal the results
	 * 
	 *    "X" - the coordinates hit a ship
	 *    "-" - the coordinates missed 
	 */
	private static void playGame()
	{
		int row_value = 0,
			col_value = 0;
		
		while(!game_over)
		{
			/*
			 * User enters valid row
			 */
			invalid_input = true;
			System.out.println(" ");
			
			row_value = evaluteRowValue();
			
			invalid_input = true;
			
			/*
			 * User enters valid column
			 */
			while (!game_over && invalid_input) 
			{
				col_value = evaluateColumnValue();
			} 
			
			System.out.println(" ");
			
			/*
			 * Game board is checked to see if user's input is a 
			 * a hit, miss or previously played element.
			 */
			if (!game_over)
			{
				evaluateGameBoardElement(row_value, col_value);					
				
				missiles_remaining --;

				if (missiles_remaining == 0)
				{
					game_over = true;
				}
				
				GameBoard.getGameBoard(suppress_ships);
				System.out.println("\n ");
			}
			
			/*
			 * Game is over meaning user hit all of the ships,
			 * ran out of missiles, or quit.
			 */
			if (game_over)
			{
				displayEndOfGame();			
			}
		}				
	}
	
	/**
	 * evaluateRowValue method checks the GameBoard(), starting at letter A , to 
	 * validate the input column is within the bounds of the size of the game.  
	 * Then it translates the input value to for checking the ArrayList game board.
	 * @return		int: element value of row
	 */
	private static int evaluteRowValue()
	{
		int row_value = 0;
		
		/*
		 * Verifies user had entered a valid row.  If not, it forces the user to re-enter
		 * a value until valid.
		 */
		do
		{
			System.out.println("************************************************");
			System.out.println("* Missiles Remaining: " + missiles_remaining);
			System.out.println("* Enter a Row Value (A, B, C, ... / Q to Quit): ");
			input_row =  input.next();

			/*
			 * If player enters a "Q" then the player quits the game
			 */
			if (input_row.equalsIgnoreCase("Q"))
			{
				game_over = true;
				player_quit_game = true;
			}
			else
			{
				/*
				 * Check the GameBoard(), starting at letter A, to validate the input row
				 * is within the bounds of the size of the game.
				 */
				for (int i = 0; i < GameBoard.getGameBoardSize() && invalid_input; i++)
				{					
					if (input_row.equalsIgnoreCase(GameBoard.getRowValue(i)))
					{
						invalid_input = false;
						row_value = i;
					}
				}
				/*
				 * Error message is displayed if the value entered by the user is not a valid 
				 * game board element.
				 */
				if (invalid_input)
				{
					System.out.println("     !!! The Row Value ("+ input_row +") is not valid !!! ");
					System.out.println(" ");
				}
			}
			
		} while (invalid_input && !game_over);

		return row_value;
	}
	
	
	/**
	 * evaluateColumn Value method checks the GameBoard(), starting at number 1 , to 
	 * validate the input column is within the bounds of the size of the game.  Then 
	 * it translates the input value to for checking the ArrayList game board.
	 * @return 	int: element value of column
	 */
	private static int evaluateColumnValue()
	{
		int col_value = 0;
		
		System.out.println("* Enter a Column Value (1, 2, 3, ...): ");
		
		/*
		 * Verifies user had entered a valid column.  If not, it forces the user to 
		 * re-enter a value until valid.
		 */
		try 
		{
			input_col =  input.nextInt();

			col_value = input_col;
			col_value--;
			
			if (input_col <= GameBoard.getGameBoardSize() &&
			    input_col > 0)
			{
				invalid_input = false;
			}
			else 
			{
				System.out.println("      !!! The Column value ("+ input_col +") is not valid !!! ");
				System.out.println(" ");
			}
		}
		catch (InputMismatchException e)
		{
			System.out.println("      !!! The Column value is not numeric !!! ");
			input.nextLine();
			System.out.println(" ");
		}
		
		return col_value;
	}
	
	
	/**
	 * evaluateGameBoardElements method takes the user input and checks the game board to 
	 * determine if the entry was a hit, missed or a reused missile.  By now the Letters
	 * and numbers of the game board should have been translated to check an ArrayList.   
	 * @param row_value 	ArrayList formatted row value
	 * @param col_value		ArrayList formatted column value
	 */
	private static void evaluateGameBoardElement(int row_value, int col_value)
	{
		String element_value = GameBoard.getGameBoardElementValue(row_value, col_value);
		
		/*
		 * Checks the game board for a hit, miss or water symbol.  If none of those, then
		 * the value is a ship value and the symbol is replaced with a hit symbol.
		 */
		switch(element_value)
		{
		case GameBoard.BOARD_HIT:
		case GameBoard.BOARD_MISS:
			System.out.println("-> You're a bozo! You just wasted a missile! ");
			break;
		case GameBoard.BOARD_WATER:
			System.out.println("-> Nice try, but the missile missed. ");
			try 
			{
				GameBoard.setGameBoardElementValue(row_value, col_value, GameBoard.BOARD_MISS);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("-> The missile HIT the " + GameBoard.getShipType(element_value) + "!");
			try 
			{				
				GameBoard.setGameBoardElementValue(row_value, col_value, GameBoard.BOARD_HIT);				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			try
			{				
				GameBoard.setShipHit(element_value);
				
				if (GameBoard.getShipSunkStatus(element_value))
				{
					System.out.println("-> Ships Remaining: " + GameBoard.getNumberOfShipsLeft());
					if (GameBoard.getNumberOfShipsLeft() == 0)
					{
						game_over = true;
						player_won_game = true;
					}
				}				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}		
		}	
	}
	
	
	/**
	 * displayEndOfGame method is used to display the final message of the game. The user
	 * has either quit the game, won the game or loss the game.  A final game board display 
	 * will appear revealing the number of ship elements left, if any.  
	 */
	private static void displayEndOfGame()
	{
		suppress_ships = false;
		
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("************************************************");
		System.out.println(" ");
		System.out.println(" ");		
		
		/*
		 * Determines what needs to be displayed at end of game.  If player quits, then game board 
		 * is displayed with ship placement and hit ships (if any).  If the player hits all of the
		 * ships then a win message is displayed with the hit elements.  If the player loses then
		 * a loss message is displayed with the placement of the remaining ships.    
		 */
		if (player_quit_game)
		{
			System.out.println("     Thank you for playing! ");	
			GameBoard.getGameBoard(suppress_ships);
		}				
		else
		{
			if (player_won_game)
			{
				System.out.println("    YOU WIN!!!!!  Remaining Missles: " + missiles_remaining + 
						           ", Thank you for playing! ");	
				GameBoard.getGameBoard(suppress_ships);
			}
			else 
			{
				System.out.println("     YOU LOSE!!!!!  Thank you for playing! ");	
				GameBoard.getGameBoard(suppress_ships);
			}
		}
			
		
	}
		
}
