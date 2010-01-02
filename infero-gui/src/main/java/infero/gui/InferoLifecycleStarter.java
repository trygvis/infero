package infero.gui;

import com.google.inject.*;
import infero.gui.action.*;
import infero.gui.widgets.*;
import static javax.swing.UIManager.*;
import net.guts.gui.application.*;
import net.guts.gui.application.WindowController.*;
import net.guts.gui.exit.*;
import net.guts.gui.menu.*;
import net.guts.gui.message.*;
import net.guts.gui.resource.*;

import javax.swing.*;
import java.awt.event.*;

public class InferoLifecycleStarter implements AppLifecycleStarter {
    private final WindowController windowController;
    private final MenuFactory menuFactory;
    private final MessageFactory messageFactory;
    private final ResourceInjector injector;
    private final ExitController exitController;
    private final GutsApplicationActions appActions;
    private final MainView mainView;
    private final LogicAnalyzerActions logicAnalyzerActions;

    @Inject
    public InferoLifecycleStarter(WindowController windowController,
                                  MenuFactory menuFactory,
                                  MessageFactory messageFactory,
                                  ResourceInjector injector,
                                  ExitController exitController,
                                  GutsApplicationActions appActions,
                                  MainView mainView,
                                  LogicAnalyzerActions logicAnalyzerActions) {
        this.windowController = windowController;
        this.menuFactory = menuFactory;
        this.messageFactory = messageFactory;
        this.injector = injector;
        this.exitController = exitController;
        this.appActions = appActions;
        this.mainView = mainView;
        this.logicAnalyzerActions = logicAnalyzerActions;
    }

    public void startup(String[] args) {
        System.out.println("InferoLifecycleStarter.startup");

        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFactory.createMenu("menu.file", 
                logicAnalyzerActions.simulate,
                MenuFactory.ACTION_SEPARATOR,
                appActions.quit()));

        JFrame mainFrame = new JFrame();

        mainFrame.setName("mainFrame");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                exitController.shutdown();
            }
        });
        mainFrame.setContentPane(mainView);
        windowController.show(mainFrame, BoundsPolicy.PACK_AND_CENTER);
    }

    public void ready() {
        System.out.println("InferoLifecycleStarter.ready");
    }
}
