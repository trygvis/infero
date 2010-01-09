package infero.gui;

import com.google.inject.*;
import infero.gui.domain.*;
import static infero.gui.domain.InferoLogEntry.*;
import infero.gui.widgets.*;
import static javax.swing.UIManager.*;
import net.guts.gui.application.*;
import net.guts.gui.application.WindowController.*;
import net.guts.gui.exit.*;

import javax.swing.*;
import java.awt.event.*;

public class InferoLifecycleStarter implements AppLifecycleStarter {
    private final WindowController windowController;
    private final ExitController exitController;
    private final MainView mainView;
    private final MainMenuBar mainMenuBar;
    private final MemoryUsageModel memoryUsageModel;
    private final InferoLog inferoLog;

    @Inject
    public InferoLifecycleStarter(WindowController windowController,
                                  ExitController exitController,
                                  MainView mainView,
                                  MainMenuBar mainMenuBar,
                                  MemoryUsageModel memoryUsageModel,
                                  InferoLog inferoLog) {
        this.windowController = windowController;
        this.exitController = exitController;
        this.mainView = mainView;
        this.mainMenuBar = mainMenuBar;
        this.memoryUsageModel = memoryUsageModel;
        this.inferoLog = inferoLog;
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

        JFrame mainFrame = new JFrame();

        mainFrame.setName("mainFrame");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setJMenuBar(mainMenuBar);
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
        while (true) {
            memoryUsageModel.update(runtime.freeMemory(), runtime.maxMemory(), runtime.totalMemory());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void ready() {
        inferoLog.logEntry(info("The Infero is ready, bring it on!"));
    }
}
