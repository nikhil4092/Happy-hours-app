package waverr.project.com.waverr;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import waverr.project.com.waverr.R;
import org.json.JSONArray;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String REGISTRATION_COMPLETE = "Registration complete";
   // private GlobalData mGlobalData;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

      //  mGlobalData = GlobalData.inflateGlobalData(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                String authorizedEntity = "405303744393"; // Project id from Google Developers Console
                String scope = "GCM"; // e.g. communicating using GCM, but you can use any
                // URL-safe characters up to a maximum of 1000, or
                // you can also leave it blank.
                String token = InstanceID.getInstance(this).getToken(authorizedEntity,scope);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.

                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            /*mGlobalData.setSentTokenToServer(false);
            GlobalData.writeGlobalData(mGlobalData, this);*/
            Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        String[] url = {
                "http://waverr.in/beacon/registertoken.php",
                "regid", token,
                "shareregid", "true",
                "emailid", Global.Email
        };
        new JSONObtainer() {
            @Override
            protected void onPostExecute(JSONArray array) {
                Context context = RegistrationIntentService.this;

                Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
                LocalBroadcastManager.getInstance(context).sendBroadcast(registrationComplete);
            }
        }.execute(url);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws java.io.IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}