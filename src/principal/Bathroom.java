package principal;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Class to represent a Bathroom
 * A Bathroom have a capacity, a current sex on bathroom, the set of people using the bathroom,
 * 	a semaphore to access the bathroom, a semaphore to enter in the bathroom, and a semaphore to exit the bathroom.
 * 
 * @author Erick O. Silva, Luis E. A. Silva
 * @version 2018.05.23
 */
public class Bathroom 
{
	// Capacity of bathroom
	private final int capacity;
	
	// Current sex on bathroom
	private Sex currentSex;
	
	// Set of people on bathroom
	private Set<Person> currentPeople;
	
	// Control the access to Set
	private Semaphore semaphore;
	
	// Control the enter of people of Set
	private Semaphore enterControl;
	
	// Control the exit of people of Set
	private Semaphore exitControl;
	
	/**
	 * Constructs a Bathroom.
	 * 
	 * @param capacity The capacity of the bathroom
	 */
	public Bathroom( int capacity )
	{
		this.capacity = capacity;
		this.currentSex = Sex.NONE; // Null sex
		this.currentPeople = new LinkedHashSet<Person>(); // Inits the Set
		this.semaphore = new Semaphore( this.capacity, true ); // Init the semaphore
		this.enterControl = new Semaphore( 1, true ); // Init the semaphore
		this.exitControl = new Semaphore( 1, true ); // Init the semaphore
		
	}
	
	/**
	 * @brief Control the people who wants to enter in the bathroom.
	 * 
	 * @param person Person who wants to enter in bathroom
	 * 
	 * @return true if the person had entered on bathroom, false in otherwise
	 */
	public boolean enterPerson( Person person )
	{
		// Try to acquire the Bathroom semaphore
		try
		{
			this.semaphore.acquire();
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		
		// In case of empty bathroom, set a the new current sex 
		//  	as the sex of this person who wants to enter
		if( currentPeople.size() == 0 )
		{
			this.currentSex = person.getSex();
		}
		
		// Try to acquire the semaphore to enter, because only 1 person could be 
		// 		enter in the same moment
		try 
		{
			this.enterControl.acquire();
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		
		// In case the bathroom is not full, the person is not on bathroom yet and 
		// 		the sex is the same of the people who is on bathroom right now.
		if (  !(currentPeople.size() == this.capacity ) 
			    && !( this.currentPeople.contains(person) ) 
				&& this.currentSex.equals( person.getSex() ) )
		{
			
			// If the person had success to enter on bathroom, then prints a message
			// 		showing the name, the of the person
			// 		and the number of people on bathroom
			if( this.currentPeople.add( person ) )
			{
				if( this.currentSex.equals( Sex.WOMAN ) )
				{
					System.out.printf( "The woman %s entered on bathroom, now have %d woman(s) on bathroom!!\n", person.getName(), this.currentPeople.size() );
				} else 
				{
					System.out.printf( "The man %s entered on bathroom, now have %d man(s) on bathroom!!\n", person.getName(), this.currentPeople.size() );
				}
			}
			// After entering release the enter semaphore
			this.enterControl.release();
			// End of successful entering
			return true; 
		}
		// In case the Bathroom is full, then could not enter right now
		else if ( currentPeople.size() == this.capacity )
		{
			System.out.printf( "The Bathroom is full now!!!\n" );
			this.semaphore.release();	
		}
		// In case the Person could not enter on Bathroom, then release the Bathroom semaphore
		else {
			this.semaphore.release();					
		}
		// After entering release the enter semaphore
		this.enterControl.release();
		// Return false to inform the error to enter in the Bathroom
		return false;
	}
	
	/**
	 * @brief Exits a person of the bathroom
	 * 
	 * @param person Person who wants to exit
	 */
	void exitPerson( Person person )
	{
		// Release the Bathroom semaphore
		this.semaphore.release();
		// Try to acquire the exit semaphore, 
		// 		because only 1 person could be left the Bathroom
		//		in the same time
		try
		{
			this.exitControl.acquire();
		} catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
		
		// In case the person has successful removed of the currentPerson's set
		// 		showing a message informing who person left the Bathroom right now
		if( currentPeople.remove( person ) )
		{
			System.out.printf( "%s is lefting the bathroom right now!!\n", person.getName() );
		}
		// Release the exit semaphore
		this.exitControl.release();
		
		// In case the Bathroom is empty after the exit of this person, then
		// 		shows a message to inform this and set the null sex
		if( currentPeople.size() == 0 )
		{
			System.out.printf( "The Bathroom is empty now!!!\n" );
			this.currentSex = Sex.NONE;
		}
		
	}
}
