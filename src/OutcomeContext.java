public class OutcomeContext implements OutcomeStrategy {

    @Override
    public Outcome resolve(Hand hand, Hand dealerHand) {
        int pv = hand.value();
        int dv = dealerHand.value();

        // Busts first
        if (hand.isBust()) return new Outcome(OUTCOME_TYPE.PLAYER_BUST, pv, dv);
        if (dealerHand.isBust()) return new Outcome(OUTCOME_TYPE.DEALER_BUST, pv, dv);

        // Naturals
        boolean pBJ = hand.isBlackjack();
        boolean dBJ = dealerHand.isBlackjack();
        if (pBJ && dBJ) return new Outcome(OUTCOME_TYPE.PUSH_BJ, pv, dv);
        if (pBJ) return new Outcome(OUTCOME_TYPE.PLAYER_BJ, pv, dv);
        if (dBJ) return new Outcome(OUTCOME_TYPE.DEALER_BJ, pv, dv);

        // Compare totals
        if (pv > dv) return new Outcome(OUTCOME_TYPE.PLAYER_WIN, pv, dv);
        if (pv < dv) return new Outcome(OUTCOME_TYPE.DEALER_WIN, pv, dv);
        return new Outcome(OUTCOME_TYPE.PUSH, pv, dv);
    }
}
