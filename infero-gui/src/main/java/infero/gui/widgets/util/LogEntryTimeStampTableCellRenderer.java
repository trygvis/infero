package infero.gui.widgets.util;

import static java.awt.Font.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class LogEntryTimeStampTableCellRenderer extends DefaultTableCellRenderer {
    private final Font font = new Font(MONOSPACED, super.getFont().getStyle(), super.getFont().getSize());
    private final DateFormat format;

    public LogEntryTimeStampTableCellRenderer() {
        format = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    protected void setValue(Object value) {
        setText(format.format((Date) value));
    }
}
