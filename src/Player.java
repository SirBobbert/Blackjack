import java.util.ArrayList;
import java.util.List;

public class Player {

    private int id;
    private int bankroll;
    private List<Hand> hands = new ArrayList<>();

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public List<Hand> getHands() {
        return hands;
    }

    public void newRound() {
        hands.clear();
    }

    public Hand createHand() {
        Hand h = new Hand();
        hands.add(h);
        return h;
    }

    public boolean isBlackjack() {
        return hands.size() == 1 && hands.get(0).isBlackjack();
    }

    public boolean isBust() {
        for (Hand hand : hands) {
            if (!hand.isBust()) return false;
        }
        return true;
    }

    public int value() {

        int score = 0;
        for (Hand hand : hands) {
            score += hand.value();
        }
        return score;
    }


    public void split(List<Hand> hand) {
        if (hand.getFirst().canSplit()) {
            splitAt();
        } else {
            throw new IllegalStateException("Cannot split hand: " + hand);
        }

    }

    private void splitAt() {
        Hand original = hands.getFirst();
        Hand newHand = new Hand();
        original.transferTo(1, newHand);
        hands.add(newHand);
    }
}
