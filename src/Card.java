public final class Card {

    private final RANK rank;
    private final SUIT suit;

    public Card(RANK rank, SUIT suit) {
        if (rank == null || suit == null) throw new IllegalArgumentException("rank/suit null");
        this.rank = rank;
        this.suit = suit;
    }

    public RANK getRank() {
        return rank;
    }

    public SUIT getSuit() {
        return suit;
    }

    public int getValue() {
        return rank.baseValue;
    }

    public boolean isAce() {
        return rank.isAce();
    }

    @Override
    public String toString() {
        return suit.getSymbol() + rank;
    }
}
