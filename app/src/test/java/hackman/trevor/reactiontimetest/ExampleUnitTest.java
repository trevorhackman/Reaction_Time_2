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
        int trials = (int)Math.pow(10, 5);
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

    @Test
    public void test_average() throws Exception {
        ReactionTimeTest RT = new ReactionTimeTest(null);
        print(RT.getAverage());

        /*int trials = (int)Math.pow(10, 6);
        for (int j = 0; j < trials; j++) {
            long[] times = new long[5];

            for (int i = 0; i < times.length; i++) {
                if (i % 2 == 0)
                    times[i] = (long) (Math.random()*Math.random() * 10000);
                if (i % 2 == 1)
                    times[i] = (long) (Math.random() * 30);
                RT.putScore(times[i]);
            }

            long average = 0;
            for (long time : times) {
                average += time;
            }
            average /= 5;

            assertTrue(average == RT.getAverage());
            RT.again();
        }*/

        RT.putScore(5000);
        RT.putScore(5000);
        RT.putScore(5001);
        print(RT.getAverage());
    }

    private void print(String string) {
        System.out.println(string);
    }

    private <G> void print(G generic) {
        System.out.println(generic.toString());
    }
}