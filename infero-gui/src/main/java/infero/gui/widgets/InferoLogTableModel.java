package infero.gui.widgets;

import com.google.inject.*;
import infero.gui.domain.*;

import javax.swing.table.*;
import java.util.*;

@Singleton
public class InferoLogTableModel extends AbstractTableModel {
    private final String[] columnNames = new String[]{
            "Time",
            "Entry"
    };

    private final List<InferoLogEntry> entries = new ArrayList<InferoLogEntry>();

    // -----------------------------------------------------------------------
    // AbstractTableModel Implementation
    // -----------------------------------------------------------------------

    public int getRowCount() {
        return entries.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Date.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        InferoLogEntry logEntry = entries.get(rowIndex);

        if (columnIndex == 0) {
            return logEntry.date;
        }
        return logEntry.text;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public void logEntry(InferoLogEntry entry) {

        entries.add(entry);
        super.fireTableRowsInserted(entries.size(), entries.size());
    }

    public void logEntries(List<InferoLogEntry> entries) {
        int firstRow = this.entries.size();
        this.entries.addAll(entries);
        super.fireTableRowsInserted(firstRow, firstRow + entries.size());
    }

    public void clear() {
        int size = entries.size();
        entries.clear();
        super.fireTableRowsDeleted(0, size - 1);
    }
}
