package infero.gui.widgets;

import com.google.inject.*;
import com.jeta.forms.components.image.*;
import com.jeta.forms.components.panel.*;
import com.jeta.forms.gui.form.*;
import infero.gui.action.*;
import infero.gui.domain.*;
import static infero.gui.domain.ExceptionListModel.Properties.*;
import static infero.gui.domain.MemoryUsageModel.Properties.*;
import static infero.gui.domain.SampleBufferModel.Properties.*;
import static infero.gui.widgets.MainView.Names.*;
import infero.gui.widgets.error.*;
import infero.gui.widgets.util.*;
import infero.util.*;
import static infero.util.NumberFormats.*;
import static java.lang.String.*;
import static javax.swing.JTable.*;
import net.guts.gui.action.*;
import net.guts.gui.application.*;
import net.guts.gui.application.WindowController.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

@Singleton
public class MainView extends FormPanel {
    protected static class Names {
        public static final String ID_TIME_SCROLLBAR = "time.scrollbar";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_ZOOM = "zoom";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_DECIMAL = "value.decimal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIMESPAN = "timespan";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_HEX = "value.hex";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_OCTAL = "value.octal";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_VALUE_BINARY = "value.binary";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME_PER_PIXEL = "time.per.pixel";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME = "time";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_PIXELS = "pixels";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_TIME_PER_SAMPLE = "time.per.sample";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_LOG_FORM = "log.form";  //javax.swing.JPanel
        public static final String ID_CLEAR_BUTTON = "clear.button";  //javax.swing.JButton
        public static final String ID_SAMPLE_COUNT = "sample.count";  //javax.swing.JComboBox
        public static final String ID_SAMPLE_RATE = "sample.rate";  //javax.swing.JComboBox
        public static final String ID_SIMULATE_BUTTON = "simulate.button";  //javax.swing.JButton
        public static final String ID_TRACES = "traces";  //javax.swing.JPanel
        public static final String ID_MEMORY_USAGE = "memory.usage";  //javax.swing.JProgressBar
        public static final String ID_ERROR_INDICATOR = "error.indicator";  //com.jeta.forms.components.image.ImageComponent

        // Custom - this was not copied from the name export - bug?
        public static final String ID_LOG_TABLE_SCROLL_PANE = "log.table.scroll.pane";
    }

    private final SampleBufferModel sampleBufferModel;
    private final LogicAnalyzer logicAnalyzer;
    private final InferoLogTableModel inferoLogTableModel;

    private final JComboBox sampleCount;
    private final JComboBox sampleRate;

    // Status panel
    private final JLabel zoom;
    private final JLabel decimal;
    private final JLabel hex;
    private final JLabel octal;
    private final JLabel binary;
    private final JLabel time;
    private final JLabel timespan;
    private final JLabel timePerPixel;
    private final JLabel timePerSample;
    private final JLabel pixels;

    // Error
    private final Icon smileIcon;
    private final Icon sadIcon;
    private final ImageComponent errorIndicator;

    private final JScrollBar timelineScrollbar;

    private final JTable logTable;

    private final ChannelTracePanel firstChannelTracePanel;

    // -----------------------------------------------------------------------
    // Initialization
    // -----------------------------------------------------------------------

