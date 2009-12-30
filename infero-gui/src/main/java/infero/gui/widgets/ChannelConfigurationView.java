package infero.gui.widgets;

import com.jeta.forms.components.panel.FormPanel;
import infero.gui.domain.Channel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static infero.gui.domain.Channel.Properties.*;

/**
 */
public class ChannelConfigurationView extends FormPanel {
    
    private final Channel channel;
    private final JTextField channelName;
    private final JComboBox triggerA;

    public ChannelConfigurationView(Channel channel) {
        super("ChannelConfigurationView.jfrm");

        this.channel = channel;

        getLabel("label").setText(channel.indexText);
        channelName = getTextField("name");
        channelName.setText(channel.getName());

        channel.addPropertyChangeListener(NAME, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                channelName.setText(evt.getNewValue().toString());
            }
        });

        triggerA = getComboBox("triggerA");
    }
}
