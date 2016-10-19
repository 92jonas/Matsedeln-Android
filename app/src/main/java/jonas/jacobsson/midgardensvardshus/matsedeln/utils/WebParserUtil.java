//package jonas.jacobsson.midgardensvardshus.matsedeln.utils;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.text.Html;
//import android.widget.TextView;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import jonas.jacobsson.midgardensvardshus.matsedeln.R;
//import jonas.jacobsson.midgardensvardshus.matsedeln.activities.MainActivity;
//
///**
// * Created by Jonas on 16-09-14.
// */
//public class WebParserUtil extends AsyncTask<Void, Void, Void> {
//
//    private ArrayList<String> weekdays, descs;
//
//    private  String lunchmeny;
//    private static final String START_H1_U = "<h1><u>";
//    private static final String END_H1_U = "</h1></u>";
//    private static final String START_H3_BLACK = "<h3><font color=\"black\">";
//    private static final String END_H3_BLACK = "</font></h3>";
//
//
//    private static final String START_H3_U = "<h3><u>";
//    private static final String END_H3_U = "</h3></u>";
//    private static final String START_H3 = "<h3>";
//    private static final String END_H3 = "</h3>";
//
//    private static final String START_B_BLACK = "<b><font color=\"black\">";
//    private static final String END_B_BLACK = "</font></b>";
//    private static final String START_BLACK = "<font color=\"black\">";
//    private static final String END_BLACK = "</font>";
//
//
//    private ProgressDialog mProgressDialog;
//
//    public static String getWeekdayLunches() {
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        mProgressDialog = new ProgressDialog(MainActivity.this);
//        mProgressDialog.setTitle("Hämtar matsedeln");
//        mProgressDialog.setMessage("Laddar...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.show();
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//        try {
//            // Connects to the web site
//            Document document = Jsoup.connect(url).get();
//
//            // Lunchmeny v.XX
//            Elements ee = document.select("h3[class=food_menu_catagory_name]");
//            if (ee.size() > 0) {
//                System.out.println("H3s: size=" + ee.toString());
//                lunchmeny = ee.get(0).text();
//            }
//
//            // In this chapter the menu is
//            Elements e = document.select("section[id=chapter-124]");
//
//            // Extracts weekdays to the meals
//            Elements divWeekdays = e.select("div[class=food_menu_small_image_name]");
//            Elements hrefWeekdays = divWeekdays.select("a[href]");
//
//            // Extracts meal descriptions
//            Elements divDescs = e.select("div[class=food_menu_small_image_desc]");
//            Elements hrefDescs = divDescs.select("p");
//
//            // Puts the elements in ArrayLists as Strings
//            weekdays = new ArrayList<>();
//            descs = new ArrayList<>();
//            while (hrefWeekdays.size() > 0) {
//                weekdays.add(hrefWeekdays.first().text());
//                descs.add(hrefDescs.first().text());
//                hrefWeekdays.remove(0);
//                hrefDescs.remove(0);
//                System.out.println("weekdays=" + hrefWeekdays.size() + " descs= " + hrefDescs.size());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        StringBuilder text = new StringBuilder();
//        StringBuilder dagens = new StringBuilder();
//        Calendar calendar = Calendar.getInstance();
//
//        text.append(START_H1_U + lunchmeny + END_H1_U);
//
//        String dagensStr = context.getResources().getString(R.string.dagens);
//        dagens.append(START_H1_U + dagensStr + END_H1_U);
//        // sunday = 1 ... saturday = 7
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        if (weekdays != null && weekdays.size() > 1) {
//            for (int i = 0; i < weekdays.size(); i++) {
//
//
//                // sunday = -1 ... saturday = 5
//                // Dagens
//                if ((day - 2) == i) {
//
//
//                    dagens.append(START_H3_BLACK + descs.get(i) + END_H3_BLACK);
//
//                    text.append(START_H3_U + weekdays.get(i) + END_H3_U);
//                    text.append(START_B_BLACK + descs.get(i) + END_B_BLACK);
//
//
//                } else {
//
//                    text.append(START_H3 + weekdays.get(i) + END_H3);
//                    text.append(START_BLACK + descs.get(i) + END_BLACK);
//                }
//
//            }
//        }
//
//        // Helg!
//        if (day == 1 || day == 7) {
//            String ingenDagensStr = context.getResources().getString(R.string.ingenDagens);
//            dagens.append(START_BLACK + ingenDagensStr + END_BLACK);
//        }
//
//
//        // Set description into TextView
//        TextView tvDagens = (TextView) context.findViewById(R.id.dagens);
//        TextView tvMatsedel = (TextView) context.findViewById(R.id.matsedel);
//
//        tvDagens.setText(Html.fromHtml(dagens.toString()));
//        tvMatsedel.setText(Html.fromHtml(text.toString()));
//        mProgressDialog.dismiss();
//    }
//}
//}
