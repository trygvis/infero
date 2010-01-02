package infero.gui.widgets.util;

import infero.util.*;

import javax.swing.*;
import java.awt.*;

public class FormattedNumberListCellRenderer extends DefaultListCellRenderer {
    private final String suffix;

    public FormattedNumberListCellRenderer(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, ((FormattedNumber) value).text() + suffix, index, isSelected, cellHasFocus);
    }
}
