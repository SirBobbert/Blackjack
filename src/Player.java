import java.util.ArrayList;
import java.util.List;

public class Player {

    private final int id;
    private final List<Hand> hands = new ArrayList<>();

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

}
