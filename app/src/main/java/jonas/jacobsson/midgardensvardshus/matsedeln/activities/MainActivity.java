package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
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
    private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;
    private GoogleMap map;
    private MapView mapView;
    private LinearLayout mapLl, contactLl;
    private RelativeLayout menuRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeekList();
    }

    private void initViews() {

        this.weekListView = (ListView) findViewById(R.id.week_list_view);
        this.mapLl = (LinearLayout) findViewById(R.id.main_map_ll);
        this.contactLl = (LinearLayout) findViewById(R.id.main_contact_ll);
        this.menuRl = (RelativeLayout) findViewById(R.id.main_menu_rl);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                final LatLng TutorialsPoint = new LatLng(21 , 57);
                Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint));
//                map.setMyLocationEnabled(true);
//                map.addMarker(new MarkerOptions());
            }
        });

        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        this.swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh - SwipeRefreshLayout");

                new AsyncWebLoader().execute();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);

        initBottomBar();

    }

    private void initBottomBar() {
        menuRl.setVisibility(View.VISIBLE);
        mapLl.setVisibility(View.GONE);
        contactLl.setVisibility(View.GONE);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.main_bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_menu:
                        menuRl.setVisibility(View.VISIBLE);
                        mapLl.setVisibility(View.GONE);
                        contactLl.setVisibility(View.GONE);
                        break;
                    case R.id.tab_map:
                        menuRl.setVisibility(View.GONE);
                        mapLl.setVisibility(View.VISIBLE);
                        contactLl.setVisibility(View.GONE);
                        break;
                    case R.id.tab_mail:
                        menuRl.setVisibility(View.GONE);
                        mapLl.setVisibility(View.GONE);
                        contactLl.setVisibility(View.VISIBLE);
                }
            }
        });
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
