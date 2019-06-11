import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * 
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. It is used to model a Big Two card game that supports 4 players playing over the internet. 
 * 
 * @author rishabhjain
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList <CardGamePlayer> playerList;
	private ArrayList <Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	
	/**
	 * 
	 * Checker for first player.
	 * 
	 */
	public static boolean firstPlayer = true;
	/**
	 * 
	 * Checker to check if move is legal or not.
	 * 
	 */
	public static boolean isLegal = true;
	/**
	 * 
	 * Number of passes
	 * 
	 */
	public static int numOfPasses=0;
	/**
	 * 
	 * Checker for game play, to show if game play is to be continued or not
	 * 
	 */
	public static boolean gamePlay =true;
	/**
	 * 
	 * Checker to check if three consecutive passes have been made or not
	 * 
	 */
	public static boolean passedThrice=false;

	
	/**
	 * 
	 *  A constructor for creating a Big Two client. 
	 * 
	 */
	public BigTwoClient() {
		handsOnTable = new ArrayList<Hand>();
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0; i<4; i++) {
			playerList.add(new CardGamePlayer());
		}
		table = new BigTwoTable(this);
		table.disable();
		makeConnection();
		table.repaint();
	}
	/**
	 * 
	 * Main method to create a new bigTwoClient object
	 * 
	 * @param args not being used
	 */
	public static void main( String[] args) {
		@SuppressWarnings("unused")
		BigTwoClient bigTwoClient = new BigTwoClient();
	}

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
		
	}

	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}

	@Override
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
	}

	@Override
	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP;
	}

	@Override
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = serverIP;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return serverPort;
	}

	@Override
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
	}

	@Override
	public void makeConnection() {
		// TODO Auto-generated method stub
		this.playerName = (String) JOptionPane.showInputDialog("Please enter your name:");
		if(playerName==null) {
			playerName = "Default";
		}
		setServerIP("127.0.0.1");
		setServerPort(2396);
		try {
			sock = new Socket(this.serverIP, this.serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			Runnable newJob = new ServerHandler();
			Thread newThread = new Thread(newJob); 
			newThread.start();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));

		} catch ( Exception ex ){
			ex.printStackTrace();
		}
		table.repaint();
	}

	@Override
	public synchronized void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		if(message.getType() == CardGameMessage.PLAYER_LIST) {
			if(message.getData()!=null) {
				for(int i=0; i<4; i++) {
					if(((String[])message.getData())[i]!=null) {
						this.playerList.get(i).setName(((String[])message.getData())[i]);
					}
				}
			}
			this.playerID = message.getPlayerID();
			table.repaint();
		} 
		
		else if(message.getType() == CardGameMessage.FULL) {
			playerID = -1;
			table.printMsg("Game Server is full!\n");
			table.repaint();
		}
		
		else if(message.getType() == CardGameMessage.QUIT){
			table.printMsg( this.playerList.get(message.getPlayerID()).getName() + " has left the game\n");
			playerList.get(message.getPlayerID()).setName("");
			if(!endOfGame()) {
				table.disable();
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				for(int i=0; i<4; i++) {
					playerList.get(i).removeAllCards();
				}
			}
			table.repaint();
		}
		
		else if(message.getType() == CardGameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
			table.printMsg("Player "  + playerList.get(message.getPlayerID()).getName() + " has joined the game\n");
		}
		
		else if(message.getType() == CardGameMessage.READY) {
			handsOnTable = new ArrayList <Hand>();
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!\n");
			table.repaint();
		} 
		
		else if(message.getType() == CardGameMessage.START) {
			this.deck = new BigTwoDeck();
			this.deck = (BigTwoDeck) message.getData();
			start(this.deck);
			table.printMsg("Game has started!!!\n");
			table.enable();
			table.repaint();
		}
		
		else if(message.getType() == CardGameMessage.MSG) {
			table.printChat((String)message.getData());
		}
		
		else if(message.getType() == CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(), (int[])message.getData());
			if(!endOfGame()) {
				if(currentIdx==playerID)
				{
					table.enable();	
					table.printMsg("Your turn:\n");
				}
				else
					table.printMsg(playerList.get(currentIdx).getName()+ "'s turn:\n");
			}
			table.repaint();
			
		}
		
		else {
			table.printMsg("Message type unknown"+ message.getType()+"\n");
			table.repaint();
		}
	}

	@Override
	public void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return numOfPlayers;
	}

	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return deck;
	}

	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return playerList;
	}

	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return handsOnTable;
	}

	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return currentIdx;
	}

	@Override
	public void start(Deck deck) {
		// TODO Auto-generated method stub
		handsOnTable.clear();
		
		for(int i=0; i<4; i++) {
			playerList.get(i).removeAllCards();
			for(int j=0; j<13; j++) {
				Card card = deck.removeCard(0);
				playerList.get(i).addCard(card);
				if(card.getSuit()==0 && card.getRank()==2) {
					currentIdx=i;
				}
				
			}
		}
		for(int i=0; i<4; i++) {
			playerList.get(i).getCardsInHand().sort();
		}
		
		table.setActivePlayer(currentIdx);
		table.repaint();
	}

	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx);
		sendMessage(message);
	}

	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		Hand checkHand = null;
		CardList cardList = new CardList();
		CardList hand = new CardList();
		CardGamePlayer player = new CardGamePlayer();
		player = playerList.get(currentIdx);
		cardList = player.getCardsInHand();
		hand = player.play(cardIdx);
		table.repaint();
		if(currentIdx == playerID) {
			//table.printMsg("boom0");
			if(hand == null && firstPlayer == true) {
				isLegal = false;
				//table.printMsg("Boom1");
			}
			
			else {
				
				if(hand!=null) {
					//table.printMsg("boom!null");
					checkHand = composeHand(player,hand);
					if(firstPlayer==true && checkHand!=null) {
						//table.printMsg("boom!null2");
						if(checkHand.contains(new Card(0,2)) && gamePlay==true){
							gamePlay=false;
							isLegal=false;
						}
						if(!gamePlay) {
							//table.printMsg("Player "+playerID +"'s turn:");
							table.printMsg("{" + checkHand.getType()+"}"+ checkHand.toString()+"\n");
							handsOnTable.add(checkHand);
							numOfPasses=0;
							if(!passedThrice) {
								firstPlayer= false;
							} else {
								firstPlayer = true;
							}
							currentIdx=(currentIdx+1)%4;
							table.setActivePlayer(currentIdx);
							for(int i=0; i<checkHand.size(); i++) {
								cardList.removeCard(checkHand.getCard(i));
							}
							passedThrice=false;	
							isLegal=true;
							//table.printMsg("boom2");
						}
					}
					else if(handsOnTable.size()!=0 && checkHand!=null) {
						int m=handsOnTable.size()-1;
						if(checkHand.beats(handsOnTable.get(m)) && handsOnTable.get(m).size() == checkHand.size()) {
							//table.printMsg("boom3");
							//table.printMsg("Player "+playerID +"'s turn:");
							table.printMsg("{" + checkHand.getType() + "}" + checkHand.toString() + "\n");
							handsOnTable.add(checkHand);
							numOfPasses=0;
							isLegal=true;
							if(!passedThrice) {
								firstPlayer=false;
							} else {
								firstPlayer=true;
								passedThrice=false;
							}
							currentIdx=(currentIdx+1)%4;
							table.setActivePlayer(currentIdx);
							for(int i=0; i<checkHand.size(); i++) {
								cardList.removeCard(checkHand.getCard(i));
							}	
						} else {
							isLegal=false;
						}
					} else {
						isLegal = false;
					}		
						
				} else {
						numOfPasses+=1;
						//table.printMsg("Player "+playerID +"'s turn:");
						table.printMsg("{Pass}\n");
						currentIdx = (currentIdx+1)%4;
						if(numOfPasses==3) {
							numOfPasses=0;
							passedThrice=true;
							isLegal=true;
						}
						table.setActivePlayer(currentIdx);
						if(!passedThrice) {
							firstPlayer=false;
						} else {
							firstPlayer = true;
						}
						passedThrice=false;	
				
				}
			}
			
			if(gamePlay==true) {
				//table.printMsg("boom5");
				isLegal=false;
			}
			
			if(!isLegal) {
				//table.printMsg("boom6");
				if(hand!=null) {
					//table.printMsg("boom7");
					//table.printMsg("Player "+playerID +"'s turn:");
					table.printMsg(hand.toString() + " <== Not a legal move!!!\n");
				} else {
					//table.printMsg("Player "+playerID +"'s turn:");
					table.printMsg("Not a legal move!!!\n");
					
				}
				isLegal=true;
			}
			
			
		}	
		if(endOfGame() == true) {
			table.disable();
			String GameEnds = "Game Ends\n";
			
			
			for(int i = 0; i < playerList.size();i++)
			{
					if(playerList.get(i).getCardsInHand().size() == 0)
					{
						GameEnds+=playerList.get(i).getName() + " wins the game\n"; 
					}
				
				else
				{
					GameEnds+=playerList.get(i).getName() + " has " + playerList.get(i).getCardsInHand().size() + " cards in hand\n";
				}
					
				table.disable();
			}
			int buttonpress =  JOptionPane.showConfirmDialog(null,GameEnds ,"End Of Game" ,JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if( buttonpress == JOptionPane.OK_OPTION) {
				try {
					sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} 
			for(int i=0; i<4; i++) {
				playerList.get(i).removeAllCards();
			}
			handsOnTable.clear();
			table.disable();
			
		}
		table.resetSelected();
		table.repaint();

	}
	
	/**
	 * 
	 * An inner class that implements the Runnable interface.
	 * 
	 * @author rishabhjain
	 *
	 */
	public class ServerHandler implements Runnable {
		private ObjectInputStream input;
		/**
		 * 
		 * Constructor to create an Object input stream
		 * 
		 */
		public ServerHandler() {
			try {
				input = new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
					CardGameMessage msg = (CardGameMessage)input.readObject();
					parseMessage(msg);
					//System.out.println("Recieving...");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
	}
	
	
	
	/**
	 * 
	 * Composing a hand type from the selected cards
	 * 
	 * @param player Player possessing the cards
	 * @param cards List of cards
	 * @return Type of hand
	 */
	public Hand composeHand(CardGamePlayer player, CardList cards) {	
		Single single = new Single(player, cards);
		Pair pair = new Pair(player, cards);
		Triple triple = new Triple(player, cards);
		Straight straight = new Straight(player, cards);
		Flush flush = new Flush(player, cards);
		FullHouse fullhouse = new FullHouse(player, cards);
		Quad quad = new Quad(player, cards);
		StraightFlush straightflush = new StraightFlush(player, cards);
		if(single.isValid() == true) {
			return single; 
		}
			
		if(pair.isValid() == true) {
			return pair; 
		}
		if(triple.isValid() == true) {
			return triple; 
		}
		if(straight.isValid() == true) {
			return straight; 
		}
		else if(flush.isValid() == true) {
			return flush;
		}
		else if(fullhouse.isValid() == true) {
			return fullhouse;
		}
		else if(quad.isValid() == true) {
			return quad;
		}
		else if(straightflush.isValid() == true) {
			return straightflush;
		}
		return null;
		
	}
	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		int prev;
		if (currentIdx != 0) {
			prev = currentIdx - 1;
		}
		else {
			prev = 3;
		}
		
		if (playerList.get(prev).getNumOfCards()==0) {
			return true;
		}
		return false;
	}
	
	
	

}