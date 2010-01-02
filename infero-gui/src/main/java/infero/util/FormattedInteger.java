package infero.util;

public class FormattedInteger implements FormattedNumber {

    public final int numeric;
    public final String text;

    protected FormattedInteger(int numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String text() {
        return text;
    }
}
