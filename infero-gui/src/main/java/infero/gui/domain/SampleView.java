package infero.gui.domain;

import infero.util.*;

import java.awt.*;

public class SampleView {
    public final SampleBuffer sampleBuffer;
    public final int viewWidth;
    public final int zoom;

    // TODO: the mouse info should not be here
    private final int mouseX;

    public final NanoSeconds timePerPixel;
    public final NanoSeconds timePerSample;

    public SampleView(SampleBuffer sampleBuffer, int viewWidth, int zoom, Point mousePosition) {
        this.sampleBuffer = sampleBuffer;
        this.viewWidth = viewWidth;
        this.zoom = zoom;
        this.mouseX = mousePosition.x;

        this.timePerPixel = sampleBuffer.timespan.dividedBy(viewWidth);
        this.timePerSample = sampleBuffer.timePerSample;
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
