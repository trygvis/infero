package infero.gui.domain;

import com.google.inject.*;
import infero.gui.domain.MemoryUsageModel.*;
import static infero.gui.domain.MemoryUsageModel.Properties.*;

@Singleton
public class MemoryUsageModel extends AbstractDomainObject<Properties> {
    public enum Properties {
        FREE,
    }

    private long free;
    private long max;
    private long total;

    public long getFree() {
        return free;
    }

    public long getMax() {
        return max;
    }

    public long getTotal() {
        return total;
    }

    public void update(long free, long max, long total) {
        this.max = max;
        this.total = total;
        firePropertyChange(FREE, this.free, this.free = free);
    }
}
