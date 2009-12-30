package infero.gui.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jeta.forms.components.panel.FormPanel;
import infero.gui.domain.CleverInteger;
import infero.gui.domain.CurrentSample;
import infero.gui.domain.LogicAnalyzer;
import infero.gui.widgets.util.CleverIntegerListCellRenderer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Singleton
public class MainViewHeader extends FormPanel {

    public static final String ID_SAMPLE_COUNT = "sample.count";  //javax.swing.JComboBox
    public static final String ID_SAMPLE_RATE = "sample.rate";  //javax.swing.JComboBox
    public static final String ID_SIMULATE_BUTTON = "simulate.button";  //javax.swing.JButton

    private final CurrentSample currentSample;
    private final JComboBox sampleCount;
    private final JComboBox sampleRate;

    @Inject
    public MainViewHeader(LogicAnalyzer logicAnalyzer, CurrentSample currentSample) {
        super("MainViewHeader.jfrm");
        this.currentSample = currentSample;

        sampleCount = getComboBox(ID_SAMPLE_COUNT);
        DefaultComboBoxModel sampleCountModel = (DefaultComboBoxModel) sampleCount.getModel();
        for (CleverInteger i : logicAnalyzer.sampleCounts) {
            sampleCountModel.addElement(i);
        }
        sampleCount.setRenderer(new CleverIntegerListCellRenderer(" samples"));

        sampleRate = getComboBox(ID_SAMPLE_RATE);
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

    private void onSimulateButtonClicked() {
        byte[] samples = new byte[((CleverInteger) sampleCount.getSelectedItem()).numeric];

        for (int i = 0, samplesLength = samples.length; i < samplesLength; i++) {
            samples[i] = (byte)(i & 0xff);
        }

        currentSample.setSample(samples);
    }
}
