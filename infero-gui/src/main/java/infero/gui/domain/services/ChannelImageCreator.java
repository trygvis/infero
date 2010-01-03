package infero.gui.domain.services;

import com.google.inject.Singleton;
import infero.gui.domain.Channel;
import infero.gui.domain.SampleBuffer;
import infero.gui.domain.SampleBuffer.Chunk;

import static infero.gui.domain.services.ChannelImageCreator.ChannelPixel.*;

/**
 * @version $Id$
 */
@Singleton
public class ChannelImageCreator {
    public enum ChannelPixel {
        HIGH,
        LOW,
        BOTH,
    }

    public ChannelPixel[] createImage(Channel channel, SampleBuffer sampleBuffer, int width) {
        ChannelPixel[] pixels = new ChannelPixel[width];

        byte[] values = sampleBuffer.getSamples();
        byte filter = (byte) (1 << channel.index);

        Chunk[] chunks = sampleBuffer.createChunks(0, sampleBuffer.size() - 1, width);
        for (int i = 0, chunksLength = chunks.length; i < chunksLength; i++) {
            Chunk chunk = chunks[i];
            boolean high = false;
            boolean low = false;

            for (int j = chunk.start; j <= chunk.end; j++) {
                byte value = values[j];

                if ((value & filter) == 0) {
                    low = true;
                } else {
                    high = true;
                }
            }

            pixels[i] = high ? (low ? BOTH : HIGH ) : LOW;
        }

        return pixels;
    }
}
