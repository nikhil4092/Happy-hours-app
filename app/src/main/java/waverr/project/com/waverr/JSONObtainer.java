package waverr.project.com.waverr;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.net.UnknownHostException;

public class JSONObtainer extends AsyncTask<String[], Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(String[]... url) {
        // TODO Auto-generated method stub
        try {
            return JSONFunctions.getJSONfromURL(url[0]);
        } catch (UnknownHostException e) {
            Log.e("Error in obtainer", e.toString());
            return new JSONArray();
        }
    }

}
