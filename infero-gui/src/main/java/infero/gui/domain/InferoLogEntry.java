package infero.gui.domain;

import static java.lang.String.*;

import java.util.*;

public class InferoLogEntry {
    public final Date date;
    public final String text;

    InferoLogEntry(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public static InferoLogEntry info(String text, Object... args) {
        return new InferoLogEntry(new Date(), format(text, args));
    }

    public static InferoLogEntry entryWithDate(Date date, String text) {
        return new InferoLogEntry(date, text);
    }
}
