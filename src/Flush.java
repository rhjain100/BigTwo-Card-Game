import java.util.Arrays;

/**
 * 
 * Flush class is a subclass of Hand which is used to model a Flush hand with methods to check if the hand is a valid Flush type, and also return the top card of the hand if it is a valid Flush 
 * 
 * 
 * @author Rishabh
 *
 */



public class Flush extends Hand{

	private static final long serialVersionUID = 1L;

	/**
	 * This constructor helps creating a Flush object holding the flush hand of specific player and cards played by that player 
	 * 
	 * @param player
	 * 		CardGamePlayer object containing specified player with the current hand 
	 * 
	 * @param cards
	 * 		CardList object containing list of cards being played by the active player
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		int[] ranks = new int[5];
		for(int i=0; i<5; i++) {
			if(this.getCard(i).getRank()==0) {
				ranks[i]=13;
			} else if(this.getCard(i).getRank()==1) {
				ranks[i]=14;
			} else {
				ranks[i]=this.getCard(i).getRank();
			}
		}
		Arrays.sort(ranks);
		if(ranks[4]>13){
			ranks[4]-=13;
		}
		int x=0;
		for(int i = 0; i<5; i++) {
			if(this.getCard(i).getRank()==ranks[4]) {
				x=i;
			}
		}
		return this.getCard(x);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size()!=5)
			return false;
		if(this.getCard(0).getSuit()==this.getCard(1).getSuit() && this.getCard(1).getSuit() == this.getCard(2).getSuit() && this.getCard(2).getSuit() == this.getCard(3).getSuit() && this.getCard(3).getSuit() == this.getCard(4).getSuit()) {
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		String s = "Flush";
		return s;
	}

}
