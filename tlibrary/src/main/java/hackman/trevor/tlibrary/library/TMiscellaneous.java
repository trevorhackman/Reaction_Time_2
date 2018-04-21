package hackman.trevor.tlibrary.library;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;

// Class for various tidbits, if enough tidbits of any common type accumulate I will agglomerate them into a new library class
public final class TMiscellaneous {
    private TMiscellaneous() {} // Private constructor to stop instances of this class, everything is static so instances are pointless

    // To use call startActivity(moreGamesIntent()); from an activity
    // This will need to be accordingly adjusted if I ever change my developer name
    public static Intent moreGamesIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:Hackman"));
        return intent;
    }

    // To use call startActivity(rateGameIntent(getPackageName())); from an activity
    public static Intent rateGameIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName)); // package name is hackman.trevor.copycat
        return intent;
    }

    // To call from activity (onResume and onWindowFocusChanged), getWindow() for parameter, making activity fullscreen
    // OUTDATED - Add line "<item name="android:windowFullscreen">true</item>' to @style/AppTheme for easier, better solution
    // Still useful if I want to add a setting for fullscreen vs not
    public static void goFullScreen(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // Create a ripple drawable for buttons - adds ripple effect onPress and onClick to a normal drawable
    // Ripple drawables can wrap around other kinds of drawables
    // @param background is not altered and is the appearance of the resulting RippleDrawable
    // @param pressedColor is the color of the ripple effect
    public static RippleDrawable createRippleDrawable(Drawable background, int pressedColor) {
        ColorStateList csl = new ColorStateList(
                new int[][]
                        {
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor
                        }
        );
        return new RippleDrawable(csl, background, null);
    }
}
