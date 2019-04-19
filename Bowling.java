/**
 * Author:  Adam D
 * Date:    10/06/2018
 * 
 * Class Name: Bowling
 * Class Purpose:
 *       This class inputs bowling scores for 10 frames then displays the each roll per 
 *       frame, each frames total and a running total per frame.  
 *       
 *       An ArrayList (roll_score) is used to store the user's entered roll score.  
 *       The ArrayList will populate each frame with at least two scores.  If the user 
 *       enters a strike, then the STRIKE will be recorded for the first roll and a NA 
 *       will be recorded for the second roll.
 *       Example
 *       Frame  |  1  |  2  |  3  |  ...
 *       Roll   | 4 3 | X - | 6 / |  ... 
 *        
 * ************************************************************************************
 * ************************************ CHANGE LOG ************************************
 * Date: 01/01/2000 | Name:                        | ID:
 * -----------------------------------------------------------------------------------
 * Change Description:
 * 
 * ***********************************************************************************
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Bowling 
{	
	public static final String STRIKE  = "X";
	public static final String ROLL_NA = "-";
	public static final String SPARE   = "/";
	
	public static final int MAX_FRAME_SCORE = 10;
	public static final int MAX_FRAMES = 10;               
	
	
	/**
	 * Main process for bowling tracker.  This prompts the user to input bowling 
	 * scores then prints out each frame's results and a running total of the
	 * results.  
	 */
	public void bowlingScoreTracker() 
	{		
		// Instantiate an ArrayList to store scores by roll
		ArrayList <String>  roll_score    = new ArrayList <String>();
		// Instantiate an ArrayList to store running totals by frame
		ArrayList <Integer> running_total = new ArrayList <Integer>();
				
		displayWelcomeScreen();
		
		inputScores(roll_score);
	
		displayLine();
		displayFrameTitle();
		displayLine();
		displayResult(roll_score);
		displayLine();
		displayFrameScore(roll_score, running_total);
		displayLine();
		displayRunningTotal(running_total);
		displayLine();
		
		displayEndOfProgram();	
	}
	
	
	/**
	 * Collects the score for each roll from the user's input.  The values are stored in 
	 * an array.
	 * 	
	 * @param roll_score 	Array of user's entered bowling scores by roll
	 */
	public static void inputScores(ArrayList <String> roll_score)
	{
		Scanner input = new Scanner(System.in);
		String roll_score_in = "";
		
		for (int frame_count = 1; frame_count <= MAX_FRAMES; frame_count++)
		{
			int roll_count = 1;
						
			// Roll One input
			displayInputPrompt(frame_count, roll_count);
			roll_score_in = input.nextLine();
			
			// Check to see if input is valid.  If not display error and prompt again
			// until valid entry.
			while (!isInputValid(roll_score, roll_score_in, frame_count, roll_count))
			{
				displayInvalidInputError(roll_score_in, frame_count, roll_count);
				roll_score_in = input.nextLine();
			}
			
			roll_score.add(roll_score_in.toUpperCase());
			
			roll_count++;	
									
			// Roll Two	input					
			if 	(roll_score_in.equalsIgnoreCase(STRIKE) && frame_count != MAX_FRAMES)
			{
				roll_score.add(ROLL_NA);          // if strike, 2nd roll is NA unless 10th frame				
			}
			else
			{
				displayInputPrompt(frame_count, roll_count);
				roll_score_in = input.nextLine();
				
				while (!isInputValid(roll_score, roll_score_in, frame_count, roll_count))
				{
					displayInvalidInputError(roll_score_in, frame_count, roll_count);
					roll_score_in = input.nextLine();
				}							
						
				roll_score.add(roll_score_in.toUpperCase());				
			}
						
			roll_count++;	
			
			// Roll Three input (10th frame only)
			if 	(frame_count == MAX_FRAMES)
			{
				if ((roll_score.get(roll_score.size() - 2).equalsIgnoreCase(STRIKE)) ||
				    (roll_score.get(roll_score.size() - 1).equalsIgnoreCase(SPARE)))	
				{
					displayInputPrompt(frame_count, roll_count);
					roll_score_in = input.nextLine();
					
					while (!isInputValid(roll_score, roll_score_in, frame_count, roll_count))
					{
						displayInvalidInputError(roll_score_in, frame_count, roll_count);
						roll_score_in = input.nextLine();
					}			
							
					roll_score.add(roll_score_in.toUpperCase());				
				}
			}
							
			System.out.println(" ");

		}
	}
	
	
	/**
	 * Displays welcome screen to user
	 */
	public static void displayWelcomeScreen()
	{		
		System.out.println("*****************************************");
		System.out.println("*  Welcome to the Bowling Score Tracker *");
		System.out.println("*                                       *");
		System.out.println("*       Strike = X | Spare = /          *");
		System.out.println("*****************************************");
		System.out.println(" ");			
	}
	
	
	/**
	 * Prompts user to enter the roll score for a given frame
	 * 
	 * @param frame_count	bowling frame of game
	 * @param roll_count	bowling roll within frame
	 */			
	public static void displayInputPrompt(int frame_count, int roll_count)
	{
		System.out.println("Enter result for Frame " + frame_count + " Roll " + roll_count + ": ");
	}
	
	
	/**
	 * If the user's input is invalid, then notify user the input was invalid, include what data they
	 * entered and prompt the user to re-enter the roll data.  
	 * 
	 * @param roll_score_in	bowling roll score entered by user
	 * @param frame_count	bowling frame of game
	 * @param roll_count	bowling roll within frame
	 */
	public static void displayInvalidInputError(String roll_score_in, int frame_count, int roll_count)
	{		
		System.out.println("   INVALID VALUE: " + roll_score_in);
		System.out.println("Please re-enter result for Frame " + frame_count + " Roll " + roll_count
				                                               + ": ");
	}
	
	
	/**
	 * Checks the value of the bowling roll entered to determine if the input value is valid.  The 
	 * following checks are tested:
	 * 1) Check that only "X", "/" or "0-9" are used   
	 * 2) Check that roll 1 is NOT SPARE          
	 * 3) Check SPARE does not follow a STRIKE or SPARE in 10th frame
	 * 4) Check that SPARE is not back to back    
	 * 5) Check that SPARE does not follow STRIKE    
	 * 6) Check that STRIKE in 2nd roll of 10th frame only occurs after a previous STRIKE               
	 * 7) Check that the total of roll 1 and roll 2 do not exceed 10
	 * 
	 * @param roll_score   	Array of user's entered bowling scores by roll
	 * @param input        	user's input bowling score
	 * @param frame			bowling frame of game
	 * @param roll			bowling roll within frame
	 * @return          	is input valid
	 */	
	public static boolean isInputValid(ArrayList <String> roll_score, String input, int frame, int roll)
	{	
		int input_value = 0;
					
		try
		{
			input_value = Integer.parseInt(input);
			
			// 1) Score must be between 1-9, a SPARE or a STRIKE			
			if (input_value < 0 || input_value > 9)
			{				
				return false;
			}		
			else 
			{
				// 7) Check that the total of roll 1 and 2 do not exceed the MAX_FRAME_SCORE
				// 7) Also if = MAX_FRAME_SCORE, then invalid because it should be a SPARE 
				if (roll == 2 && frame != MAX_FRAMES &&
				   (input_value + Integer.parseInt(roll_score.get(roll_score.size() - 1)) 
				                                                     > MAX_FRAME_SCORE - 1))
				{
					return false;
				}
				
				// 7) Check that the total of roll 1 and 2 do not exceed the MAX_FRAME_SCORE
				//    for Frame 10
				if ((roll == 2 && frame == MAX_FRAMES) || (roll == 3 && frame == MAX_FRAMES))
				{
					if (!((roll_score.get(roll_score.size() - 1)).equalsIgnoreCase(STRIKE)) &&
					    !((roll_score.get(roll_score.size() - 1)).equalsIgnoreCase(SPARE)))
					{
						if (input_value + Integer.parseInt(roll_score.get(roll_score.size() - 1)) 
                            > MAX_FRAME_SCORE - 1)
						{
							return false;
						}		
					}
				}
			}
		}
		catch(NumberFormatException e)
		{
			if (input.equalsIgnoreCase(SPARE) || input.equalsIgnoreCase(STRIKE))
			{
				
				// 2, 4 & 5) First roll cannot be a SPARE
				if (roll == 1 && input.equalsIgnoreCase(SPARE))
				{
					return false;
				}
				
				// 5) Second roll of frame cannot be a STRIKE (except 10th frame)
				if (frame != MAX_FRAMES && roll == 2 && input.equalsIgnoreCase(STRIKE))			
				{
					return false;
				}
				
				// 6) Check that STRIKE in 2nd roll of 10th frame only occurs after a previous STRIKE 
				if (frame == MAX_FRAMES && roll == 2 && input.equalsIgnoreCase(STRIKE) &&
						!(roll_score.get(roll_score.size() - 1).equalsIgnoreCase(STRIKE)))
				{
					return false;
				}
				
				// 3) SPARE cannot follow a STRIKE or SPARE in 10th frame
				if ((frame == MAX_FRAMES && roll == 2 && input.equalsIgnoreCase(SPARE) &&
							roll_score.get(roll_score.size() - 1).equalsIgnoreCase(STRIKE)) ||
					(frame == MAX_FRAMES && roll == 3 && input.equalsIgnoreCase(SPARE) &&
							roll_score.get(roll_score.size() - 1).equalsIgnoreCase(STRIKE)) ||
				    (frame == MAX_FRAMES && roll == 3 && input.equalsIgnoreCase(SPARE) &&
				   			roll_score.get(roll_score.size() - 1).equalsIgnoreCase(SPARE)))
				{					
					return false;
				}
			}			
			else
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Displays a horizontal line for separation
	 * Ex: '---------------------------'
	 */
	public static void displayLine()
	{
		
		for (int i = 0; i < 101 ; i++)
		{
			System.out.print("-");
		}							
				
		System.out.printf("\n");
	}
	
	
	/**
	 * Displays the frame title (0-10) 
	 * Ex: 'Frame:        1  2  3  4 ...'
	 */
	public static void displayFrameTitle()
	{

		System.out.printf("Frame:\t\t");
		
		for (int frame_number = 1; frame_number <= MAX_FRAMES; frame_number++)		
		{
			System.out.printf("\t %d", frame_number);		
		}
							
		System.out.printf("\n");
	}
	

	/**
	 * Reads roll_score ArrayList and displays the score of each frame
	 * Ex: 'Result:        1 2  X  3 /  X ...'
	 * 
	 * @param roll_score	Array of user's bowling scores
	 */
	public static void displayResult(ArrayList <String> roll_score)
	{
		int frame_count   = 1; 
		
		System.out.printf("Result:\t\t");
		
		for (int i = 0; i < roll_score.size(); i=i+2)
		{
			
			if (frame_count == MAX_FRAMES) 
			{
				System.out.printf("\t%s", roll_score.get(i));
				System.out.printf(" %s", roll_score.get(i+1));
				try 
				{
					System.out.printf(" %s", roll_score.get(i+2));
					break;
				}
				catch (IndexOutOfBoundsException e)	
				{
					break;
				}
			}
			else
			{
				if (roll_score.get(i).equalsIgnoreCase(STRIKE))
				{
					System.out.printf("\t %s", roll_score.get(i));
				}
				else
				{
					System.out.printf("\t%s", roll_score.get(i));
					System.out.printf(" %s", roll_score.get(i+1));
				}
			}
			frame_count++;
		}		
		
		
		System.out.printf("\n");
	}
	
	
	/**
	 * Processes roll_score ArrayList of user's score input. It reads each score
	 * and calculates the frame's total score.  It also builds a running_total
	 * ArrayList for later use. This method assumes data has already been validated.
	 * 
	 * @param roll_score		Array of user's entered bowling scores by roll
	 * @param running_total		Array of each frame's running totals
	 */
	public static void displayFrameScore(ArrayList <String> roll_score, ArrayList <Integer> running_total)
	{
		int    roll_value_01 = 0,
		       roll_value_02 = 0,
		       roll_value_03 = 0,       // 10th frame calculation 
			   frame_count   = 1,
			   frame_total_score = 0,
		       running_total_score = 0; // Used to populate running_total ArrayList
		
		System.out.printf("Frame Score:\t");
		
		for (int i = 0; i < roll_score.size(); i=i+2)
		{
			int roll_1 = i;
			int roll_2 = i + 1;	
			int roll_3 = i + 2;    // 10th frame
			
			// Roll One Calculation / Display
			try 
			{
				roll_value_01 = Integer.parseInt(roll_score.get(roll_1));
			}
			catch (NumberFormatException e)
			{
				
				if (frame_count == MAX_FRAMES)
				{
					// Calculate STRIKE (10th frame)
					roll_value_01 = MAX_FRAME_SCORE;                             
				}
				else
				{
					if (roll_score.get(roll_1).equalsIgnoreCase(STRIKE))
					{
						// Calculate STRIKE (non-10th frame)
						roll_value_01 = MAX_FRAME_SCORE                            
									  + getNextRollScore (roll_score, roll_1, STRIKE)  
									  + getNextTwoRollScore (roll_score, roll_2);        
					}
				}
			}
			
			// Roll Two Calculation / Display
			try 
			{
				roll_value_02 = Integer.parseInt(roll_score.get(roll_2));
			}
			catch(NumberFormatException e)
			{
				
                if (frame_count == MAX_FRAMES)
				{
                	
					if (roll_score.get(roll_2).equalsIgnoreCase(SPARE))
					{
						// Calculate SPARE (10th frame)
					    roll_value_02 = (MAX_FRAME_SCORE - roll_value_01);
					}
					else
					{
						// Calculate STRIKE (10th frame)
						roll_value_02 = MAX_FRAME_SCORE; 
					}
				}			
				else					
				{
					
					if (roll_score.get(roll_2).equalsIgnoreCase(SPARE))
					{
						// Calculate SPARE (non-10th frame)
						roll_value_02 = (MAX_FRAME_SCORE - roll_value_01) 
								      + getNextRollScore (roll_score, roll_2, SPARE); 
					}
					else 
					{
						// Calculate STRIKE (non-10th frame)
						roll_value_02 = 0;                                    
					}
				}	
			}
			
			// Roll Three Calculation / Display (10th frame only)
			// Max score for 10th frame = 30
			if (frame_count == MAX_FRAMES)
			{
				try 
				{
					roll_value_03 = Integer.parseInt(roll_score.get(roll_3));
				}
				catch(IndexOutOfBoundsException e)		
				{
					// No 10th frame 3rd roll
					roll_value_03 = 0;					
				}			
				catch(NumberFormatException e)
				{
					
					if (roll_score.get(roll_3).equalsIgnoreCase(SPARE))
					{
						// Calculate SPARE (10th frame)
						roll_value_03 = MAX_FRAME_SCORE 
								      - Integer.parseInt(roll_score.get(roll_2));					
					}
					else
					{
						// Calculate STRIKE (10th frame)
						roll_value_03 = MAX_FRAME_SCORE;    
					}
				}
			}
			
			// Calculate frame and running totals
			frame_total_score    = roll_value_01 + roll_value_02 + roll_value_03;
			running_total_score += frame_total_score; 
	
			running_total.add(running_total_score);

			System.out.printf("\t%3s", frame_total_score);
			
			// break needed for 10th frame logic if a third roll was recorded
			if (frame_count == MAX_FRAMES)
			{
				break;
			}
			else 
			{
	 			frame_count++;							
			}
			
		}		
		
		System.out.printf("\n");
	}
	
	
	/**
	 * Processes the running_total array.  It reads and outputs the values of the array per frame. 
	 * 
	 * @param running_total		Array of each frame's running totals
	 */
	public static void displayRunningTotal(ArrayList <Integer> running_total)
	{
		System.out.printf("Running Total:\t");
		
		for (int i = 0; i < running_total.size(); i++)
		{
			System.out.printf("\t%3s", running_total.get(i));
		}	
		
		System.out.printf("\n");
	}
	
	
	/**
	 * Calculates the score one frame ahead.  It is used to calculate STRIKE and SPARE scores.  
	 * The roll parameter is to tell the method which array record to begin calculating.  When
	 * called for a strike, increment next_index + 2 (except 10th frame - skip second roll for 
	 * strike).  When called for a spare, increment next_index + 1 
	 * 
	 * @param roll_score	Array of user's entered bowling scores by roll
	 * @param roll			The frame's roll to be calculated.  (STRIKE roll_1 / SPARE roll_2)
	 * @param roll_type		Which calculation type is needed, STRIKE or SPARE
	 * @return				Returns the next roll value
	 */
	public static int getNextRollScore(ArrayList <String> roll_score, int roll, String roll_type)
	{
	    int next_roll_score = 0;
	    
	    if (roll_type.equalsIgnoreCase(STRIKE))
	    {
	    	roll+= 2;
	    }
	    else
	    {
	    	roll++;
	    }
	    
		try 
		{
			next_roll_score = Integer.parseInt(roll_score.get(roll));
		}
		catch(NumberFormatException e)
		{		
			
			if (roll_score.get(roll).equalsIgnoreCase(STRIKE))
			{
				next_roll_score = MAX_FRAME_SCORE;
			}
			else 	
			{
				// Input prevents back to back "/", so this should never happen
				System.out.println ("!!! Invalid Next Roll Data !!!");
			}
		}	    
	    
		return next_roll_score;
	}
	
	
	/**
	 * Calculates the score two frames ahead.  This is for strike calculations only.
	 * 
	 * @param roll_score 	Array of user's entered bowling scores by roll
	 * @param roll			The frame's roll to be calculated.  (STRIKE roll_1 only applies)
	 * @return				Returns the next two roll's value
	 */
	public static int getNextTwoRollScore(ArrayList <String> roll_score, int roll)
	{
	    int next2_roll_score = 0;		
	    
	    roll+= 2;  // this is for a strike only
	    
		try 
		{
			next2_roll_score = Integer.parseInt(roll_score.get(roll));
		}
		catch(NumberFormatException e)
		{	
			
			switch(roll_score.get(roll))
			{
			case ROLL_NA:
				{
					try 
					{
						// second roll invalid due to strike, check first roll in next frame (+1)
						next2_roll_score = Integer.parseInt(roll_score.get(roll + 1)); 
					}
					catch(NumberFormatException e2)   
					{				
						// means next roll/frame was also strike 
						next2_roll_score = MAX_FRAME_SCORE;   
					}
					break;
				}				
			case SPARE:
				{
					try 
					{
						// Calculating pins in second roll when spare.  We need previous pins 
						// for calculation 
						// Example.  "3 | /"  score (=7) = 10 - 3 
						next2_roll_score = MAX_FRAME_SCORE 
										- Integer.parseInt(roll_score.get(roll - 1));
					}
					catch(NumberFormatException e3)	
					{
						// Input prevents "/" before "X", so this should never happen
						System.out.println("!!! Invalid Next Two Roll Data !!!");
					}
					break;
				}
			default:
				{
					// This is for when the 9th frame looks into the 10th frame and second 10th
					// frame roll is a strike
					next2_roll_score = MAX_FRAME_SCORE; 
				}
			}						
		}	    
		
		return next2_roll_score;
	}	
	
	public static void displayEndOfProgram()
	{
		System.out.println(" ");
		System.out.println("----------------------");
		System.out.println("*** END OF PROGRAM ***");
	}
	
}
