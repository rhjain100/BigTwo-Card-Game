/**
 * 
 * Pair class is a subclass of Hand which is used to model a Pair hand with methods to check if the hand is a valid pair type, and also return the top card of the hand if it is a valid pair
 * 
 * 
 * @author Rishabh
 *
 */

public class Pair extends Hand{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that helps in creating a Pair object holding the pair hand of the specific player and cards played by that player 
	 * 
	 * @param player
	 * 		CardGamePlayer object containing specified player with the current hand 
	 * 
	 * @param cards
	 * 		CardList object containing list of cards being played by the active player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(1);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size()==2) {
			if(this.getCard(0).getRank()==this.getCard(1).getRank()) {
				return true;
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		String s = "Pair";
		return s;
	}

}
