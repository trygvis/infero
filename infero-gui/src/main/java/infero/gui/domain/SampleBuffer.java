package infero.gui.domain;

import infero.util.*;
import static infero.util.NanoSeconds.*;
import static java.lang.String.*;

/**
 * Represents a single capture from the Logic.
 */
public class SampleBuffer {

    private final byte[] samples;
    public final long samplesPerSecond;
    public final NanoSeconds timePerSample;
    public final NanoSeconds timespan;

    public SampleBuffer(byte[] samples, long samplesPerSecond) {
        this.samples = samples;
        this.samplesPerSecond = samplesPerSecond;

        this.timePerSample = nanoSecondsFromSeconds(1.0 / samplesPerSecond);
        timespan = timePerSample.multipliedWith(samples.length);
    }

    public int size() {
        return samples.length;
    }

    public byte getSample(int sample) {
        return samples[sample];
    }

    public byte[] getSamples() {
        return samples;
    }

    public int getIndex(NanoSeconds time) {
        return (int) (time.dividedBy(timePerSample));
    }

    public static class Chunk {
        public final int start;
        public final int end;

        public Chunk(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * Both start and end are inclusive.
     */
    public Chunk[] createChunks(NanoSeconds startTime, NanoSeconds endTime, int nChunks) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException(format("start is after end: start=%s, end=%s", startTime, endTime));
        }

        if (endTime.isAfter(timespan)) {
            throw new RuntimeException(format("end is after timespan: end=%s, timespan=%s", endTime, timespan));
        }

        Chunk[] chunks = new Chunk[nChunks];

        double startIndex = getIndex(startTime);
        int endIndex = getIndex(endTime);

        if (nChunks == 1) {
            return new Chunk[]{new Chunk((int) startIndex, endIndex)};
        }

        double selectedNumberOfSamples = endIndex - startIndex;
        double size = selectedNumberOfSamples / nChunks;

        for (int i = 0; i < nChunks; i++) {
            int end = (int) (startIndex + size);
            int start = (int) startIndex;
            chunks[i] = new Chunk(start, end == start ? start + 1 : end);
            startIndex = startIndex + size;
        }

        // Make the last chunk include the rest of the array
        Chunk c = chunks[chunks.length - 1];

        chunks[chunks.length - 1] = new Chunk(c.start, endIndex);

        return chunks;
    }
}
