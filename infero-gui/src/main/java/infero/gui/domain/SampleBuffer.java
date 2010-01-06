package infero.gui.domain;

import infero.util.*;
import static infero.util.NanoSeconds.nanoSecondsFromSeconds;
import static java.lang.String.*;

/**
 * Represents a single stream from the Logic.
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

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

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
    public Chunk[] createChunks(int start, int end, int nChunks) {
        int sampleBufferSize = samples.length;

        if (start > end) {
            throw new RuntimeException(format("start >= end: start=%d, end=%d", start, end));
        }
        if (end >= sampleBufferSize) {
            throw new RuntimeException(format("end >= samples.length: end=%d, samples.length=%d", end, samples.length));
        }

        Chunk[] chunks = new Chunk[nChunks];

        int last = start;
        int size = (sampleBufferSize - start - (sampleBufferSize - end - 1)) / nChunks;

//        System.out.println("(samples.length - start - (samples.length - end + 1)) = " + (samples.length - start - (samples.length - end + 1)));
//        System.out.println(format("start = %d, end = %d, length = %d, size = %d", start, end, samples.length, size));
//        System.out.println("size = " + size);

        for (int i = 0; i < nChunks; i++) {
            chunks[i] = new Chunk(last, last + size - 1);
            last = last + size;
        }

        // Make the last chunk include the rest of the array
        if (nChunks > 1) {
            Chunk c = chunks[chunks.length - 1];

            chunks[chunks.length - 1] = new Chunk(c.start, end);
        }

        return chunks;
    }
}
