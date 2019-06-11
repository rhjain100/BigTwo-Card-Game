import java.util.Arrays;
/**
 * 
 * Straight class is a subclass of Hand which is used to model a Straight hand with methods to check if the hand is a valid straight type, and also return the top card of the hand if it is a valid Straight
 * 
 * 
 * @author Rishabh
 *
 */

public class Straight extends Hand {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that helps in creating a Straight object holding the Straight hand of the specific player and single played by that player 
	 * 
	 * @param player
	 * 		CardGamePlayer object containing specified player with the current hand 
	 * 
	 * @param cards
	 * 		CardList object containing list of cards being played by the active player
	 * 
	 */
	public Straight(CardGamePlayer player, CardList cards) {
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
		if(ranks[4]>=13){
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
		int[] ranks = new int[5];
		for(int i=0; i<5; i++) {
			if(this.getCard(i).rank==0) {
				ranks[i]=13;
			} else if(this.getCard(i).rank==1) {
				ranks[i]=14;
			} else {
				ranks[i]=this.getCard(i).rank;
			}
		}
		Arrays.sort(ranks);
		for(int i=0; i<4; i++) {
			if((ranks[i]+1)!=(ranks[i+1])) {
				return false;
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		String a = "Straight";
		return a;
	}

}
