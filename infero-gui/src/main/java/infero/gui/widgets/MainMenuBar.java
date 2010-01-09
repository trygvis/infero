package infero.gui.widgets;

import com.google.inject.*;
import infero.gui.action.*;
import net.guts.gui.application.*;
import net.guts.gui.menu.*;

import javax.swing.*;

@Singleton
public class MainMenuBar extends JMenuBar {
    @Inject
    public MainMenuBar(MenuFactory menuFactory,
                       GutsApplicationActions appActions,
                       LogicAnalyzerActions logicAnalyzerActions) {
        add(menuFactory.createMenu("menu.file",
                logicAnalyzerActions.simulate,
                MenuFactory.ACTION_SEPARATOR,
                appActions.quit()));
        add(menuFactory.createMenu("menu.view",
                logicAnalyzerActions.zoomIn,
                logicAnalyzerActions.zoomOut));
    }
}
