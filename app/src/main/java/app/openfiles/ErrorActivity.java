package app.openfiles;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Tells the user that this phone has no built-in file manager to open. */
public class ErrorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        float dp = getResources().getDisplayMetrics().density;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        int padding = Math.round(32 * dp);
        root.setPadding(padding, padding, padding, padding);

        TextView title = new TextView(this);
        title.setText(R.string.error_title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        title.setTypeface(null, Typeface.BOLD);
        title.setCompoundDrawablesRelativeWithIntrinsicBounds(
                android.R.drawable.ic_dialog_alert, 0, 0, 0);
        title.setCompoundDrawablePadding(Math.round(16 * dp));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.bottomMargin = Math.round(8 * dp);
        root.addView(title, titleParams);

        TextView message = new TextView(this);
        message.setText(R.string.error_message);
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        root.addView(message, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }
}
