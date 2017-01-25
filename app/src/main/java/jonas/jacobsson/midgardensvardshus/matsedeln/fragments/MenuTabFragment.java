package jonas.jacobsson.midgardensvardshus.matsedeln.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import jonas.jacobsson.midgardensvardshus.matsedeln.adapters.WeekItemRecyclerAdapter;
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
    RecyclerView weekListView;

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
        loadWeekList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        weekListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        weekListView.setLayoutManager(linearLayoutManager);


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
            WeekItemRecyclerAdapter adapter = new WeekItemRecyclerAdapter(getContext(), items);
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
                Document doc = Jsoup.connect("http://midgarden.se/dagens-lunch/").get();
                Elements divs = doc.getElementsByTag("blockquote");
                Element div = divs.first();
                Elements es = div.getElementsByTag("p");
                for (Element e : es) {
                    Log.i(TAG, e.text());
                    rows.add(e.text());
                }
            } catch (IOException e) {
                Log.i(TAG, "Couldn't load website..");
            }

            return rows;
        }

        @Override
        protected void onPostExecute(ArrayList<String> menuItems) {
            try {
                if (!menuItems.isEmpty()) {
                    ArrayList<WeekItem> items = new ArrayList<>();

                    String strWeekNum = MenuParser.getWeekNumber(menuItems);
                    items.add(new WeekItem(strWeekNum, getResources().getString(R.string.times)));
                    ArrayList<String> weekdays = new ArrayList<>();
                    weekdays.add("Måndag");
                    weekdays.add("Tisdag");
                    weekdays.add("Onsdag");
                    weekdays.add("Torsdag");
                    weekdays.add("Fredag");
//                    weekdays.add("Lördag");

                    for (int i = 0; i < weekdays.size(); i++) {
                        items.add(MenuParser.getMealOfTheDay(menuItems, weekdays.get(i)));
                    }

                    WeekItemRecyclerAdapter adapter = new WeekItemRecyclerAdapter(getContext(), items);
                    Paper.book().write(WEEK_MEALS, items);
                    weekListView.setAdapter(adapter);

                } else {
//                Toast.makeText(getContext(), "Det gick inte att hämta veckans meny. Försök igen!", Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            } catch (NullPointerException | IllegalStateException e) {
                // Do nothing
            }
        }

    }
}
