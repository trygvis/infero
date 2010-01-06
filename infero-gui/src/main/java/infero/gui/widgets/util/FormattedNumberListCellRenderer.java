package infero.gui.widgets.util;

import javax.swing.*;

public class FormattedNumberListCellRenderer extends DefaultListCellRenderer {
    private final String suffix;

    public FormattedNumberListCellRenderer(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String getText() {
        return super.getText() + suffix;
    }
}
