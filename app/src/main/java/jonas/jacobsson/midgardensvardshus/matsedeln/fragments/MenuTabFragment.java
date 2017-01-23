package jonas.jacobsson.midgardensvardshus.matsedeln.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.adapters.WeekItemAdapter;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;
import jonas.jacobsson.midgardensvardshus.matsedeln.utils.MenuParser;

/**
 * Created by Jonas on 2016-11-11.
 */

public class MenuTabFragment extends Fragment {

    private static final String TAG = MenuTabFragment.class.getSimpleName();

    private static final String WEEK_MEALS = "weekMeals";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;

    @BindView(R.id.week_list_view)
    ListView weekListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "MenuTabFragment - onResume");
        loadWeekList();
    }

    private void initViews() {

        this.swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh - SwipeRefreshLayout");

                new AsyncWebLoader().execute();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
    }

    private void loadWeekList() {
        ArrayList<WeekItem> items = Paper.book().read(WEEK_MEALS);
        if (items != null) {
            WeekItemAdapter adapter = new WeekItemAdapter(getActivity(), R.layout.week_item, items);
            weekListView.setAdapter(adapter);
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshListener.onRefresh();
            }
        });
    }


    public class AsyncWebLoader extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> rows = new ArrayList<>();

            try {

                Log.i(TAG, "JSoup - Start");
                Document doc = Jsoup.connect("http://midgarden.se/dagens-lunch/").get();

                Log.i(TAG, "JSoup - Doc loaded");
                Elements divs = doc.getElementsByTag("blockquote");
                Element div = divs.first();

                Elements es = div.getElementsByTag("p");

                for (Element e : es) {
                    Log.i(TAG, e.text());
                    rows.add(e.text());
                }

                Log.i(TAG, "JSoup - Stop");
            } catch (IOException e) {
                Log.i(TAG, "Couldn't load website..");
            }

            return rows;
        }

        @Override
        protected void onPostExecute(ArrayList<String> menuItems) {
            if (!menuItems.isEmpty()) {
                ArrayList<WeekItem> items = new ArrayList<>();
                Log.i(TAG, "items size= " + menuItems.size());

                String strWeekNum = MenuParser.getWeekNumber(menuItems);
                items.add(new WeekItem(strWeekNum, "Lunch 11.00 - 14.00"));
                ArrayList<String> weekdays = new ArrayList<>();
                weekdays.add("Måndag");
                weekdays.add("Tisdag");
                weekdays.add("Onsdag");
                weekdays.add("Torsdag");
                weekdays.add("Fredag");
                weekdays.add("Lördag");

                for (int i = 0; i < weekdays.size(); i++) {
                    items.add(MenuParser.getMealOfTheDay(menuItems, weekdays.get(i)));
                }

                WeekItemAdapter adapter = new WeekItemAdapter(getActivity(), R.layout.week_item, items);
                Paper.book().write(WEEK_MEALS, items);
                weekListView.setAdapter(adapter);

            } else {
                Toast.makeText(getActivity(), "Det gick inte att hämta veckans meny. Försök igen!", Toast.LENGTH_LONG).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
