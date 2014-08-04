package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import base.Card;
import base.Deck;

/**
 * Contains the main GUI and main method.
 * @author Vance Zuo
 */
public class BlackjackGui {

	/** Minimum bet of the table  */
	public static final int MIN_BET = 25;

	/** Money each player starts with */
	public static final int START_MONEY = 10000;

	/**
	 * Contains GUI components.
	 */			
	public class GameWindow extends JFrame implements ActionListener {
		private ChoicePanel playerChoices;
		private PlayerPanel p1; // human slot
		private PlayerPanel p2;
		private PlayerPanel p3;
		private PlayerPanel p4;
		private DealerPanel dealer;
		private Deck deck;
		private boolean turnContinue;
		private boolean hasHuman;

		private Image cardImages;

		/**
		 * Opens window containing Blackjack game.
		 */
		public GameWindow() {
			super("Herricks Quest Project 2011: Blackjack");		
			setLookAndFeel();
			getContentPane().setBackground(new Color(80,135,85));
			loadImages();
			hasHuman = JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(
					null,
					"Welcome to Blackjack.\nAre you here to play as a human,\n"
							+ "or run a test with the 4 AI algorithms?",
					"Choose Mode", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, 
					null, 
					new String[] { "Human Play", "AI Experiment" }, 
					null);
			initComponents();			
			pack(); 
			setLocationRelativeTo(null); // centers the screen
			//setResizable(false);
			setDefaultCloseOperation(EXIT_ON_CLOSE);			
			setVisible(true);
		}

		/**
		 * Responds to button presses from the ChoicePanel. 
		 * @param a The event
		 */
		@Override
		public void actionPerformed(ActionEvent a) {
			String command = a.getActionCommand();
			String bop = "That tickles!"; //Placeholder for actual execution
			if (command.equals("Hit")) {
				giveCard(p1);
				boolean busted = p1.getHand().isBusted();
				turnContinue = !busted;
				playerChoices.disableSurrender();
				playerChoices.disableDouble();
			} else if (command.equals("Stand")) {
				turnContinue = false;
			} else if (command.equals("Double")){
				p1.doubleDown();
				giveCard(p1);
				turnContinue = false;
			} else if (command.equals("Split")) {
				JOptionPane.showMessageDialog(this, bop);
			} else if (command.equals("Surrender")) {				
				JOptionPane.showMessageDialog(this, "Not feeling it? Fine, " +
						"take back $" + p1.getCurrentBet() / 2 + ".");
				collectCards(p1);
				p1.addWinnings(p1.getCurrentBet() / 2);
				turnContinue = false;
			}
			repaint();
		}

