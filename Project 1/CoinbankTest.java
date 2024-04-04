/**
 * JUnit test class.  Use these tests as models for your own.
 */
import org.junit.*;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class CoinbankTest {
	
	@Rule // a test will fail if it takes longer than 1/10 of a second to run
 	public Timeout timeout = Timeout.millis(100); 

	/**
	 * Sets up a bank with the given coins
	 * @param pennies number of pennies you want
	 * @param nickels number of nickels you want
	 * @param dimes number of dimes you want
	 * @param quarters number of quarters you want
	 * @return the Coinbank filled with the requested coins of each type
	 */
	private Coinbank makeBank(int pennies, int nickels, int dimes, int quarters) {
		Coinbank c = new Coinbank();
		int[] money = new int[]{pennies, nickels, dimes, quarters};
		int[] denom = new int[]{1,5,10,25};
		for (int index=0; index<money.length; index++) {
			int numCoins = money[index];
			for (int coin=0; coin<numCoins; coin++) {
				c.insert(denom[index]);
			}
		}
		return c;
	}

	@Test // bank should be empty upon construction
	public void testConstruct() {
		Coinbank emptyDefault = new Coinbank();
		assertEquals(0, emptyDefault.get(1));
		assertEquals(0, emptyDefault.get(5));
		assertEquals(0, emptyDefault.get(10));
		assertEquals(0, emptyDefault.get(25));
	}
	

	@Test // inserting nickel should return true & one nickel should be in bank
	public void testInsertNickel_return()
	{
		Coinbank c = new Coinbank();
		assertTrue(c.insert(5));
		assertEquals(1,c.get(5));
	}

	@Test // inserting a 3 cent coin should return false
	public void testInsertInvalid(){
		Coinbank c = new Coinbank();
		assertEquals(false,c.insert(3));
	}

	@Test // getter should return correct values
	public void testGet()
	{
		Coinbank c = makeBank(0,2,15,1);
		assertEquals(0,c.get(1));
		assertEquals(2,c.get(5));
		assertEquals(15,c.get(10));
		assertEquals(1,c.get(25));
	}
	
	@Test // getter should not alter the bank
	public void testGet_contents()
	{
		Coinbank c = makeBank(0,2,15,1);
		c.get(1);
		c.get(5);
		c.get(10);
		c.get(25);
		String expected = "The bank currently holds $1.85 consisting of \n0 pennies\n2 nickels\n15 dimes\n1 quarters\n";
		assertEquals(expected,c.toString());
	}

	@Test // testing get with invalid denominations
	public void testGetWithInvalid(){
		Coinbank c = makeBank(0,2,15,1);
		assertEquals(-1,c.get(3));
	}

	@Test // test of remove
	public void testRemove_justEnough()
	{
		Coinbank c = makeBank(4,1,3,5);
		assertEquals(5,c.remove(25,5));
		String expected = "The bank currently holds $0.39 consisting of \n4 pennies\n1 nickels\n3 dimes\n0 quarters\n";
		assertEquals(expected,c.toString());
	}

	@Test // test removing more than available
	public void testRemove_more()
	{
		Coinbank c = makeBank(4,1,3,5);
		assertEquals(5,c.remove(25,1795));
		String expected = "The bank currently holds $0.39 consisting of \n4 pennies\n1 nickels\n3 dimes\n0 quarters\n";
		assertEquals(expected,c.toString());
	}

	@Test // test removing within limit
	public void testRemove_less(){
		Coinbank c = makeBank(4,1,3,5);
		assertEquals(3, c.remove(25, 3));
		String expected = "The bank currently holds $0.89 consisting of \n4 pennies\n1 nickels\n3 dimes\n2 quarters\n";
		assertEquals(expected, c.toString());
	}
	
	@Test // remove should not do anything if a 3-cent coin is requested
	public void testRemove_invalidCoin()
	{
		Coinbank c = makeBank(4,1,3,5);
		assertEquals(0,c.remove(3,1));
	}

	@Test // trying to remove invalid coins to see if it changes contents
	public void testRemove_invalidChangingContent(){
		Coinbank c = makeBank(4,1,3,2);
		c.remove(3,4);
		String expected = "The bank currently holds $0.89 consisting of \n4 pennies\n1 nickels\n3 dimes\n2 quarters\n";
		assertEquals(expected, c.toString());
	}

	@Test // trying to remove negative coins
	public void testRemove_negativeCoins(){
		Coinbank c = makeBank(4,1,3,5);
		assertEquals(0,c.remove(4,-1));
	}

	@Test // trying to remove negative coins and seeing if it changes the contents
	public void testRemove_negativeChangingContent(){
		Coinbank c = makeBank(4,1,3,2);
		c.remove(5,-4);
		String expected = "The bank currently holds $0.89 consisting of \n4 pennies\n1 nickels\n3 dimes\n2 quarters\n";
		assertEquals(expected, c.toString());
	}

	@Test // test the insert method, which in effect also tests the setter
	public void testInsert(){
		Coinbank c = makeBank(0,0,0,0);
		c.insert(1);
		assertEquals(1,c.get(Coinbank.PENNY_VALUE));
	}

	@Test // test the insert method when an invalid coin is sent, checking for the holder
	public void testInsert_invalidCoin(){
		Coinbank c = makeBank(0,0,0,0);
		c.insert(3);
		assertEquals(0,c.get(Coinbank.PENNY_VALUE));
		assertEquals(0,c.get(Coinbank.NICKEL_VALUE));
		assertEquals(0,c.get(Coinbank.DIME_VALUE));
		assertEquals(0,c.get(Coinbank.QUARTER_VALUE));
	}

	@Test // test the insert method when a negative coin is sent. This should behave just like invalid coin
	public void testInsert_negativeCoins(){
		Coinbank c = makeBank(0,0,0,0);
		c.insert(-3);
		assertEquals(0,c.get(Coinbank.PENNY_VALUE));
		assertEquals(0,c.get(Coinbank.NICKEL_VALUE));
		assertEquals(0,c.get(Coinbank.DIME_VALUE));
		assertEquals(0,c.get(Coinbank.QUARTER_VALUE));
	}

	@Test // test the insert method when an invalid coin is sent, checking for what insert returns
	public void testInsert_invalidCoinFromInsert(){
		Coinbank c = makeBank(0,0,0,0);
		assertEquals(false,c.insert(3));
	}

	@Test // test the insert method when a negative coin is sent, checking for what insert returns
	public void testInsert_negativeCoinFromInsert(){
		Coinbank c = makeBank(0,0,0,0);
		assertEquals(false,c.insert(-3));
	}

	@Test // test insert method to see if content changes for an invalid coin
	public void testInsert_invalidCoinContent(){
		Coinbank c = makeBank(5,3,3,1);
		c.insert(3);
		String expected = "The bank currently holds $0.75 consisting of \n5 pennies\n3 nickels\n3 dimes\n1 quarters\n";
		assertEquals(expected, c.toString());
	}

	@Test // test insert method to see if content changes for a negative coin
	public void testInsert_negativeCoinContent(){
		Coinbank c = makeBank(5,3,3,1);
		c.insert(-3);
		String expected = "The bank currently holds $0.75 consisting of \n5 pennies\n3 nickels\n3 dimes\n1 quarters\n";
		assertEquals(expected, c.toString());
	}
}
