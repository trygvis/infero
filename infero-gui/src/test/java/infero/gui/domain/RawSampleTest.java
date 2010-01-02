package infero.gui.domain;

import infero.gui.domain.RawSample.Chunk;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RawSampleTest {
    RawSample rawSample;

    public RawSampleTest() {
        byte[] values;
        values = new byte[10];
        for(int i = 0; i < values.length; i++) {
            values[i] = (byte)i;
        }

        rawSample = new RawSample();
        rawSample.setSample(values, 1000);
    }

    @Test
    public void testBasic() {

        // chunks(0123456789, 0, 10, 2) =>
        //  [01234, 56789]

        Chunk[] chunks = rawSample.createChunks(0, rawSample.getCount() - 1, 2);
        assertEquals(2, chunks.length);
        assertEquals(0, chunks[0].start);
        assertEquals(4, chunks[0].end);
        assertEquals(5, chunks[1].start);
        assertEquals(9, chunks[1].end);
    }

    @Test
    public void testTenValuesIntoChunksOfThree() {

        // chunks(0123456789, 0, 10, 3) =>
        //  [012, 345, 6789]

        Chunk[] chunks = rawSample.createChunks(0, rawSample.getCount() - 1, 3);
        assertEquals(3, chunks.length);
        assertEquals(0, chunks[0].start);
        assertEquals(2, chunks[0].end);
        assertEquals(3, chunks[1].start);
        assertEquals(5, chunks[1].end);
        assertEquals(6, chunks[2].start);
        assertEquals(9, chunks[2].end);
    }
}
