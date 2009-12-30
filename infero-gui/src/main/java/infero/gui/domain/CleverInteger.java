package infero.gui.domain;

import static java.lang.String.valueOf;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CleverInteger {

    private final static int GIGA = 1000000000;
    private final static int MEGA = 1000000;
    private final static int KILO = 1000;

    public final int numeric;
    public final String text;

    public CleverInteger(int numeric) {
        this.numeric = numeric;

        if(numeric >= GIGA) {
            text = (numeric / GIGA) + "G";
        }
        else if(numeric >= MEGA) {
            text = (numeric / MEGA) + "M";
        }
        else if(numeric >= KILO) {
            text = (numeric / KILO) + "k";
        }
        else {
            text = valueOf(numeric);
        }
    }

    public static CleverInteger cleverInteger(int value) {
        return new CleverInteger(value);
    }
}
