package infero.util;

import static infero.util.NumberFormats.*;
import static org.junit.Assert.*;
import org.junit.*;

public class FormattedNumberTest {
    @Test
    public void testSiInteger() {
        assertEquals("-10", siFormattingOf(-10).text);
        assertEquals("10", siFormattingOf(10).text);
        assertEquals("1M", siFormattingOf(1000000).text);
        assertEquals("999M", siFormattingOf(999000000).text);
    }

    @Test
    public void testSiDouble() {
        assertEquals("1m", siFormattingOf(0.001).text);
        assertEquals("100m", siFormattingOf(0.1).text);
        assertEquals("1", siFormattingOf(1d).text);
        assertEquals("10", siFormattingOf(10d).text);
        assertEquals("1M", siFormattingOf(1000000d).text);
        assertEquals("999M", siFormattingOf(999000000d).text);
    }

    @Test
    public void testNanoTimeInteger() {
        assertEquals("10ns", nanoTimeFormattingOf(10).text);
        assertEquals("1ms", nanoTimeFormattingOf(1000000).text);
        assertEquals("999ms", nanoTimeFormattingOf(999000000).text);
    }
}
