package jonas.jacobsson.midgardensvardshus.matsedeln.utils;

import android.util.Log;

import java.util.ArrayList;

import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2017-01-23.
 */
public class MenuParser {

    private static final String[] days = {
            "Måndag",
            "Tisdag",
            "Onsdag",
            "Torsdag",
            "Fredag",
            "Lördag",
            "Söndag"
    };

    public static String getWeekNumber(ArrayList<String> rows) {
        for (String row : rows) {
            if (row.startsWith("V.") || row.startsWith("v.")) {
                String[] parts = row.split("\\.");
                return "Vecka " + parts[1];
            }
        }
        return "";
    }

    public static WeekItem getMealOfTheDay(ArrayList<String> rows, int dayNbr) {
        String strMeal = "";
//        for (int i = 0; i < rows.size(); i++) {
//            if (weekday.equalsIgnoreCase(rows.get(i))) {
//                for (int j = i + 1; j < i + 4; j++) {
//                    if (j >= rows.size() - 1) {
//                        j = rows.size() - 1;
//                    }
//                    strMeal += rows.get(j);
//                    if (j != i + 4) {
//                        strMeal += "\n";
//                    }
//                    Log.i(weekday, strMeal);
//                }
//                break;
//            }
//        }
//        return new WeekItem(weekday, strMeal);


        for (int rowNbr = 0; rowNbr < rows.size(); rowNbr++) {
            if (days[dayNbr].equalsIgnoreCase(rows.get(rowNbr))) {
                rowNbr++;
                Log.i("day= "+days[dayNbr], "rowNbr= " + rowNbr);
                while (rowNbr < rows.size() && !rows.get(rowNbr).equalsIgnoreCase(days[dayNbr + 1])) {
                    strMeal += rows.get(rowNbr);
                    if (rowNbr != rows.size()-1) {
                        strMeal += "\n";
                    }
                    rowNbr++;
                }
                Log.i(days[dayNbr], strMeal);
            }
        }
        return new WeekItem(days[dayNbr], strMeal);
    }
}
