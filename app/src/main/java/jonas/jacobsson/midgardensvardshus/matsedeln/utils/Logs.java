package jonas.jacobsson.midgardensvardshus.matsedeln.utils;

import android.util.Log;

/**
 * Created by Jonas on 2017-04-29.
 */

class Logs {

    private static final boolean DEBUG = true;

    public static void i(String where, String what) {
        if (DEBUG)
            Log.i(where, what);
    }
}
