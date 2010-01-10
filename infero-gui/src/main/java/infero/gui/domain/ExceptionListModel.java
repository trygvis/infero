package infero.gui.domain;

import com.google.inject.*;
import static infero.gui.domain.ExceptionListModel.Properties.*;

import java.io.*;
import java.util.*;

@Singleton
public class ExceptionListModel extends AbstractDomainObject<infero.gui.domain.ExceptionListModel.Properties> {

    private int currentException = -1;
    private final List<ExceptionModel> exceptions = new ArrayList<ExceptionModel>();

    public enum Properties {
        CURRENT_EXCEPTION,
        EXCEPTION_COUNT,
    }

    public void addException(Throwable throwable) {
        int oldSize = exceptions.size();
        exceptions.add(new ExceptionModel(throwable));
        int oldCurrentException = currentException;
        currentException = exceptions.size() - 1;

        firePropertyChange(EXCEPTION_COUNT, oldSize, oldSize + 1);
        firePropertyChange(CURRENT_EXCEPTION, oldCurrentException, currentException);
    }

    public void clearAll() {
        int oldSize = exceptions.size();
        exceptions.clear();
        int oldCurrentException = currentException;
        currentException = -1;

        firePropertyChange(EXCEPTION_COUNT, oldSize, 0);
        firePropertyChange(CURRENT_EXCEPTION, oldCurrentException, currentException);
    }

    public boolean canMoveToNextException() {
        return currentException < exceptions.size() - 1;
    }

    public void moveToNextException() {
        firePropertyChange(CURRENT_EXCEPTION, currentException, ++this.currentException);
    }

    public boolean canMoveToPreviousException() {
        return currentException > 0;
    }

    public void moveToPreviousException() {
        firePropertyChange(CURRENT_EXCEPTION, currentException, --this.currentException);
    }

    public int getCount() {
        return exceptions.size();
    }

    public ExceptionModel getCurrentException() {
        return exceptions.get(currentException);
    }

    public int getCurrentExceptionIndex() {
        return currentException;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public static class ExceptionModel {
        public final Date timeStamp;
        public final String text;

        public ExceptionModel(Throwable throwable) {
            timeStamp = new Date();
            CharArrayWriter charArrayWriter = new CharArrayWriter(8 * 1024);
            throwable.printStackTrace(new PrintWriter(charArrayWriter));
            text = charArrayWriter.toString();
        }
    }
}
