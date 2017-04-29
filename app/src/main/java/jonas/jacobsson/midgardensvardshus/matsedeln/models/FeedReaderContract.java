package jonas.jacobsson.midgardensvardshus.matsedeln.models;

import android.provider.BaseColumns;

/**
 * Created by Jonas on 2017-04-29.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "menu";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_FOOD = "food";
    }


}

