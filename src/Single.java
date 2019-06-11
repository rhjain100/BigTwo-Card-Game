/**
 * 
 * Single class is a subclass of Hand which is used to model a Single hand with methods to check if the hand is a valid single type, and also return the top card of the hand if it is a valid single
 * 
 * 
 * @author Rishabh
 *
 */

public class Single extends Hand{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that helps in creating a single object holding the single hand of the specific player and single played by that player 
	 * @param player
	 * 		CardGamePlayer object containing specified player with the current hand 
	 * @param cards
	 * 		CardList object containing list of cards being played by the active player
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		return this.getCard(0);
	}
	
	
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size()!=1)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		String a = "Single";
		return a;
	}

}
