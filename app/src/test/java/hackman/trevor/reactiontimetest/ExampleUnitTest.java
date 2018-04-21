package hackman.trevor.reactiontimetest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test_randomization() throws Exception {
        ReactionTimeTest RT = new ReactionTimeTest(null);
        List<Long> list = new ArrayList<>();
        int trials = (int)Math.pow(10, 6);
        for (int i = 0; i < trials; i++) {
            list.add(RT.generateRandomTime());
        }

        long minimum = list.get(0);
        long maximum = list.get(0);
        long average = 0;
        for (Long L: list) {
            average += L;
            if (L < minimum) minimum = L;
            if (L > maximum) maximum = L;
        }
        average /= list.size();

        print("Min: " + minimum);
        print("Max: " + maximum);
        print("Avg: " + average);
    }

    private void print(String string) {
        System.out.println(string);
    }
}