/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package base;

import java.util.ArrayList;
import java.util.Random;
/**
 * Template for creating a deck of cards for a blackjack game. 
 * Static variables can be changed to alter the number of cards/suits in a deck
 * /Cards in the first hand of the game/Faces in the deck. Methods will shuffle
 * the deck, draw a card, and add a card to the bottom of the deck.
 * @author Brian Wang
 */
public class Deck {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private final static int NUM_DECKS = 8;
    private int numCardsRemaining;
    private final static int FACES_IN_DECK = 13;
    private final static int SUITS_IN_DECK = 4;
    private final static int CARDS_IN_DECK = 52;
    private final static int CARDS_IN_FIRST_HAND = 2;
    private int count;
    
    /**
     * Initalizes a simulated deckshoe, and shuffles.
     */
    public Deck(){
        for(int i = 0; i < NUM_DECKS; i++){
            for(int j = 0; j < SUITS_IN_DECK; j++){
                for(int k = 0; k < FACES_IN_DECK; k++){
                    deck.add(new Card(j, k));
                }
            }
        }
        shuffle();
    }
    /**
     * Shuffles the deck. Also resets the count and the number of cards remaining.
     */
    public void shuffle(){
        ArrayList<Card> tempDeck = new ArrayList<Card>();
        Random random = new Random();
        while(deck.size() > 0){
            int cardToRemove = random.nextInt(deck.size());
            Card tempCard = deck.get(cardToRemove);
            deck.remove(cardToRemove);
            tempDeck.add(tempCard);
        }
        while(tempDeck.size() > 0){
            Card tempCard = tempDeck.get(0);
            tempDeck.remove(0);
            deck.add(tempCard);            
        }
        count = 0;
        numCardsRemaining = NUM_DECKS * CARDS_IN_DECK;
    }
    
    /**
     * Removes a card from top of the deck.
     * @return Card card from the top of the Deck Shoe
     */    
    public Card draw(){
        Card toDraw = deck.get(0);
        deck.remove(0);
        numCardsRemaining--;
        int face = toDraw.getFace();
        if (face >= Card.TWO && face <= Card.SIX)
        	count++;
        if (face >= Card.TEN || face == Card.ACE)
        	count--;
        return toDraw;
    }
    
    /**
     * Gets the current true card count (for AI use).
     * @return Integer representing the current card count
     */
    public int getCount() {
    	double decksLeft = (double) numCardsRemaining / CARDS_IN_DECK;    	
    	return (int)Math.round(count / decksLeft);
    }
    
    /**
     * Adds a card to the bottom of the deck. Will also shuffle if there
     * are few cards remaining.
     * @param c Card to be added the bottom of the deck
     */
    public void addToBottom(Card c){
        deck.add(c);
        if (numCardsRemaining < 52*2) { //2 decks left
        	shuffle(); 
        }
    }
}
