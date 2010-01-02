package infero.gui.domain;

public class InferoLogEntry {
    public final long timeInMilliseconds;
    public final String text;

    InferoLogEntry(long timeInMilliseconds, String text) {
        this.timeInMilliseconds = timeInMilliseconds;
        this.text = text;
    }

    public static InferoLogEntry info(String text) {
        return new InferoLogEntry(System.currentTimeMillis(), text);
    }
}
