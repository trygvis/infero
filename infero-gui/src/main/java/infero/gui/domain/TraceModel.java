package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.TraceModel.*;
import static infero.gui.domain.TraceModel.Properties.*;

import java.awt.*;

@Singleton
public class TraceModel extends AbstractDomainObject<Properties> {
    public enum Properties {
        ZOOM,
        MOUSE_POSITION,
    }

    /**
     * A zoom of 1 means that the user will see the entire sample in the view.
     */
    private double zoom = 1;

    private Point mousePosition;

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if (zoom < 0 || zoom > 1) {
            System.err.println("Invalid zoom: " + zoom);
            return;
        }
        firePropertyChange(ZOOM, this.zoom, this.zoom = zoom);
    }

    public void setMousePosition(Point mousePosition) {
        firePropertyChange(MOUSE_POSITION, this.mousePosition, this.mousePosition = mousePosition);
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public long getTimeInNanoSeconds() {
        return (long) mousePosition.getX();
    }
}
