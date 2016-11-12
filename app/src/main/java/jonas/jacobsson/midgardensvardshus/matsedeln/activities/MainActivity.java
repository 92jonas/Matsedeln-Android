package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.ContactTabFragment;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.MapTabFragment;
import jonas.jacobsson.midgardensvardshus.matsedeln.fragments.MenuTabFragment;
import jonas.jacobsson.midgardensvardshus.matsedeln.utils.MapHandler;


public class MainActivity extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String URL = "http://midgarden.se/dagens-lunch/";
    public static final LatLng MAP_LOCATION = new LatLng(56.252793, 12.892882);
    public static final float MAP_ZOOM = 12f;

    private GoogleMap map;
    private MapFragment mapFragment;
    private MapHandler mapHandler;
    private LinearLayout mapLl, contactLl;
    private RelativeLayout menuRl;
    private static final int PERMISSION_MY_LOCATION = 12;
    private ContactTabFragment mContactFragment;
    private MenuTabFragment mMenuFragment;
    private MapTabFragment mMapFragment;

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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_MY_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapHandler.initMyLocation(this);

                }
            }
        }
    }


}
