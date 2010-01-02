package infero.util;

public class FormattedLong implements FormattedNumber {

    public final long numeric;
    public final String text;

    protected FormattedLong(long numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String text() {
        return text;
    }
}
