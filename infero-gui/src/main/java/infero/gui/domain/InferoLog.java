package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.InferoLog.Properties;
import static infero.gui.domain.InferoLog.Properties.*;
import static java.util.Collections.*;

import java.util.*;

@Singleton
public class InferoLog extends AbstractDomainObject<Properties> {

    public enum Properties {
        LOG_ENTRIES
    }

    private final List<InferoLogEntry> mutableEntries = new ArrayList<InferoLogEntry>();
    public final List<InferoLogEntry> entries = unmodifiableList(mutableEntries);

    public void logEntry(InferoLogEntry entry) {
        firePropertyChange(LOG_ENTRIES, null, null);
    }

    public void clear() {
        mutableEntries.clear();
        firePropertyChange(LOG_ENTRIES, null, null);
    }
}
