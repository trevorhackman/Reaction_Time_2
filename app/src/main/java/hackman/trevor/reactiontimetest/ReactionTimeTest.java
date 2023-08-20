package hackman.trevor.reactiontimetest;

import android.os.Handler;

import java.util.Random;

import static hackman.trevor.reactiontimetest.ReactionTimeTest.State.Finished;
import static hackman.trevor.reactiontimetest.ReactionTimeTest.State.To_Press;
import static hackman.trevor.reactiontimetest.ReactionTimeTest.State.To_Release;
import static hackman.trevor.tlibrary.library.TLogging.report;

import androidx.annotation.VisibleForTesting;

/**
 * Created by Trevor on 3/17/2018.
 * Logical unit
 */
public class ReactionTimeTest {
    // Internal state
    enum  State {
        To_Press,   // Blue
        Hold,       // Red
        To_Release, // Green
        Finished    // All trials finished OR released early
    }

    private final Random random;
    private State state;
    private final Handler handler;
    private final MainActivity main;

    private final int NUM_TRIALS = 5;
    private final long MIN_RANDOM = 1000; // In milliseconds
    private int trial; // The current trial. There are N trials starting from trial=0 to trial=N-1
    private long[] trial_times; // In milliseconds
    private long oldTime;

    public ReactionTimeTest(MainActivity mainActivity) {
        random = new Random();
        handler = new Handler();
        state = To_Press;
        trial = 0;
        trial_times = new long[NUM_TRIALS];
        main = mainActivity;
    }

    public State getState() { return state;}

    public int getTrial() { return trial;}

    public long getTrialTime(int which) { return trial_times[which];}

    public long getLastTrialTime() {
        try {
            return trial_times[trial - 1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            report("Wtf: getLastTrialTime");
            return trial_times[4];
        }
    }

    public long getAverage() {
        // Finished
        if (state == State.Finished) {
            long sum = 0;
            for (long time: trial_times) sum += time;
            return sum / trial_times.length;
        }
        // Haven't started
        if (trial == 0) {
            return 0; // default
        }
        // else In Progress
        long sum = 0;
        for (int i = 0; i < trial; i++) {
            sum += trial_times[i];
        }
        return Math.round(sum / (double)trial);
    }

    public synchronized void press() {
        if (state == To_Press) {
            state = State.Hold;
            Visual.Hold.affect(main);
            long wait = generateRandomTime();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    oldTime = System.currentTimeMillis();
                    state = To_Release;
                    Visual.ToRelease.affect(main);
                }
            };
            handler.postDelayed(r, wait);
        }
    }

    public synchronized void release() {
        // To make measurement ever so tiny tiny bit less delayed
        long score = System.currentTimeMillis() - oldTime;

        // Proper release
        if (state == To_Release) {
            putScore(score);
        }
        // Early release
        else if (state == State.Hold) {
            handler.removeCallbacksAndMessages(null); // Removes all scheduled tasks
            trial = 0;
            trial_times = new long[NUM_TRIALS];
            state = Finished;
            Visual.ReleasedEarly.affect(main);
        }
    }

    @VisibleForTesting
    void putScore(long score) {
        trial_times[trial++] = score;
        if (trial < NUM_TRIALS) {
            state = To_Press;
            Visual.Released.affect(main);
        }
        else if (trial == NUM_TRIALS) {
            state = State.Finished;
            Visual.Finished.affect(main);
        }
        else { throw new RuntimeException("Wtf: Invalid trial from To_Release");}
    }

    public synchronized void again() {
        if (state == State.Finished) {
            trial = 0;
            trial_times = new long[5];
            state = To_Press;
            Visual.Start.affect(main);
        }
    }

    // In milliseconds
    // Generates a time that is at least MIN_RANDOM
    // Time has no upper bound, but reasonable average and distribution
    long generateRandomTime() {
        int interval = 100;
        int increment = 0;
        while (random.nextDouble() < .925) increment++;

        long wait = MIN_RANDOM;
        wait += (long) increment * interval;
        wait += random.nextInt(interval + 1);
        return wait;
    }

    // External visual
    enum Visual {
        Start {
            @Override
            void affect(MainActivity main) {
                main.startEffect();
            }
        },
        Hold {
            @Override
            void affect(MainActivity main) {
                main.holdEffect();
            }
        },
        ReleasedEarly {
            @Override
            void affect(MainActivity main) {
                main.releasedEarlyEffect();
            }
        },
        ToRelease {
            @Override
            void affect(MainActivity main) {
                main.toReleaseEffect();
            }
        },
        Released {
            @Override
            void affect(MainActivity main) {
                main.releasedEffect();
            }
        },
        Finished {
            @Override
            void affect(MainActivity main) {
                main.finishedEffect();
            }
        };
        abstract void affect(MainActivity main);
    }
}
