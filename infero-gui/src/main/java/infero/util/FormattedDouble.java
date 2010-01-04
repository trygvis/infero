package infero.util;

public class FormattedDouble {

    public final double numeric;
    public final String text;

    protected FormattedDouble(double numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
