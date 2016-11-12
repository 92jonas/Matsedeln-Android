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
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.adapters.WeekItemAdapter;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;

/**
 * Created by Jonas on 2016-11-11.
 */

public class MenuTabFragment extends Fragment {

    private static final String TAG = MenuTabFragment.class.getSimpleName();

    private ListView weekListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


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
        this.weekListView = (ListView) getView().findViewById(R.id.week_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
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
                TextView weekNumTv = (TextView) getView().findViewById(R.id.tv_week_number);
                weekNumTv.setText(menuItems.get(0));
                Log.i(TAG, "items size= " + menuItems.size());

                for (int i = 1; i < menuItems.size(); i += 4) {
                    Log.i(TAG, "items id= " + i);

                    Log.i(TAG, "veckodag: " + menuItems.get(i));
                    if (menuItems.size() > i + 3) {
                        items.add(new WeekItem(menuItems.get(i), menuItems.get(i + 1), menuItems.get(i + 2), menuItems.get(i + 3)));
                    } else if (menuItems.size() > i + 2) {
                        items.add(new WeekItem(menuItems.get(i), menuItems.get(i + 1), menuItems.get(i + 2), ""));
                    } else if (menuItems.size() > i + 1) {
                        items.add(new WeekItem(menuItems.get(i), menuItems.get(i + 1), "", ""));
                    } else {
                        items.add(new WeekItem(menuItems.get(i), "", "", ""));
                    }

                }


                WeekItemAdapter adapter = new WeekItemAdapter(getActivity(), R.layout.week_item, items);
                weekListView.setAdapter(adapter);


            } else {
                Toast.makeText(getActivity(), "Misslyckades, försök igen!", Toast.LENGTH_LONG).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
