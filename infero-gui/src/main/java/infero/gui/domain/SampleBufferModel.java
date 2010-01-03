package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.SampleBufferModel.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;

import java.awt.*;

@Singleton
public class SampleBufferModel extends AbstractDomainObject<Properties> {

    public enum Properties {
        SAMPLE_BUFFER,
        ZOOM,
        MOUSE_POSITION,
        VIEW_WIDTH,
    }

    /**
     * A zoom of 1 means that the user will see the entire sample in the view.
     */
    private int zoom = 1;

    private Point mousePosition;

    /**
     * How wide the current view is in pixels
     */
    private int viewWidth;

    private SampleBuffer sampleBuffer;

    public void setZoom(int zoom) {
        if (zoom <= 0 || zoom > 100) {
            System.err.println("Invalid zoom: " + zoom);
            return;
        }
        firePropertyChange(ZOOM, this.zoom, this.zoom = zoom);
    }

    public void setMousePosition(Point mousePosition) {
        firePropertyChange(MOUSE_POSITION, this.mousePosition, this.mousePosition = mousePosition);
    }

    public void setViewWidth(int viewWidth) {
        firePropertyChange(VIEW_WIDTH, this.viewWidth, this.viewWidth = viewWidth);
    }

    public void setSample(SampleBuffer sampleBuffer) {
        firePropertyChange(SAMPLE_BUFFER, this.sampleBuffer, this.sampleBuffer = sampleBuffer);
    }

    public SampleView getView() {
        return new SampleView(sampleBuffer, viewWidth, zoom, mousePosition);
    }

    public boolean isValid() {
        return sampleBuffer != null;
    }
}
