package infero.util;

import java.io.PrintStream;

import static java.lang.System.nanoTime;

public class Lap {

    public final Lap previous;
    public final long time;
    public final String name;

    public static Lap startTimer() {
        return new Lap(null, nanoTime(), null);
    }

    public static Lap startTimer(String name) {
        return new Lap(null, nanoTime(), name);
    }

    private Lap(Lap previous, long time, String name) {
        this.previous = previous;
        this.time = time;
        this.name = name;
    }

    public Lap lap() {
        return new Lap(this, nanoTime(), null);
    }

    public Lap lap(String name) {
        return new Lap(this, nanoTime(), name);
    }

    public String toString() {
        if (previous == null) {
            return (name == null ? "Start: " : name + ": ") + time;
        }

        String prefix = name == null ? "elapsed: " : name + ": ";
        long diff = time - previous.time;

        String unit;

        final long US = 1000;
        final long MS = 1000 * 1000;
        final long S = 1000 * 1000 * 1000;

        if (diff >= S) {
            diff /= S;
            unit = "s";
        } else if (diff >= MS) {
            diff /= MS;
            unit = "ms";
        } else if (diff >= US) {
            diff /= US;
            unit = "us";
        } else {
            unit = "ns";
        }

        return prefix + " " + diff + unit;
    }

    public void println(PrintStream out) {
        if (previous != null) {
            previous.println(out);
        }

        out.println(toString());
    }
}
