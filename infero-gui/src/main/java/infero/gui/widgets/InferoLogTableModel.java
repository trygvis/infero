package infero.gui.widgets;

import infero.gui.domain.*;

import javax.swing.table.*;

/**
* @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
* @version $Id$
*/
class InferoLogTableModel extends AbstractTableModel {
    private final String[] columnNames = new String[] {
            "Time",
            "Entry"
    };

    private final InferoLog inferoLog;

    public InferoLogTableModel(InferoLog inferoLog) {
        this.inferoLog = inferoLog;
    }

    public int getRowCount() {
        return inferoLog.entries.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Long.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        InferoLogEntry logEntry = inferoLog.entries.get(rowIndex);

        if(rowIndex == 0) {
            return logEntry.timeInMilliseconds;
        }
        return logEntry.text;
    }
}
