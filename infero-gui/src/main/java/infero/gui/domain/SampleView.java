package infero.gui.domain;

import infero.util.*;

import java.awt.*;

public class SampleView {
    public final SampleBuffer sampleBuffer;
    public final int viewWidth;
    public final double zoom;
    private final int mouseX;

    public final NanoSeconds timePerPixel;

    public SampleView(SampleBuffer sampleBuffer, int viewWidth, double zoom, Point mousePosition) {
        this.sampleBuffer = sampleBuffer;
        this.viewWidth = viewWidth;
        this.zoom = zoom;
        this.mouseX = mousePosition.x;

        this.timePerPixel = sampleBuffer.timespan.dividedBy(viewWidth);
    }

    /**
     * Returns the time at the location of the mouse pointer
     */
    public NanoSeconds getMouseTime() {
        return sampleBuffer.timespan.dividedBy(((double) viewWidth) / mouseX);
    }

    public byte getMouseValue() {
        return sampleBuffer.getSample((int) (((double) sampleBuffer.size() * mouseX) / viewWidth));
    }
}
