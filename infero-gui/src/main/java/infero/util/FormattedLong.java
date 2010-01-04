package infero.util;

public class FormattedLong {

    public final long numeric;
    public final String text;

    protected FormattedLong(long numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
