package infero.util;

public class FormattedDouble implements FormattedNumber {

    public final double numeric;
    public final String text;

    protected FormattedDouble(double numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String text() {
        return text;
    }
}
