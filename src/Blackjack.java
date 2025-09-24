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
    private final int playerId = 0;
    private int currentHandId = 0;

    private Deck deck;

    public void play() {
        ui.headline("BLACKJACK");
        Hand p1 = new Hand();
        playerHand.put(playerId, p1);

        this.deck = new Deck();
        dealersInitialAction();

        if (p1.isBlackjack() || dealerHand.isBlackjack()) {
            finishRound();
            return;
        }

        // Players turn
        playPlayerHands();

        // 👇 Only let dealer act if at least one hand is still alive
        if (anyHandNotBusted()) {
            playDealerTurn();
        }

        finishRound();
    }

    private boolean anyHandNotBusted() {
        for (Hand h : playerHand.values()) {
            if (!h.isBust()) return true;
        }
        return false;
    }


    private void playPlayerHands() {
        for (int id = 0; id < playerHand.size(); id++) {
            currentHandId = id;

            ui.headline("PLAYING HAND " + (currentHandId + 1));

            Hand hand = playerHand.get(currentHandId);

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

    public void dealersInitialAction() {

        playerDrawCard();
        dealerDrawCard(false);
        playerDrawCard();

        playerSplit();


        dealerDrawCard(true);
        showDealerHand(true);
    }

    private void playerSplit() {

        playerHand.get(0).cards().get(0).setRank(RANK.TWO);
        playerHand.get(0).cards().get(1).setRank(RANK.TWO);

        Hand hand = playerHand.get(playerId);


        ui.action("player", "checking for split option");

        if (hand.cards().get(0).getValue() == hand.cards().get(1).getValue()) {
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

    public void playerDrawCard() {
        Card card = deck.draw();

        Hand hand = playerHand.get(playerId);
        hand.add(card);

        ui.action("Player", "draw – " + card);
        showPlayerHand();

    }

    private void showPlayerHand() {
        if (playerHand.size() > 1) {
            for (int id = 0; id < playerHand.size(); id++) {
                String label = "PLAYER HAND " + (id + 1) + (id == currentHandId ? "  ⟵ CURRENT" : "");
                ui.showHand(label, playerHand.get(id), false);
            }
        } else {
            ui.showHand("PLAYER", playerHand.get(playerId), false);
        }
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
