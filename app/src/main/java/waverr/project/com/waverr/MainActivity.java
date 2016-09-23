package waverr.project.com.waverr;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ActionBarActivity implements OnScrollListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog progressDialog;
    ArrayList<Restaurant> restaurants;
    ArrayList<Restaurant> filteredRestaurants;
    TextView direction,deals;
    FloatingActionButton filter;
    private ProgressDialog logoutDialog;
    private GoogleApiClient mGoogleApiClient;
    String city;
    String[] cities;
    AlertDialog.Builder locationPromptBuilder;
    AlertDialog locationPrompt;
    SharedPreferences preferences;
    String[] cuisines,area;
    int ThatsAll =0;
    List<String> cui = new ArrayList<>();
    List<String> are = new ArrayList<>();
    RelativeLayout topLevelLayout;
    private Global mGlobalData;
    String LoginState="";
    String Terms="";
    private int VERSION_CODE;
    boolean doubleBackToExitPressedOnce = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */


    //Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        city = preferences.getString("CityName", "");
        if(city.equalsIgnoreCase(""))
        {SharedPreferences.Editor editor = preferences.edit();
            editor.putString("CityName","Bangalore");
            city="Bangalore";
            editor.apply();}


        ((AnalyticsBase)getApplication()).getDefaultTracker();
        Tracker t = ((AnalyticsBase)getApplication()).getDefaultTracker();
        t.enableAdvertisingIdCollection(true);

        try {
            VERSION_CODE = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            VERSION_CODE = 0;
            e.printStackTrace();
        }
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        if(Global.Name.compareToIgnoreCase("")!=0 && Global.Email.compareToIgnoreCase("")!=0)
        {
            addUser();
        }



        Global.Cuisine="";
        Global.Area="";
        Global.Costfortwo="";
        restaurants = new ArrayList<>();
        filteredRestaurants = new ArrayList<>();
        setTitle(city);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        filter=(FloatingActionButton)findViewById(R.id.filter);


        mRecyclerView.addOnScrollListener(new MyScrollListenerRestaurantList(this) {
            @Override
            public void onMoved(int distance) {
                filter.setTranslationY(distance*2);
              //  d_bar.setTranslationY(distance);
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findLastCompletelyVisibleItemPosition() == restaurants.size() - 1 && ThatsAll==0

                        ) {
                    //Toast.makeText(RestaurantListActivity.this, "Scroll stopped on last", Toast.LENGTH_SHORT).show();
                    getRestaurants(restaurants.size());
                }
            }
        });
        boolean check = isNetworkAvailable();
        if(check == true)
        {fetchNewCities();
            getCuisines();
            getArea();
            getTerms();
            getVersion();
        }
        else{
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                    MainActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Internet connection unavailable");

            // Setting Dialog Message
            alertDialog.setMessage("Please check your internet connection.");

            // Setting Icon to Dialog


            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        topLevelLayout = (RelativeLayout)findViewById(R.id.top_layout);
        if (isFirstTime()) {
            topLevelLayout.setVisibility(View.INVISIBLE);
            getRestaurants(0);
        }
        if(!isFirstTime()) {
            getRestaurants(0);
        }
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        direction=(TextView)findViewById(R.id.legendDirection);
        deals=(TextView)findViewById(R.id.legendDeals);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(MainActivity.this,R.string.ga_cat_button_click,R.string.ga_act_filter);
                int j=0;
                cuisines = new String[cui.size()];
                for(String s : cui)
                {
                    cuisines[j]=s;
                    j++;
                }
                j=0;

                area = new String[are.size()];
                for(String s : are)
                {
                    area[j]=s;
                    j++;
                }
                saveArray(area,"area",MainActivity.this);
                saveArray(cuisines,"cuisines",MainActivity.this);
                Intent i = new Intent(MainActivity.this,Filters.class);
                startActivity(i);
            }


        });
        direction.setTypeface(face);
        deals.setTypeface(face);
        mAdapter = new CardAdapter(restaurants,this);
        mRecyclerView.setAdapter(mAdapter);



    }

    private void addUser() {


        String[] url = new String[]{
                "http://waverr.in/beacon/adduser.php",
                "name", Global.Name,
                "email", Global.Email
        };

        new JSONObtainer() {
            protected void onPostExecute(JSONArray array) {

            }
        }.execute(url);


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }


    private void getRestaurants(int offset){


        city=preferences.getString("CityName", "");
        setTitle(city);
        ThatsAll=0;
        String[] url = new String[]{
                "http://waverr.in/beacon/getrestaurants.php",
                "offset","0",
                "city",city,
                "cuisine",Global.Cuisine,
                "area",Global.Area,
                "costfortwo",Global.Costfortwo
        };
        url[2] = String.valueOf(offset);



        progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Getting Information...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();



        JSONObtainer obtainer;
        obtainer = new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {

                final String things[] = {
                        "RestaurantID",
                        "RestaurantFullName",
                        "Area",
                        "Address",
                        "GPSCoordinates",
                        "CuisineTags",
                        "OtherTags",
                        "RestaurantContactNumber",
                        "RestaurantSecondaryContactNumber",
                        "RestaurantTimings",
                        "CostForTwo",
                        "RestaurantType",
                        "MondayHappyHourTimings",
                        "TuesdayHappyHourTimings",
                        "WednesdayHappyHourTimings",
                        "ThursdayHappyHourTimings",
                        "FridayHappyHourTimings",
                        "SaturdayHappyHourTimings",
                        "SundayHappyHourTimings",
                        "Deal1",
                        "Deal2",
                        "Deal3",
                        "Deal4",
                        "Deal5"

                };
                /*progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Getting Restaurant List...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);

                progressDialog.show();*/
                try {

                    if(array==null)
                    {
                        ThatsAll=1;
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "There are no more restaurants listed", Toast.LENGTH_SHORT).show();
                    }
                    else if (array.length()==0 ) {
                        {
                            if(ThatsAll==0) {
                                progressDialog.dismiss();

                                Toast.makeText(MainActivity.this, "An error occurred. Please check your network connection and try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.getJSONObject(i);
                            Restaurant c = new Restaurant();
                            c.setRestaurantID(object.getString(things[0]));
                            c.setName(object.getString(things[1]));
                            c.setArea(object.getString(things[2]));
                            c.setAddress(object.getString(things[3]));
                            c.setGPS(object.getString(things[4]));
                            c.setCuisine(object.getString(things[5]));
                            c.setOtherTags(object.getString(things[6]));
                            c.setRestaurantContact(object.getString(things[7]));
                            c.setSecondaryContact(object.getString(things[8]));
                            c.setRestaurnatTimings(object.getString(things[9]));
                            c.setCostfortwo(object.getString(things[10]));
                            c.setType(object.getString(things[11]));
                            c.setMondayHH(object.getString(things[12]));
                            c.setTuesdayHH(object.getString(things[13]));
                            c.setWednesdayHH(object.getString(things[14]));
                            c.setThursdayHH(object.getString(things[15]));
                            c.setFridayHH(object.getString(things[16]));
                            c.setSaturdayHH(object.getString(things[17]));
                            c.setSundayHH(object.getString(things[18]));
                            c.setDeal1(object.getString(things[19]));
                            c.setDeal2(object.getString(things[20]));
                            c.setDeal3(object.getString(things[21]));
                            c.setDeal4(object.getString(things[22]));
                            c.setDeal5(object.getString(things[23]));


                            //Toast.makeText(RestaurantListActivity.this, "Distance: "+results[0], Toast.LENGTH_SHORT).show();

                            restaurants.add(c);

                        }
                    }

                    //Toast.makeText(RestaurantListActivity.this, restaurants.toString(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    mAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        };

        obtainer.execute(url);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.locationCity) {
            Global.sendAnalyticsEventHit(MainActivity.this,R.string.ga_cat_button_click,R.string.ga_act_city_select);
            locationPrompt.show();
        }
        if(id==R.id.Refresh){
            Global.sendAnalyticsEventHit(MainActivity.this,R.string.ga_cat_button_click,R.string.ga_act_refresh);
            restaurants.clear();
            Global.filterClickedFlag=0;
            Global.Costfortwo="";
            Global.Cuisine="";
            Global.Area="";
            getRestaurants(0);
        }

        if(id==R.id.action_terms){
            Global.sendAnalyticsEventHit(MainActivity.this,R.string.ga_cat_button_click,R.string.ga_act_terms);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Terms");
            if(Terms.equalsIgnoreCase(""))
                Terms="Waverr is a platform to list different happy hours and deals for the user to enhance nightlife.\n\nWaverr is not responsible if the restaurant does not provide the deals or happy hours.";
            builder.setMessage(Terms)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    ;
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }

        if(id==R.id.action_contactus)
        {
            Global.sendAnalyticsEventHit(this,R.string.ga_cat_button_click,R.string.ga_act_contactus);
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + "contact@waverr.in")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Waverr - Customer feedback");
            if (intent.resolveActivity(getPackageManager()) != null) {
                 startActivity(intent);
            }
        }
        if(id==R.id.action_share){
            Global.sendAnalyticsEventHit(this,R.string.ga_cat_button_click,R.string.ga_act_mainmenu_share);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey :D. Your friend "+Global.Name+", thinks you should check out Waverr.\nDownload Waverr to find all the happy hours near you. http://www.waverr.in/getwaverr");
            startActivity(Intent.createChooser(intent, "Share with"));
        }

        if(id==R.id.action_rateus)
        {
            Global.sendAnalyticsEventHit(this, R.string.ga_cat_button_click, R.string.ga_act_rateus);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.project.waverr")));
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.project.waverr"));
                startActivity(intent);
            }
        }
        if(id==R.id.action_signout)
        {
            Global.sendAnalyticsEventHit(MainActivity.this,R.string.ga_cat_button_click,R.string.logout);
            startLogoutProcess();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
        mGoogleApiClient.disconnect();
        logoutDialog.dismiss();
        Global.Name="";
        Global.Email="";
        Global.PicURL="";
        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class MyDialogClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("CityName", cities[which]);
            editor.apply();

           // filteredRestaurants.clear();
            restaurants.clear();
            getRestaurants(0);
            dialog.dismiss();

        }
    }

    private void fetchNewCities() {

        String[] url = {
                "http://waverr.in/beacon/getcitylist.php"
        };
        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {

                try {
                    if(array==null)
                    {cities = loadArray("cities",MainActivity.this);}
                    else if(array.length()==0)
                    {cities = loadArray("cities",MainActivity.this);}
                    else
                    {
                        cities = new String[array.length()];
                        for(int i=0; i<array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        cities[i] = object.getString("City");

                    }
                        saveArray(cities,"cities",MainActivity.this);
                    }
                    int position=0;
                    for (int i=0;i<cities.length;i++) {
                        if (cities[i].equals(city)) {
                            position = i;
                            break;
                        }
                    }
                    locationPromptBuilder = new AlertDialog.Builder(MainActivity.this);
                    locationPromptBuilder.setSingleChoiceItems(cities, position, new MyDialogClickListener());
                    locationPromptBuilder.setTitle("Choose city");
                    locationPrompt = locationPromptBuilder.create();

                }
                catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }.execute(url);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    public boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(Global.filterClickedFlag==1)
        {
            Toast.makeText(this,"Restaurants are filtered",Toast.LENGTH_SHORT).show();
            restaurants.clear();
            if(Global.fromFilter==1)
            {getRestaurants(0);Global.fromFilter=0;}

        }


    }


    public void getCuisines(){

        String[] url = {
                "http://waverr.in/beacon/getcuisinelist.php"
        };



        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {

                try {


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        cui.add(object.getString("Cuisine"));

                    }


                }catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }.execute(url);
    }

    public void getTerms(){

        String[] url = {
                "http://waverr.in/beacon/getterms.php"
        };



        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {

                try {


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Terms=object.getString("Terms");

                    }


                }catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }.execute(url);
    }

    public void getArea(){

        String[] url = {
                "http://waverr.in/beacon/getarealist.php"
        };



        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {

                try {


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        are.add(object.getString("Area"));

                    }


                }catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }.execute(url);
    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            topLevelLayout.setVisibility(View.VISIBLE);
            topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    getRestaurants(0);
                    return false;
                }

            });


        }
        return ranBefore;

    }


    public void performFilter() {

        if(Global.filterClickedFlag==1)
        {



            String cui=Global.cuisines.toString();

            if(cui.compareTo("")!=0) {
                filteredRestaurants.clear();
                int flag;
                for (Restaurant restaurant : restaurants) {
                    flag = 0;
                    String restCuisines = restaurant.getCuisine();


                    for (String s : Global.cuisines) {
                        if (restCuisines.toLowerCase().contains(s.toLowerCase())) {

                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        filteredRestaurants.add(restaurant);

                    }
                }


            }
            /*String are=Global.area.toString();
            if(are.compareTo("")!=0) {
                ArrayList<Restaurant> temp = new ArrayList<>();
                if(filteredRestaurants.size()==restaurants.size())
                {filteredRestaurants.clear();

                    for (Restaurant restaurant : restaurants) {
                    temp.add(restaurant);
                    }
                }
                else
                {
                    for (Restaurant restaurant : filteredRestaurants) {
                        temp.add(restaurant);
                    }
                    filteredRestaurants.clear();
                }
                int flag;
                for (Restaurant restaurant : temp) {
                    flag = 0;
                    String restArea = restaurant.getArea();


                    for (String s : Global.area) {
                        if (restArea.toLowerCase().contains(s.toLowerCase())) {
                            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        filteredRestaurants.add(restaurant);

                    }
                }



            }*/

            if(filteredRestaurants.size()<3 || ThatsAll==0)
            {
                getRestaurants(restaurants.size());
            }



        }

    }
    private void startLogoutProcess() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LoginState", "0");
        editor.apply();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out? Your will lose your local info.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proceedToLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void proceedToLogout() {

        logoutDialog = new ProgressDialog(this);
        logoutDialog.setMessage("Logging you out...");
        logoutDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        logoutDialog.setIndeterminate(true);
        logoutDialog.show();


                logoutGoogle();

    }

    private void logoutGoogle() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();
    }
   /* @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press back again to exit Waverr", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void getVersion(){
        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {
                if(array!=null) {
                    try {
                        JSONObject object = array.getJSONObject(0);
                        int latestVersion = object.getInt("version");
                        if(VERSION_CODE < latestVersion) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("A new version of Waverr is available on the Play Store. Update?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.project.waverr")));
                                    } catch (ActivityNotFoundException e) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.project.waverr"));
                                        startActivity(intent);
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(false);
                            builder.create().show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute(new String[]{
                "http://waverr.in/beacon/getversion.php"
        });
    }
}
