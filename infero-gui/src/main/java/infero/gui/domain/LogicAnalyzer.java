package infero.gui.domain;

import com.google.inject.*;
import infero.util.*;
import static infero.util.NumberFormats.*;
import static java.util.Collections.*;

import java.util.*;

@Singleton
public class LogicAnalyzer {
    public final List<Channel> channels;

    public final List<FormattedInteger> sampleRates;

    public final List<FormattedInteger> sampleCounts;

    public LogicAnalyzer() {
        this.channels = unmodifiableList(new ArrayList<Channel>() {{
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
        this.sampleRates = unmodifiableList(new ArrayList<FormattedInteger>() {{
            add(siFormattingOf(24000000));
            add(siFormattingOf(16000000));
            add(siFormattingOf(12000000));
            add(siFormattingOf(8000000));
            add(siFormattingOf(4000000));
            add(siFormattingOf(2000000));
            add(siFormattingOf(1000000));
            add(siFormattingOf(500000));
            add(siFormattingOf(250000));
            add(siFormattingOf(200000));
            add(siFormattingOf(100000));
            add(siFormattingOf(50000));
        }});

        this.sampleCounts = unmodifiableList(new ArrayList<FormattedInteger>() {{
            add(siFormattingOf(1000));
            add(siFormattingOf(2000));
            add(siFormattingOf(1000000));
            add(siFormattingOf(10000000));
            add(siFormattingOf(25000000));
            add(siFormattingOf(50000000));
            add(siFormattingOf(100000000));
            add(siFormattingOf(250000000));
            add(siFormattingOf(500000000));
            add(siFormattingOf(1000000000));
        }});
    }
}
