package infero.gui.widgets;

import infero.gui.domain.Channel;
import infero.gui.domain.CurrentSample;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static infero.gui.domain.CurrentSample.Properties.VALUES;
import static infero.gui.domain.CurrentSample.Properties.ZOOM;

/**
 * TODO: As an optimization the GUI should buffer the last rendered image.
 *
 * This will prevent 
 *
 */
public class ChannelTracePanel extends JPanel {
    private final Channel channel;
    private final CurrentSample currentSample;

//    private long timestampOfLastPaintedSample;

    public ChannelTracePanel(Channel channel, CurrentSample currentSample) {
        this.channel = channel;
        this.currentSample = currentSample;
        currentSample.addPropertyChangeListener(VALUES, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });
        currentSample.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ChannelTracePanel.this.repaint();
            }
        });
    }

    int repaintCounter;

    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        repaintCounter++;
        if (!currentSample.isValid()) {
            graphics.drawLine(0, 0, getWidth(), getHeight());
            graphics.drawLine(0, getHeight(), getWidth(), 0);

            return;
        }

        // This was not smart enough
//        if (timestampOfLastPaintedSample == currentSample.getTimestamp()) {
//            System.out.println("ChannelTracePanel.paint. Skipping, same sample");
//            return;
//        }

        // -----------------------------------------------------------------------
        // Clear the area
        // -----------------------------------------------------------------------

        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        graphics.setColor(getForeground());

        byte[] values = currentSample.getValues();
        int valuesLength = currentSample.getValues().length;

        double zoom = currentSample.getZoom();
        int width = getWidth();
        int highY = 1;
        int lowY = getHeight() - 2;

        byte filter = (byte) (1 << channel.index);

        boolean last = (values[0] & filter) == 1;
        int lastX = 0;
        int lastY = last ? highY : lowY;

//        System.out.println("********************************************************");
//        System.out.println("********************************************************");
//        System.out.println("********************************************************");
//        System.out.println("********************************************************");
//        System.out.println("********************************************************");
//        System.out.println("********************************************************");
//        System.out.println("ChannelTracePanel.paint: valid=" + currentSample.isValid() + ", repaintCounter=" + repaintCounter);
//        System.out.println("channel.index = " + channel.index);
//        System.out.println("zoom = " + zoom);
//        System.out.println("width = " + width);
//        System.out.println("valuesLength = " + valuesLength);
//        System.out.println("filter = " + filter);
        for (int i = 1; i < valuesLength; i++) {
            byte b = currentSample.getValues()[i];
            boolean value = (b & filter) > 0;

            int x = (int)(((double) i) / valuesLength * zoom * width);
            int y = value ? highY : lowY;

//            System.out.format("#%d, x=%09f, y=%d, value=%d, state=%s", i, x, y,b,  value ? "value" : "low");
//            System.out.println();

            if(last == value) {

                graphics.drawLine(lastX, lastY, x, y);
            }
            else {
                if(last) {
                    //
                    // ..-|
                    //    |-------. .
                    //

                    graphics.drawLine(lastX, highY, lastX, lowY);
                    graphics.drawLine(lastX, lowY, x, lowY);
                }
                else {
                    //
                    //    |-------. .
                    // ..-|
                    //

                    graphics.drawLine(lastX, highY, lastX, lowY);
                    graphics.drawLine(lastX, highY, x, highY);
                }
            }

            lastX = x;
            lastY = y;
            last = value;
        }
    }
}
