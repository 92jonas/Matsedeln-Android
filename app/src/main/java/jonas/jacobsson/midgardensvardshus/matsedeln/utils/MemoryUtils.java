package jonas.jacobsson.midgardensvardshus.matsedeln.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jonas.jacobsson.midgardensvardshus.matsedeln.models.FeedReaderContract;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.FeedReaderDbHelper;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2017-04-29.
 */

public class MemoryUtils {


    public static void saveMenu(Context context, ArrayList<WeekItem> menu) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onReset(db);

        for (WeekItem item : menu) {

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAY, item.getDay());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD, item.getFood());
            Logs.i("saveMenu","day="+item.getDay() +", food="+item.getFood());
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }
    }

    public static ArrayList<WeekItem> loadMenu(Context context) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DAY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD
        };

        // Filter results WHERE "title" = 'My Title'
//        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_DAY + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        ArrayList<WeekItem> menu = new ArrayList<>();
        while (cursor.moveToNext()) {
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String day = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DAY));
            String food = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD));
            Logs.i("loadMenu","day="+day +", food="+food);
            menu.add(new WeekItem(day, food));
        }
        cursor.close();
        return menu;
    }

}
