import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack implements iBlackjackMethods {

    int playerScore;
    int dealerScore;

    HelperClass helper = new HelperClass();

    private ArrayList<Card> deck = new ArrayList<>(52);

    private final ArrayList<Card> playerHand = new ArrayList<>(52);
    private final ArrayList<Card> dealerHand = new ArrayList<>(52);

    private final Scanner scanner = new Scanner(System.in);

    public void play() {

        this.deck = new Card().createDeck();

        while (true) {

            dealersInitialAction();

            if (playerScore == 21) {
                helper.headline("YOU WON!");
                break;
            }

            if (playerScore > 21) {
                helper.headline("YOU LOST!");
                break;
            }

            for (int i = 0; i < deck.size(); i++) {

                helper.headline("your hand: " + playerHand + " | TOTAL SCORE: " + playerScore);
                helper.headline("what would you like to do?");
                printOptions();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        hit(i);
                        if (bust()) {
                            helper.headline("BUST! Final score: " + playerScore);
                            return;
                        }
                        if (playerScore == 21) {
                            helper.headline("You win!");
                            return;
                        }
                    }
                    case 2 -> {
                        stand();
                        revealSecondCard();
                        dealerCompleteHand();
                        boolean roundOver = concludeGame();
                        if (roundOver) return;
                    }
                }
            }
        }
    }

    private boolean concludeGame() {
        // Dealer must hit until 17 or more
        while (dealerScore < 17) {
            dealerDrawCard();
//            helper.headline("Dealer hit - " + dealerHand.getLast().toString());
            dealerCompleteHand();
        }

        // Resolve outcomes
        if (dealerScore > 21) {
            helper.headline("Dealer busts! You win!");
            return true;
        }

        if (dealerScore > playerScore) {
            helper.headline("Dealer win!");
            return true;
        }

        if (dealerScore < playerScore && playerScore <= 21) {
            helper.headline("You win!");
            return true;
        }

        if (dealerScore == 21 && playerScore == 21) {
            helper.headline("Dealer win!");
            return true;
        }

        // Equal totals
        helper.headline("Push!");
        return true;
    }

    public void dealersInitialAction() {
        playerDrawCard();
        dealerDrawCard();
        playerDrawCard();
        dealerDrawCard();
        revealFirstCard();
    }

    public void playerDrawCard() {
        Card card = deck.getFirst();

        if (card.rank.equals("A")) {
            while (true) {
                helper.headline("You got an Ace! Choose its value (1 or 11): ");

                String input = scanner.nextLine().trim();

                // Check if it's "1" or "11"
                if (input.equals("1")) {
                    card.value = 1;
                    break;
                } else if (input.equals("11")) {
                    card.value = 11;
                    break;
                } else {
                    helper.headline("Invalid choice. Please type exactly 1 or 11.");
                }
            }
        }

        playerHand.add(card);
        playerScore += card.getValue();
        deck.removeFirst();
        helper.headline("Player draw!");
        playerCompleteHand();
    }

    public void dealerDrawCard() {
        Card card = deck.getFirst();

        if (card.rank.equals("A")) {
            card.value = 11;
        }

        dealerHand.add(card);
        dealerScore += card.getValue();
        deck.removeFirst();
        helper.headline("Dealer draw!");
    }

    public void revealFirstCard() {
        helper.headline("first dealer card revealed!");
        helper.headline("Dealer hand: " + dealerHand.get(1) + " | Total score: " + dealerHand.get(1).getValue());
    }

    public void revealSecondCard() {
        helper.headline("second dealer card revealed");
    }

    public void dealerCompleteHand() {
        helper.headline("Dealer hand: " + dealerHand + " - TOTAL SCORE: " + dealerScore);
    }

    public void playerCompleteHand() {
        helper.headline("Player hand: " + playerHand + " - TOTAL SCORE: " + playerScore);
    }


    // adds another card to hand
    @Override
    public void hit(int x) {
        Card currentCard = deck.get(x);

        helper.headline("Player hit - you drew: " + currentCard);

        playerHand.add(currentCard);
        playerScore += currentCard.getValue();
        deck.remove(currentCard);
    }

    // don't add another card to hand
    @Override
    public void stand() {
        helper.headline("you chose to stand");
    }

    // going above 21 in score
    @Override
    public boolean bust() {
        return playerScore > 21;
    }

    // both dealer and player(s) have the same scores
    @Override
    public void push() {
        helper.headline("both lost - same hand");
    }

    private void printOptions() {
        System.out.println("1: hit");
        System.out.println("2: stand");
    }
}
