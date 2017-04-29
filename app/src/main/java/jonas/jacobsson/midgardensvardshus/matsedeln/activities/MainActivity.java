package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.ContactTabFragment;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.MapTabFragment;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.MenuTabFragment;


public class MainActivity extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String URL = "http://midgarden.se/dagens-lunch/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    private void initViews() {
        initBottomBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initBottomBar() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.main_bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment newFragment;
                switch (tabId) {
                    default:
                        newFragment = new MenuTabFragment();
                        break;
                    case R.id.tab_map:
                        newFragment = new MapTabFragment();
                        break;
                    case R.id.tab_mail:
                        newFragment = new ContactTabFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.rl_tabs, newFragment);
                transaction.commit();
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
