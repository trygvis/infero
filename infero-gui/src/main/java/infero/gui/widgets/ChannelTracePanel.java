package infero.gui.widgets;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;
import infero.gui.domain.services.ChannelImageCreator.*;
import infero.util.*;
import static infero.util.Lap.*;

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
    private final Channel channel;
    private final SampleBufferModel sampleBufferModel;

    @Inject
    public ChannelTracePanel(InferoLog log,
                             @Assisted Channel channel,
                             final SampleBufferModel sampleBufferModel) {
        this.log = log;
        this.channel = channel;
        this.sampleBufferModel = sampleBufferModel;

        /*
         * If you're missing a handler for the component size updating the width of the panels, it's
         * added from the MainView as only the first need the handler.
         */

        sampleBufferModel.addPropertyChangeListener(SAMPLE_BUFFER, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.revalidate();
                ChannelTracePanel.this.repaint();
            }
        });
        sampleBufferModel.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.revalidate();
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
        try {
            doPaint((Graphics2D) g);
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    public void doPaint(Graphics2D graphics) {
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

        ChannelPixel[] pixels = sampleBufferModel.getPixelsForChannel(channel);

        int highY = 1;
        int lowY = getHeight() - 2;

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
