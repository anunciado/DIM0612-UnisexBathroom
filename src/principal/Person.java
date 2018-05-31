package principal;

import java.util.Random;

/**
 * Class to represent a Person. 
 * A Person have Sex, time of use and a reference to a Bathroom.
 * 
 * @author Erick O. Silva, Luis E. A. Silva
 * @version 2018.05.20
 */
public class Person extends Thread
{
	
	// Bathroom to use
	private final Bathroom bathroom;
	
	// Person sex
	private final Sex sex;
	
	// Time on bathroom
	private int time;
	
	// The of the Person
	private String name;

	// Maximum time, in seconds, of a person on bathroom
	private static final int MAX_TIME = 20;
	
	/**
	 * Contructs a Person
	 * 
	 * @param name Name of the person
	 * @param sex Enum for sex of the person (MAN or WOMAN)
	 * @param bathroom Bathroom requested to use by the person
	 */
	public Person(  String name, Sex sex, Bathroom bathroom )
	{
		super( name );
		this.sex = sex;
		this.bathroom = bathroom;
		this.time = new Random().nextInt( MAX_TIME ) + 1;
	}


	/**
	 * Gets the time that the Person expend on Bathroom
	 * 
	 * @return time of this Person expend on Bathroom
	 */
	public int getTime() 
	{
		return time;
	}

		
	/**
	 * Gets the sex of this Person
	 * 
	 * @return the sex of this Person
	 */
	public Sex getSex() 
	{
		return sex;
	}
	
	@Override
	public void run()
	{
		while( !this.bathroom.enterPerson( this ) );
		try {
			Person.sleep( this.time * 1000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.bathroom.exitPerson( this );
	}
	
}
