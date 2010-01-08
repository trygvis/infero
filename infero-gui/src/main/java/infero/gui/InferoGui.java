package infero.gui;

import com.google.inject.*;
import net.guts.gui.application.*;

import java.util.*;

public class InferoGui extends AbstractAppLauncher {
    static public void main(String[] args) {
        new InferoGui().launch(args);
    }

    @Override
    protected void initModules(String[] args, List<Module> modules) {
        modules.add(new InferoGuiModule());
    }
}
