public record Outcome(OUTCOME_TYPE type, int playerValue, int dealerValue) {

    @Override
    public String toString() {
        // TODO: Prettier output
        return type.getFormattedTxt().toUpperCase() + " - " + playerValue + " / " + dealerValue;
    }
}