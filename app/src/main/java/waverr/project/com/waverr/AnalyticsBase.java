package waverr.project.com.waverr;/*

 */

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsBase extends Application {
    private Tracker mTracker;

    /**
     * Gets the default {@link com.google.android.gms.analytics.Tracker} for this {@link android.app.Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            //analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

            mTracker = analytics.newTracker(R.xml.ga_tracker_config);
        }
        return mTracker;
    }
}
