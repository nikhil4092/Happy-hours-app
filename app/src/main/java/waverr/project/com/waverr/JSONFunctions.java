package waverr.project.com.waverr;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class JSONFunctions {

    public static JSONArray getJSONfromURL(String[] url) throws UnknownHostException {
        InputStream is = null;
        String result = "";
        JSONArray jArray = null;
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url[0]);
            int i;
            nameValuePairs.add(new BasicNameValuePair("dbname", "wavermmi_MAINDB"));
            nameValuePairs.add(new BasicNameValuePair("dbuser", "wavermmi_WAVADM"));
            nameValuePairs.add(new BasicNameValuePair("dbpass", "J1EnM4CUw8zS"));
            for (i = 1; i + 1 < url.length; i++)
                nameValuePairs.add(new BasicNameValuePair(url[i], url[i + 1]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (UnsupportedEncodingException e) {
            Log.e("log_tag", "Wrong encoding: " + e.toString());
        } catch (ClientProtocolException e) {
            Log.e("log_tag", "Some shizz: " + e.toString());
        } catch (IOException e) {
            if (e.getClass() == UnknownHostException.class)
                throw (UnknownHostException) e;
            else
                Log.e("Error", e.getMessage());
        }
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        //parse json data
        try {
            jArray = new JSONArray(result);
        } catch (JSONException e) {
            if(!e.toString().startsWith("org.json.JSONException: Value null of type")) {
                Log.e("log_tag", "Error parsing data " + e.toString());
                throw new UnknownHostException();
            }
        }
        return jArray;
    }
}