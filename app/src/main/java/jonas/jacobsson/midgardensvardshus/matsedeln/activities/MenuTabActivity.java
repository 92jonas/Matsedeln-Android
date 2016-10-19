package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by Jonas on 2016-10-04.
 */
public class MenuTabActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        WebView webView = (WebView) findViewById(R.id.main_web_view) ;
        webView.loadUrl(MainActivity.URL);
    }
}
