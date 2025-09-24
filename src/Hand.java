import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final ArrayList<Card> cards = new ArrayList<>();

    public void add(Card c) {
        cards.add(c);
    }

    public Card removeAt(int index) {
        return cards.remove(index);
    }

    public List<Card> cards() {
        return List.copyOf(cards);
    }

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getValue() == cards.get(1).getValue();
    }

    public void transferTo(int index, Hand other) {
        Card c = removeAt(index);
        other.add(c);
    }

    public void clear() {
        cards.clear();
    }

    public int value() {


        int sum = 0, aces = 0;
        for (Card c : cards) {
            sum += c.getRank().baseValue;
            if (c.getRank().isAce()) aces++;
        }
        while (sum > 21 && aces-- > 0) sum -= 10;
        return sum;
    }

    public boolean isBust() {
        return value() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && value() == 21;
    }

    public boolean isPairOfAces() {
        return cards.size() == 2 && cards.get(0).getRank().isAce() && cards.get(1).getRank().isAce();
    }

    @Override
    public String toString() {
        return cards.toString();
    }

}
