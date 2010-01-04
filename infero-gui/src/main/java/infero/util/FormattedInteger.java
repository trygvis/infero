package infero.util;

public class FormattedInteger {

    public final int numeric;
    public final String text;

    protected FormattedInteger(int numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
