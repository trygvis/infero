package infero.gui.domain;

import static infero.util.NumberFormats.*;
import static org.junit.Assert.*;
import org.junit.*;

public class FormattedNumberTest {
    @Test
    public void testIsoInteger() {
        assertEquals("10", siFormattingOf(10).text);
        assertEquals("1M", siFormattingOf(1000000).text);
        assertEquals("999M", siFormattingOf(999000000).text);
    }

    @Test
    public void testNanoTimeInteger() {
        assertEquals("10ns", nanoTimeFormattingOf(10).text);
        assertEquals("1ms", nanoTimeFormattingOf(1000000).text);
        assertEquals("999ms", nanoTimeFormattingOf(999000000).text);
    }
}
