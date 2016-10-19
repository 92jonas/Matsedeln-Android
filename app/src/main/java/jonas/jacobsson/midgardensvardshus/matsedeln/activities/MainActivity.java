package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.adapters.WeekItemAdapter;
import jonas.jacobsson.midgardensvardshus.matsedeln.models.WeekItem;


public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String URL = "http://midgarden.se/dagens-lunch/";
    public static final LatLng MAP_LOCATION = new LatLng(56.252793, 12.892882);
    public static final float MAP_ZOOM = 12f;

    private ListView weekListView;
    private ArrayList<Integer> rowsForDays;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        initViews();

        this.weekListView = (ListView) findViewById(R.id.week_list_view);

        initDayChecker();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        final SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh - SwipeRefreshLayout");

                new AsyncWebLoader().execute();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshListener.onRefresh();
            }
        });

    }

    private void initDayChecker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        int startRow = -1;
        rowsForDays = new ArrayList<>();


        switch (day) {

            case Calendar.MONDAY:
                startRow = 1;
                break;
            case Calendar.TUESDAY:
                startRow = 5;
                break;

            case Calendar.WEDNESDAY:
                startRow = 9;
                break;

            case Calendar.THURSDAY:
                startRow = 13;
                break;
            case Calendar.FRIDAY:
                startRow = 17;
                break;

            case Calendar.SATURDAY:

                break;

            case Calendar.SUNDAY:

                break;

        }
        if (startRow > -1 && startRow < 21) {
            for (int i = startRow; i < startRow + 4; i++) {
                rowsForDays.add(i);
            }
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    public void initViews() {
        initTabHost();


    }

    private void initTabHost() {


    }

    private class AsyncWebLoader extends AsyncTask<Void, Void, ArrayList<String>> {

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
                String title = doc.title();

                // et_pb_text et_pb_module et_pb_bg_layout_light et_pb_text_align_center et_pb_text_1

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
            ArrayList<WeekItem> items = new ArrayList<>();
            TextView weekNumTv = (TextView) findViewById(R.id.week_num_tv);
            weekNumTv.setText(menuItems.get(0));
            Log.i(TAG, "items size= " + menuItems.size());

            for (int i = 1; i < menuItems.size(); i += 4) {
                Log.i(TAG, "items id= " + i);
//                String item = menuItems.get(i);
//                View child = getLayoutInflater().inflate(R.layout.week_item, null);
//                TextView tv = (TextView) child.findViewById(R.id.week_item_tv);
//                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.otherDayText));
//
//                if (i % 4 == 1) { // If weekday row
//                    tv.setTextSize(16f);
//                    tv.setTypeface(Typeface.DEFAULT_BOLD);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
//                    float marginTop = ConverterUtils.convertDpToPixel(10f, MainActivity.this);
//                    params.setMargins(0, (int) marginTop, 0, 0);
//
//                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    tv.setLayoutParams(params);
//
//
//                }
//
//                if (i == 0) {
//                    tv.setTextSize(22f);
//                    tv.setTypeface(Typeface.DEFAULT_BOLD);
//                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.weekNum));
//                }
//
//                if (rowsForDays.contains(i)) {
//
//                    tv.setTypeface(Typeface.DEFAULT_BOLD);
//                    tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.currentDayText2));
//
//                }
//
//
//                tv.setText(item);

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


            WeekItemAdapter adapter = new WeekItemAdapter(context, R.layout.week_item, items);
            weekListView.setAdapter(adapter);


            swipeRefreshLayout.setRefreshing(false);
        }

    }


}