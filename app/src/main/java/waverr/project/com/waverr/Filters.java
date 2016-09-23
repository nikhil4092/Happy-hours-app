package waverr.project.com.waverr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Filters extends ActionBarActivity implements View.OnClickListener {

    Button setFilter,resetFilter;
    int checkboxes=0,areacheckboxes=0,costcheckboxes=0;
    String[] cuisines,area,costfortwo={"Less than Rs.500","Rs.500 - Rs.1000","Rs.1000 - Rs.1500","Rs.1500 - Rs.2000","More than Rs.2000"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");


        cuisines = loadArray("cuisines",Filters.this);
        area = loadArray("area",Filters.this);

        ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkbox_container);

        setFilter=(Button)findViewById(R.id.set_filter);
        resetFilter=(Button)findViewById(R.id.reset_Filter);
       /* Spinner filter = new Spinner(this);
        filter.setId(400);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cuisines);
        filter.setAdapter(adapter);
        checkboxContainer.addView(filter);*/


        TextView Cuisine = new TextView(this);
        Cuisine.setText("Cuisine Tags");
        Cuisine.setTypeface(face);
        Cuisine.setTextSize(25);
        Cuisine.setPadding(0,0,0,20);
        checkboxContainer.addView(Cuisine);
        int count=0;
        for (int i = 0; i < cuisines.length; i+=3) {
            LinearLayout hor = new LinearLayout(this);
            hor.setOrientation(LinearLayout.HORIZONTAL);
            hor.setWeightSum(3.0f);
            if(count<cuisines.length) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(cuisines[count]);
                checkBox.setTypeface(face);
                checkBox.setId(count);
                checkBox.setPadding(0,20,0,20);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                count++;
                checkboxes++;
                hor.addView(checkBox);
            }
            if(count<cuisines.length){
                CheckBox checkBox2 = new CheckBox(this);
                checkBox2.setText(cuisines[count]);
                checkBox2.setTypeface(face);
                checkBox2.setId(count);
                checkBox2.setPadding(0,20,0,20);
                checkBox2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                count++;
                checkboxes++;
                hor.addView(checkBox2);
            }
            if(count<cuisines.length){
                CheckBox checkBox2 = new CheckBox(this);
                checkBox2.setText(cuisines[count]);
                checkBox2.setTypeface(face);
                checkBox2.setId(count);
                checkBox2.setPadding(0,20,0,20);
                checkBox2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                count++;
                checkboxes++;
                hor.addView(checkBox2);
            }
            checkboxContainer.addView(hor);
        }

        TextView Area = new TextView(this);
        Area.setText("Restaurant area");
        Area.setTypeface(face);
        Area.setTextSize(25);
        Area.setPadding(0,50,0,20);
        checkboxContainer.addView(Area);
        count=0;
        for (int i = 0; i < area.length; i+=3) {
            LinearLayout hor = new LinearLayout(this);
            hor.setOrientation(LinearLayout.HORIZONTAL);
            hor.setWeightSum(3.0f);
            if(count<area.length) {
                CheckBox areacheckBox = new CheckBox(this);
                areacheckBox.setText(area[count]);
                areacheckBox.setTypeface(face);
                areacheckBox.setId(100 + count);
                areacheckBox.setPadding(0,20,0,20);
                areacheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                areacheckboxes++;
                count++;
                hor.addView(areacheckBox);
            }
            if(count<area.length) {
                CheckBox areacheckBox = new CheckBox(this);
                areacheckBox.setText(area[count]);
                areacheckBox.setTypeface(face);
                areacheckBox.setId(100 + count);
                areacheckBox.setPadding(0,20,0,20);
                areacheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                areacheckboxes++;
                count++;
                hor.addView(areacheckBox);
            }
            if(count<area.length) {
                CheckBox areacheckBox = new CheckBox(this);
                areacheckBox.setText(area[count]);
                areacheckBox.setTypeface(face);
                areacheckBox.setId(100 + count);
                areacheckBox.setPadding(0,20,0,20);
                areacheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                areacheckboxes++;
                count++;
                hor.addView(areacheckBox);
            }
            checkboxContainer.addView(hor);
        }

        TextView Costfortwo = new TextView(this);
        Costfortwo.setText("Cost for two");
        Costfortwo.setTypeface(face);
        Costfortwo.setTextSize(25);
        Costfortwo.setPadding(0,50,0,20);
        checkboxContainer.addView(Costfortwo);
        count=0;
        for (int i = 0; i < costfortwo.length; i+=2) {
            LinearLayout hor = new LinearLayout(this);
            hor.setOrientation(LinearLayout.HORIZONTAL);
            hor.setWeightSum(2.0f);
            if(count<costfortwo.length) {
                CheckBox costcheckBox = new CheckBox(this);
                costcheckBox.setText(costfortwo[count]);
                costcheckBox.setTypeface(face);
                costcheckBox.setId(200 + count);
                costcheckBox.setPadding(0,20,0,20);
                costcheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                costcheckboxes++;
                count++;
                hor.addView(costcheckBox);
            }
            if(count<costfortwo.length) {
                CheckBox costcheckBox = new CheckBox(this);
                costcheckBox.setText(costfortwo[count]);
                costcheckBox.setTypeface(face);
                costcheckBox.setId(200 + count);
                costcheckBox.setPadding(0,20,0,20);
                costcheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                costcheckboxes++;
                count++;
                hor.addView(costcheckBox);
            }
            checkboxContainer.addView(hor);
        }
        TextView spam = new TextView(this);
        spam.setText("");
        spam.setTypeface(face);
        spam.setTextSize(25);
        checkboxContainer.addView(spam);
        setFilter.setOnClickListener(this);
        resetFilter.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.set_filter)
        {
            Global.sendAnalyticsEventHit(Filters.this,R.string.ga_cat_button_click,R.string.ga_act_set_filter);
            Global.cuisines.clear();
            for(int i=0;i<checkboxes;i++)
            {

                CheckBox cb=(CheckBox)findViewById(i);
                if(cb.isChecked())
                {
                    Global.cuisines.add((String)cb.getText());
                }
            }
            Global.Cuisine=TextUtils.join(",", Global.cuisines);

            Global.area.clear();
            for(int i=0;i<areacheckboxes;i++)
            {

                CheckBox cb=(CheckBox)findViewById(100+i);
                if(cb.isChecked())
                {

                    Global.area.add((String)"'"+cb.getText()+"'");
                }
            }
            Global.Area=TextUtils.join(",", Global.area);

            Global.cost.clear();
            for(int i=0;i<costcheckboxes;i++)
            {

                CheckBox cb=(CheckBox)findViewById(200+i);
                if(cb.isChecked())
                {

                    Global.cost.add((String)"'"+cb.getText()+"'");
                }
            }
            Global.Costfortwo=TextUtils.join(",", Global.cost);


            Global.filterClickedFlag=1;
            finish();
            Global.fromFilter=1;

            if(Global.cuisines.size()==0 && Global.area.size()==0 && Global.cost.size()==0)
            {
                Global.filterClickedFlag=0;
            }

        }
        if(v.getId()==R.id.reset_Filter){
            Global.sendAnalyticsEventHit(Filters.this,R.string.ga_cat_button_click,R.string.ga_act_reset_filter);
            Global.Area="";
            Global.Costfortwo="";
            Global.Cuisine="";
            Global.filterClickedFlag=1;
            finish();
            Global.fromFilter=1;
        }

    }
    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

}
