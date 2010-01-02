package infero.gui.domain;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static infero.gui.domain.CleverInteger.cleverInteger;

/**
 */
@Singleton
public class LogicAnalyzer {
    public final List<Channel> channels;

    public final List<CleverInteger> sampleRates;

    public final List<CleverInteger> sampleCounts;

    public LogicAnalyzer() {
        this.channels = unmodifiableList(new ArrayList<Channel>(){{
            add(Channel.getBlankChannel(0));
            add(Channel.getBlankChannel(1));
            add(Channel.getBlankChannel(2));
            add(Channel.getBlankChannel(3));
            add(Channel.getBlankChannel(4));
            add(Channel.getBlankChannel(5));
            add(Channel.getBlankChannel(6));
            add(Channel.getBlankChannel(7));
        }});

        // TODO: This should be loaded after the GUI connects to the analyzer
        this.sampleRates = unmodifiableList(new ArrayList<CleverInteger>() {{
            add(cleverInteger(24000000));
            add(cleverInteger(16000000));
            add(cleverInteger(12000000));
            add(cleverInteger(8000000));
            add(cleverInteger(4000000));
            add(cleverInteger(2000000));
            add(cleverInteger(1000000));
            add(cleverInteger(500000));
            add(cleverInteger(250000));
            add(cleverInteger(200000));
            add(cleverInteger(100000));
            add(cleverInteger(50000));
        }});

        this.sampleCounts = unmodifiableList(new ArrayList<CleverInteger>() {{
            add(cleverInteger(1000));
            add(cleverInteger(1000000));
            add(cleverInteger(10000000));
            add(cleverInteger(25000000));
            add(cleverInteger(50000000));
            add(cleverInteger(100000000));
            add(cleverInteger(250000000));
            add(cleverInteger(500000000));
            add(cleverInteger(1000000000));
        }});
    }
}
