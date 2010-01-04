package infero.gui.domain;

import java.beans.*;

public class AbstractDomainObject<E extends Enum> {
    private final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(E e, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(e.name(), listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(E e, final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(e.name(), listener);
    }

    protected void firePropertyChange(E e, final Object oldValue, final Object newValue) {
        if (oldValue == null && newValue == null) {
            return;
        }
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            propertyChangeSupport.firePropertyChange(e.name(), oldValue, newValue);
        }
    }
}
