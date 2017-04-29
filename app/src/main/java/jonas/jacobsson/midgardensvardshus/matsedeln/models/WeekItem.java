package jonas.jacobsson.midgardensvardshus.matsedeln.models;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by Jonas on 2016-10-18.
 */

public class WeekItem {

    private String day, food;

    public WeekItem(String day, String food) {
        this.day = day;
        this.food = food;
    }

    public String getFood() {
        return food;
    }

    public String getDay() {
        return day;
    }
}
