package infero.gui.widgets.error;

import com.google.inject.*;
import com.jeta.forms.components.panel.*;
import infero.gui.action.*;
import infero.gui.domain.*;
import infero.gui.domain.ExceptionListModel.*;
import static infero.gui.domain.ExceptionListModel.Properties.*;
import static infero.gui.widgets.error.ErrorView.Names.*;
import static java.lang.String.*;

import javax.swing.*;
import javax.swing.text.*;
import java.beans.*;
import java.text.*;

@Singleton
public class ErrorView extends FormPanel {
    protected static class Names {
        public static final String ID_EXCEPTION_TEXT = "exception.text";  //javax.swing.JTextArea
        public static final String ID_TIME_STAMP = "time.stamp";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_EXCEPTION_INDEX = "exception.index";  //com.jeta.forms.components.label.JETALabel
        public static final String ID_CLEAR_ALL = "clear.all";  //javax.swing.JButton
        public static final String ID_PREVIOUS = "previous";  //javax.swing.JButton
        public static final String ID_NEXT = "next";  //javax.swing.JButton

        // Custom - this was not copied from the name export - bug?
        public static final String ID_SCROLL_PANE = "scroll.pane";
    }

    private static final DateFormat dateFormat = DateFormat.getTimeInstance();

    final JTextArea exceptionText;
    final JLabel timeStamp;
    final JLabel exceptionIndex;

    @Inject
    public ErrorView(ErrorActions errorActions, final ExceptionListModel exceptionListModel) {
        super("ErrorView.jfrm");

        getButton(ID_NEXT).setAction(errorActions.showNextError.action());
        getButton(ID_PREVIOUS).setAction(errorActions.showPreviousError.action());
        getButton(ID_CLEAR_ALL).setAction(errorActions.clearErrorLog.action());

        exceptionText = (JTextArea) getTextComponent(ID_EXCEPTION_TEXT);
        timeStamp = getLabel(ID_TIME_STAMP);
        exceptionIndex = getLabel(ID_EXCEPTION_INDEX);

        updateStatus(exceptionListModel);

        exceptionListModel.addPropertyChangeListener(CURRENT_EXCEPTION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateStatus(exceptionListModel);
            }
        });
    }

    private void updateStatus(ExceptionListModel exceptionListModel) {
        if (exceptionListModel.getCurrentExceptionIndex() == -1) {
            timeStamp.setText("Log is empty");
            exceptionIndex.setText("");
            exceptionText.setText("");
        } else {
            ExceptionModel currentException = exceptionListModel.getCurrentException();
            timeStamp.setText(dateFormat.format(currentException.timeStamp));
            exceptionIndex.setText(format("%d of %d",
                    exceptionListModel.getCurrentExceptionIndex() + 1,
                    exceptionListModel.getCount()));

//            int updatePolicy = -1;
//            if (exceptionText.getCaret() instanceof DefaultCaret) {
//                DefaultCaret caret = (DefaultCaret) exceptionText.getCaret();
//                updatePolicy = caret.getUpdatePolicy();
//                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
//            }

            exceptionText.setText(currentException.text);
            exceptionText.setCaretPosition(0);

//            if (exceptionText.getCaret() instanceof DefaultCaret && updatePolicy != -1) {
//                DefaultCaret caret = (DefaultCaret) exceptionText.getCaret();
//                caret.setUpdatePolicy(updatePolicy);
//            }
        }
    }
}