		public void setLookAndFeel() {
			try {
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {			        	
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
				UIManager.put("nimbusBase", new Color(140,125,51));
				UIManager.put("nimbusBlueGrey", new Color(191,188,170));
				UIManager.put("control", new Color(222,220,213));
			} catch (Exception e) { } // Revert to default "metal" look
		}

		/**
		 * Adds components to the frame.
		 */
		private void initComponents() {
			deck = new Deck();
			turnContinue = true;

			setLayout(new BorderLayout(5, 5));				

			dealer = new DealerPanel(MIN_BET, cardImages);
			add(dealer, BorderLayout.LINE_START);

			JPanel players = new JPanel();
			players.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.DARK_GRAY), "Players"));
			if (hasHuman) {
				p1 = new PlayerPanel("You", true, -1, 
						START_MONEY, MIN_BET, cardImages);
			} else {
				p1 = new PlayerPanel("Amateur/Beginner AI", false,
						PlayerPanel.EASY_BET | PlayerPanel.EASY_PLAY, 
						START_MONEY, MIN_BET, cardImages);
			}
			p2 = new PlayerPanel("Card-Counter/Beginner AI", false,
					PlayerPanel.HARD_BET | PlayerPanel.EASY_PLAY, 
					START_MONEY, MIN_BET, cardImages);
			p3 = new PlayerPanel("Amateur/Skilled AI", false,
					PlayerPanel.EASY_BET | PlayerPanel.HARD_PLAY, 
					START_MONEY, MIN_BET, cardImages);
			p4 = new PlayerPanel("Card-Counter/Skilled AI", false,
					PlayerPanel.HARD_BET | PlayerPanel.HARD_PLAY, 
					START_MONEY, MIN_BET, cardImages);	
			players.add(p1);
			players.add(p2);
			players.add(p3);
			players.add(p4);
			players.setOpaque(false);
			add(players, BorderLayout.CENTER);
			if (hasHuman) {
				playerChoices = new ChoicePanel();
				playerChoices.addListener(this);	
				add(playerChoices, BorderLayout.PAGE_END);	
			}
		}

		/**
		 * Gives or takes money from each player
		 * @param player 
		 */
		private void payOut(PlayerPanel player) {	
			// surrender check
			if (player.getHand().length() == 0) {
				try { Thread.sleep(10); } catch (InterruptedException e) {}
				return;
			}

			// blackjack hands
			boolean playerHasBJ = player.getHand().isBlackJack();
			boolean dealerHasBJ = dealer.getHand().isBlackJack();
			if (playerHasBJ && dealerHasBJ) {
				player.addWinnings(player.getCurrentBet());
				if (player.isHuman())
					JOptionPane.showMessageDialog(this,
							"We both have Blackjack," + " a push. Your $"
									+ player.getCurrentBet()
									+ " bet is returned.");
				return;
			} else if (playerHasBJ && !dealerHasBJ) {
				player.addWinnings(player.getCurrentBet() * 5 / 2);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this,
							"Not bad, a Blackjack. " + "You win $"
									+ player.getCurrentBet() * 5 / 2 + ".");
				return;
			} else if (!playerHasBJ && dealerHasBJ) {
				player.addWinnings(0);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this, "I have Blackjack. "
							+ "Sorry, you lose your bet.");			
				return;
			}

			// busting check
			boolean playerHasBusted = player.getHand().isBusted();
			boolean dealerHasBusted = dealer.getHand().isBusted();
			if (playerHasBusted) {
				player.addWinnings(0);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this, "You have busted. "
							+ "Sorry, you lose.");
				return;
			} else if (dealerHasBusted) {
				player.addWinnings(player.getCurrentBet() * 2);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this, "Damn, I've busted. "
							+ "You get $" + player.getCurrentBet() * 2 + ".");
				return;
			}

			// normal hands
			int playerValue = player.getHand().getBestValue();
			int dealerValue = dealer.getHand().getBestValue();	
			if (playerValue > dealerValue) {
				player.addWinnings(player.getCurrentBet() * 2);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this,
							"Looks like you've won. " + "Take your $"
									+ player.getCurrentBet() * 2 + ".");
				return;
			} else if (playerValue == dealerValue){
				player.addWinnings(player.getCurrentBet());
				if (player.isHuman())
					JOptionPane.showMessageDialog(this, "A push. Your $"
							+ player.getCurrentBet() + " bet is returned.");
				return;
			} else {
				player.addWinnings(0);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this, "My hand wins. "
							+ "Better luck next time around.");
				return;
			}
		}

		/**
		 * Asks for insurance bets from each player
		 * @param player 
		 */
		private void doInsurance(PlayerPanel player) {
			int insureBet = player.askInsurance(deck.getCount());
			if (insureBet == 0) 
				return;
			if (dealer.getHand().isBlackJack()) {
				player.addWinnings(insureBet * 3);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this,
							"Lucky you, I have Blackjack." + "Take $"
									+ insureBet * 3 + ".");
				turnContinue = false;
			} 
			else {
				player.addWinnings(0);
				if (player.isHuman())
					JOptionPane.showMessageDialog(this,
							"Lucky you, I don't have Blackjack. "
									+ "You lose your\n$" + insureBet + " "
									+ "bet, but you still have a "
									+ "chance to win.");
			}
		}

		/**
		 * Load card images file.
		 */
		private void loadImages() {
			ClassLoader cl = GameWindow.class.getClassLoader();
			URL imageURL = cl.getResource("gui/cards.png");
			if (imageURL != null)
				cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
			else {
				String errorMsg = "Card image file loading failed.";
				JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE); 
				System.exit(1);
			}	        	
		}

		/**
		 * Deals cards to the dealer
		 * @param dealer The dealer to deal cards to
		 */
		private void dealerCards(DealerPanel dealer){
			Card c1 = deck.draw();
			Card c2 = deck.draw();
			dealer.startHand(c1, c2);
			dealer.flipSecond();                    
		}

		/**
		 * Deals cards to the player
		 * @param player The player to deal cards to
		 */
		private void dealCards(PlayerPanel player){
			Card c1 = deck.draw();
			Card c2 = deck.draw();
			player.startHand(c1, c2);
		}

		/**
		 * Collects cards from the dealer
		 */
		private void collectDealerCards(){
			ArrayList<Card> toCollect = dealer.clearHand();
			for(Card c: toCollect){
				deck.addToBottom(c);
			}
		}

		/**
		 * Collects cards from the player
		 * @param player The player to collect cards from
		 */
		private void collectCards(PlayerPanel player){
			ArrayList<Card> toCollect = player.clearHand();
			for(Card c: toCollect){
				deck.addToBottom(c);
			}
		}

		/**
		 * Gives a card to the player
		 * @param player 
		 */
		private void giveCard(PlayerPanel player){
			player.getHand().addCard(deck.draw());
		}

		/**
		 * Enables and disables some buttons 
		 * @param hitState The hit button state
		 * @param standState The stand button state
		 * @param doubleState The double button state
		 * @param splitState The split button state
		 * @param surrenderState The surrender button state
		 */
		private void setButtonState(boolean hitState, boolean standState,
				boolean doubleState, boolean splitState, boolean surrenderState) {
			if (hitState)
				playerChoices.enableHit();
			else
				playerChoices.disableHit();

			if (standState)
				playerChoices.enableStand();
			else
				playerChoices.disableStand();

			if (doubleState)
				playerChoices.enableDouble();
			else
				playerChoices.disableDouble();

			if (splitState)
				playerChoices.enableSplit();
			else
				playerChoices.disableSplit();

			if (surrenderState)
				playerChoices.enableSurrender();
			else
				playerChoices.disableSurrender();
		}

		/**
		 * Asks for bets from players
		 */
		private void askBets() {
			p1.askBet(deck.getCount());
			p2.askBet(deck.getCount());
			p3.askBet(deck.getCount());
			p4.askBet(deck.getCount());
		}     

		/**
		 * Deals out cards to players and dealer
		 */
		private void deal() {
			dealerCards(dealer);
			dealCards(p1);
			dealCards(p2);
			dealCards(p3);
			dealCards(p4);
		}                

		/**
		 * Asks for insurance bets from players
		 */
		public void insurance() {
			if (dealer.checkAce()) {
				doInsurance(p1);
				doInsurance(p2);
				doInsurance(p3);
				doInsurance(p4);
			}
		}

		/**
		 * Asks for AI to do their turns
		 */
		public void doAITurns() {
			int aiAction;
			if (!hasHuman) {
				do {
					aiAction = p1.askComputerAction(dealer.getHand().get(0));
				} while (parseAIActions(p1, aiAction) == true);
			}
			do {
				aiAction = p2.askComputerAction(dealer.getHand().get(0));
			} while (parseAIActions(p2, aiAction) == true);
			do {
				aiAction = p3.askComputerAction(dealer.getHand().get(0));
			} while (parseAIActions(p3, aiAction) == true);
			do {
				aiAction = p4.askComputerAction(dealer.getHand().get(0));
			} while (parseAIActions(p4, aiAction) == true);
		}

		/**
		 * Processes the AI's actions
		 * @param ai The AI to parse actions for
		 * @param action The action to do
		 * @return true if AI can continue to play, false otherwise
		 */
		private boolean parseAIActions(PlayerPanel ai, int action) {
			switch (action) {
			case 0:
				return false;
			case 1:
				giveCard(ai);
				return true;
			case 2:
				return false; // AI never surrenders anyway
			case 3:
				ai.doubleDown();
				giveCard(ai);
				return false;
			default:
				return false;
			}
		}

		/**
		 * Does the dealer's turn.
		 */
		public void doDealerTurn() {
			dealer.flipSecond();
			while (dealer.getHand().getBestValue() < 17) {
				dealer.getHand().addCard(deck.draw());
			}
		}

		/**
		 * Gives out the money winnings.
		 */
		public void doPayOuts() {
			payOut(p2);
			payOut(p3);
			payOut(p4);
			payOut(p1);
		}

		/**
		 * Clears the cards from the table.
		 */
		private void reset() {
			collectCards(p1);
			collectCards(p2);
			collectCards(p3);
			collectCards(p4);
			collectDealerCards();  
			turnContinue = true;
		}
	}

	/** 
	 * Runs the Game. :)
	 * @param args
	 */
	public static void main(String[] args) {		
		BlackjackGui b = new BlackjackGui();
		GameWindow game = b.new GameWindow();
		int delay = 0;
		if (!game.hasHuman) {
			try {
				delay = Integer.parseInt(JOptionPane.showInputDialog(game,
						"Enter delay:"));
			} catch (Exception e) {
				delay = 500;
				JOptionPane.showMessageDialog(null, "Ugh, something went "
						+ "wrong.\nSetting to default value of " + delay + ".");
			}			
		}
		
		while (true) {
			if (game.hasHuman && game.p1.getMoney() < MIN_BET) {
				JOptionPane.showMessageDialog(game, 
						"Sorry, no money, no play.");
				System.exit(0);
			}
			//System.out.println(game.deck.getCount());
			game.askBets(); 
			game.deal();         	
			game.repaint();
			game.insurance();
			if (game.hasHuman) {
				game.setButtonState(true, true, true, false, true);
				if (game.p1.getCurrentBet() > game.p1.getMoney()) 
					game.playerChoices.disableDouble();
				while (game.turnContinue) { 
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				game.setButtonState(false, false, false, false, false);
			}     	
			game.doAITurns();
			game.doDealerTurn();
			game.repaint();       
			game.doPayOuts();
			if (!game.hasHuman) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	  
			game.reset();   
		}        
	}

}