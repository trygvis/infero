package infero.gui.widgets.error;

import com.google.inject.*;

import javax.swing.*;
import java.awt.*;

@Singleton
public class ErrorFrame extends JFrame {

    @Inject
    public ErrorFrame(ErrorView errorView) {
        super.setName("errorFrame");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setContentPane(errorView);
        pack();
//        Dimension dimension = new Dimension(1000, 600);
        Dimension dimension = new Dimension(200, 200);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }
}
