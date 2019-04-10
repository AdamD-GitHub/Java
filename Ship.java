/**
 * 
 * @author Adam D
 * Date:   11/16/2018
 * 
 * Class Name: Ship object
 * Class Purpose:
 *       This class is the blue print for the ship objects.  Each ship 
 *       will have an object which stores the name, type and condition of 
 *       each ship.
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

public class Ship 
{
	private String name;
	private String type;
	private boolean sunk;
	private int hits_left;
	
	
	/**
	 * Constructor for object class.
	 * @param name		Ship's name
	 * @param type		Ship's type
	 * @param size		Ship's size
	 */
	public Ship (String name, String type, int size)
	{
		this.name = name;
		this.type = type;
		this.hits_left = size;
		this.sunk = false;
	}

	
	/**
	 * isSink method returns if the ship has been sunk.
	 * @return		boolean value if ship is sunk
	 */
    public boolean isSunk() 
    {
        return sunk;
    }

    
	/**
	 * getType method returns the type of ship.
	 * @return		String name of ship
	 */
    public String getType() 
    {
        return type;
    }
    
    /**
     * setShipHit method is used to adjust the remaining hits left on a ship.
     * If a ship has been sunk a sunk message is displayed along with the name
     * of the ship.
     */
    public void setShipHit() 
    {
        hits_left--;
    	if(hits_left == 0) 
        {
            sunk = true;
            System.out.println("*****************************************");
            System.out.println(" !!! You sunk the " + type + " " + name + "!!!");
            System.out.println("*****************************************");
            return;
        }
    }
}
