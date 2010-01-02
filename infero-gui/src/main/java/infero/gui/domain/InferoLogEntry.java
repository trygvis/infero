package infero.gui.domain;

import static java.lang.String.format;

public class InferoLogEntry {
    public final long timeInMilliseconds;
    public final String text;

    InferoLogEntry(long timeInMilliseconds, String text) {
        this.timeInMilliseconds = timeInMilliseconds;
        this.text = text;
    }

    public static InferoLogEntry info(String text, Object ... args) {
        return new InferoLogEntry(System.currentTimeMillis(), format(text, args));
    }
}
