package infero.gui.domain.services;

import infero.gui.domain.*;
import static infero.gui.domain.Channel.*;
import infero.gui.domain.SampleBuffer.*;
import infero.gui.domain.services.ChannelImageCreator.*;
import static infero.gui.domain.services.ChannelImageCreator.ChannelPixel.*;
import infero.util.*;
import static infero.util.Lap.*;
import static infero.util.NanoSeconds.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class ChannelImageCreatorTest {
    @Test
    public void testBasic() {
        Channel channel0 = getBlankChannel(0);
        Channel channel1 = getBlankChannel(1);
        Channel channel7 = getBlankChannel(7);

        byte[] values = new byte[100];
        Arrays.fill(values, (byte) 0xf0);
        SampleBuffer sampleBuffer = new SampleBuffer(values, 1000);

        NanoSeconds startTime = nanoSeconds(0);
        NanoSeconds endTime = sampleBuffer.timespan;
        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, 10);

        values[chunks[2].start] |= 1 << channel1.index;

        ChannelImageCreator imageCreator = new ChannelImageCreator();

        ChannelPixel[] pixels0 = imageCreator.createImage(values, chunks, channel0, 10);
        ChannelPixel[] pixels1 = imageCreator.createImage(values, chunks, channel1, 10);
        ChannelPixel[] pixels7 = imageCreator.createImage(values, chunks, channel7, 10);

        assertEquals(10, pixels0.length);

        for (ChannelPixel pixel : pixels0) {
            assertEquals(ChannelPixel.LOW, pixel);
        }

        for (int i = 0, pixels1Length = pixels1.length; i < pixels1Length; i++) {
            ChannelPixel pixel = pixels1[i];
            if (i == 2) {
                assertEquals(ChannelPixel.BOTH, pixel);
            } else {
                assertEquals(ChannelPixel.LOW, pixel);
            }
        }

        for (ChannelPixel pixel : pixels7) {
            assertEquals(HIGH, pixel);
        }
    }

    @Test
    public void testLots() {
        Channel channel0 = getBlankChannel(0);

        byte[] values = new byte[1000 * 1000 * 8];

        Arrays.fill(values, (byte) 0xf0);
        SampleBuffer sampleBuffer = new SampleBuffer(values, 1000);

        NanoSeconds startTime = nanoSeconds(0);
        NanoSeconds endTime = sampleBuffer.timespan;
        int width = 1000;
        Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, width);

        ChannelImageCreator imageCreator = new ChannelImageCreator();

        for (int i = 0; i < 1000; i++) {

            Lap lap = startTimer("Create image");
            ChannelPixel[] pixels0 = imageCreator.createImage(values, chunks, channel0, width);

            assertEquals(width, pixels0.length);

            for (ChannelPixel pixel : pixels0) {
                assertEquals(ChannelPixel.LOW, pixel);
            }

            StoppedTimer stoppedTimer = lap.done();
//            System.out.println("stoppedTimer.totalTime = " + nanoTimeFormattingOf(stoppedTimer.totalTime));
//            for (String s : longFormatting(stoppedTimer)) {
//                System.out.println(s);
//            }
        }
    }
}
