package infero.gui.domain;

import infero.gui.domain.SampleBuffer.*;
import infero.gui.domain.services.*;
import infero.util.*;
import static infero.util.NanoSeconds.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.awt.*;

public class SampleBufferTest {
    SampleBuffer sampleBuffer;
    final NanoSeconds startTime;
    final NanoSeconds endTime;

    public SampleBufferTest() {
        byte[] values;
        values = new byte[10];
        for (int i = 0; i < values.length; i++) {
            values[i] = (byte) i;
        }

        sampleBuffer = new SampleBuffer(values, 1000);
        startTime = nanoSeconds(0);
        endTime = sampleBuffer.timespan;
    }

    @Test
    public void testBasic() {

        // values = 0123456789@1000Hz
        // chunks(0, timespan, 2) =>
        //  [01234, 56789]

        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, 2);
        assertEquals(2, chunks.length);
        assertEquals(0, chunks[0].start);
        assertEquals(5, chunks[0].end);
        assertEquals(5, chunks[1].start);
        assertEquals(10, chunks[1].end);
    }

    @Test
    public void testTenValuesIntoChunksOfThree() {

        // chunks(0123456789, 0, 10, 3) =>
        //  [012, 345, 6789]

        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, 3);
        assertEquals(3, chunks.length);
        assertEquals(0, chunks[0].start);
        assertEquals(3, chunks[0].end);
        assertEquals(3, chunks[1].start);
        assertEquals(6, chunks[1].end);
        assertEquals(6, chunks[2].start);
        assertEquals(10, chunks[2].end);
    }

    @Test
    public void testTenValuesIntoChunksOfOne() {

        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, sampleBuffer.size());
        assertEquals(10, chunks.length);
        assertEquals(0, chunks[0].start);
        assertEquals(1, chunks[0].end);
        assertEquals(1, chunks[1].start);
        assertEquals(2, chunks[1].end);
        assertEquals(2, chunks[2].start);
        assertEquals(3, chunks[2].end);
    }

    @Test
    public void testThousandValuesIntoChunksOfNineHundred() {

        sampleBuffer = new SampleBuffer(new byte[1000], 1000);
        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, sampleBuffer.size());
//
//        System.out.println("chunks:");
//        for (Chunk chunk : chunks) {
//            System.out.println(String.format("chunk: start=%d, end=%d, size=%d", chunk.start, chunk.end, chunk.end - chunk.start));
//        }
//
//        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, sampleBuffer.size());
//        assertEquals(10, chunks.length);
//        assertEquals(0, chunks[0].start);
//        assertEquals(1, chunks[0].end);
//        assertEquals(1, chunks[1].start);
//        assertEquals(2, chunks[1].end);
//        assertEquals(2, chunks[2].start);
//        assertEquals(3, chunks[2].end);
    }

    @Test
    public void testTiming() {
        // 1M samples @ 100kHz.
        sampleBuffer = new SampleBuffer(new byte[1000 * 1000], 100 * 1000);

        assertEquals(nanoSecondsFromSeconds(10), sampleBuffer.timespan);
        assertEquals(nanoSecondsFromMicroSeconds(10), sampleBuffer.timePerSample);

        SampleBufferModel sampleBufferModel = new SampleBufferModel(new ChannelImageCreator(), new LogicAnalyzer());
        sampleBufferModel.setSample(sampleBuffer);
        sampleBufferModel.setViewWidth(100);
        sampleBufferModel.setMousePosition(new Point(50, 0));

        SampleView sampleView = sampleBufferModel.getView();

        // view=100px, zoom=1 => timePerPixel = timespan / 100px = 10s / 100 = 0.1s
        //   1M samples, timePerSample * 1M
        assertEquals(nanoSecondsFromSeconds(0.1), sampleView.timePerPixel);

        // view=100px, mouse = 50px => timespan / (view - mouseX) = 10s / 50
        assertEquals(nanoSecondsFromSeconds(5), sampleView.getMouseTime());

        sampleBufferModel.setMousePosition(new Point(25, 0));
        assertEquals(nanoSecondsFromSeconds(2.5), sampleBufferModel.getView().getMouseTime());

        sampleBufferModel.setMousePosition(new Point(80, 0));
        assertEquals(nanoSecondsFromSeconds(8), sampleBufferModel.getView().getMouseTime());
    }
}
