package infero.gui.action;

import com.google.inject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;
import infero.gui.widgets.*;
import net.guts.gui.action.*;
import net.guts.gui.action.blocker.*;

import java.beans.*;

public class LogicAnalyzerActions {
    private final InferoLog inferoLog;
    private final SampleBufferModel sampleBufferModel;
    private final Provider<MainView> mainView;

    public final GutsAction simulate;
    public final GutsAction zoomIn;
    public final GutsAction zoomOut;

    @Inject
    public LogicAnalyzerActions(InferoLog inferoLog,
                                final SampleBufferModel sampleBufferModel,
                                Provider<MainView> mainView) {
        this.inferoLog = inferoLog;
        this.sampleBufferModel = sampleBufferModel;
        this.mainView = mainView;

        simulate = new GutsAction("action.simulate") {
            @Override
            protected void perform() {
                getDefaultTaskService().execute(new Task<Object, Object>() {
                    public Object doInBackground(TaskController<Object> publisher) throws Exception {
                        try {
                            runGenerateSamples();
                        } catch (RuntimeException e) {
                            System.out.println("LogicAnalyzerActions.doInBackground: RuntimeException");
//                            e.printStackTrace(System.out);
                            throw e;
                        }
                        return null;
                    }
                }, this, GlassPaneBlocker.class);
            }
        };

        zoomIn = new GutsAction("action.zoomIn") {
            {
                sampleBufferModel.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        action().setEnabled(sampleBufferModel.canZoomIn());
                    }
                });
                action().setEnabled(false);
            }

            @Override
            protected void perform() {
                sampleBufferModel.zoomIn();
            }
        };

        zoomOut = new GutsAction("action.zoomOut") {
            {
                sampleBufferModel.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        action().setEnabled(sampleBufferModel.canZoomOut());
                    }
                });
                action().setEnabled(false);
            }

            @Override
            protected void perform() {
                sampleBufferModel.zoomOut();
            }
        };
    }

    // -----------------------------------------------------------------------
    // Action Implementations
    // -----------------------------------------------------------------------

    // TODO: This should be a Task
    private void runGenerateSamples() {
        final MainView mainView = this.mainView.get();

        inferoLog.logEntry(info("Generating samples."));

        byte[] samples = new byte[mainView.getSelectedSampleCount()];

        inferoLog.logEntry(info("Generating %d samples.", samples.length));

        for (int i = 0, samplesLength = samples.length; i < samplesLength; i++) {
            samples[i] = (byte) (i & 0xff);
        }

        int samplesPerSecond = mainView.getSelectedSampleRate();

        sampleBufferModel.setViewWidth(mainView.getTracePanelWidth());

        sampleBufferModel.setSample(new SampleBuffer(samples, samplesPerSecond));
    }
}
