package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.SampleBuffer.*;
import infero.gui.domain.SampleBufferModel.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;
import infero.gui.domain.services.*;
import infero.gui.domain.services.ChannelImageCreator.*;
import infero.util.*;
import static infero.util.NanoSeconds.*;

import java.awt.*;

/**
 * TODO: Consider renaming this to "trace model" or something similar as it's not close enough samples. - trygve
 */
@Singleton
public class SampleBufferModel extends AbstractDomainObject<Properties> {

    public enum Properties {
        SAMPLE_BUFFER,
        ZOOM,
        MOUSE_POSITION,
        VIEW_WIDTH,
    }

    private final ChannelImageCreator imageCreator;
    private final LogicAnalyzer logicAnalyzer;

    /**
     * A zoom of 1 means that the user will see the entire sample in the view.
     */
    private int zoom = 1;

    private int maxZoom = 10;

    private Point mousePosition;

    /**
     * How wide the current view is in pixels
     */
    private int viewWidth;

    private SampleBuffer sampleBuffer;

    private NanoSeconds startTime;

    private NanoSeconds endTime;

    private P1<ChannelPixel[][]> pixels;

    @Inject
    public SampleBufferModel(ChannelImageCreator imageCreator, LogicAnalyzer logicAnalyzer) {
        this.imageCreator = imageCreator;
        this.logicAnalyzer = logicAnalyzer;
    }

    public void zoomIn() {
        if(!canZoomIn()) {
            throw new RuntimeException("Can't zoom more in.");
        }
        onZoomUpdated(zoom * 2);
    }

    public boolean canZoomIn() {
        return zoom <= maxZoom;
    }

    public void zoomOut() {
        if(!canZoomOut()) {
            throw new RuntimeException("Can't zoom more out.");
        }
        onZoomUpdated(zoom / 2);
    }

    public boolean canZoomOut() {
        return zoom > 1;
    }

    private void onZoomUpdated(int zoom) {
        if(!isValid()) {
            return;
        }
        pixels = new Pixels();
        startTime = nanoSeconds(0);
        endTime = sampleBuffer.timespan;
        firePropertyChange(ZOOM, 0, this.zoom = zoom);
    }

    public void setMousePosition(Point mousePosition) {
        firePropertyChange(MOUSE_POSITION, this.mousePosition, this.mousePosition = mousePosition);
    }

    public void setViewWidth(int viewWidth) {
        pixels = new Pixels();
        firePropertyChange(VIEW_WIDTH, this.viewWidth, this.viewWidth = viewWidth);
    }

    // TODO: Add setStartTime(). Should be updated when scrolling

    public void setSample(SampleBuffer sampleBuffer) {
        pixels = new Pixels();
        maxZoom = 10; // TODO: This has to be calculated. At the max zoom level it should show 50 px per sample.
        SampleBuffer oldSampleBuffer = this.sampleBuffer;
        this.sampleBuffer = sampleBuffer;
        this.mousePosition = new Point(0, 0);
        onZoomUpdated(1);
        firePropertyChange(SAMPLE_BUFFER, this.sampleBuffer, oldSampleBuffer);
    }

    public boolean isValid() {
        return sampleBuffer != null;
    }

    // -----------------------------------------------------------------------
    // Generated
    // -----------------------------------------------------------------------

    public SampleView getView() {
        return new SampleView(sampleBuffer, viewWidth, zoom, mousePosition);
    }

    public ChannelPixel[] getPixelsForChannel(Channel channel) {
        return pixels._1()[channel.index];
    }

    private class Pixels extends P1<ChannelPixel[][]> {
        @Override
        public ChannelPixel[][] calculate() {

            byte[] values = sampleBuffer.getSamples();
            Chunk[] chunks = sampleBuffer.createChunks(startTime, endTime, viewWidth);

            ChannelPixel[][] data = new ChannelPixel[logicAnalyzer.channels.size()][];

            Lap lap = Lap.startTimer();
            for (int i = 0, channelsSize = logicAnalyzer.channels.size(); i < channelsSize; i++) {
                Channel channel = logicAnalyzer.channels.get(i);
                data[i] = imageCreator.createImage(values, chunks, channel, viewWidth);
                lap = lap.lap();
            }

//            lap.println(System.out);

            return data;
        }
    }
}
