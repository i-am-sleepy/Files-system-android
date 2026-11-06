package app.openfiles;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Opens the file manager that is built into Android (called "DocumentsUI") and
 * immediately closes itself, so this app is only a shortcut.
 *
 * It tries a list of ways to reach the file manager, from the most specific to
 * the most generic, and uses the first one that works. If none works it shows
 * ErrorActivity.
 *
 * {@link SmallWindowActivity} reuses all of this but asks Android to open the
 * file manager in a small floating window instead of full screen.
 */
public class MainActivity extends Activity {

    /** Start folder: the phone's main storage. */
    private static final String PRIMARY_STORAGE =
            "content://com.android.externalstorage.documents/root/primary";

    private static final String ACTION_BROWSE = "android.provider.action.BROWSE";
    private static final String ACTION_BROWSE_DOCUMENT_ROOT =
            "android.provider.action.BROWSE_DOCUMENT_ROOT";

    /** Android's internal number for "floating window" (WINDOWING_MODE_FREEFORM). */
    private static final int WINDOWING_MODE_FREEFORM = 5;

    // Size and position of the small window, as a fraction of the screen.
    // Change these numbers if you want a bigger or smaller window.
    private static final float SMALL_WINDOW_WIDTH = 0.90f;
    private static final float SMALL_WINDOW_HEIGHT = 0.55f;
    private static final float SMALL_WINDOW_TOP = 0.10f;

    /** Subclasses return true to open the file manager in a small window. */
    protected boolean wantSmallWindow() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri root = Uri.parse(PRIMARY_STORAGE);
        boolean small = wantSmallWindow();

        boolean opened =
                // Google's build of the file manager (most phones)
                launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.google.android.documentsui",
                                "com.android.documentsui.files.FilesActivity"), small)
                // Plain Android build
                || launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.android.documentsui",
                                "com.android.documentsui.files.FilesActivity"), small)
                // Older Android versions
                || launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.android.documentsui",
                                "com.android.documentsui.FilesActivity"), small)
                // Let Android pick any app that can show this folder
                || launch(Intent.ACTION_VIEW, root, null, small)
                || launch(ACTION_BROWSE, root, null, small)
                || launch(ACTION_BROWSE_DOCUMENT_ROOT, root, null, small);

        if (!opened) {
            startActivity(new Intent(this, ErrorActivity.class));
        }

        finish();
        removeTransitionAnimation();
    }

    private boolean launch(String action, Uri data, ComponentName component, boolean small) {
        try {
            Intent intent = new Intent(action, data);
            if (component != null) {
                intent.setComponent(component);
            }

            removeTransitionAnimation();

            if (small) {
                // A new, separate window is needed so it can float above other apps.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(intent, createSmallWindowOptions().toBundle());
            } else {
                startActivity(intent);
            }
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    /**
     * Builds the "please open as a small floating window" request.
     *
     * Two things are asked for:
     *  1. a window rectangle (official Android API)
     *  2. floating-window mode (hidden Android API, reached by reflection; not
     *     every phone allows it, so failure is expected and handled).
     */
    private ActivityOptions createSmallWindowOptions() {
        ActivityOptions options = ActivityOptions.makeBasic();

        DisplayMetrics screen = getResources().getDisplayMetrics();
        int width = Math.round(screen.widthPixels * SMALL_WINDOW_WIDTH);
        int height = Math.round(screen.heightPixels * SMALL_WINDOW_HEIGHT);
        int left = (screen.widthPixels - width) / 2;
        int top = Math.round(screen.heightPixels * SMALL_WINDOW_TOP);
        options.setLaunchBounds(new Rect(left, top, left + width, top + height));

        if (!requestFloatingMode(options)) {
            Toast.makeText(getApplicationContext(), R.string.small_window_unavailable,
                    Toast.LENGTH_LONG).show();
        }
        return options;
    }

    private static boolean requestFloatingMode(ActivityOptions options) {
        try {
            Method setMode = ActivityOptions.class.getMethod("setLaunchWindowingMode", int.class);
            setMode.invoke(options, WINDOWING_MODE_FREEFORM);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /** No slide animation, so this shortcut is invisible. */
    private void removeTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0);
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0);
        }
        overridePendingTransition(0, 0);
    }
}
