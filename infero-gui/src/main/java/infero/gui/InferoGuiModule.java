package infero.gui;

import com.google.inject.*;
import static com.google.inject.assistedinject.FactoryProvider.*;
import infero.gui.widgets.*;
import net.guts.gui.application.*;
import net.guts.gui.resource.*;

public class InferoGuiModule implements Module {
    public void configure(Binder binder) {
        binder.bind(AppLifecycleStarter.class).
                to(InferoLifecycleStarter.class).
                asEagerSingleton();

        Resources.bindRootBundle(binder, getClass(), "resources");

        binder.bind(ChannelTracePanelFactory.class).
                toProvider(newFactory(ChannelTracePanelFactory.class, ChannelTracePanel.class));
    }
}
