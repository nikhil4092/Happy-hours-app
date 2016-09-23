package waverr.project.com.waverr;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by clive on 23-May-14.
 * * www.101apps.co.za
 */
public class SecondFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragament_two, container, false);
        Typeface face= Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        Gson gson = new Gson();
        String extras = getActivity().getIntent().getExtras().getString("Restaurant");
        final Restaurant c = gson.fromJson(extras,Restaurant.class);
        ((AnalyticsBase)getActivity().getApplicationContext()).getDefaultTracker();
        final ArrayList<String> values = new ArrayList<String>();
        int i=1;
        if(c.getDeal1().equalsIgnoreCase(""))
        {}
        else
        {
            values.add(i+". "+c.getDeal1());
            i++;
        }
        if(c.getDeal2().equalsIgnoreCase("")){}
        else
        {
            values.add(i+". "+c.getDeal2());
            i++;
        }
        if(c.getDeal3().equalsIgnoreCase("")){}
        else
        {
           values.add(i+". "+c.getDeal3());
            i++;
        }
        if(c.getDeal4().equalsIgnoreCase("")){}
        else
        {
            values.add(i+". "+c.getDeal4());
            i++;
        }
        if(c.getDeal5().equalsIgnoreCase("")){}
        else
        {
            values.add(i+". "+c.getDeal5());
            i++;
        }
        if(values.size()==0)
        {
            values.add("No Deals Currently");
        }
        ViewGroup dealContainer = (ViewGroup) view.findViewById(R.id.deals);
        TextView rest = new TextView(view.getContext());
        rest.setText(c.getName());
        rest.setTypeface(face);
        rest.setTextSize(20);
        rest.setPadding(40,40,10,2);
        rest.setTextColor(Color.parseColor("#00aeef"));
        dealContainer.addView(rest);
        for(int j=0;j<values.size();j++)
        {

            TextView deal = new TextView(view.getContext());
            deal.setText(values.get(j));
            deal.setTypeface(face);
            deal.setTextSize(15);
            deal.setPadding(40,10,10,30);
            dealContainer.addView(deal);
        }


        return view;
    }
}
