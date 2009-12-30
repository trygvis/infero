package infero.gui.domain;

import com.google.inject.Singleton;
import infero.gui.domain.CurrentSample.Properties;

import static infero.gui.domain.CurrentSample.Properties.*;

/**
 */
@Singleton
public class CurrentSample extends AbstractDomainObject<Properties> {
    public enum Properties {
        VALUES,
        ZOOM,
    }

    private byte[] values = new byte[0];
    private boolean valid;

    /**
     * The timestamp of the current sample.
     */
    private long timestamp;

    /**
     * A zoom of 1 means that the user will see the entire sample in the view.
     */
    private double zoom = 1;

    public CurrentSample() {
        System.out.println("CurrentSample.CurrentSample");
    }

    public void setSample(byte[] values) {
        valid = true;
        timestamp = System.currentTimeMillis();
        firePropertyChange(VALUES, this.values, this.values = values);
    }

    public byte[] getValues() {
        return values;
    }

    public boolean isValid() {
        return valid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if(zoom < 1) {
            System.err.println("Invalid zoom: " + zoom);
            return;
        }
        firePropertyChange(ZOOM, this.zoom, this.zoom = zoom);
    }
}
