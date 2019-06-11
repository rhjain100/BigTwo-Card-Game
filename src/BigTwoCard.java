/**
 * 
 * The BigTwoCard class is a subclass of Card which allows us to model the cards which are being used in the BigTwo card game. 
 * 
 * @author Rishabh
 *
 */

public class BigTwoCard extends Card{

	private static final long serialVersionUID = 1L;
	/**
	 * This constructor builds card with a specified suit and rank
	 * 
	 * @param suit an int value between 0 and 3 representing the suit of a card: 0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * 
	 * 
	 * @param rank an int value between 0 and 12 representing the rank of a card: 0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 = 'Q', 12 = 'K'
	 *           
	 *            
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	/**
	 *  Method for comparing the card to a specified card using BigTwo logics 
	 *  
	 *  @param card the card to be compared to using BigTwo Logics
	 * 
	 */
	public int compareTo(Card card) {
		int rank1 = this.getRank();
		int rank2 = card.getRank();
		if (rank1==0) { rank1=13; }
		if (rank1==1) { rank1=14; }
		if (rank2==0) { rank2=13; }
		if (rank2==1) { rank2=14; }
		if (rank1 > rank2) {
			return 1;
		} else if (rank1 < rank2) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}

