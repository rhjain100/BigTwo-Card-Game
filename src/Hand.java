import java.util.Arrays;

/**
 * The Hand public class is a subclass of CardList .It models the hand of cards and has private instance variables storing the cards played and methods which check if the played hand is valid, gets the type of hand and also gets the top card
 * 
 *  
 * @author Rishabh
 *
 */

public abstract class Hand extends CardList{

	private static final long serialVersionUID = 1L;
	/**
	 * Instance variable storing the player playing the current hand 
	 */
	private CardGamePlayer player;
	/**
	 * Constructor that helps in modeling specific player's hand and list of cards
	 * 
	 * @param player
	 * 		CardGamePlayer object which containing list of players
	 * 
	 * @param cards
	 * 		CardList object containing list of cards played
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for( int i=0; i< cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	/**
	 * 
	 * Method used to retrieving the player of the active hand 
	 * 
	 * 
	 * @return CardGamePlayer player containing the active player
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	/**
	 * 
	 * Method for retrieving top card of the current hand. It is overridden by subclasses
	 * 
	 * 
	 * @return null in Hand class otherwise a card in subclasses
	 * 
	 */
	public Card getTopCard() {
		return null;
	}
	/**
	 * 
	 * Method used for checking if the current hand beats the last hand 
	 * 
	 * @param hand hand to check if it is beaten
	 * 
	 * @return true if the current hand beats the last hand else false
	 * 
	 */
	public boolean  beats(Hand hand) {
		String type1 = this.getType();
		String type2 = hand.getType();
		String[] types = {"Single", "Pair", "Triple", "Straight", "Flush", "FullHouse", "Quad", "StraightFlush"};
		int x =0, y=0;
		int j=0;
		while(j<8) {
			if(type1==types[j]) {
				x = j;
			}
			if(type2==types[j]) {
				y = j;
			}
			j++;
		}
		if(x<y) {
			return false;
		}
		else if(x>y) {
			return true;
		}
		else {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * 
	 * Method used to check if the particular hand is a valid hand or not
	 * 
	 * @return true or false whether hand is of the particular type
	 * 
	 */
	public abstract boolean isValid();
	/**
	 * Method to get the type of the Hand 
	 * 
	 * @return a string which states type of the particular hand. 
	 */
	
	public abstract String getType();
}
