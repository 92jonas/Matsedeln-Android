package jonas.jacobsson.midgardensvardshus.matsedeln.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2016-10-18.
 */

public class WeekItemAdapter extends ArrayAdapter<WeekItem> {

    private Context context;
    private TextView day, food;
    private int currentDay;

    public WeekItemAdapter(Context context, int resource, ArrayList<WeekItem> items) {
        super(context, resource, items);
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        this.currentDay = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.week_item, null);
        }

        WeekItem p = getItem(position);

        if (p != null) {
            int textColorId;
            if (position == currentDay - 1) {
                textColorId = p.getTextColor(true);
            } else {
                textColorId = p.getTextColor(false);
            }
            int textColor = ContextCompat.getColor(context, textColorId);

            day = (TextView) v.findViewById(R.id.week_item_day);
            food = (TextView) v.findViewById(R.id.week_item_food);

            if (day != null) {
                day.setText(p.getDay());
                day.setTextColor(textColor);
            }

            if (food != null) {
                if (p.getFood().equals("")) {
                    food.setVisibility(View.GONE);
                } else {
                    food.setText(p.getFood());
                    food.setTextColor(textColor);
                }
            }

        }

        return v;
    }

    public void setCurrentDay() {

        day.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
        food.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
    }

}
