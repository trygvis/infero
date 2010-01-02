package infero.util;

import static java.lang.String.*;

public class NumberFormats {

    // -----------------------------------------------------------------------
    // Integer
    // -----------------------------------------------------------------------

    public static FormattedInteger siFormattingOf(int value) {
        return new FormattedInteger(value, get(value, SiEnum.values()));
    }

    // -----------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------

    public static FormattedLong siFormattingOf(long value) {
        return new FormattedLong(value, get(value, SiEnum.values()));
    }

    public static FormattedLong nanoTimeFormattingOf(long value) {
        return new FormattedLong(value, get(value, NanoTimeEnum.values()));
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private interface BreakingPoint {
        String name();

        long value();
    }

    public enum SiEnum implements BreakingPoint {
        G(1000000000),
        M(1000000),
        k(1000);

        private final long value;

        private SiEnum(long value) {
            this.value = value;
        }

        public long value() {
            return value;
        }
    }

    public enum NanoTimeEnum implements BreakingPoint {
        s(1000000000),
        ms(1000000),
        us(1000),
        ns(1);

        private final long value;

        private NanoTimeEnum(long value) {
            this.value = value;
        }

        public long value() {
            return value;
        }
    }

    static String get(long value, BreakingPoint[] breakingPoints) {
        for (BreakingPoint breakingPoint : breakingPoints) {
            if (value >= breakingPoint.value()) {
                return value / breakingPoint.value() + breakingPoint.name();
            }
        }

        return valueOf(value);
    }
}
