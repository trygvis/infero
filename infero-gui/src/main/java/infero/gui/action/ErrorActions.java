package infero.gui.action;

import com.google.inject.*;
import infero.gui.domain.*;
import net.guts.gui.action.*;

import java.beans.*;

@Singleton
public class ErrorActions {
    public final GutsAction showNextError;
    public final GutsAction showPreviousError;
    public final GutsAction clearErrorLog;

    @Inject
    public ErrorActions(final ExceptionListModel exceptionListModel) {
        showNextError = new GutsAction("action.showNextError") {
            {
                action().setEnabled(false);
                exceptionListModel.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        action().setEnabled(exceptionListModel.canMoveToNextException());
                    }
                });
            }

            protected void perform() {
                exceptionListModel.moveToNextException();
            }
        };

        showPreviousError = new GutsAction("action.showPreviousError") {
            {
                action().setEnabled(false);
                exceptionListModel.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        action().setEnabled(exceptionListModel.canMoveToPreviousException());
                    }
                });
            }

            protected void perform() {
                exceptionListModel.moveToPreviousException();
            }
        };

        clearErrorLog = new GutsAction("action.clearErrorLog") {
            protected void perform() {
                exceptionListModel.clearAll();
            }
        };
    }
}
