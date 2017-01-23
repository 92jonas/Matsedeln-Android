package jonas.jacobsson.midgardensvardshus.matsedeln.utils;

import android.util.Log;

import java.util.ArrayList;

import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2017-01-23.
 */
public class MenuParser {


    public static String getWeekNumber(ArrayList<String> rows) {
        for (String row : rows) {
            if (row.startsWith("V.") || row.startsWith("v.")) {
                return row;
            }
        }
        return "";
    }

    public static WeekItem getMealOfTheDay(ArrayList<String> rows, String weekday) {
        String strMeal = "";
        for (int i = 0; i < rows.size(); i++) {
            if (weekday.equalsIgnoreCase(rows.get(i))) {
                for (int j = i + 1; j < i + 4; j++) {
                    strMeal += rows.get(j);
                    if (j != i + 4) {
                        strMeal += "\n";
                    }
                    Log.i(weekday, strMeal);
                }
                break;
            }
        }
        return new WeekItem(weekday, strMeal);
    }
}
