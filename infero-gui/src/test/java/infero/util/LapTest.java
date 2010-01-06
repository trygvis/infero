package infero.util;

import infero.util.Lap.*;
import static junit.framework.Assert.*;
import org.junit.*;

public class LapTest {

    @Test
    public void testCount() {
        assertEquals(2, Lap.startTimer().lap().done().round);
    }

    @Test
    public void testSingleRound() {
        Lap lap = Lap.startTimer();
        StoppedTimer stoppedTimer = lap.done();
        assertEquals(1, stoppedTimer.round);
        String[] strings = Lap.longFormatting(stoppedTimer);
//        for (String string : strings) {
//            System.out.println(string);
//        }
    }

    @Test
    public void testTwoRounds() {
        Lap lap = Lap.startTimer();
        lap = lap.lap();
        StoppedTimer stoppedTimer = lap.done();
        assertEquals(2, stoppedTimer.round);
        String[] strings = Lap.longFormatting(stoppedTimer);
//        for (String string : strings) {
//            System.out.println(string);
//        }
    }

    @Test
    public void testBasic() throws Exception {
        Lap lap = Lap.startTimer();
//        Thread.sleep(100);
        lap = lap.lap();
//        Thread.sleep(200);
        lap = lap.lap();
//        Thread.sleep(300);
        lap = lap.lap();
//        Thread.sleep(400);
        StoppedTimer stoppedTimer = lap.done();
        assertEquals(4, stoppedTimer.round);
        String[] strings = Lap.longFormatting(stoppedTimer);
//        for (String string : strings) {
//            System.out.println(string);
//        }
    }
}
