package infero.gui.widgets;

import com.google.inject.*;
import com.jeta.forms.components.panel.*;
import com.jeta.forms.gui.form.*;
import infero.gui.domain.*;
import infero.gui.domain.services.*;
import static infero.gui.widgets.MainView.Names.*;
import infero.gui.widgets.util.*;

import javax.swing.*;
import java.awt.event.*;

@Singleton
public class MainView extends FormPanel {
    public static class Names {
        public static final String ID_TIMELINESLIDER = "timelineSlider";  //javax.swing.JSlider
        public static final String ID_ZOOMSLIDER = "zoomSlider";  //javax.swing.JSlider
        public static final String ID_TIMELINE = "timeline";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_ZOOM = "zoom";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_DECIMAL = "value.decimal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME = "time";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_HEX = "value.hex";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_OCTAL = "value.octal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_BINARY = "value.binary";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_LOG = "log";  //javax.swing.JTextArea
        public static final String ID_SAMPLE_COUNT = "sample.count";  //javax.swing.JComboBox
        public static final String ID_SAMPLE_RATE = "sample.rate";  //javax.swing.JComboBox
        public static final String ID_SIMULATE_BUTTON = "simulate.button";  //javax.swing.JButton
        public static final String ID_TRACES = "traces";  //javax.swing.JPanel
    }

    private final RawSample rawSample;
    private final LogicAnalyzer logicAnalyzer;
    private final JComboBox sampleCount;
    private final JComboBox sampleRate;

    // -----------------------------------------------------------------------
    // Initialization
    // -----------------------------------------------------------------------

    @Inject
    public MainView(ChannelImageCreator imageCreator, LogicAnalyzer logicAnalyzer, RawSample rawSample) {
        super("MainView.jfrm");
        this.rawSample = rawSample;
        this.logicAnalyzer = logicAnalyzer;
        sampleCount = getComboBox(ID_SAMPLE_COUNT);
        sampleRate = getComboBox(ID_SAMPLE_RATE);

        initializeHeader();
        initializeChannels(imageCreator);
    }

    private void initializeHeader() {
        DefaultComboBoxModel sampleCountModel = (DefaultComboBoxModel) sampleCount.getModel();
        for (CleverInteger i : logicAnalyzer.sampleCounts) {
            sampleCountModel.addElement(i);
        }
        sampleCount.setRenderer(new CleverIntegerListCellRenderer(" samples"));

        DefaultComboBoxModel sampleRateModel = (DefaultComboBoxModel) sampleRate.getModel();
        for (CleverInteger i : logicAnalyzer.sampleRates) {
            sampleRateModel.addElement(i);
        }
        sampleRate.setRenderer(new CleverIntegerListCellRenderer("Hz"));

        getButton(ID_SIMULATE_BUTTON).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSimulateButtonClicked();
            }
        });
    }

    private void initializeChannels(ChannelImageCreator imageCreator) {
        FormAccessor topForm = getFormAccessor();
        FormAccessor tracesForm = getFormAccessor(ID_TRACES);

        for (Channel channel : logicAnalyzer.channels) {
            ChannelConfigurationView channelConfiguration = new ChannelConfigurationView(channel);

            // This is a safety measure against refactoring problems
            JLabel c = getLabel("channel." + channel.index + ".configuration");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".configuration'.");
            }
            topForm.replaceBean(c, channelConfiguration);

            ChannelTracePanel channelTrace = new ChannelTracePanel(imageCreator, channel, rawSample);
            c = tracesForm.getLabel("channel." + channel.index + ".trace");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".trace'.");
            }
            tracesForm.replaceBean("channel." + channel.index + ".trace", channelTrace);
        }
    }

    // -----------------------------------------------------------------------
    // Events Handlers
    // -----------------------------------------------------------------------

    private void onSimulateButtonClicked() {
        byte[] samples = new byte[((CleverInteger) sampleCount.getSelectedItem()).numeric];

        for (int i = 0, samplesLength = samples.length; i < samplesLength; i++) {
            samples[i] = (byte) (i & 0xff);
        }

        rawSample.setSample(samples);
    }
}
