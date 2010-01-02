package infero.gui.domain.services;

import infero.gui.domain.Channel;
import infero.gui.domain.RawSample;
import infero.gui.domain.RawSample.Chunk;
import infero.gui.domain.services.ChannelImageCreator.ChannelPixel;
import infero.util.Lap;
import org.junit.Test;

import java.util.Arrays;

import static infero.gui.domain.Channel.getBlankChannel;
import static infero.gui.domain.services.ChannelImageCreator.ChannelPixel.HIGH;
import static infero.util.Lap.startTimer;
import static org.junit.Assert.assertEquals;

public class ChannelImageCreatorTest {
    @Test
    public void testBasic() {
        Channel channel0 = getBlankChannel(0);
        Channel channel1 = getBlankChannel(1);
        Channel channel7 = getBlankChannel(7);

        byte[] values = new byte[100];
        Arrays.fill(values, (byte)0xf0);
        RawSample rawSample = new RawSample();
        rawSample.setSample(values, 1000);

        Chunk[] chunks = rawSample.createChunks(0, rawSample.getCount() - 1, 10);

        values[chunks[2].start] |= 1 << channel1.index;

        ChannelImageCreator imageCreator = new ChannelImageCreator();

        ChannelPixel[] pixels0 = imageCreator.createImage(channel0, rawSample, 10);
        ChannelPixel[] pixels1 = imageCreator.createImage(channel1, rawSample, 10);
        ChannelPixel[] pixels7 = imageCreator.createImage(channel7, rawSample, 10);

        assertEquals(10, pixels0.length);

        for (ChannelPixel pixel : pixels0) {
            assertEquals(ChannelPixel.LOW, pixel);
        }

        for (int i = 0, pixels1Length = pixels1.length; i < pixels1Length; i++) {
            ChannelPixel pixel = pixels1[i];
            if(i == 2) {
                assertEquals(ChannelPixel.BOTH, pixel);
            }
            else {
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

        Lap lap = startTimer("memory allocation");

        byte[] values = new byte[1000 * 1000 * 8];

        lap = lap.lap("memory fill");
        Arrays.fill(values, (byte)0xf0);
        RawSample rawSample = new RawSample();
        rawSample.setSample(values, 1000);

        lap = lap.lap("Create chunks");

        ChannelImageCreator imageCreator = new ChannelImageCreator();

        lap = lap.lap("Create image");
        ChannelPixel[] pixels0 = imageCreator.createImage(channel0, rawSample, 10);

        assertEquals(10, pixels0.length);

        for (ChannelPixel pixel : pixels0) {
            assertEquals(ChannelPixel.LOW, pixel);
        }

        lap = lap.lap("Asserts");
        lap.println(System.out);
    }
}
