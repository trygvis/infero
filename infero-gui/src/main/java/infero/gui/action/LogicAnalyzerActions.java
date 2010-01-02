package infero.gui.action;

import com.google.inject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import infero.gui.widgets.*;
import net.guts.gui.action.*;

public class LogicAnalyzerActions {
    private final InferoLog inferoLog;
    private final RawSample rawSample;
    private final Provider<MainView> mainView;

    @Inject
    public LogicAnalyzerActions(InferoLog inferoLog, RawSample rawSample, Provider<MainView> mainView) {
        this.inferoLog = inferoLog;
        this.rawSample = rawSample;
        this.mainView = mainView;
    }

    public final GutsAction simulate = new GutsAction("action.simulate") {
        @Override
        protected void perform() {
            getDefaultTaskService().execute(new Task<Object, Object>() {
                public Object doInBackground(TaskController<Object> publisher) throws Exception {
                    inferoLog.logEntry(info("Generating samples."));

                    byte[] samples = new byte[mainView.get().getSelectedSampleCount()];

                    inferoLog.logEntry(info("Generating %d samples.", samples.length));

                    for (int i = 0, samplesLength = samples.length; i < samplesLength; i++) {
                        samples[i] = (byte) (i & 0xff);
                    }

                    int samplesPerSecond = mainView.get().getSelectedSampleRate();

                    rawSample.setSample(samples, samplesPerSecond);

                    return null;
                }
            });
        }
    };
}
