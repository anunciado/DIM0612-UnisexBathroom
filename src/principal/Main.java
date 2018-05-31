package principal;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class to execute the Unissex Bathroom project
 * 
 * @author Erick O. Silva, Luis E. A. Silva
 * @version 2018.05.23
 */
public class Main 
{
	// Constant to number of people asking to use the Bathroom
	private static final int NUMBER_OF_PEOPLE = 20;
	
	public static void main(String[] args) throws InterruptedException 
	{
		Sex sex[] = { Sex.MAN, Sex.WOMAN };
		String name[] = { "John", "Mary" };
		int capacity;
		int number_woman = 0, number_man = 0; // Used to count the woman's and man's number on execution
		
		Scanner teclado = new Scanner( System.in );
		System.out.print( "Enter a capacity for the Bathroom: " );
		capacity = teclado.nextInt();
		teclado.close();
		
		Bathroom toilet = new Bathroom(capacity); // Instantiates the Bathroom
		
		ArrayList<Person> people = new ArrayList<Person>(); // Person's lists
		for( int i=0; i < NUMBER_OF_PEOPLE; i++ )
		{
			Person person;
			int rand = new Random().nextInt( 2 ); // Used to generate a random sex and name
			if( rand == 0 )
			{
				number_man++;
				person = new Person( name[ rand ]+number_man, sex[ rand ], toilet );
			} else
			{
				number_woman++;
				person = new Person( name[ rand ]+number_woman, sex[ rand ], toilet );
			}
			people.add( person ); // Add the person to list
			people.get(i).start(); // Start the person execution
		}
		
		for( int i=0; i<NUMBER_OF_PEOPLE; i++ )
		{
			people.get(i).join(); // Join all the person, to main wait the end of all threads
		}
	}
}
