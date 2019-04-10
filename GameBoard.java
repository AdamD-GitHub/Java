/**
 * 
 * @author Adam Diel (S0854801)
 * Date:    11/16/2018
 * 
 * Class Name: GameBoard
 * Class Purpose:
 *       This class builds and controls the board.  It randomly positions  
 *       the ships on an ArrayList and contains the getters and setters
 *       for the ArrayList and the ship objects.
 *        
 * ***********************************************************************
 * ************************************ CHANGE LOG ***********************
 * Date: 01/01/2000 | Name:                        | ID:
 * -----------------------------------------------------------------------
 * Change Description:
 * 
 * ***********************************************************************
 */


import java.util.ArrayList;
import java.security.SecureRandom;

public class GameBoard 
{
    public static final int CARRIER_SIZE = 5;
    public static final int BATTLESHIP_SIZE = 4;
    public static final int DESTROYER_SIZE = 3;
    public static final int SUBMARINE_SIZE = 3;
    public static final int PATROL_SIZE = 2;
	
    public static final String CARRIER_SYMBOL = "C";
    public static final String BATTLESHIP_SYMBOL = "B";
    public static final String DESTROYER_SYMBOL = "D";
    public static final String SUBMARINE_SYMBOL = "S";
    public static final String PATROL_SYMBOL = "P";

    public static final String CARRIER_NAME = "USS Nimitz";
    public static final String BATTLESHIP_NAME = "USS Iowa";
    public static final String DESTROYER_NAME = "USS Higgins";
    public static final String SUBMARINE_NAME = "USS Plunger";
    public static final String PATROL_NAME = "USS Spike";
    
    public static final String CARRIER_TYPE = "Carrier";
    public static final String BATTLESHIP_TYPE = "Battleship";
    public static final String DESTROYER_TYPE = "Destoyer";
    public static final String SUBMARINE_TYPE = "Submarine";
    public static final String PATROL_TYPE = "Patrol";
    
    public static final String BOARD_WATER = " ";
	public static final String BOARD_MISS  = "-";
	public static final String BOARD_HIT   = "X";

	public static final int TOTAL_SHIPS = 5;
	private static Ship car = null;
	private static Ship bat = null;
	private static Ship des = null;
	private static Ship sub = null;
	private static Ship pat = null;
	
	static String[] rowLetter = {"A","B","C","D","E","F","G","H","I","J","K","L"};
	static String[] colNumber = {"  ","  1","  2","  3","  4","  5","  6",
			                     "  7","  8","  9"," 10"," 11"," 12"};
	
	static ArrayList<ArrayList<String>> game_board = new ArrayList<ArrayList<String>>();

	static int game_board_size = 0;
	
