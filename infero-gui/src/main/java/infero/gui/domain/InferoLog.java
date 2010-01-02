package infero.gui.domain;

import com.google.inject.*;
import infero.gui.widgets.*;

@Singleton
public class InferoLog {
    private final InferoLogTableModel model;

    @Inject
    public InferoLog(InferoLogTableModel model) {
        this.model = model;
    }

    public void logEntry(InferoLogEntry entry) {
        model.logEntry(entry);
    }

    /**
     * TODO: Instead of this there should be a way to have multi-line log entries.
     */
    public void logEntry(InferoLogEntry ... entries) {
        for (InferoLogEntry entry : entries) {
            model.logEntry(entry);
        }
    }

    public void clear() {
        model.clear();
    }
}
