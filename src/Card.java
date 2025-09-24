public final class Card {

    // NOT FINAL FOR TESTING
    private RANK rank;
    private final SUIT suit;

    public Card(RANK rank, SUIT suit) {
        if (rank == null || suit == null) throw new IllegalArgumentException("rank/suit null");
        this.rank = rank;
        this.suit = suit;
    }

    public RANK getRank() {
        return rank;
    }

    public int getValue() {
        return rank.baseValue;
    }

    // FOR TESTING OF SPLITTING
    public RANK setRank(RANK rank) {
        return this.rank = rank;
    }


    @Override
    public String toString() {
        return suit.getSymbol() + rank;
    }
}
