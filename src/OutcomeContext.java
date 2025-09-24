public class OutcomeContext implements OutcomeStrategy {

    @Override
    public Outcome resolve(Hand playerHand, Hand dealerHand) {

        if (playerHand.isBlackjack() && dealerHand.isBlackjack()) {
            return new Outcome(OUTCOME_TYPE.DEALER_BJ, playerHand.value(), dealerHand.value());
        }

        if (playerHand.isBlackjack()) {
            return new Outcome(OUTCOME_TYPE.PLAYER_BJ, playerHand.value(), dealerHand.value());
        }

        if (dealerHand.isBlackjack()) {
            return new Outcome(OUTCOME_TYPE.DEALER_BJ, playerHand.value(), dealerHand.value());
        }

        if (playerHand.isBust()) {
            return new Outcome(OUTCOME_TYPE.PLAYER_BUST, playerHand.value(), dealerHand.value());
        }

        if (dealerHand.isBust()) {
            return new Outcome(OUTCOME_TYPE.DEALER_BUST, playerHand.value(), dealerHand.value());
        }

        if (playerHand.value() > dealerHand.value()) {
            return new Outcome(OUTCOME_TYPE.PLAYER_WIN, playerHand.value(), dealerHand.value());
        }

        if (playerHand.value() < dealerHand.value()) {
            return new Outcome(OUTCOME_TYPE.DEALER_WIN, playerHand.value(), dealerHand.value());
        }

        if (playerHand.value() == 21 && playerHand.cards().size() == 2) {
            return new Outcome(OUTCOME_TYPE.PUSH_BJ, playerHand.value(), dealerHand.value());
        }

        return new Outcome(OUTCOME_TYPE.PUSH, playerHand.value(), dealerHand.value());
    }
}

