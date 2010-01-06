package infero.gui.widgets;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;
import infero.gui.domain.services.*;
import infero.gui.domain.services.ChannelImageCreator.*;
import infero.util.*;
import static infero.util.Lap.*;
import static infero.util.NumberFormats.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;

/**
 * TODO: As an optimization the GUI should buffer the last rendered image.
 * <p/>
 * This will prevent lots of calculation to render the graph. The image should be be invalidated on:
 * 1) new samples
 * 2) resizes. For this it might be possible to store the lines used to render the graphs and just redraw
 */
public class ChannelTracePanel extends JPanel {
    private final InferoLog log;
    private final ChannelImageCreator imageCreator;
    private final Channel channel;
    private final SampleBufferModel sampleBufferModel;

    @Inject
    public ChannelTracePanel(InferoLog log,
                             ChannelImageCreator imageCreator,
                             @Assisted Channel channel,
                             final SampleBufferModel sampleBufferModel) {
        this.log = log;
        this.imageCreator = imageCreator;
        this.channel = channel;
        this.sampleBufferModel = sampleBufferModel;

        sampleBufferModel.addPropertyChangeListener(SAMPLE_BUFFER, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });
        sampleBufferModel.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                sampleBufferModel.setMousePosition(e.getPoint());
            }
        });
    }

    int repaintCounter;

    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        repaintCounter++;

        // -----------------------------------------------------------------------
        // Clear the area
        // -----------------------------------------------------------------------

        Lap lap = startTimer("clear existing image");
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());

        if (!sampleBufferModel.isValid()) {
            return;
        }

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        graphics.setColor(getForeground());

        double zoom = sampleBufferModel.getView().zoom;
        int width = getWidth();
        int highY = 1;
        int lowY = getHeight() - 2;

        lap = lap.lap("create image");
        ChannelPixel[] pixels = imageCreator.createImage(channel, sampleBufferModel.getView().sampleBuffer, width);

        lap = lap.lap("rendering " + pixels.length + " pixels");

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

        log.logEntries(entries(lap.done()));
    }

    private List<InferoLogEntry> entries(StoppedTimer stoppedTimer) {
        String[] strings = Lap.longFormatting(stoppedTimer);
        List<InferoLogEntry> list = new ArrayList<InferoLogEntry>(strings.length);

        for (String string : strings) {
            list.add(info(string));
        }
        return list;
    }
}
