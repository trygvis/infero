package infero.gui.widgets;

import infero.gui.domain.*;
import infero.gui.domain.InferoLog.*;

import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

public class InferoLogTableModel extends AbstractTableModel implements ChangeListener {
    private final String[] columnNames = new String[]{
            "Time",
            "Entry"
    };

    private final InferoLog inferoLog;

    public InferoLogTableModel(InferoLog inferoLog) {
        this.inferoLog = inferoLog;
        inferoLog.addChangeListener(this);
    }

    // -----------------------------------------------------------------------
    // AbstractTableModel Implementation
    // -----------------------------------------------------------------------

    public int getRowCount() {
        return inferoLog.entries.size();
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
        InferoLogEntry logEntry = inferoLog.entries.get(rowIndex);

        if (columnIndex == 0) {
            return logEntry.date;
        }
        return logEntry.text;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public void stateChanged(ChangeEvent event) {
        if (!(event instanceof LogSizeEvent)) {
            return;
        }

        LogSizeEvent e = (LogSizeEvent) event;

        if(e.newSize == 0) {
            if(e.oldSize > 0) {
                super.fireTableRowsDeleted(0, e.oldSize);
            }
        }
        else {
            super.fireTableRowsInserted(e.oldSize, e.newSize);
        }
    }
}
