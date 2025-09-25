import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Blackjack {

    private final OutcomeStrategy outcomeStrategy = new OutcomeContext();
    private final Scanner scanner = new Scanner(System.in);
    private final UI ui = new UI();
    private Deck deck;

    private final Map<Integer, Player> players = new LinkedHashMap<>();
    private final Hand dealerHand = new Hand();
    private boolean dealerHoleHidden = true;
    private int playerCount = 1;

    public void play() {
        ui.headline("WELCOME TO BLACKJACK");
        System.out.println("How many players are playing? (1-4)");

        this.playerCount = scanner.nextInt();
        // consume newline left-over
        scanner.nextLine();

        seedPlayers();
        initiateBetting();
        initializeGame();
        playerTurn();
        dealerTurn();
        finishRound();
    }

    private void initiateBetting() {

        for (Player p : players.values()) {
            ui.headline("PLAYER " + (p.getId() + 1) + " BETTING");
            int bet;
            while (true) {
                System.out.println("You have $" + p.getBalance() + ". How much do you want to bet?");
                System.out.print("  > ");
                String s = scanner.nextLine().trim();
                try {
                    bet = Integer.parseInt(s);
                    if (bet < 1) {
                        ui.warn("Bet must be at least $1.");
                    } else if (bet > p.getBalance()) {
                        ui.warn("You cannot bet more than your current balance.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    ui.warn("Please enter a valid number.");
                }
            }
            p.placeBet(bet);
            ui.success("Player " + (p.getId() + 1) + " bets $" + bet + ".");
        }

    }

    private void seedPlayers() {
        players.clear();
        for (int i = 0; i < playerCount; i++) {
            players.put(i, new Player(i));

            System.out.println("Player " + (i + 1) + " is joining the game.");
            Player p = players.get(i);
            p.resetHands();
        }
    }

    public void initializeGame() {
        this.deck = new Deck();
        dealerHoleHidden = true;
        dealerHand.clear();

        ui.headline("DEALING CARDS");

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < playerCount; i++) {
                Player p = players.get(i);
                Hand hand = p.getHands().getFirst();
                Card card = deck.draw();
                hand.add(card);
                ui.action("PLAYER " + (i + 1), "draw – " + card);
            }
            dealerDrawCard();
        }
        showDealerHand();
    }


    private void playerTurn() {
        int seat = 0;

        for (Player p : players.values()) {

            // For testing purposes
//            Hand h = p.getHands().getFirst();
//            h.clear();
//            h.add(new Card(RANK.ACE, SUIT.HEARTS));
//            h.add(new Card(RANK.KING, SUIT.CLUBS));
//
//            h.add(new Card(RANK.TWO, SUIT.HEARTS));
//            h.add(new Card(RANK.TWO, SUIT.CLUBS));
            // For testing purposes

            ui.headline("PLAYER " + (++seat));

            if (p.getHands().getFirst().canSplit()) {
                ui.info("You can split!");
                showPlayerHand(p, 0, true);

                ui.menu("SPLIT?", "yes", "no");

                int splitChoice;
                while (true) {
                    String s = scanner.nextLine().trim().toLowerCase();
                    if (s.equals("1") || s.equals("y") || s.equals("yes")) {
                        splitChoice = 1;
                        break;
                    }
                    if (s.equals("2") || s.equals("n") || s.equals("no")) {
                        splitChoice = 2;
                        break;
                    }
                    ui.warn("Please type 1 or 2 (or y/n).");
                    System.out.print("  > ");
                }

                if (splitChoice == 1) {
                    p.split(0);
                    ui.info("Player has split their hand into two hands: " + p.getHands().get(0) + " and " + p.getHands().get(1));
                }
            }

            for (int handIndex = 0; handIndex < p.getHands().size(); handIndex++) {
                while (true) {


                    // Check for blackjack on initial player hand
                    if (p.getHands().get(handIndex).isBlackjack()) {
                        showPlayerHand(p, handIndex, true);
                        ui.success("Hand " + (handIndex + 1) + " has Blackjack!");
                        break;
                    }

                    int choice = promptAction(p, handIndex);

                    if (choice == 1) {
                        hitCurrent(p, handIndex);
                        Hand hand = p.getHands().get(handIndex);

                        if (hand.isBust()) {
                            showPlayerHand(p, handIndex, true);
                            ui.warn("Hand " + (handIndex + 1) + " busts.");
                            break;
                        }
                        if (hand.value() == 21) {
                            showPlayerHand(p, handIndex, true);
                            ui.success("Hand " + (handIndex + 1) + " has 21.");
                            break;
                        }
                    } else {
                        ui.action("Player", "stands");
                        break;
                    }
                }
            }
        }
    }


    private void hitCurrent(Player p, int handIndex) {
        Hand hand = p.getHands().get(handIndex);

        Card card = deck.draw();
        hand.add(card);

        boolean multi = p.getHands().size() > 1;
        String who = multi ? "PLAYER (HAND " + (handIndex + 1) + ")" : "PLAYER";

        ui.action(who, "draw – " + card);
    }


    private void showPlayerHand(Player p, int handIndex, boolean isActive) {
        Hand hand = p.getHands().get(handIndex);
        boolean multi = p.getHands().size() > 1;

        // title: no "HAND 1" until there are multiple hands
        String who = multi
                ? ("PLAYER " + (p.getId() + 1) + " - HAND " + (handIndex + 1))
                : ("PLAYER " + (p.getId() + 1));
        // "CURRENT" when only one hand
        boolean showCurrent = isActive && multi;
        ui.showHand(who, hand, false, showCurrent, hand.isBust());
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
            showDealerHand();
        }
    }

    private void showDealerHand() {
        ui.showHand("Dealer", dealerHand, dealerHoleHidden);
    }

    private void dealerTurn() {

        if (players.values().stream().allMatch(Player::allHandBusted)) {
            ui.headline("ALL PLAYERS BUSTED");
            ui.info("Dealer does not play since all players have busted.");
        } else {
            ui.headline("DEALER");
            revealDealerHole();
            while (dealerHand.value() < 17) {
                dealerDrawCard();
                showDealerHand();
            }
        }
    }

    private int promptAction(Player p, int handIndex) {
        while (true) {
            for (int i = 0; i < p.getHands().size(); i++) {
                showPlayerHand(p, i, i == handIndex);
            }

            boolean multi = p.getHands().size() > 1;
            String title = multi ? ("YOUR TURN — HAND " + (handIndex + 1)) : "YOUR TURN";
            ui.menu(title, "hit", "stand");

            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1") || s.equals("h") || s.equals("hit")) return 1;
            if (s.equals("2") || s.equals("s") || s.equals("stand")) return 2;
            ui.warn("Please type 1 or 2 (or h/s).");
        }
    }

    private void finishRound() {
        int seat = 0;
        ui.headline("ROUND OVER — OUTCOMES");
        for (Player p : players.values()) {
            int handNo = 0;
            for (Hand h : p.getHands()) {
                Outcome o = outcomeStrategy.resolve(h, dealerHand);
                ui.outcome("Player " + (seat + 1) + " — Hand " + (++handNo) + ": " + o);

                System.out.println("RESOLVE BETS FOR PLAYER " + (seat + 1));

                System.out.println("Current balance:");
                System.out.println(p.getBalance());

                System.out.println("current bet");
                System.out.println(p.getCurrentBet());

                // parse logic
                p.updateBalance(updatedBalance(p, o));

            }
            seat++;
        }
    }

    private int updatedBalance(Player p, Outcome o) {
        System.out.println(o.toString());

        int bet = p.getCurrentBet();
        int balance = p.getBalance();


        switch (o.type()) {
            case PUSH_BJ, PUSH -> {
                System.out.println("PUSH (DRAW)");
                balance = balance + bet;
            }
            case PLAYER_BJ -> {
                System.out.println("PLAYER BLACKJACK!");
                balance = (int) (balance + (bet * 2.5));
            }
            case PLAYER_WIN, DEALER_BUST -> {
                System.out.println("PLAYER WINS!");
                balance = balance + (bet * 2);
            }
            case DEALER_BJ, DEALER_WIN, PLAYER_BUST -> {
                System.out.println("PLAYER LOSES!");
            }
        }

        System.out.println("New balance");
        System.out.println(balance);
        return balance;
    }

}






