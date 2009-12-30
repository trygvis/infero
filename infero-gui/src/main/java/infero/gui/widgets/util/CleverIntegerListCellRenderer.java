package infero.gui.widgets.util;

import infero.gui.domain.CleverInteger;

import javax.swing.*;
import java.awt.*;

/**
* @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
* @version $Id$
*/
public class CleverIntegerListCellRenderer extends DefaultListCellRenderer {
    private final String suffix;

    public CleverIntegerListCellRenderer(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, ((CleverInteger) value).text + suffix, index, isSelected, cellHasFocus);
    }
}
