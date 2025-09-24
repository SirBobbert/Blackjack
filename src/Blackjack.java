import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Blackjack {

    private final Scanner scanner = new Scanner(System.in);
    private final UI ui = new UI();
    private final OutcomeStrategy outcomeStrategy = new OutcomeContext();

    private final Map<Integer, Hand> playerHand = new LinkedHashMap<>();
    private final Hand dealerHand = new Hand();

    // TODO: Iterate this with more players
    private int playerId = 0;
    private int currentHandId = 1;

    private boolean dealerHoleHidden = true;

    private int playerCount = 1;

    private Deck deck;

    public void play() {
        ui.headline("WELCOME TO BLACKJACK");

        System.out.println("How many players are playing? (1-4)");

        this.playerCount = scanner.nextInt();

        // TODO: Split
//        playerSplit();

        InitializeGame();
        playerTurn();
        dealerTurn();
        finishRound();
    }


    public void InitializeGame() {

        this.deck = new Deck();

        for (int i = 0; i < playerCount; i++) {
            System.out.println("Player " + (i + 1) + " is joining the game.");
        }

        for (int i = 0; i < playerCount; i++) {
            playerHand.put(i, new Hand());
        }

        ui.headline("DEALING CARDS");


        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < playerCount; i++) {
                Card card = this.deck.draw();
                Hand hand = playerHand.get(i);
                hand.add(card);
                ui.action("PLAYER " + (i + 1), "draw – " + card);
            }
            dealerDrawCard(); // first call prints [HIDDEN], second shows actual card
        }
        showDealerHand(); // shows up-card with correct hide state
        scanner.nextLine(); // consume newline left-over
    }


    private void playerTurn() {

//        playerSplit();

        for (int id = 0; id < playerHand.size(); id++) {
            currentHandId = id;
            ui.headline("PLAYER " + (id + 1));

            Hand hand = playerHand.get(currentHandId);

            System.out.println("hejsa");
            System.out.println("hejsa");
            System.out.println(hand.toString());

            System.out.println(playerHand.get(1).toString());

            System.out.println("hejsa");
            System.out.println("hejsa");

            while (true) {
                int choice = promptAction(currentHandId);
                if (choice == 1) {
                    hitCurrent();

                    if (hand.isBust()) {
                        showPlayerHand();
                        ui.warn("Hand " + (currentHandId + 1) + " busts.");
                        break;
                    }

                    if (hand.value() == 21) {
                        showPlayerHand();
                        ui.success("Hand " + (currentHandId + 1) + " has 21.");
                        break;
                    }
                } else {
                    stand();
                    break;
                }
            }
        }
    }

    private void hitCurrent() {
        Card card = deck.draw();
        Hand hand = playerHand.get(currentHandId);
        hand.add(card);
        ui.action("Player (Hand " + (currentHandId + 1) + ")", "draw – " + card);
    }

    private void stand() {
        ui.action("Player", "stands");
    }

    private void playerSplit() {

        playerHand.get(0).cards().get(0).setRank(RANK.TWO);
        playerHand.get(0).cards().get(1).setRank(RANK.TWO);

        Hand hand = playerHand.get(playerId);


        ui.action("player", "checking for split option");

        if (hand.canSplit()) {
            String s;

            ui.menu("SPLIT?", "yes", "no");
            s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1") || s.equals("y") || s.equals("yes")) s = "yes";
            if (s.equals("2") || s.equals("n") || s.equals("no")) s = "no";

            if (s.equals("yes")) {
                hand = playerHand.get(playerId);
                Hand newHand = new Hand();

                hand.transferTo(1, newHand);

                int newId = playerId + 1;
                playerHand.put(newId, newHand);

                // Deal one replacement card to each split hand
                hand.add(deck.draw());
                newHand.add(deck.draw());

            }


        } else {
            System.out.println("No splitting required");
        }
        ui.info("Split complete. You will play HAND 1 first, then HAND 2.");
        showPlayerHand();
    }


    private void showPlayerHand() {
        Hand hand = playerHand.get(currentHandId);
        ui.showHand("PLAYER HAND " + (currentHandId + 1), hand, false);
    }


    public void dealerDrawCard() {
        Card card = deck.draw();
        dealerHand.add(card);

        // If this is the (new) first card and the hole is still hidden, show [HIDDEN]
        boolean isFirstCard = dealerHand.cards().size() == 1;
        if (isFirstCard && dealerHoleHidden) {
            ui.action("Dealer", "draw – [HIDDEN]");
        } else {
            ui.action("Dealer", "draw – " + card);
        }
    }

    public void revealDealerHole() {
        if (dealerHoleHidden && !dealerHand.cards().isEmpty()) {
            Card hole = dealerHand.cards().getFirst();
            ui.action("Dealer", "reveals hole card – " + hole);
            dealerHoleHidden = false;
            showDealerHand(); // re-render with the hole visible
        }
    }

    private void showDealerHand() {
        ui.showHand("Dealer", dealerHand, dealerHoleHidden);
    }


    private void dealerTurn() {
        revealDealerHole();          // flip the state + print the reveal
        while (dealerHand.value() < 17) {
            dealerDrawCard();        // all subsequent cards are shown
            showDealerHand();
        }
    }

    private int promptAction(int handIndex) {
        while (true) {
            showPlayerHand();                     // ✅ single source of truth
            ui.menu("YOUR TURN — HAND " + (handIndex + 1), "hit", "stand");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1") || s.equals("h") || s.equals("hit")) return 1;
            if (s.equals("2") || s.equals("s") || s.equals("stand")) return 2;
            ui.warn("Invalid input - try again");
        }
    }

    private void finishRound() {
        int idx = 0;
        for (Hand h : playerHand.values()) {
            Outcome o = outcomeStrategy.resolve(h, dealerHand);
            ui.outcome("Hand " + (++idx) + ": " + o);
        }
    }

}
