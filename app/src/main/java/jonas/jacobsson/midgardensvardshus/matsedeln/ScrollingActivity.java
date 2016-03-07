package jonas.jacobsson.midgardensvardshus.matsedeln;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {
    Context context;
    String url = "http://midgarden.se/menu-category/food-menu/";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        context = getApplicationContext();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        init();
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "A La Carte menyn!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                showMap(Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia&avoid=tf"));
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hitta hit karta!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                String uri = String.format(Locale.ENGLISH, "daddr=%f,%f", 56.252886, 12.892947);
                showMap(Uri.parse(uri));
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

    public void init() {
        new WebParser().execute();
    }

    private class WebParser extends AsyncTask<Void, Void, Void> {

        ArrayList<String> weekdays, descs;
        String lunchmeny;
        String START_H1_U = "<h1><u>";
        String END_H1_U = "</h1></u>";
        String START_H3_BLACK = "<h3><font color=\"black\">";
        String END_H3_BLACK = "</font></h3>";


        String START_H3_U = "<h3><u>";
        String END_H3_U = "</h3></u>";
        String START_H3 = "<h3>";
        String END_H3 = "</h3>";

        String START_B_BLACK = "<b><font color=\"black\">";
        String END_B_BLACK = "</font></b>";
        String START_BLACK = "<font color=\"black\">";
        String END_BLACK = "</font>";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScrollingActivity.this);
            mProgressDialog.setTitle("Hämtar matsedeln");
            mProgressDialog.setMessage("Laddar...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connects to the web site
                Document document = Jsoup.connect(url).get();

                // Lunchmeny v.XX
                Elements ee = document.select("h3[class=food_menu_catagory_name]");
                if(ee.size() > 0){
                    System.out.println("H3s: size=" + ee.toString());
                    lunchmeny = ee.get(0).text();
                }

                // In this chapter the menu is
                Elements e = document.select("section[id=chapter-124]");

                // Extracts weekdays to the meals
                Elements divWeekdays = e.select("div[class=food_menu_small_image_name]");
                Elements hrefWeekdays = divWeekdays.select("a[href]");

                // Extracts meal descriptions
                Elements divDescs = e.select("div[class=food_menu_small_image_desc]");
                Elements hrefDescs = divDescs.select("p");

                // Puts the elements in ArrayLists as Strings
                weekdays = new ArrayList<>();
                descs = new ArrayList<>();
                while(hrefWeekdays.size() > 0){
                    weekdays.add(hrefWeekdays.first().text());
                    descs.add(hrefDescs.first().text());
                    hrefWeekdays.remove(0);
                    hrefDescs.remove(0);
                    System.out.println("weekdays=" + hrefWeekdays.size() + " descs= " + hrefDescs.size());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            StringBuilder text = new StringBuilder();
            StringBuilder dagens = new StringBuilder();
            Calendar calendar = Calendar.getInstance();

            text.append(START_H1_U + lunchmeny + END_H1_U);

            String dagensStr = context.getResources().getString(R.string.dagens);
            dagens.append(START_H1_U + dagensStr + END_H1_U);
            // sunday = 1 ... saturday = 7
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if(weekdays != null && weekdays.size()>1) {
                for (int i = 0; i < weekdays.size(); i++) {


                    // sunday = -1 ... saturday = 5
                    // Dagens
                    if((day-2) == i){


                        dagens.append(START_H3_BLACK + descs.get(i) + END_H3_BLACK);

                        text.append(START_H3_U + weekdays.get(i) + END_H3_U);
                        text.append(START_B_BLACK + descs.get(i) + END_B_BLACK);


                    }else{

                        text.append(START_H3 + weekdays.get(i) + END_H3);
                        text.append(START_BLACK + descs.get(i) + END_BLACK);
                    }

                }
            }

            // Helg!
            if(day == 1 || day == 7){
                String ingenDagensStr = context.getResources().getString(R.string.ingenDagens);
                dagens.append(START_BLACK + ingenDagensStr + END_BLACK);
            }


            // Set description into TextView
            TextView tvDagens = (TextView) findViewById(R.id.dagens);
            TextView tvMatsedel = (TextView) findViewById(R.id.matsedel);

            tvDagens.setText(Html.fromHtml(dagens.toString()));
            tvMatsedel.setText(Html.fromHtml(text.toString()));
            mProgressDialog.dismiss();
        }
    }
}
