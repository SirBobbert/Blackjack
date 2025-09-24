public class UI {

    // Layout
    private static final int WIDTH = 52;
    private static final String IND = "  ";
    private static final int PAD = 1;

    private final boolean colorEnabled;

    public UI() {
        this(true);
    }

    public UI(boolean colorEnabled) {
        this.colorEnabled = colorEnabled;
    }

    // ===== Sections / boxes =====
    public void headline(String title) {
        banner(title.toUpperCase(), '─');
    }

    public void section(String title) {
        banner(title.toUpperCase(), '═');
    }

    private void banner(String title, char ch) {
        System.out.println();
        int inner = Math.max(title.length() + PAD * 2, WIDTH + 2);
        String line = String.valueOf(ch).repeat(inner);
        int pad = inner - title.length(), left = pad / 2, right = pad - left;
        System.out.println(IND + line);
        System.out.println(IND + " ".repeat(left) + title + " ".repeat(right));
        System.out.println(IND + line);
        System.out.println();
    }

    // ===== Logs / status =====
    public void info(String msg) {
        log("INFO", "36", msg);
    }

    public void warn(String msg) {
        log("WARN", "33", msg);
    }

    public void success(String msg) {
        log("OK", "32", msg);
    }

    public void error(String msg) {
        log("ERR", "31", msg);
    }

    private void log(String tag, String code, String msg) {
        String label = "[" + tag + "]";
        if (colorEnabled) label = ansi(code, label);
        System.out.println("\n" + IND + label + " " + msg);
    }

    // ===== Actions =====
    public void action(String actor, String what) {
        String label = actor.toUpperCase() + ":";
        if (colorEnabled) label = ansi(actorCode(actor), label);
        System.out.println("\n" + IND + "→ " + label + " " + what);
    }

    // ===== Show hand =====
    public void showHand(String who, Hand hand, boolean hideHole) {
        String title = who.toUpperCase();
        if (colorEnabled) title = ansi(actorCode(who), title);

        String cards;
        int total;
        if (hideHole && hand.cards().size() >= 2) {
            var up = hand.cards().get(1);
            cards = "[HIDDEN], " + up;
            total = up.getValue();
        } else {
            cards = hand.toString();
            total = hand.value();
        }

        String content = alignTwoCols(cards, "TOTAL: " + total, WIDTH);
        String line = "─".repeat(WIDTH);
        System.out.println();
        System.out.println(IND + "┌ " + title + " " + line.substring(title.length() + 1));
        System.out.println(IND + "│ " + content);
        System.out.println(IND + "└" + "─".repeat(WIDTH + 1));
    }

    // ===== Compact scoreboard =====
    public void scores(Hand player, Hand dealer, boolean dealerHidden) {
        String p = "PLAYER", d = "DEALER";
        if (colorEnabled) {
            p = ansi(actorCode(p), p);
            d = ansi(actorCode(d), d);
        }

        int pv = player.value();
        int dv = (dealerHidden && dealer.cards().size() >= 2)
                ? dealer.cards().get(1).getValue()
                : dealer.value();

        String left = String.format("%s: %d", p, pv);
        String right = String.format("%s: %d", d, dv);
        System.out.println();
        System.out.println(IND + alignTwoCols(left, right, WIDTH));
        System.out.println();
    }

    // ===== Prettifier =====
    public void menu(String title, String... options) {
        String t = title.toUpperCase();
        String line = "─".repeat(WIDTH);
        System.out.println();
        System.out.println(IND + "┌ " + t + " " + line.substring(t.length() + 1));
        for (int i = 0; i < options.length; i++) {
            String num = "[" + (i + 1) + "]";
            String key = "[" + Character.toUpperCase(options[i].charAt(0)) + "]";
            if (colorEnabled) {
                num = ansi("36", num);
                key = ansi("90", key);
            }
            System.out.println(IND + "│  " + String.format("%-8s %-6s %s", num, key, options[i]));
        }
        System.out.println(IND + "└" + "─".repeat(WIDTH + 1));
        System.out.print(IND + "> ");
    }

    // ===== Result =====
    public void outcome(String message) {
        String title = "RESULT";
        String line = "─".repeat(WIDTH);

        String msg = message;
        if (colorEnabled) {
            String code = message.startsWith("YOU WIN") ? "32"
                    : message.startsWith("DEALER WIN") ? "31" : "33";
            msg = ansi(code, message);
        }

        System.out.println();
        System.out.println(IND + "┌ " + title + " " + line.substring(title.length() + 1));
        System.out.println(IND + "│ " + padRightConsideringAnsi(msg, WIDTH));
        System.out.println(IND + "└" + "─".repeat(WIDTH + 1));
        System.out.println();
    }


    // Helpers
    public void divider() {
        System.out.println(IND + "-".repeat(WIDTH));
    }

    public void spacer() {
        System.out.println();
    }

    private static String alignTwoCols(String left, String right, int width) {
        int space = Math.max(1, width - visibleLen(left) - visibleLen(right));
        return left + " ".repeat(space) + right;
    }

    private static String padRightConsideringAnsi(String s, int width) {
        int pad = Math.max(0, width - visibleLen(s));
        return s + " ".repeat(pad);
    }

    private static int visibleLen(String s) {
        return stripAnsi(s).length();
    }

    private static String stripAnsi(String s) {
        return s.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    private static String ansi(String code, String s) {
        return "\u001B[" + code + "m" + s + "\u001B[0m";
    }

    private static String actorCode(String who) {
        return switch (who.toUpperCase()) {
            case "PLAYER" -> "32"; // greed
            case "DEALER" -> "31"; // red
            default -> "37";       // white
        };
    }

    // TODO: Color code cards based on suit (red/black)
}
