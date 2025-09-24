public enum OUTCOME_TYPE {
    PUSH_BJ("Blackjack Push!"),
    PLAYER_BJ("Player Blackjack!"),
    DEALER_BJ("Dealer Blackjack!"),
    PLAYER_BUST("Player Bust!"),
    DEALER_BUST("Dealer Bust!"),
    PLAYER_WIN("Player Wins!"),
    DEALER_WIN("Dealer Wins!"),
    PUSH("Push");

    private final String formattedTxt;

    OUTCOME_TYPE(String formattedTxt) {
        this.formattedTxt = formattedTxt;
    }

    public String getFormattedTxt() {
        return formattedTxt;
    }
}