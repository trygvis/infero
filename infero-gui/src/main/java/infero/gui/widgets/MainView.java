package infero.gui.widgets;

import com.google.inject.*;
import com.jeta.forms.components.panel.*;
import com.jeta.forms.gui.form.*;
import infero.gui.action.*;
import infero.gui.domain.*;
import static infero.gui.domain.TraceModel.Properties.*;
import infero.gui.domain.services.*;
import static infero.gui.widgets.MainView.Names.*;
import infero.gui.widgets.util.*;
import infero.util.*;
import static infero.util.NumberFormats.*;
import static java.lang.String.valueOf;
import static javax.swing.JTable.*;
import net.guts.gui.action.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

@Singleton
public class MainView extends FormPanel {
    public static class Names {
        public static final String ID_ZOOM_SLIDER = "zoom.slider";  //javax.swing.JSlider
        public static final String ID_TIMELINE = "timeline";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME_SCROLLBAR = "time.scrollbar";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_ZOOM = "zoom";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_DECIMAL = "value.decimal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME = "time";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_HEX = "value.hex";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_OCTAL = "value.octal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_BINARY = "value.binary";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_LOG_FORM = "log.form";  //javax.swing.JPanel
        public static final String ID_LOG_TABLE = "log.table";  //javax.swing.JTable
        public static final String ID_CLEAR_BUTTON = "clear.button";  //javax.swing.JButton
        public static final String ID_SAMPLE_COUNT = "sample.count";  //javax.swing.JComboBox
        public static final String ID_SAMPLE_RATE = "sample.rate";  //javax.swing.JComboBox
        public static final String ID_SIMULATE_BUTTON = "simulate.button";  //javax.swing.JButton
        public static final String ID_TRACES = "traces";  //javax.swing.JPanel
    }

    private final InferoLog inferoLog;
    private final RawSample rawSample;
    private final TraceModel traceModel;
    private final LogicAnalyzer logicAnalyzer;
    private final InferoLogTableModel inferoLogTableModel;

    private final JComboBox sampleCount;
    private final JComboBox sampleRate;

    private final JLabel zoom;

    private final JLabel decimal;
    private final JLabel hex;
    private final JLabel octal;
    private final JLabel binary;

    private final JSlider zoomSlider;
    private final JScrollBar timelineScrollbar;

    private final JTable logTable;

    // -----------------------------------------------------------------------
    // Initialization
    // -----------------------------------------------------------------------

    @Inject
    public MainView(ChannelImageCreator imageCreator, InferoLog inferoLog, RawSample rawSample, TraceModel traceModel,
                    LogicAnalyzer logicAnalyzer, InferoLogTableModel inferoLogTableModel,
                    LogicAnalyzerActions logicAnalyzerActions, infero.gui.action.LogActions logActions) {
        super("MainView.jfrm");
        this.inferoLog = inferoLog;
        this.rawSample = rawSample;
        this.traceModel = traceModel;
        this.logicAnalyzer = logicAnalyzer;
        this.inferoLogTableModel = inferoLogTableModel;

        sampleCount = getComboBox(ID_SAMPLE_COUNT);
        sampleRate = getComboBox(ID_SAMPLE_RATE);

        zoom = getLabel(ID_ZOOM);

        decimal = getLabel(ID_VALUE_DECIMAL);
        hex = getLabel(ID_VALUE_HEX);
        octal = getLabel(ID_VALUE_OCTAL);
        binary = getLabel(ID_VALUE_BINARY);

        zoomSlider = (JSlider) getComponentByName(ID_ZOOM_SLIDER);
        timelineScrollbar = new JScrollBar(JScrollBar.HORIZONTAL);

        logTable = getFormAccessor(ID_LOG_FORM).getTable(ID_LOG_TABLE);

        initializeHeader(logicAnalyzerActions.simulate);
        initializeChannels(imageCreator, traceModel);
        initializeSliders();
        initializeStatusPanel(traceModel);
        initializeLog(logActions.clearLogEntriesAction);
    }

    private void initializeHeader(GutsAction simulateAction) {
        DefaultComboBoxModel sampleCountModel = (DefaultComboBoxModel) sampleCount.getModel();
        for (FormattedInteger i : logicAnalyzer.sampleCounts) {
            sampleCountModel.addElement(i);
        }
        sampleCount.setRenderer(new FormattedNumberListCellRenderer(" samples"));

        DefaultComboBoxModel sampleRateModel = (DefaultComboBoxModel) sampleRate.getModel();
        for (FormattedInteger i : logicAnalyzer.sampleRates) {
            sampleRateModel.addElement(i);
        }
        sampleRate.setRenderer(new FormattedNumberListCellRenderer("Hz"));

        getButton(ID_SIMULATE_BUTTON).setAction(simulateAction.action());
    }

    private void initializeChannels(ChannelImageCreator imageCreator, TraceModel traceModel) {
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

            ChannelTracePanel channelTrace = new ChannelTracePanel(imageCreator, channel, inferoLog, rawSample, traceModel);
            c = tracesForm.getLabel("channel." + channel.index + ".trace");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".trace'.");
            }
            tracesForm.replaceBean("channel." + channel.index + ".trace", channelTrace);
        }
    }

    private void initializeSliders() {
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onZoomChanged(zoom, zoomSlider);
            }
        });

        FormAccessor topForm = getFormAccessor();

        topForm.replaceBean(ID_TIME_SCROLLBAR, timelineScrollbar);

        timelineScrollbar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                onTimelineChanged();
            }
        });
    }

    private void initializeStatusPanel(final TraceModel traceModel) {
        final JLabel time = getLabel(ID_TIME);

        onSampleInvalidated();

        traceModel.addPropertyChangeListener(MOUSE_POSITION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (rawSample.isValid()) {
                    time.setText(nanoTimeFormattingOf(traceModel.getTimeInNanoSeconds()).text);
                }
            }
        });
    }

    private void initializeLog(GutsAction clearLogEntriesAction) {

        logTable.setModel(inferoLogTableModel);

        TableColumn timeColumn = logTable.getColumnModel().getColumn(0);
        timeColumn.setPreferredWidth(100);
        timeColumn.setResizable(false);

        logTable.setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);

        getButton(ID_CLEAR_BUTTON).setAction(clearLogEntriesAction.action());

        inferoLogTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                scrollToLastLogEntry();
            }
        });
    }

    // -----------------------------------------------------------------------
    // Events Handlers
    // -----------------------------------------------------------------------

    private void onSampleInvalidated() {
        zoom.setText("-");

        decimal.setText("-");
        hex.setText("-");
        octal.setText("-");
        binary.setText("-");
        binary.setText("-");

        zoomSlider.setEnabled(false);
        timelineScrollbar.setEnabled(false);
    }

    private void onZoomChanged(JLabel zoom, JSlider zoomSlider) {
        double currentZoom = ((double) zoomSlider.getValue()) / zoomSlider.getMaximum();
        traceModel.setZoom(currentZoom);

        if (rawSample.isValid()) {
            zoom.setText(valueOf(currentZoom));
        }
    }

    private void onTimelineChanged() {
    }

    // -----------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------

    public int getSelectedSampleCount() {
        return ((FormattedInteger) sampleCount.getSelectedItem()).numeric;
    }

    public int getSelectedSampleRate() {
        return ((FormattedInteger) sampleRate.getSelectedItem()).numeric;
    }

    /**
     * This doesn't exactly work as advertised, it only scrolls to the second to last entry.
     */
    public void scrollToLastLogEntry() {
        Dimension dimension = logTable.getSize();
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();

        logTable.scrollRectToVisible(new Rectangle(width, height, width, height));
    }
}
