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
    private TextView day, alt1, divider, alt2;
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
            if (position == currentDay - 2) {
                textColorId = p.getTextColor(true);
            } else {
                textColorId = p.getTextColor(false);
            }
            int textColor = ContextCompat.getColor(context, textColorId);

            day = (TextView) v.findViewById(R.id.week_item_day);
            alt1 = (TextView) v.findViewById(R.id.week_item_food_alt1);
            divider = (TextView) v.findViewById(R.id.week_item_divider);
            alt2 = (TextView) v.findViewById(R.id.week_item_food_alt2);

            if (day != null) {
                day.setText(p.getDay());
                day.setTextColor(textColor);
            }

            if (alt1 != null) {
                alt1.setText(p.getFoodAlt1());
                alt1.setTextColor(textColor);
            }

            if (divider != null) {
                divider.setText(p.getDivider());
                divider.setTextColor(textColor);
            }

            if (alt2 != null) {
                alt2.setText(p.getFoodAlt2());
                alt2.setTextColor(textColor);
            }

        }

        return v;
    }

    public void setCurrentDay() {

        day.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
        alt1.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
        divider.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
        alt2.setTextColor(ContextCompat.getColor(context, R.color.currentDayText));
    }

}
