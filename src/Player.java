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

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int placeBet(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Bet amount exceeds current balance.");
        }
        balance -= amount;
        currentBet = amount;
        return currentBet;
    }

    public int updateBalance(int amount) {
        balance += amount;
        return balance;
    }
}
