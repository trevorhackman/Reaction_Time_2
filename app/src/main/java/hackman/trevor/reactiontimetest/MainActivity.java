package hackman.trevor.reactiontimetest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import hackman.trevor.tlibrary.library.TLogging;
import hackman.trevor.tlibrary.library.TMath;
import hackman.trevor.tlibrary.library.TMiscellaneous;

import static hackman.trevor.tlibrary.library.TLogging.flog;
import static hackman.trevor.tlibrary.library.TLogging.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rootLayout;
    private LinearLayout topLayout;
    private Button tryAgainButton;
    private Space tryAgainButtonSpace;

    private Window window;
    private int startStatusBarColor;
    private int holdStatusBarColor;
    private int releasedEarlyStatusBarColor;
    private int releaseStatusBarColor;

    private TextView headText;
    private TextView explanatoryText;
    private TextView trial1;
    private TextView trial2;
    private TextView trial3;
    private TextView trial4;
    private TextView trial5;
    private TextView[] trials;
    private TextView averageText;

    private TrialTableRow tableRow0; // Top row
    private TrialTableRow tableRow1;
    private TrialTableRow tableRow2;
    private TrialTableRow tableRow3;
    private TrialTableRow tableRow4;
    private TrialTableRow tableRowAverage;

    static Application application;
    ReactionTimeTest game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crashlytics disabled be default, automatically enable it here if not testing
        if (!TLogging.TESTING) FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        else TLogging.disableCrashlytics(); // Necessary else crashlytics crashes from not being initialized with fabric

        flog("OnCreate()");
        setContentView(R.layout.activity_main);

        game = new ReactionTimeTest(this);
        application = getApplication();

        rootLayout = findViewById(R.id.rootLayout);
        topLayout = findViewById(R.id.topLayout);
        headText = findViewById(R.id.headText);
        explanatoryText = findViewById(R.id.explanatoryText);

        trial1 = findViewById(R.id.text_time1);
        trial2 = findViewById(R.id.text_time2);
        trial3 = findViewById(R.id.text_time3);
        trial4 = findViewById(R.id.text_time4);
        trial5 = findViewById(R.id.text_time5);
        trials = new TextView[5];
        trials[0] = trial1;
        trials[1] = trial2;
        trials[2] = trial3;
        trials[3] = trial4;
        trials[4] = trial5;
        averageText = findViewById(R.id.text_timeAverage);

        window = getWindow();
        startStatusBarColor = getColor(R.color.colorPrimaryDark);
        holdStatusBarColor = getColor(R.color.status_bar_hold);
        releasedEarlyStatusBarColor = getColor(R.color.status_bar_released_early);
        releaseStatusBarColor = getColor(R.color.status_bar_release);

        tableRow0 = findViewById(R.id.tableRow0);
        tableRow1 = findViewById(R.id.tableRow1);
        tableRow2 = findViewById(R.id.tableRow2);
        tableRow3 = findViewById(R.id.tableRow3);
        tableRow4 = findViewById(R.id.tableRow4);
        tableRowAverage = findViewById(R.id.tableRowAverage);

        tableRow0.setType(TrialTableRow.Type.Top);
        tableRow1.setType(TrialTableRow.Type.Regular);
        tableRow2.setType(TrialTableRow.Type.Regular);
        tableRow3.setType(TrialTableRow.Type.Regular);
        tableRow4.setType(TrialTableRow.Type.Regular);
        tableRowAverage.setType(TrialTableRow.Type.Average);

        tryAgainButton = new TryAgainButton(this);

        startEffect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!isFinishing()) {
            finish();
        }
    }

    void startEffect() {
        rootLayout.setBackgroundColor(getColor(R.color.background));
        headText.setText(R.string.head_start);
        explanatoryText.setText(R.string.explanatory_start);
        window.setStatusBarColor(startStatusBarColor);

        setAllRows(TrialTableRow.ColorPalette.blue);
        for (TextView trialTime: trials) {
            trialTime.setText(R.string.empty_time);
        }
        averageText.setText(R.string.empty_time);
    }

    void holdEffect() {
        rootLayout.setBackgroundColor(getColor(R.color.background_hold));
        headText.setText(R.string.head_hold);
        explanatoryText.setText(R.string.explanatory_hold);
        window.setStatusBarColor(holdStatusBarColor);
        setAllRows(TrialTableRow.ColorPalette.red);
    }

    void releasedEarlyEffect() {
        rootLayout.setBackgroundColor(getColor(R.color.background_released_early));
        headText.setText(R.string.head_released_early);
        explanatoryText.setText(R.string.explanatory_released_early);
        window.setStatusBarColor(releasedEarlyStatusBarColor);

        setAllRows(TrialTableRow.ColorPalette.orange);
        for (TextView trialTime: trials) {
            trialTime.setText(R.string.empty_time);
        }
        averageText.setText(R.string.empty_time);
        addTryAgainButton();
    }

    void toReleaseEffect() {
        rootLayout.setBackgroundColor(getColor(R.color.background_release));
        headText.setText(R.string.head_to_release);
        explanatoryText.setText(R.string.explanatory_to_release);
        window.setStatusBarColor(releaseStatusBarColor);
        setAllRows(TrialTableRow.ColorPalette.green);

        System.out.println(System.currentTimeMillis());
    }

    void releasedEffect() {
        rootLayout.setBackgroundColor(getColor(R.color.background));
        headText.setText(R.string.head_released);
        window.setStatusBarColor(startStatusBarColor);
        setAllRows(TrialTableRow.ColorPalette.blue);

        String expText = getString(R.string.explanatory_released1) + " " + game.getLastTrialTime() + " " + getString(R.string.explanatory_released2);
        explanatoryText.setText(expText);

        updateLastTrial();
    }

    void finishedEffect() {
        int backgroundColor = getColor(R.color.background);
        rootLayout.setBackgroundColor(backgroundColor);
        String hText = getString(R.string.head_finished);
        headText.setText(hText);

        String staticString = getString(R.string.explanatory_finished1);
        int INT_START = staticString.length() + 1;

        String dynamicString = game.getAverage() + " " + getString(R.string.explanatory_finished2);

        String fullString = staticString + "\n" + dynamicString;
        int INT_END = fullString.length();

        SpannableStringBuilder partStylizedString = new SpannableStringBuilder(fullString);
        partStylizedString.setSpan(new RelativeSizeSpan(1.2f), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        explanatoryText.setText(partStylizedString);

        window.setStatusBarColor(startStatusBarColor);
        setAllRows(TrialTableRow.ColorPalette.blue);

        updateLastTrial();
        addTryAgainButton();
    }

    private void addTryAgainButton() {
        // Safe against adding the button multiple times
        if (topLayout.indexOfChild(tryAgainButton) == -1 && topLayout.indexOfChild(tryAgainButtonSpace) == -1) {
            topLayout.addView(tryAgainButton);
            topLayout.addView(tryAgainButtonSpace);
        } else {
            report("tryAgainButton already part of layout");
        }
    }

    private void updateLastTrial() {
        String time = "" + game.getLastTrialTime() + "ms";
        try {
            trials[game.getTrial() - 1].setText(time);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            report("Wtf: updateLastTrial");
            trials[4].setText(time);
        }
        updateAverage();
    }

    private void updateAverage() {
        String average = "" + game.getAverage() + "ms";
        averageText.setText(average);
    }

    private void setAllRows(TrialTableRow.ColorPalette colorPalette) {
        tableRow0.setBackground(colorPalette);
        tableRow1.setBackground(colorPalette);
        tableRow2.setBackground(colorPalette);
        tableRow3.setBackground(colorPalette);
        tableRow4.setBackground(colorPalette);
        tableRowAverage.setBackground(colorPalette);
    }

    class TryAgainButton extends AppCompatButton {
        private final ObjectAnimator fadeOutAnimator;
        private boolean enabled = true;

        public TryAgainButton(Context context) {
            super(context);
            Resources resources = context.getResources();

            // Text
            this.setText(R.string.try_again);
            this.setTextAppearance(context, R.style.Text);
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, TMath.convertDpToPixel(30, context));

            // Background
            int backgroundColor = getColor(R.color.try_again_button);
            int pressedColor = getColor(R.color.try_again_button_ripple);
            int strokeColor = getColor(R.color.try_again_button_stroke);

            GradientDrawable background = new GradientDrawable();
            background.setColor(backgroundColor);
            background.setStroke((int)TMath.convertWpToPixel(2, context), strokeColor);
            background.setCornerRadius((int)TMath.convertWpToPixel(3, context));

            RippleDrawable rd = TMiscellaneous.createRippleDrawable(background, pressedColor);
            this.setBackground(rd);

            // Layout parameters
            int margin = (int)TMath.convertWpToPixel(7, context);
            LinearLayout.LayoutParams buttonLP = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLP.setMargins(margin, 0, margin, 0);
            this.setLayoutParams(buttonLP);

            // OnClick
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enabled) {
                        enabled = false;
                        removeButton();
                    }
                }
            });

            // Spacing in layout
            tryAgainButtonSpace = new Space(context);
            LinearLayout.LayoutParams spaceLP = new LinearLayout.LayoutParams(0,0);
            spaceLP.weight = .125f;
            tryAgainButtonSpace.setLayoutParams(spaceLP);

            // Create fade out animation
            fadeOutAnimator = ObjectAnimator.ofFloat(this, "alpha", 0);
            fadeOutAnimator.setDuration(450);

            fadeOutAnimator.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationRepeat(Animator animation) {}
                @Override public void onAnimationCancel(Animator animation) {}
                @Override public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    game.again();
                    topLayout.removeView(tryAgainButton);
                    topLayout.removeView(tryAgainButtonSpace);
                    startEffect();
                    tryAgainButton.setAlpha(1.0f);
                    enabled = true;
                }
            });
        }

        public void removeButton() {
            fadeOutAnimator.start();
        }
    }
}
