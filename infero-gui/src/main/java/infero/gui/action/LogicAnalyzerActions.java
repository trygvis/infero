package infero.gui.action;

import com.google.inject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import infero.gui.widgets.*;
import net.guts.gui.action.*;

public class LogicAnalyzerActions {
    private final InferoLog inferoLog;
    private final SampleBufferModel sampleBufferModel;
    private final Provider<MainView> mainView;

    @Inject
    public LogicAnalyzerActions(InferoLog inferoLog,
                                SampleBufferModel sampleBufferModel,
                                Provider<MainView> mainView) {
        this.inferoLog = inferoLog;
        this.sampleBufferModel = sampleBufferModel;
        this.mainView = mainView;
    }

    public final GutsAction simulate = new GutsAction("action.simulate") {
        @Override
        protected void perform() {
            final MainView mainView = LogicAnalyzerActions.this.mainView.get();

            getDefaultTaskService().execute(new Task<Object, Object>() {
                public Object doInBackground(TaskController<Object> publisher) throws Exception {
                    inferoLog.logEntry(info("Generating samples."));

                    byte[] samples = new byte[mainView.getSelectedSampleCount()];

                    inferoLog.logEntry(info("Generating %d samples.", samples.length));

                    try {
                        for (int i = 0, samplesLength = samples.length; i < samplesLength; i++) {
                            samples[i] = (byte) (i & 0xff);
                        }

                        int samplesPerSecond = mainView.getSelectedSampleRate();

                        System.out.println("mainView.getTracePanelWidth() = " + mainView.getTracePanelWidth());
                        sampleBufferModel.setViewWidth(mainView.getTracePanelWidth());

                        sampleBufferModel.setSample(new SampleBuffer(samples, samplesPerSecond));
                    } catch (Throwable e) {
                        e.printStackTrace(System.out);
                    }

                    return null;
                }
            });
        }
    };
}
