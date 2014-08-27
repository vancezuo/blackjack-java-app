quest-blackjack
===============

This is an old Blackjack app I made with a friend, Brian Wang, in 2011 as a high school project. The app is programmed in Java using Swing. It implements most rules of casino Blackjack, except splitting. The interface simulates playing Blackjack against a dealer at a table with three AI players. There is also a mode for testing four AI players at once. 

The AI comes in different flavors based on how they play and how they bet. An AI may play in two different ways:

1. Naively hit based its own risk of busting, e.g. 12 has ~4/13 chance of busting, so it would hit 9/13 of the time. This models beginner play.
2. Play according to a hard-coded rule-based table that takes into account its own and the dealer's visible cards. The strategy is significantly more successful than the beginner mode, though not optimal.

The AI can also bet in two different ways:

1. Bet according to "emotion"--raising bets after wins, dropping after losses--without regard for the deck state.
2. Bet according to a Hi Lo card counting based strategy.

In total, these different strategies allow for four unique AIs, which are what the AI testing mode works with. As expected, table-based play is more successful than naive play, and card-counting superior to emotional betting. It is interesting, nonetheless, to see these AIs compete over thousands of hands. The weaker AIs often exceed the better AI's cash on hand, but the advantage is  short-lived, and the card-counting table-using AI is always the last to go bankrupt (Note that in this implementation, none of the AI strategies are good enough to beat the house, though the best puts up a reasonable fight.)
