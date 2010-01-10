package infero.gui.domain;

import com.google.inject.*;
import static java.util.Arrays.asList;

import javax.swing.event.*;
import java.util.*;

@Singleton
public class InferoLog {
    private final List<InferoLogEntry> _entries = new ArrayList<InferoLogEntry>();
    public final List<InferoLogEntry> entries = Collections.unmodifiableList(_entries);

    private final EventListenerList listenerList = new EventListenerList();

    @Inject
    public InferoLog() {
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public void logEntry(InferoLogEntry entry) {
        int oldSize = _entries.size();
        _entries.add(entry);
        fireChangeEvent(new LogSizeEvent(oldSize, _entries.size()));
    }

    public void logEntries(List<InferoLogEntry> entries) {
        int oldSize = _entries.size();
        _entries.addAll(entries);
        fireChangeEvent(new LogSizeEvent(oldSize, _entries.size()));
    }

    public void logEntry(InferoLogEntry... entries) {
        logEntries(asList(entries));
    }

    public void clear() {
        int oldSize = _entries.size();
        _entries.clear();
        fireChangeEvent(new LogSizeEvent(oldSize, _entries.size()));
    }

    // -----------------------------------------------------------------------
    // Event
    // -----------------------------------------------------------------------

    private void fireChangeEvent(ChangeEvent changeEvent) {
        for (ChangeListener listener : listenerList.getListeners(ChangeListener.class)) {
            listener.stateChanged(changeEvent);
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        listenerList.add(ChangeListener.class, changeListener);
    }

    public class LogSizeEvent extends ChangeEvent {
        public final int oldSize;
        public final int newSize;

        public LogSizeEvent(int oldSize, int newSize) {
            super(InferoLog.this);
            this.oldSize = oldSize;
            this.newSize = newSize;
        }
    }
}
