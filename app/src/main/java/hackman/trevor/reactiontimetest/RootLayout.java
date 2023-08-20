package hackman.trevor.reactiontimetest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Trevor on 3/21/2018.
 */
public class RootLayout extends RelativeLayout implements View.OnTouchListener {
    MainActivity main;

    public RootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        main = (MainActivity) context;
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            main.game.press();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            main.game.release();
        }
        return false;
    }
}
