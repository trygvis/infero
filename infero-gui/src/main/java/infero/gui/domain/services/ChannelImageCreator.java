package infero.gui.domain.services;

import com.google.inject.*;
import infero.gui.domain.*;
import infero.gui.domain.SampleBuffer.*;
import static infero.gui.domain.services.ChannelImageCreator.ChannelPixel.*;

@Singleton
public class ChannelImageCreator {
    public enum ChannelPixel {
        HIGH,
        LOW,
        BOTH,
    }

    public ChannelPixel[] createImage(byte[] values, Chunk[] chunks, Channel channel, int width) {
        ChannelPixel[] pixels = new ChannelPixel[width];

        byte filter = (byte) (1 << channel.index);
        for (int i = 0, chunksLength = chunks.length; i < chunksLength; i++) {
            Chunk chunk = chunks[i];
            int j = chunk.start;
            int end = chunk.end;

            if ((values[j] & filter) == 0) {
                while (j < end) {
                    if ((values[j] & filter) == 1) {
                        pixels[i] = BOTH;
                        break;
                    }
                    j++;
                }
                if (j == end) {
                    pixels[i] = LOW;
                }
            } else {
                while (j < end) {
                    if ((values[j] & filter) == 0) {
                        pixels[i] = BOTH;
                        break;
                    }
                    j++;
                }
                if (j == end) {
                    pixels[i] = HIGH;
                }
            }
        }

        return pixels;
    }
}
