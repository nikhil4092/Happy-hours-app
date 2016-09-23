package waverr.project.com.waverr;
import android.app.Activity;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class InRestaurant extends ActionBarActivity {

    private static final String TAG = "junk";
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        setUpTabs(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AnalyticsBase)getApplication()).getDefaultTracker();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //            save the selected tab's index so it's re-selected on orientation change
        outState.putInt("tabIndex", getSupportActionBar().getSelectedNavigationIndex());
    }

    private void setUpTabs(Bundle savedInstanceState) {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setTitle("In Restaurant");

        android.support.v7.app.ActionBar.Tab tab_one = actionBar.newTab();
        android.support.v7.app.ActionBar.Tab tab_two = actionBar.newTab();

        Gson gson = new Gson();
        String extras = getIntent().getExtras().getString("Restaurant");
        final Restaurant c = gson.fromJson(extras,Restaurant.class);

        FirstFragment firstFragment = new FirstFragment();
        tab_one.setText("Info")
                .setContentDescription("Information Tab")
                .setTabListener(
                        new MyTabListener<FirstFragment>(
                                firstFragment));

        if(c.getDeal1().equals("")&&c.getDeal2().equals("")&&c.getDeal3().equals("")&&c.getDeal4().equals("")&&c.getDeal5().equals(""))
        {}
        else {
            SecondFragment secondFragment = new SecondFragment();
            tab_two.setText("Deals")
                    .setContentDescription("Deals tab")
                    .setTabListener(
                            new MyTabListener<SecondFragment>(
                                    secondFragment));
        }


        actionBar.addTab(tab_one);
        if(c.getDeal1().equals("")&&c.getDeal2().equals("")&&c.getDeal3().equals("")&&c.getDeal4().equals("")&&c.getDeal5().equals(""))
        {}
        else {actionBar.addTab(tab_two);}


        if (savedInstanceState != null) {
            Log.i(TAG, "setting selected tab from saved bundle");
//            get the saved selected tab's index and set that tab as selected
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tabIndex", 0));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Global.sendAnalyticsEventHit(InRestaurant.this,R.string.ga_cat_button_click,R.string.ga_act_backbutton_inRestaurant);
                // API 5+ solution
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
