public class Main {
    public static void main(String[] args) {

        // TODO: Tests (hand value, bust, blackjack, pair of aces, can split)
        // TODO: file structure - cards/game/rules/ui
        // TODO: Insurance
        // TODO: README - how to run, rules, features, what feedback I want
        // TODO: Color code suits (red/black)
        // TODO: Outcome "prettier" output
        // TODO: Code comments
        // TODO: Player names
        // TODO: Split up Blackjack class
        // TODO: Fix width in UI


        // DONE
        // Split
        // Multiple players
        // Ace calculation (soft/hard)
        // If active players are bust, skip dealer turn
        // Auto-stand on natural blackjack
        // KISS/DRY refactoring
        // Betting system
        // Player cap
        // Gameplay loop
        // Multiple decks

        Blackjack blackjack = new Blackjack();
        blackjack.play();

    }
}