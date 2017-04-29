package jonas.jacobsson.midgardensvardshus.matsedeln.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2017-01-24.
 */

public class WeekItemRecyclerAdapter extends RecyclerView.Adapter<WeekItemRecyclerAdapter.ViewHolder> {
    private ArrayList<WeekItem> list;
    private int currentDay;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day, food;

        public ViewHolder(View v) {
            super(v);
            day = (TextView) v.findViewById(R.id.week_item_day);
            food = (TextView) v.findViewById(R.id.week_item_food);
        }
    }

    public WeekItemRecyclerAdapter(Context context, ArrayList<WeekItem> list) {
        this.list = list;
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        this.currentDay = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weekitem, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeekItem p = list.get(position);
        if (p != null) {
            int textColor;
            if (position == currentDay - 1) {
                textColor = ContextCompat.getColor(context, R.color.currentDayText);
            } else {
                textColor = ContextCompat.getColor(context, R.color.otherDayText);
            }

            if (p.getDay().equals("")) {
                holder.day.setVisibility(View.GONE);
            } else {
                holder.day.setText(p.getDay());
                holder.day.setTextColor(textColor);
            }

            if (p.getFood().equals("")) {
                holder.food.setVisibility(View.GONE);
            } else {
                holder.food.setText(p.getFood());
                holder.food.setTextColor(textColor);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
