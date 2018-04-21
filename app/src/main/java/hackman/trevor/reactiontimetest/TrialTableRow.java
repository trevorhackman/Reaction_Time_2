package hackman.trevor.reactiontimetest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.TableRow;

import hackman.trevor.tlibrary.library.TMath;

import static hackman.trevor.reactiontimetest.MainActivity.resources;
import static hackman.trevor.tlibrary.library.TLogging.report;

/**
 * Created by Trevor on 3/25/2018.
 *
 */

public class TrialTableRow extends TableRow {
    enum Type {Top("Top"), Regular("Regular"), Average("Average");
        String toString;

        Type(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString;
        }
    }
    enum ColorPalette {
        blue(resources.getColor(R.color.table_row), resources.getColor(R.color.table_row_stroke)),
        red(resources.getColor(R.color.table_row_red), resources.getColor(R.color.table_row_red)),
        orange(resources.getColor(R.color.table_row_orange), resources.getColor(R.color.table_row_orange)),
        green(resources.getColor(R.color.table_row_green), resources.getColor(R.color.table_row_green));

        int solidColor;
        int borderColor;

        ColorPalette(int solidColor, int borderColor) {
            this.solidColor = solidColor;
            this.borderColor = borderColor;
        }
    }
    Type type;

    public TrialTableRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    // Only intended to be used once onCreation
    void setType(Type type) {
        if (this.type == null) {
            this.type = type;
        }
        else {
            report(getClass() + " Type already set to " + type);
        }
    }

    void setBackground(ColorPalette colorPalette) {
        if (type != null) {
            GradientDrawable border = new GradientDrawable();
            GradientDrawable solid = new GradientDrawable();

            Drawable[] layers = {border, solid};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            int pixelInset = (int)TMath.convertDpToPixel(2, getContext());

            if (type == Type.Top) {
                float x = TMath.convertWpToPixel(12, getContext());
                float y = TMath.convertWpToPixel(12, getContext());

                // For each corner, the array contains 2 values, [X_radius, Y_radius]
                // The corners are ordered top-left, top-right, bottom-right, bottom-left.
                float[] cornerRadii = {x, y, x, y, 0, 0, 0, 0};
                solid.setCornerRadii(cornerRadii);
                border.setCornerRadii(cornerRadii);

                layerDrawable.setLayerInset(1, pixelInset, pixelInset * 3/4, pixelInset, 0);
            }
            else {
                layerDrawable.setLayerInset(1, pixelInset, 0, pixelInset, 0);
            }

            border.setColor(colorPalette.borderColor);
            solid.setColor(colorPalette.solidColor);

            setBackground(layerDrawable);
        }
        else {
            report("No tableRow type found");
            // Recovery of non-fatal error
            type = Type.Regular;
            setBackground(colorPalette);
        }
    }
}
