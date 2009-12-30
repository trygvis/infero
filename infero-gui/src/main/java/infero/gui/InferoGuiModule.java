package infero.gui;

import com.google.inject.Binder;
import com.google.inject.Module;
import net.guts.gui.application.AppLifecycleStarter;

/**
 */
public class InferoGuiModule implements Module {
    public void configure(Binder binder) {
        binder.bind(AppLifecycleStarter.class).
                to(InferoLifecycleStarter.class).
                asEagerSingleton();
    }
}
