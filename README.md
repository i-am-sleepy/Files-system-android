# OpenFiles

A tiny open-source shortcut that opens your phone's **built-in file manager**
(Android's "DocumentsUI"), with an extra icon that tries to open it in a
**small floating window**.

## What you get

| Icon | What it does |
|------|--------------|
| **Files** (blue) | Opens the built-in file manager, full screen. |
| **Files (small)** (orange) | Asks Android to open the file manager in a small floating window. |

## How it works

Neither icon contains a file manager. Each one is a launcher that closes
itself right away, after asking Android to open the file manager that is
already on the phone. It tries these, in order, and uses the first that works:

1. `com.google.android.documentsui` / `com.android.documentsui.files.FilesActivity`
2. `com.android.documentsui` / `com.android.documentsui.files.FilesActivity`
3. `com.android.documentsui` / `com.android.documentsui.FilesActivity` (older Android)
4. any app that handles `VIEW` on the main-storage folder
5. `android.provider.action.BROWSE`
6. `android.provider.action.BROWSE_DOCUMENT_ROOT`

If none exist, an explanatory screen is shown.

### The small-window part

The small-window icon adds two requests when launching:

* a window rectangle (`ActivityOptions.setLaunchBounds`, an official API), and
* floating-window mode (`setLaunchWindowingMode(5)`, a hidden API reached via
  reflection).

Android does not officially offer apps a "pop-up window" button, so whether
this works depends on the phone and its software version. If it opens full
screen instead, nothing is broken: you still get the file manager. On Samsung
phones you can also use Recents -> tap the app icon -> **Small window**.

To change the window size, edit the three `SMALL_WINDOW_*` numbers at the top
of `app/src/main/java/app/openfiles/MainActivity.java`.

## Building (no computer needed)

The APK is built automatically by GitHub Actions
(`.github/workflows/build.yml`). After each build, open the repository's
**Releases** page and download `OpenFiles.apk`.

## Note on origin

This project re-implements the *behaviour* of a similar shortcut app (which
tries the same system file-manager entry points). No code, icons or text were
copied from it; it is not affiliated with that app.

## License

MIT, see `LICENSE`. The included `openfiles.jks` is a throw-away signing key
for personal builds; create your own if you publish the app anywhere.
