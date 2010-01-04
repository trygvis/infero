package infero.gui;

import com.google.inject.*;
import infero.gui.action.*;
import infero.gui.domain.*;
import infero.gui.widgets.*;
import static javax.swing.UIManager.*;
import net.guts.gui.application.*;
import net.guts.gui.application.WindowController.*;
import net.guts.gui.exit.*;
import net.guts.gui.menu.*;

import javax.swing.*;
import java.awt.event.*;

public class InferoLifecycleStarter implements AppLifecycleStarter {
    private final WindowController windowController;
    private final MenuFactory menuFactory;
    private final ExitController exitController;
    private final GutsApplicationActions appActions;
    private final MainView mainView;
    private final MemoryUsageModel memoryUsageModel;
    private final LogicAnalyzerActions logicAnalyzerActions;

    @Inject
    public InferoLifecycleStarter(WindowController windowController,
                                  MenuFactory menuFactory,
                                  ExitController exitController,
                                  GutsApplicationActions appActions,
                                  MainView mainView,
                                  MemoryUsageModel memoryUsageModel,
                                  LogicAnalyzerActions logicAnalyzerActions) {
        this.windowController = windowController;
        this.menuFactory = menuFactory;
        this.exitController = exitController;
        this.appActions = appActions;
        this.mainView = mainView;
        this.memoryUsageModel = memoryUsageModel;
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

        Thread thread = new Thread(new Runnable() {
            public void run() {
                memorySupervisor();
            }
        }, "Memory Supervisor");
        thread.setDaemon(true);
        thread.start();

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

    private void memorySupervisor() {
        final Runtime runtime = Runtime.getRuntime();
        while(true) {
            memoryUsageModel.update(runtime.freeMemory(), runtime.maxMemory(), runtime.totalMemory());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void ready() {
    }
}
