package infero.gui;

import com.google.inject.Module;
import net.guts.gui.application.AbstractAppLauncher;

import java.util.List;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class InferoGui extends AbstractAppLauncher {
    static public void main(String[] args) {
        new InferoGui().launch(args);
    }

    @Override
    protected void initModules(String[] args, List<Module> modules) {
        modules.add(new InferoGuiModule());
    }
}