package app.openfiles;

/** Second launcher icon: same as {@link MainActivity}, but asks for a small window. */
public class SmallWindowActivity extends MainActivity {
    @Override
    protected boolean wantSmallWindow() {
        return true;
    }
}
