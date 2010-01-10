package infero.gui.action;

import com.google.inject.*;
import infero.gui.domain.*;
import infero.gui.widgets.*;
import net.guts.gui.action.*;

@Singleton
public class LogActions {
    private final InferoLog inferoLog;
    private final Provider<MainView> mainView;

    @Inject
    public LogActions(InferoLog inferoLog, Provider<MainView> mainView) {
        this.inferoLog = inferoLog;
        this.mainView = mainView;
    }

    public final GutsAction clearLogEntriesAction = new GutsAction("action.clear-log-entries") {
        protected void perform() {
            inferoLog.clear();
            mainView.get().scrollToLastLogEntry();
        }
    };
}
