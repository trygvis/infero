package infero.gui.domain;

import com.google.inject.*;
import infero.gui.widgets.*;

import java.util.*;

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

    public void logEntries(List<InferoLogEntry> entries) {
        model.logEntries(entries);
    }

    public void logEntry(InferoLogEntry ... entries) {
        for (InferoLogEntry entry : entries) {
            model.logEntry(entry);
        }
    }

    public void clear() {
        model.clear();
    }
}
