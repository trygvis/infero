package infero.util;

import static infero.util.NumberFormats.*;

public class NanoSeconds {
    public final FormattedLong value;

    public NanoSeconds(long value) {
        this.value = nanoTimeFormattingOf(value);
    }

    public static NanoSeconds nanoSecondsFromSeconds(long seconds) {
        return new NanoSeconds(seconds * 1000 * 1000 * 1000);
    }

    public static NanoSeconds nanoSecondsFromSeconds(double seconds) {
        return new NanoSeconds((long) (seconds * 1000 * 1000 * 1000));
    }

    public static NanoSeconds nanoSecondsFromMilliSeconds(long milliSeconds) {
        return new NanoSeconds(milliSeconds * 1000 * 1000);
    }

    public static NanoSeconds nanoSecondsFromMicroSeconds(long microSeconds) {
        return new NanoSeconds(microSeconds * 1000);
    }

    public static NanoSeconds nanoSeconds(int nanoSeconds) {
        return new NanoSeconds(nanoSeconds);
    }

    public NanoSeconds multipliedWith(long factor) {
        return new NanoSeconds(value.numeric * factor);
    }

    public NanoSeconds multipliedWith(NanoSeconds factor) {
        return new NanoSeconds(value.numeric * factor.value.numeric);
    }

    public long dividedBy(NanoSeconds divisor) {
        return value.numeric / divisor.value.numeric;
    }

    public NanoSeconds dividedBy(long divisor) {
        return new NanoSeconds(value.numeric / divisor);
    }

    public NanoSeconds dividedBy(double divisor) {
        return new NanoSeconds((long) (((double) value.numeric) / divisor));
    }

    public long asLong() {
        return value.numeric;
    }

    public int asInt() {
        return (int) value.numeric;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public int hashCode() {
        return (int) value.numeric;
    }

    public boolean equals(Object o) {
        return this == o || o instanceof NanoSeconds &&
                value.numeric == (((NanoSeconds) o).value.numeric);
    }

    public String toString() {
        return value.text;
    }

    public boolean isAfter(NanoSeconds other) {
        return this.value.numeric > other.value.numeric;
    }
}
