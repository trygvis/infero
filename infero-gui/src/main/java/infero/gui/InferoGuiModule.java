package infero.gui;

import com.google.inject.Binder;
import com.google.inject.Module;
import net.guts.gui.application.AppLifecycleStarter;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class InferoGuiModule implements Module {
    public void configure(Binder binder) {
        binder.bind(AppLifecycleStarter.class).
                to(InferoLifecycleStarter.class).
                asEagerSingleton();
    }
}
