package app.openfiles;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

/**
 * Opens the file manager that is built into Android (called "DocumentsUI") and
 * immediately closes itself, so this app is only a shortcut.
 *
 * It tries a list of ways to reach the file manager, from the most specific to
 * the most generic, and uses the first one that works. If none works it shows
 * ErrorActivity.
 */
public class MainActivity extends Activity {

    /** Start folder: the phone's main storage. */
    private static final String PRIMARY_STORAGE =
            "content://com.android.externalstorage.documents/root/primary";

    private static final String ACTION_BROWSE = "android.provider.action.BROWSE";
    private static final String ACTION_BROWSE_DOCUMENT_ROOT =
            "android.provider.action.BROWSE_DOCUMENT_ROOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri root = Uri.parse(PRIMARY_STORAGE);

        boolean opened =
                // Google's build of the file manager (most phones)
                launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.google.android.documentsui",
                                "com.android.documentsui.files.FilesActivity"))
                // Plain Android build
                || launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.android.documentsui",
                                "com.android.documentsui.files.FilesActivity"))
                // Older Android versions
                || launch(Intent.ACTION_VIEW, root,
                        new ComponentName("com.android.documentsui",
                                "com.android.documentsui.FilesActivity"))
                // Let Android pick any app that can show this folder
                || launch(Intent.ACTION_VIEW, root, null)
                || launch(ACTION_BROWSE, root, null)
                || launch(ACTION_BROWSE_DOCUMENT_ROOT, root, null);

        if (!opened) {
            startActivity(new Intent(this, ErrorActivity.class));
        }

        finish();
        removeTransitionAnimation();
    }

    private boolean launch(String action, Uri data, ComponentName component) {
        try {
            Intent intent = new Intent(action, data);
            if (component != null) {
                intent.setComponent(component);
            }
            removeTransitionAnimation();
            startActivity(intent);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
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
