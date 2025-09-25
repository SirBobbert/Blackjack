import java.util.*;


public class Deck {

    private final Deque<Card> cards = new ArrayDeque<>(52);

    public Deck() {
        int amountOfDecks = 6;
        for (int i = 0; i < amountOfDecks; i++) {
            for (SUIT s : SUIT.values())
                for (RANK r : RANK.values())
                    cards.addLast(new Card(r, s));
            shuffle();
        }
    }

    private void shuffle() {
        List<Card> tmp = new ArrayList<>(cards);
        Collections.shuffle(tmp);
        cards.clear();
        for (Card c : tmp) cards.addLast(c);
    }

    public Card draw() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck is empty");
        return cards.removeFirst();
    }

    public int remaining() {
        return cards.size();
    }
}
