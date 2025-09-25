import java.util.ArrayList;
import java.util.List;

public class Hand {


    private int bet = 0;


    private final ArrayList<Card> cards = new ArrayList<>();

    public void add(Card c) {
        cards.add(c);
    }

    public int size() {
        return cards.size();
    }

    public List<Card> cards() {
        return List.copyOf(cards);
    }


    public int value() {
        int sum = 0, aces = 0;
        for (Card c : cards) {
            sum += c.getValue();
            if (c.getRank().isAce()) aces++;
        }
        while (sum > 21 && aces-- > 0) sum -= 10;
        return sum;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }


    public boolean isBlackjack() {
        return cards.size() == 2 && value() == 21;
    }

    public boolean isBust() {
        return value() > 21;
    }

    public boolean isPair() {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public void transferTo(int index, Hand target) {
        for (int i = 0; i < index; i++) {
            target.add(cards.removeFirst());
        }
    }

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        return cards.toString();
    }

}
