package infero.gui;

import com.google.inject.*;
import net.guts.gui.application.*;
import net.guts.gui.resource.*;

public class InferoGuiModule implements Module {
    public void configure(Binder binder) {
        binder.bind(AppLifecycleStarter.class).
                to(InferoLifecycleStarter.class).
                asEagerSingleton();

        Resources.bindRootBundle(binder, getClass(), "resources");
    }
}
