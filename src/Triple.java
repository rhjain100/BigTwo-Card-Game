
/**
 * 
 * Triple class is a subclass of Hand which is used to model a Triple hand with methods to check if the hand is a valid Triple type, and also return the top card of the hand if it is a valid Triple
 * 
 * 
 * @author Rishabh
 *
 */
public class Triple extends Hand {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that helps in creating a Triple object holding the Triple hand of the specific player and Triple played by that player 
	 * 
	 * @param player
	 * 		CardGamePlayer object containing specified player with the current hand 
	 * 
	 * @param cards
	 * 		CardList object containing list of cards being played by the active player
	 * 
	 */
	
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(2);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size()==3) {
			int a = this.getCard(0).getRank();
			int b = this.getCard(1).getRank();
			int c = this.getCard(2).getRank();
			if(a == b && b == c) {
				return true;
			}
		}
		return false; 
			
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		String a = "Triple";
		return a;
	}

}
