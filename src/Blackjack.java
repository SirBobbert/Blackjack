import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack implements iBlackjackMethods {

    int playerScore;
    int dealerScore;


    private ArrayList<Card> deck = new ArrayList<>(52);

    private final ArrayList<Card> playerHand = new ArrayList<>(52);
    private final ArrayList<Card> dealerHand = new ArrayList<>(52);

    private final Scanner scanner = new Scanner(System.in);


    public void play() {

        this.deck = new Card().createDeck();

        while (true) {

            dealersInitialAction();

            for (int i = 0; i < deck.size(); i++) {

                headline("your hand");
                System.out.println(playerHand);
                System.out.println("TOTAL SCORE: " + playerScore);

                headline("what would you like to do?");
                printOptions();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        hit(i);
                        if (bust()) {
                            headline("BUST! Final total: " + playerScore);
                            return;
                        }
                        if (playerScore == 21) {
                            headline("You win!");
                            return;
                        }
                    }
                    case 2 -> {
                        stand();
                        revealSecondCard();
                        dealerCompleteHand();
                        boolean roundOver = concludeGame();
                        if (roundOver) return; // <-- exit play() (ends both loops cleanly)
                    }
                }
            }
        }
    }

    private boolean concludeGame() {
        // Dealer must hit until 17 or more
        while (dealerScore < 17) {
            headline("Dealer hit!");
            dealerDrawCard();
            System.out.println(dealerHand.getLast().toString());
            dealerCompleteHand();
        }

        // Resolve outcomes
        if (dealerScore > 21) {
            headline("Dealer busts! You win!");
            return true;
        }

        if (dealerScore > playerScore) {
            headline("Dealer win!");
            return true;
        }

        if (dealerScore < playerScore && playerScore <= 21) {
            headline("You win!");
            return true;
        }

        if (dealerScore == 21 && playerScore == 21){
            headline("Dealer win!");
            return true;
        }

        // Equal totals
        headline("Push!");
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

        playerHand.add(card);
        playerScore += card.getValue();
        deck.removeFirst();
    }

    public void dealerDrawCard() {

        Card card = deck.getFirst();

        dealerHand.add(card);
        dealerScore += card.getValue();
        deck.removeFirst();
    }

    public void revealFirstCard() {
        headline("dealer's first card revealed");
        System.out.println(dealerHand.get(1));
        System.out.println("DEALER TOTAL SCORE: " + dealerHand.get(1).getValue());
    }

    public void revealSecondCard() {
        headline("dealer's second card revealed");
        System.out.println(dealerHand.get(0));
    }

    public void dealerCompleteHand() {
        headline("dealers complete hand");
        System.out.println(dealerHand + " - DEALER TOTAL SCORE: " + dealerScore);
    }

    private static final int BOX_WIDTH = 60;     // inner content width
    private static final char H = '─';           // use '-' if you prefer ASCII

    // Box grows to fit the title so it stays on one line.
// Set MAX_INNER_WIDTH to a number (e.g. 120) if you want a hard cap; leave null for no cap.
    private static final int MIN_INNER_WIDTH = 40;   // inner content width (without borders)
    private static final int SIDE_PADDING = 2;    // spaces on each side of the title (counted inside the box)
    private static final Integer MAX_INNER_WIDTH = null; // e.g. 120 or null for unlimited

    public void headline(String title) {
        String t = title.toUpperCase();

        // Compute inner width so the title fits on one line (plus padding), but never below MIN
        int needed = t.length() + SIDE_PADDING * 2;
        int inner = Math.max(MIN_INNER_WIDTH, needed);

        // Build the box
        String horizontal = "─".repeat(inner); // use "-" if you prefer ASCII
        String top = "┌" + horizontal + "┐";
        String bottom = "└" + horizontal + "┘";

        // Center the title within the inner width
        int padTotal = inner - t.length();
        int left = padTotal / 2;
        int right = padTotal - left;

        System.out.println(top);
        System.out.println("│" + " ".repeat(left) + t + " ".repeat(right) + "│");
        System.out.println(bottom);
    }


    // adds another card to hand
    @Override
    public void hit(int x) {
        Card currentCard = deck.get(x);

        headline("HIT - " + currentCard);

        playerHand.add(currentCard);
        playerScore += currentCard.getValue();
        deck.remove(currentCard);
    }

    // don't add another card to hand
    @Override
    public void stand() {
        headline("you chose to stand");
    }

    // going above 21 in score
    @Override
    public boolean bust() {
        return playerScore > 21;
    }

    // both dealer and player(s) have the same scores
    @Override
    public void push() {
        headline("both lost - same hand");
    }

    private void printOptions() {
        System.out.println("1: hit");
        System.out.println("2: stand");
    }
}