	/**
	 * buildGameBoard method builds the game board and sizes it based on the 
	 * requested size.    
	 */
	private static void buildGameBoard() 
	{
        for(int row_num = 0; row_num < game_board_size; row_num++) 
        {
            ArrayList<String> row = new ArrayList<String>();
            game_board.add(row_num, row);
            
            for (int col_num = 0; col_num < game_board_size; col_num++)
            {
            	String cell_value = BOARD_WATER;
            	row.add(col_num, cell_value);
            }
        }

		buildShips();
    }
	
	
	/**
	 * buildShips method builds the ship's object and places its symbol on the board. 
	 */
	public static void buildShips() 
	{
		placeShipOnBoard(CARRIER_SIZE, CARRIER_SYMBOL);
		car = new Ship(CARRIER_NAME, CARRIER_TYPE, CARRIER_SIZE);
		placeShipOnBoard(BATTLESHIP_SIZE, BATTLESHIP_SYMBOL);
		bat = new Ship(BATTLESHIP_NAME, BATTLESHIP_TYPE, BATTLESHIP_SIZE);
		placeShipOnBoard(DESTROYER_SIZE, DESTROYER_SYMBOL);
		des = new Ship(DESTROYER_NAME, DESTROYER_TYPE, DESTROYER_SIZE);
		placeShipOnBoard(SUBMARINE_SIZE, SUBMARINE_SYMBOL);
		sub = new Ship(SUBMARINE_NAME, SUBMARINE_TYPE, SUBMARINE_SIZE);
		placeShipOnBoard(PATROL_SIZE, PATROL_SYMBOL);
		pat = new Ship(PATROL_NAME, PATROL_TYPE, PATROL_SIZE);
	}

	
	/**
	 * placeShipsOnBoard method looks for a valid place to put a ship.  The ship can not 
	 * conflict with other ship or another location will be used.  Once a clear and valid
	 * location id found, the ship is placed on the board.
	 * @param ship_size		Size of ship
	 * @param ship_symbol	Symbol of ship
	 */
	private static void placeShipOnBoard(int ship_size, String ship_symbol)
	{
		int row_num = 0;
		int col_num = 0;
		boolean ship_horizontal = false;
		
		/*
		 * Continue looking on the board until a valid place is found position ship. 
		 */
		do
		{
			ship_horizontal = isShipHortizontal();
			
			/*
			 * Generate random coordinate.  If the coordinate will cause the ship
			 * to be placed partially off the board, then subtract the ship_size from
			 * the coordinate.
			 * Horizontal can have any row, but limited columns.  Vertical, vice versa.
			 */
			if (ship_horizontal)
			{
				row_num = determineStartPosition(game_board_size);
				col_num = (determineStartPosition(game_board_size - ship_size));
			}
			else 
			{
				row_num = (determineStartPosition(game_board_size - ship_size));
				col_num = determineStartPosition(game_board_size);
			}
		} while (isShipConflict(game_board, row_num, col_num, 
				               ship_size, ship_horizontal));
		
		/*
		 * Place ship on the board with valid position.
		 */
		for (int i = 0; i < ship_size; i++)
		{
			if (ship_horizontal)
			{
				setElementValue(game_board, row_num, col_num, ship_symbol);
				col_num++;
			}		
			else
			{
				setElementValue(game_board, row_num, col_num, ship_symbol);
				row_num++;
			}
		}				
	}
	
	
	/**
	 * isShipConflict method takes the starting coordinates then searches the 
	 * ArrayList horizontally or vertically looking to see if another ship has 
	 * already been placed in it's path.  If it does not find BOARD_WATER for all
	 * the coordinate, then the ship conflicts.   
	 * @param list				Name of the ArrayList
	 * @param row_num			Row value
	 * @param col_num			Column value
	 * @param ship_size			Size of ship
	 * @param ship_horizontal	Ships position: horizontal = true / vertical = false
	 * @return					True means another ship already exist in the element
	 */
	private static boolean isShipConflict(ArrayList<ArrayList<String>> list,
										  int row_num, int col_num, int ship_size,
			                              boolean ship_horizontal)
	{
		/*
		 * If horizontal is the "coin toss" then try to place the ship horizontal
		 * else try to place the ship vertical.
		 */
		if (ship_horizontal)
		{
			for (int i = 0; i < ship_size; i++)
			{
				if (!BOARD_WATER.equals(list.get(row_num).get(col_num)))
				{					
					return true;
				}
				col_num++;
			}
		}	
		else
		{
			for (int i = 0; i < ship_size; i++)
			{
				if (!BOARD_WATER.equals(list.get(row_num).get(col_num)))
				{					
					return true;
				}
				row_num++;
			}
		}
		
		return false;
	}
	
	
	/**
	 * getElementValue method gets the element value from an ArrayList.
	 * @param list			Name of the ArrayList
	 * @param row_num		Row value
	 * @param col_num		Column Value
	 * @return				String: element value of Arraylist
	 */
	private static String getElementValue(ArrayList<ArrayList<String>> list, 
            int row_num, int col_num)
	{		
		String element_value = list.get(row_num).get(col_num);
		return element_value;
	}

	
	/**
	 * setValue method is to set the value in the 2D ArrayList.
	 * @param list			Name of the ArrayList
	 * @param row_num		Row value
	 * @param col_num		Column value
	 * @param value			Value ArrayList element should be set to
	 */
	private static void setElementValue(ArrayList<ArrayList<String>> list, 
			                    int row_num, int col_num, String value)
	{
	     list.get(row_num).set(col_num, value);
	}
	
	
	/**
	 * determineStartPosition method is used to bring back one randomly generated 
	 * coordinate on the board.
	 * @return		int: coordinate
	 */
	private static int determineStartPosition(int random_number_max)
	{
		int position = randomNumber(random_number_max);

		return position;
	}
	
	
	/**
	 * isShipHoritzonal method determines if the ship will be placed horizontally
	 * on the board or not.
	 * @return		boolean: returns true if horizontal, false if vertical
	 */
	private static boolean isShipHortizontal()
	{	
		if (randomNumber(2) == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 * randomNumber method generates a random integer.
	 * @param max_random_numbers	Max range of numbers Ex: 2 = 0,1
	 * @return						int: Randomly generated integer
	 */
	private static int randomNumber(int max_random_numbers)
	{
		SecureRandom randomNumbers = new SecureRandom();
		int random_num = randomNumbers.nextInt(max_random_numbers);
		
		return random_num;
	}
	
	
	/**
	 * printGameBoard method prints the current game board to the console
	 * @param list		Two dimensional table
	 */
	private static void printGameBoard(ArrayList<ArrayList<String>> list, boolean supress_ships)
	{
		int row_num = 0;
		
		System.out.println("");
		
		for(int i = 0; i <= game_board_size; i++)
		{
			System.out.print(colNumber[i] + " ");
		}
		for(ArrayList<String> lists: list)
		{
			System.out.print("\n" + rowLetter[row_num++] + " | ");
			for(String element: lists)
			{
				/*
				 * Suppresses ship values if indicated
				 */
				if ((supress_ships == true &&
				     element.equals(CARRIER_SYMBOL)) ||
				    (supress_ships == true &&
 			 	     element.equals(BATTLESHIP_SYMBOL)) ||
		 		    (supress_ships == true &&
	 				 element.equals(DESTROYER_SYMBOL)) ||
				    (supress_ships == true &&
					 element.equals(SUBMARINE_SYMBOL)) ||
				    (supress_ships == true &&
				     element.equals(PATROL_SYMBOL)))
					System.out.print("  | ");
				else
					System.out.print(element + " | ");
			}
			printGameBoardLine();
		}		
	}	
	
	
	/**
	 * printGameBoard method prints out the game board and it's contents
	 */
	private static void printGameBoardLine ()
	{		
		System.out.print("\n");
		for (int i = 0; i <= ((game_board_size * 4)+2);i++) 
		{
			System.out.print("-");
		}
	}
	
	
	/**
	 * setGameBoard method is the setter method for external class.  It builds the 
	 * board and places the ships on it.  
	 * @param board_size		Size of game board
	 */
	public static void setGameBoard(int board_size)
	{
		game_board_size = board_size;
		buildGameBoard();
	}
	
	
	/**
	 * getGameBoard method is the getter method for printing the current game board. 
	 * @param reveal_ships		Value stating if ship symbols should be revealed 
	 */
	public static void getGameBoard(boolean reveal_ships)
	{
		printGameBoard(game_board, reveal_ships);
	}
	
	
	/**
	 * getGameBoardElementValue returns the specific element value which can 
	 * be used to determine if the element contains a ship.
	 * @param row_num		Row value
	 * @param col_num		Column value
	 * @return				String: The value of the element
	 */
	public static String getGameBoardElementValue(int row_num, int col_num)
	{
		String board_space_value = getElementValue(game_board, row_num, col_num);		
		return board_space_value;
	}
	
	
	/**
	 * setGameBoardElementValue method is used to set a game board elements to either HIT "X"
	 * or MISS "-".  Otherwise an exception is thrown.  
	 * @param row_num		The row element number
	 * @param col_num		The column element number
	 * @param value			The set value - either "X" nor "-".
	 * @throws Exception	Set value was not either "X" nor "-".
	 */
	public static void setGameBoardElementValue(int row_num, int col_num, String value) throws
	Exception
	{
		if (value != BOARD_MISS && value != BOARD_HIT)
		{
			throw new Exception("Exception thrown in setGameBoardElementValue: " + value);
		}		
		
		setElementValue(game_board, row_num, col_num, value);		
	}
	
	
	/**
	 * getMaxRowValue method returns the last letter of the game board which is determined by
	 * the difficulty level the user chose. 
	 * @return		String: The last letter on the game board
	 */
	public static String getMaxRowValue()
	{
		return rowLetter[game_board_size];
	}
	
	
	/**
	 * getRowValue method returns the letter value of a row.
	 * @param index		Numeric row value
	 * @return			String: row letter derived from numeric row value 
	 */
	public static String getRowValue(int index)
	{
		return rowLetter[index];
	}
	
	
	/**
 	 * getGameBoardSize method returns the size of the game board determined
	 * by the user's difficulty choice.
	 * @return			int: the size of the game board
	 */
	public static int getGameBoardSize()
	{
		return game_board_size;
	}
	
	
	/**
	 * setShipHit method marks a ship's element as hit on the ArrayList.
	 * @param ship_symbol	Ship's symbol
	 * @throws Exception	When ship's symbol is not valid
	 */
	public static void setShipHit(String ship_symbol) throws Exception
	{
		switch (ship_symbol)
		{
		case GameBoard.CARRIER_SYMBOL:
			car.setShipHit();
			break;
		case GameBoard.BATTLESHIP_SYMBOL:
			bat.setShipHit();
			break;
		case GameBoard.DESTROYER_SYMBOL:
			des.setShipHit();
			break;
		case GameBoard.SUBMARINE_SYMBOL:
			sub.setShipHit();
			break;
		case GameBoard.PATROL_SYMBOL:
			pat.setShipHit();
			break;
		default:
			throw new Exception("Exception thrown in setShipHit: " + ship_symbol);				
		}
	}
	
	
	/**
	 * getShipSunkStatus method returns the status if a given ship is sunk.
	 * @param ship_symbol	Ship's symbol
	 * @return				boolean: value stating if ship is sunk or not
	 */
	public static boolean getShipSunkStatus(String ship_symbol)
	{
		switch (ship_symbol)
		{
		case GameBoard.CARRIER_SYMBOL:
			return car.isSunk();
		case GameBoard.BATTLESHIP_SYMBOL:
			return bat.isSunk();
		case GameBoard.DESTROYER_SYMBOL:
			return des.isSunk();
		case GameBoard.SUBMARINE_SYMBOL:
			return sub.isSunk();
		case GameBoard.PATROL_SYMBOL:
			return pat.isSunk();				
		default:
			return false;
		}
	}
	
	
	/**
	 * getShipType method returns the type of ship.
	 * @param ship_symbol	Ship's symbol
	 * @return				String: ship's type
	 */
	public static String getShipType(String ship_symbol)
	{
		switch (ship_symbol)
		{
		case GameBoard.CARRIER_SYMBOL:
			return car.getType();
		case GameBoard.BATTLESHIP_SYMBOL:
			return bat.getType();
		case GameBoard.DESTROYER_SYMBOL:
			return des.getType();
		case GameBoard.SUBMARINE_SYMBOL:
			return sub.getType();
		case GameBoard.PATROL_SYMBOL:
			return pat.getType();				
		default:
			return "Other";
		}
	}
	
	/**
	 * getNumberOfShipsLeft method returns how many ships are still afloat.
	 * @return			int: number of remaining ships not sunk
	 */
	public static int getNumberOfShipsLeft()
	{
		int total_unsunk_ships = 5;
		
		if (car.isSunk()) 
			total_unsunk_ships--;
		if (bat.isSunk()) 
			total_unsunk_ships--;
		if (des.isSunk()) 
			total_unsunk_ships--;
		if (sub.isSunk()) 
			total_unsunk_ships--;
		if (pat.isSunk()) 
			total_unsunk_ships--;
	
		return total_unsunk_ships;
	}
}