    @Inject
    public MainView(SampleBufferModel sampleBufferModel,
                    LogicAnalyzer logicAnalyzer,
                    InferoLog inferoLog,
                    MemoryUsageModel memoryUsageModel,
                    final ExceptionListModel exceptionListModel,
                    final ErrorFrame errorFrame,
                    LogicAnalyzerActions logicAnalyzerActions,
                    LogActions logActions,
                    ChannelTracePanelFactory channelTracePanelProviderFactory,
                    final WindowController windowController) {
        super("MainView.jfrm");
        this.sampleBufferModel = sampleBufferModel;
        this.logicAnalyzer = logicAnalyzer;
        this.inferoLogTableModel = new InferoLogTableModel(inferoLog);

        sampleCount = getComboBox(ID_SAMPLE_COUNT);
        sampleRate = getComboBox(ID_SAMPLE_RATE);

        zoom = getLabel(ID_ZOOM);

        decimal = getLabel(ID_VALUE_DECIMAL);
        hex = getLabel(ID_VALUE_HEX);
        octal = getLabel(ID_VALUE_OCTAL);
        binary = getLabel(ID_VALUE_BINARY);
        time = getLabel(ID_TIME);
        timespan = getLabel(ID_TIMESPAN);
        timePerPixel = getLabel(ID_TIME_PER_PIXEL);
        timePerSample = getLabel(ID_TIME_PER_SAMPLE);
        pixels = getLabel(ID_PIXELS);

        smileIcon = new ImageIcon(getClass().getResource("/share/icons/Tango/16x16/emotes/face-smile.png"));
        sadIcon = new ImageIcon(getClass().getResource("/share/icons/Tango/16x16/emotes/face-sad.png"));
        errorIndicator = (ImageComponent) getComponentByName(ID_ERROR_INDICATOR);

        timelineScrollbar = new JScrollBar(JScrollBar.HORIZONTAL);

        // There's something fishy with the table we're getting from JETA
        logTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(logTable);
        // For some reason the designer won't make a constant for this property
        getFormAccessor(ID_LOG_FORM).replaceBean(ID_LOG_TABLE_SCROLL_PANE, scrollPane);

        initializeHeader(logicAnalyzerActions.simulate);
        firstChannelTracePanel = initializeChannels(channelTracePanelProviderFactory);
        initializeSliders();
        initializeSampleBufferModel(sampleBufferModel);
        initializeLog(logActions.clearLogEntriesAction);
        initializeMemoryUsage(memoryUsageModel, getProgressBar(ID_MEMORY_USAGE));
        initializeErrorIndicator(windowController, errorFrame, exceptionListModel);
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

    private ChannelTracePanel initializeChannels(ChannelTracePanelFactory channelTracePanelProvider) {
        FormAccessor topForm = getFormAccessor();
        FormAccessor tracesForm = getFormAccessor(ID_TRACES);

        ChannelTracePanel first = null;

        for (Channel channel : logicAnalyzer.channels) {
            ChannelConfigurationView channelConfiguration = new ChannelConfigurationView(channel);

            // This is a safety measure against refactoring problems
            JLabel c = getLabel("channel." + channel.index + ".configuration");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".configuration'.");
            }
            topForm.replaceBean(c, channelConfiguration);

            ChannelTracePanel channelTrace = channelTracePanelProvider.create(channel);
            c = tracesForm.getLabel("channel." + channel.index + ".trace");
            if (c == null) {
                throw new RuntimeException("No such channel: 'channel." + channel.index + ".trace'.");
            }
            tracesForm.replaceBean("channel." + channel.index + ".trace", channelTrace);
            if (first == null) {
                first = channelTrace;
            }
        }

        if (first == null) {
            throw new RuntimeException("Unable to find any channels.");
        }

