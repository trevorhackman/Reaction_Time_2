package hackman.trevor.tlibrary.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class TPreferences {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public TPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public double getDouble(String key, double defValue) {
        return Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToRawLongBits(defValue)));
    }

    public void putDouble(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }
}