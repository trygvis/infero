package infero.util;

import static infero.util.NumberFormats.*;
import static java.lang.System.*;

import java.io.*;

public class Lap {

    public final Lap previous;
    public final long time;
    public final String name;
    public final int round;

    public static Lap startTimer() {
        return startTimer(null);
    }

    public static Lap startTimer(String name) {
        return new Lap(null, name, 1);
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private Lap(Lap previous, String name, int round) {
        this.previous = previous;
        this.time = nanoTime();
        this.name = name;
        this.round = round;
    }

    public Lap lap() {
        return lap(null);
    }

    public Lap lap(String name) {
        return new Lap(this, name, round + 1);
    }

    public String toString() {
        if (previous == null) {
            return "Start: " + name;
        }

        String prefix = name + " took ";
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

    public StoppedTimer done() {
        return new StoppedTimer(this);
    }

    public static class StoppedTimer extends Lap {
        private StoppedTimer(Lap previous) {
            super(previous, "last lap", previous.round);
        }

        @Override
        public Lap lap(String name) {
            throw new RuntimeException("This timer has been stopped.");
        }
    }

    public static String[] longFormatting(final StoppedTimer timer) {
        Lap previous = timer;
        Lap current = timer.previous;
        Lap first = previous;

        String[] lines = new String[timer.round + 1];

        while (current != null) {
            String s = "Time for #" + (current.round);
            if (current.name != null) {
                s += " '" + current.name + "': ";
            } else {
                s += ": ";
            }
            s += nanoTimeFormattingOf(previous.time - current.time);
            lines[current.round - 1] = s;
            first = current;
            previous = current;
            current = current.previous;
        }

        lines[lines.length - 1] = "Total time for " + timer.round +
                " lap" + (timer.round > 1 ? "s" : "") + ": " +
                nanoTimeFormattingOf(timer.time - first.time);

        return lines;
    }
}
