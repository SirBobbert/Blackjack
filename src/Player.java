import java.util.ArrayList;
import java.util.List;

public class Player {

    private final int id;
    private final List<Hand> hands = new ArrayList<>();
    private int balance = 1000; // default starting balance
    private int currentBet = 0;

    public Player(int id) {
        this.id = id;
        hands.add(new Hand());
    }

    public int getId() {
        return id;
    }

    public List<Hand> getHands() {
        return List.copyOf(hands);
    } // read-only


    public void resetHands() {
        hands.clear();
        hands.add(new Hand());
    }

    public boolean hasActiveHand(Player p) {

        for (Hand hand : hands) {
            if (hand.getBet() > 0 && hand.size() > 0 && !hand.isBust()) return true;
        }
        return false;
    }

    public boolean allHandBusted() {
        for (Hand hand : hands) {
            if (!hand.isBust()) return false;
        }
        return true;
    }

    public void split(int idx) {
        if (idx < 0 || idx >= hands.size())
            throw new IndexOutOfBoundsException("idx=" + idx);

        Hand original = hands.get(idx);
        if (original.size() != 2 || !original.isPair())
            throw new IllegalStateException("Cannot split hand at " + idx + ": " + original);

        Hand newHand = new Hand();
        original.transferTo(1, newHand);
        hands.add(idx + 1, newHand);

        Hand h1 = original;
        Hand h2 = newHand;

        h1.setBet(currentBet);
        System.out.println("Hand 1 current bet: " + h1.getBet());

        h2.setBet(currentBet);
        System.out.println("Hand 2 current bet: " + h2.getBet());
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public int getBalance() {
        return balance;
    }

    public int setBalance(int balance) {
        this.balance = balance;
        return balance;
    }

    public void updateBalance(int amount) {
        this.balance += amount;
    }

    public int placeBet(int amount, Hand hand) {

        if (amount > balance) {
            throw new IllegalArgumentException("Bet amount exceeds current balance.");
        }

        hand.setBet(amount);
        currentBet = amount;
        return currentBet;
    }
}
