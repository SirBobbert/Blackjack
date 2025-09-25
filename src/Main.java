public class Main {
    public static void main(String[] args) {

        // TODO: Tests (hand value, bust, blackjack, pair of aces, can split)
        // TODO: file structure - cards/game/rules/ui
        // TODO: Betting system
        // TODO: Insurance
        // TODO: Multiple decks (shoe(?))
        // TODO: README - how to run, rules, features, what feedback I want
        // TODO: Color code suits (red/black)
        // TODO: Outcome "prettier" output
        // TODO: Code comments
        // TODO: Player names
        // TODO: Player cap
        // TODO: Split up Blackjack class
        // TODO: Fix width in UI


        // DONE
        // Split
        // Multiple players
        // Ace calculation
        // If active players are bust, skip dealer turn
        // Auto-stand on natural blackjack
        // KISS/DRY refactoring

        Blackjack blackjack = new Blackjack();
        blackjack.play();

    }
}