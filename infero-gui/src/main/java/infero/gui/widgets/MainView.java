package infero.gui.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.form.FormAccessor;
import infero.gui.domain.Channel;
import infero.gui.domain.RawSample;
import infero.gui.domain.LogicAnalyzer;
import infero.gui.domain.services.ChannelImageCreator;

import javax.swing.*;

/**
 */
@Singleton
public class MainView extends FormPanel {
    public static final String ID_HEADER = "header";  //com.jeta.forms.components.label.JETALabel

    @Inject
    public MainView(ChannelImageCreator imageCreator, MainViewHeader mainViewHeader, LogicAnalyzer logicAnalyzer, RawSample rawSample) {
        super("MainView.jfrm");

        FormAccessor accessor = getFormAccessor("top");

        accessor.replaceBean(ID_HEADER, mainViewHeader);

        for (Channel channel : logicAnalyzer.channels) {
            ChannelConfigurationView channelConfiguration = new ChannelConfigurationView(channel);

            // This is a safety measure against refactoring problems
            JLabel c = getLabel("channel." + channel.index + ".configuration");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".configuration'.");
            }
            accessor.replaceBean(c, channelConfiguration);

            ChannelTracePanel channelTrace = new ChannelTracePanel(imageCreator, channel, rawSample);
            c = getLabel("channel." + channel.index + ".trace");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".trace'.");
            }
            accessor.replaceBean(c, channelTrace);
        }
    }
}
