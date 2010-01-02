package infero.gui.widgets;

import infero.gui.domain.*;
import static infero.gui.domain.RawSample.Properties.*;
import infero.gui.domain.services.*;
import infero.gui.domain.services.ChannelImageCreator.*;
import infero.util.*;
import static infero.util.Lap.*;

import javax.swing.*;
import java.awt.*;
import java.beans.*;

/**
 * TODO: As an optimization the GUI should buffer the last rendered image.
 * <p/>
 * This will prevent lots of calculation to render the graph. The image should be be invalidated on:
 * 1) new samples
 * 2) resizes. For this it might be possible to store the lines used to render the graphs and just redraw
 */
public class ChannelTracePanel extends JPanel {
    private final ChannelImageCreator imageCreator;
    private final Channel channel;
    private final RawSample rawSample;

    public ChannelTracePanel(ChannelImageCreator imageCreator, Channel channel, RawSample rawSample) {
        this.imageCreator = imageCreator;
        this.channel = channel;
        this.rawSample = rawSample;
        rawSample.addPropertyChangeListener(VALUES, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });
        rawSample.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });
    }

    int repaintCounter;

    public void paint(Graphics g) {
        Lap lap = startTimer("paint()");
        Graphics2D graphics = (Graphics2D) g;

        repaintCounter++;

        // -----------------------------------------------------------------------
        // Clear the area
        // -----------------------------------------------------------------------

        lap = lap.lap("clear existing image");
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());

        if (!rawSample.isValid()) {
            return;
        }

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        graphics.setColor(getForeground());

        double zoom = rawSample.getZoom();
        int width = getWidth();
        int highY = 1;
        int lowY = getHeight() - 2;

        lap = lap.lap("create image");
        ChannelPixel[] pixels = imageCreator.createImage(channel, rawSample, width);

        lap = lap.lap("render pixels");

        ChannelPixel last = pixels[0];
        for (int x = 0, pixelsLength = pixels.length; x < pixelsLength; x++) {
            ChannelPixel pixel = pixels[x];
            switch (pixel) {
                case HIGH:
                    if (last == ChannelPixel.LOW) {
                        graphics.drawLine(x, lowY, x, highY);
                    } else {
                        graphics.drawLine(x, highY, x, highY);
                    }
                    break;
                case LOW:
                    if (last == ChannelPixel.HIGH) {
                        graphics.drawLine(x, highY, x, lowY);
                    } else {
                        graphics.drawLine(x, lowY, x, lowY);
                    }
                    break;
                case BOTH:
                    graphics.drawLine(x, highY, x, lowY);
                    break;
            }
            last = pixel;
        }

        lap.println(System.err);
    }
}
