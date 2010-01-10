package infero.gui.action;

import com.google.inject.*;
import infero.gui.widgets.*;
import net.guts.gui.action.*;

@Singleton
public class LogActions {
    private final InferoLogTableModel model;
    private final Provider<MainView> mainView;

    @Inject
    public LogActions(InferoLogTableModel model, Provider<MainView> mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    public final GutsAction clearLogEntriesAction = new GutsAction("action.clear-log-entries") {
        protected void perform() {
            model.clear();
            mainView.get().scrollToLastLogEntry();
        }
    };
}