        first.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                sampleBufferModel.setViewWidth(e.getComponent().getWidth());
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });

        return first;
    }

    private void initializeSliders() {
        FormAccessor topForm = getFormAccessor();

        topForm.replaceBean(ID_TIME_SCROLLBAR, timelineScrollbar);

//        timelineScrollbar.addAdjustmentListener(new AdjustmentListener() {
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//            }
//        });
    }

    private void initializeSampleBufferModel(final SampleBufferModel sampleBufferModel) {
        onSampleInvalidated();

        sampleBufferModel.addPropertyChangeListener(SAMPLE_BUFFER, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                onSampleValuesUpdated();
            }
        });

        sampleBufferModel.addPropertyChangeListener(MOUSE_POSITION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (!MainView.this.sampleBufferModel.isValid()) {
                    return;
                }

                try {
                    updateStatusPanel(sampleBufferModel.getView());
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                }
            }
        });

        sampleBufferModel.addPropertyChangeListener(ZOOM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (!MainView.this.sampleBufferModel.isValid()) {
                    return;
                }

                updateStatusPanel(sampleBufferModel.getView());
            }
        });
    }

    private void initializeLog(GutsAction clearLogEntriesAction) {

        logTable.setModel(inferoLogTableModel);

        TableColumn timeColumn = logTable.getColumnModel().getColumn(0);
        timeColumn.setMaxWidth(200);
        timeColumn.setPreferredWidth(timeColumn.getMaxWidth());
        timeColumn.setResizable(false);

        logTable.setDefaultRenderer(Date.class, new LogEntryTimeStampTableCellRenderer());
        logTable.setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);

        getButton(ID_CLEAR_BUTTON).setAction(clearLogEntriesAction.action());

        inferoLogTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                scrollToLastLogEntry();
            }
        });
    }

    private void initializeMemoryUsage(final MemoryUsageModel memoryUsageModel, final JProgressBar progressBar) {
        progressBar.setEnabled(true);
        memoryUsageModel.addPropertyChangeListener(FREE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String toolTip = format("Used=%s, max=%s, total=%s, free=%s",
                        siFormattingOf(memoryUsageModel.getUsed()),
                        siFormattingOf(memoryUsageModel.getMax()),
                        siFormattingOf(memoryUsageModel.getTotal()),
                        siFormattingOf(memoryUsageModel.getFree()));

                int total = (int) memoryUsageModel.getTotal();
                progressBar.setMaximum(total);
                progressBar.setValue(total - (int) memoryUsageModel.getFree());
                progressBar.setToolTipText(toolTip);
            }
        });
    }

    private void initializeErrorIndicator(final WindowController windowController,
                                          final ErrorFrame errorFrame,
                                          final ExceptionListModel exceptionListModel) {
        errorIndicator.setEnabled(true);

        MouseAdapter adapter = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // TODO: Make sure the frame ends up on top of all the other frames
                if (errorFrame.isVisible()) {
                    errorFrame.setVisible(false);
                } else {
                    windowController.show(errorFrame, BoundsPolicy.PACK_AND_CENTER);
                }
            }
        };
        errorIndicator.addMouseListener(adapter);

        exceptionListModel.addPropertyChangeListener(EXCEPTION_COUNT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // TODO: Add an option in the application to show the error frame automatically

                errorIndicator.setIcon(exceptionListModel.getCount() > 0 ? sadIcon : smileIcon);
                errorIndicator.repaint();
            }
        });
    }

    private void updateStatusPanel(SampleView sampleView) {
        zoom.setText(String.valueOf(sampleView.zoom));

        // This has to be a bit smarter, has to show more precision
        time.setText(sampleView.getMouseTime().toString());
        timespan.setText(sampleView.sampleBuffer.timespan.toString());
        timePerPixel.setText(sampleView.timePerPixel.toString());
        timePerSample.setText(sampleView.timePerSample.toString());
        pixels.setText(String.valueOf(sampleView.viewWidth));

        int i = 0xff & sampleView.getMouseValue();
        String b = Integer.toBinaryString(i);
        b = "00000000".substring(0, 8 - b.length()) + b;

        decimal.setText(Integer.toString(i));
        hex.setText((i > 0x0f ? "0x" : "0x0") + Integer.toHexString(i));
        binary.setText(b + "b");
        octal.setText(Integer.toOctalString(i));
    }

    // -----------------------------------------------------------------------
    // Events Handlers
    // -----------------------------------------------------------------------

    private void onSampleValuesUpdated() {
        timelineScrollbar.setEnabled(true);
    }

    private void onSampleInvalidated() {
        zoom.setText("-");

        decimal.setText("-");
        hex.setText("-");
        octal.setText("-");
        binary.setText("-");
        binary.setText("-");

        timelineScrollbar.setEnabled(false);
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

    public int getTracePanelWidth() {
        return firstChannelTracePanel.getWidth();
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
