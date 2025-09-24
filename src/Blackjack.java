import java.util.Scanner;

public class Blackjack {

    private final Scanner scanner = new Scanner(System.in);
    private final UI ui = new UI();
    private final OutcomeStrategy outcomeStrategy = new OutcomeContext();

    private final Hand playerHand = new Hand();
    private final Hand dealerHand = new Hand();

    private Deck deck;

    public void play() {

        ui.headline("BLACKJACK");

        this.deck = new Deck();
        dealersInitialAction();

        // check for nat bj
        if (playerHand.isBlackjack() || dealerHand.isBlackjack()) {
            finishRound();
            return;
        }

        // Players turn
        while (true) {
            int choice = promptAction();

            if (choice == 1) {
                hit();
                if (playerHand.isBust()) {
                    finishRound();
                    return;
                }

                if (playerHand.value() == 21) {
                    ui.info("You got 21! Dealers turn..");
                    break;
                }

            } else if (choice == 2) {
                stand();
                break;
            }
        }

        playDealerTurn();
        finishRound();
    }

    private void hit() {
        Card c = deck.draw();
        ui.action("Player", "hit – drew: " + c);
        playerHand.add(c);
        showPlayerHand();
    }

    private void stand() {
        ui.action("Player", "stands");
    }

    public void dealersInitialAction() {
        playerDrawCard();
        dealerDrawCard(false);
        playerDrawCard();
        dealerDrawCard(true);
        showDealerHand(true);
    }

    public void playerDrawCard() {
        Card card = deck.draw();
        playerHand.add(card);
        ui.action("Player", "draw – " + card);
        showPlayerHand();
    }

    private void showPlayerHand() {
        ui.showHand("Player", playerHand, false);
    }

    public void dealerDrawCard(boolean reveal) {
        Card card = deck.draw();
        dealerHand.add(card);
        ui.action("Dealer", reveal ? ("draw – " + card) : "draw – [HIDDEN]");
    }

    public void dealerDrawCard() {
        dealerDrawCard(true);
    }

    private void showDealerHand(boolean hideHole) {
        ui.showHand("Dealer", dealerHand, hideHole);
    }

    private void playDealerTurn() {
        Card hole = dealerHand.cards().getFirst();
        ui.action("Dealer", "reveals hole card – " + hole);

        showDealerHand(false);

        while (dealerHand.value() < 17) {
            dealerDrawCard();
            showDealerHand(false);
        }
    }

    private int promptAction() {
        ui.scores(playerHand, dealerHand, true);

        while (true) {
            ui.menu("YOUR TURN", "hit", "stand");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1") || s.equals("h") || s.equals("hit")) return 1;
            if (s.equals("2") || s.equals("s") || s.equals("stand")) return 2;
            ui.warn("Invalid input - try again");
        }
    }

    private void finishRound() {
        Outcome o = outcomeStrategy.resolve(playerHand, dealerHand);
        ui.outcome(String.valueOf(o));
    }
}
