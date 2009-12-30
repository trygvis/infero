package infero.gui;

import com.google.inject.Inject;
import net.guts.gui.application.AppLifecycleStarter;
import net.guts.gui.application.GutsApplicationActions;
import net.guts.gui.application.WindowController;
import net.guts.gui.application.WindowController.BoundsPolicy;
import net.guts.gui.exit.ExitController;
import net.guts.gui.menu.MenuFactory;
import net.guts.gui.message.MessageFactory;
import net.guts.gui.resource.ResourceInjector;
import infero.gui.widgets.MainView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 */
public class InferoLifecycleStarter implements AppLifecycleStarter {
    private final WindowController windowController;
    private final MenuFactory menuFactory;
    private final MessageFactory messageFactory;
    private final ResourceInjector injector;
    private final ExitController exitController;
    private final GutsApplicationActions appActions;
    private final MainView mainView;

    @Inject
    public InferoLifecycleStarter(WindowController windowController,
                                  MenuFactory menuFactory,
                                  MessageFactory messageFactory,
                                  ResourceInjector injector,
                                  ExitController exitController,
                                  GutsApplicationActions appActions,
                                  MainView mainView) {
        this.windowController = windowController;
        this.menuFactory = menuFactory;
        this.messageFactory = messageFactory;
        this.injector = injector;
        this.exitController = exitController;
        this.appActions = appActions;
        this.mainView = mainView;
    }

    public void startup(String[] args) {
        System.out.println("InferoLifecycleStarter.startup");

        JFrame mainFrame = new JFrame();

        mainFrame.setName("mainFrame");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
