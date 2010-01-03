package infero.util;

import static java.lang.Math.*;
import static java.text.NumberFormat.*;

import java.text.*;

public class NumberFormats {

    // -----------------------------------------------------------------------
    // Integer
    // -----------------------------------------------------------------------

    public static FormattedInteger siFormattingOf(int value) {
        NumberFormat format = getIntegerInstance();
        format.setMaximumFractionDigits(0);
        return new FormattedInteger(value, get(value, SiEnum.values(), format));
    }

    // -----------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------

    public static FormattedLong siFormattingOf(long value) {
        return new FormattedLong(value, get(value, SiEnum.values(), getIntegerInstance()));
    }

    public static FormattedLong nanoTimeFormattingOf(long value) {
        return new FormattedLong(value, get(value, NanoTimeEnum.values(), getIntegerInstance()));
    }

    // -----------------------------------------------------------------------
    // Double
    // -----------------------------------------------------------------------

    public static FormattedDouble siFormattingOf(double value) {
        return new FormattedDouble(value, get(value, SiEnumForDouble.values(), getNumberInstance()));
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private interface BreakingPoint {
        String name();

        double value();

        boolean useUnit();
    }

    private enum SiEnum implements BreakingPoint {
        G(1000000000),
        M(1000000),
        k(1000);

        private final double value;

        private SiEnum(double value) {
            this.value = value;
        }

        public double value() {
            return value;
        }

        public boolean useUnit() {
            return true;
        }
    }

    private enum NanoTimeEnum implements BreakingPoint {
        s(1000000000),
        ms(1000000),
        us(1000),
        ns(1);

        private final double value;

        private NanoTimeEnum(double value) {
            this.value = value;
        }

        public double value() {
            return value;
        }

        public boolean useUnit() {
            return true;
        }
    }

    private enum SiEnumForDouble implements BreakingPoint {
        G(1000000000, true),
        M(1000000, true),
        k(1000, true),
        x(1, false),
        m(0.001, true),
        u(0.000001, true),
        n(0.000000001, true);

        private final double value;
        private final boolean useUnit;

        private SiEnumForDouble(double value, boolean useUnit) {
            this.value = value;
            this.useUnit = useUnit;
        }

        public double value() {
            return value;
        }

        public boolean useUnit() {
            return useUnit;
        }
    }

    static String get(double value, BreakingPoint[] breakingPoints, NumberFormat numberFormat) {

        double factor = value >= 0 ? 1.0 : -1.0;
        value = abs(value);

        for (BreakingPoint breakingPoint : breakingPoints) {
            if (value >= breakingPoint.value()) {
                double v = value / breakingPoint.value();
                return numberFormat.format(factor * v) +
                        (breakingPoint.useUnit() ? breakingPoint.name() : "");
            }
        }

        return numberFormat.format(factor * value);
    }
}
