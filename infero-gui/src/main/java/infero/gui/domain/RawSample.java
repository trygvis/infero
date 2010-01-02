package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.RawSample.*;
import static infero.gui.domain.RawSample.Properties.*;
import static java.lang.String.*;

@Singleton
public class RawSample extends AbstractDomainObject<Properties> {
    public enum Properties {
        VALUES,
        ZOOM,
    }

    private byte[] values = new byte[0];
    private int samplesPerSecond;
    private boolean valid;

    // -----------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------

    public byte[] getValues() {
        return values;
    }

    public int getCount() {
        return values.length;
    }

    public boolean isValid() {
        return valid;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public void setSample(byte[] values, int samplesPerSecond) {
        valid = true;
        this.samplesPerSecond = samplesPerSecond;
        firePropertyChange(VALUES, this.values, this.values = values);
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
        if (start > end) {
            throw new RuntimeException(format("start >= end: start=%d, end=%d", start, end));
        }
        if (end >= values.length) {
            throw new RuntimeException(format("end >= values.length: end=%d, values.length=%d", end, values.length));
        }

        Chunk[] chunks = new Chunk[nChunks];

        int last = start;
        int size = (values.length - start - (values.length - end - 1)) / nChunks;

//        System.out.println("(values.length - start - (values.length - end + 1)) = " + (values.length - start - (values.length - end + 1)));
//        System.out.println(format("start = %d, end = %d, length = %d, size = %d", start, end, values.length, size));
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
