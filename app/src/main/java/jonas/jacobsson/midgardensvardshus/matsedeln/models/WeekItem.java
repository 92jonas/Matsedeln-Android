package jonas.jacobsson.midgardensvardshus.matsedeln.models;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by Jonas on 2016-10-18.
 */

public class WeekItem {

    private String day, foodAlt1, divider, foodAlt2;

    public WeekItem(String day, String foodAlt1, String divider, String foodAlt2) {
        this.day = day;
        this.foodAlt1 = foodAlt1;
        this.foodAlt2 = foodAlt2;
        this.divider = divider;
    }

    public String getDay() {
        return day;
    }

    public String getFoodAlt1() {
        return foodAlt1;
    }

    public String getDivider() {
        return divider;
    }

    public String getFoodAlt2() {
        return foodAlt2;
    }

    public int getTextColor(boolean current) {
        if (current) {
            return R.color.currentDayText;
        } else {
            return R.color.otherDayText;
        }
    }
}
