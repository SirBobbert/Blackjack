public class HelperClass {
    private static final int BOX_WIDTH = 60;     // inner content width
    private static final char H = '─';           // use '-' if you prefer ASCII

    // Box grows to fit the title so it stays on one line.
    // Set MAX_INNER_WIDTH to a number (e.g. 120) if you want a hard cap; leave null for no cap.
    private static final int MIN_INNER_WIDTH = 50;   // inner content width (without borders)
    private static final int SIDE_PADDING = 2;    // spaces on each side of the title (counted inside the box)
    private static final Integer MAX_INNER_WIDTH = null; // e.g. 120 or null for unlimited

    public void headline(String title) {
        String t = title.toUpperCase();

        // Compute inner width so the title fits on one line (plus padding), but never below MIN
        int needed = t.length() + SIDE_PADDING * 2;
        int inner = Math.max(MIN_INNER_WIDTH, needed);

        // Build the box
        String horizontal = "─".repeat(inner); // use "-" if you prefer ASCII
        String top = "┌" + horizontal + "┐";
        String bottom = "└" + horizontal + "┘";

        // Center the title within the inner width
        int padTotal = inner - t.length();
        int left = padTotal / 2;
        int right = padTotal - left;

        System.out.println(top);
        System.out.println("│" + " ".repeat(left) + t + " ".repeat(right) + "│");
        System.out.println(bottom);
    }
}
