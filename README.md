# OpenFiles

A tiny open-source shortcut that opens your phone's **built-in file manager**
(Android's "DocumentsUI", the plain Files app that many phones hide).

The app has one icon, **Files** (red). It contains no file manager of its own:
it asks Android to open the one already on the phone, then closes itself.

## How it works

It tries these, in order, and uses the first that works:

1. `com.google.android.documentsui` / `com.android.documentsui.files.FilesActivity`
2. `com.android.documentsui` / `com.android.documentsui.files.FilesActivity`
3. `com.android.documentsui` / `com.android.documentsui.FilesActivity` (older Android)
4. any app that handles `VIEW` on the main-storage folder
5. `android.provider.action.BROWSE`
6. `android.provider.action.BROWSE_DOCUMENT_ROOT`

If none exist, an explanatory screen is shown.

## About "Small window" (vivo, Samsung and others)

Phone makers decide per app whether their "Small window" / pop-up option is
offered in the Recents menu. The window you see is the phone's own file manager
(it opens in its own task), not this shortcut, so nothing in this app's
manifest can change that option. This app declares `resizeableActivity="true"`
like the app it re-implements, which is all a shortcut can do.

## Building (no computer needed)

The APK is built by GitHub Actions (`.github/workflows/build.yml`). After each
build, open the repository's **Releases** page and download `OpenFiles.apk`.

## Note on origin

This project re-implements the *behaviour* of a similar shortcut app. No code,
icons or text were copied from it; it is not affiliated with that app.

## License

MIT, see `LICENSE`. The included `openfiles.jks` is a throw-away signing key for
personal builds; create your own if you publish the app anywhere.
