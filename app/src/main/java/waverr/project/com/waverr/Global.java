package waverr.project.com.waverr;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 9/13/2015.
 */
class Global extends Application {

    public static final int LOGIN = 0;
    public static String Cuisine="";
    public static String Area="";
    public static String Costfortwo="";
    public static int filterClickedFlag=0;
    public static int fromFilter=0;
    public static String Name="";
    public static String Email="";
    public static String PicURL="";
    public static List<String> cuisines= new ArrayList<String>();
    public static List<String> area = new ArrayList<String>();
    public static List<String> cost = new ArrayList<>();
    private int loginStatus;

    public static void sendAnalyticsEventHit(Context context, int categoryRes, int actionRes) {
        Tracker tracker = ((AnalyticsBase)context.getApplicationContext()).getDefaultTracker();
        Resources resources = context.getResources();

        tracker.send(new HitBuilders.EventBuilder(resources.getString(categoryRes), resources.getString(actionRes)).build());
    }

    public static void sendAnalyticsEventHit(Context context, int categoryRes, int actionRes, String extraData) {
        Tracker tracker = ((AnalyticsBase)context.getApplicationContext()).getDefaultTracker();
        Resources resources = context.getResources();
        tracker.send(new HitBuilders.EventBuilder(resources.getString(categoryRes), resources.getString(actionRes)+":"+extraData).build());
    }



    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}