package waverr.project.com.waverr;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clive on 23-May-14.
 * * www.101apps.co.za
 */
public class FirstFragment extends Fragment{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextView name,area,TypeText,CostForTwoText,WifiText,SmokingText,CardText,ACText,FullAddress,Cuisines,Timings,HappyHours,HappyHoursHeading,AddressHeading,TimingHeading;
    ImageView Type,GetDirection,Call,Share;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragament_one, container, false);
        Gson gson = new Gson();
        String extras = getActivity().getIntent().getExtras().getString("Restaurant");
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        final Restaurant c = gson.fromJson(extras,Restaurant.class);
        name=(TextView)view.findViewById(R.id.inRestaurantName);
        area=(TextView)view.findViewById(R.id.inRestaurantArea);
        TypeText=(TextView)view.findViewById(R.id.inRestaurantTypeText);
        CostForTwoText=(TextView)view.findViewById(R.id.inRestaurantCostForTwoText);
        WifiText=(TextView)view.findViewById(R.id.inRestaurantWifiText);
        SmokingText=(TextView)view.findViewById(R.id.inRestaurantSmokingText);
        CardText=(TextView)view.findViewById(R.id.inRestaurantCardText);
        ACText=(TextView)view.findViewById(R.id.inRestaurantACText);
        FullAddress=(TextView)view.findViewById(R.id.inRestaurantFullAddress);
        Cuisines=(TextView)view.findViewById(R.id.inRestaurantCuisineText);
        Timings=(TextView)view.findViewById(R.id.inRestaurantTimings);
        HappyHours=(TextView)view.findViewById(R.id.inRestaurantHappyHours);
        HappyHoursHeading=(TextView)view.findViewById(R.id.inRestaurantHappyHoursHeading);
        AddressHeading=(TextView)view.findViewById(R.id.inRestaurantFullAddressHeading);
        TimingHeading=(TextView)view.findViewById(R.id.inRestaurantTimingHeading);
        ((AnalyticsBase)getActivity().getApplicationContext()).getDefaultTracker();
        name.setTypeface(face);
        area.setTypeface(face);
        TypeText.setTypeface(face);
        CostForTwoText.setTypeface(face);
        WifiText.setTypeface(face);
        SmokingText.setTypeface(face);
        CardText.setTypeface(face);
        ACText.setTypeface(face);
        FullAddress.setTypeface(face);
        Timings.setTypeface(face);
        Cuisines.setTypeface(face);
        HappyHours.setTypeface(face);
        AddressHeading.setTypeface(face);
        TimingHeading.setTypeface(face);
        HappyHoursHeading.setTypeface(face);

        Type=(ImageView)view.findViewById(R.id.inRestaurantTypeImage);
        GetDirection=(ImageView)view.findViewById(R.id.inRestaurantGetDirection);
        Call=(ImageView)view.findViewById(R.id.inRestaurantCall);
        Share=(ImageView)view.findViewById(R.id.inRestaurantShare);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(getActivity(),R.string.ga_cat_button_click,R.string.ga_act_share);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey :D. Your friend "+Global.Name+", thinks you should check out the happy hours at "+c.getName()+", "+c.getArea()+".\nDownload Waverr to find all the happy hours near you. http://www.waverr.in/getwaverr");
                startActivity(Intent.createChooser(intent, "Share with"));

            }
        });

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(getActivity(),R.string.ga_cat_button_click,R.string.ga_act_call);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + c.getRestaurantContact()));
                getActivity().startActivity(callIntent);

            }

            ;
        });
        GetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(getActivity(),R.string.ga_cat_button_click,R.string.ga_act_get_directions);
                String label = c.getName();
                String uriBegin = "geo:"+c.getGPS();
                String query = c.getGPS()+"(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse( uriString );
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri );

                getActivity().startActivity(intent);
            }


        });
        Cuisines.setText(c.getCuisine());
        Pair<String, Integer> simplePair1 = new Pair<>("Casual Dining", R.drawable.casualdining);
        Pair<String, Integer> simplePair2 = new Pair<>("Lounge", R.drawable.lounge);
        Pair<String, Integer> simplePair3 = new Pair<>("Pub/Bar", R.drawable.pub_bar);
        Pair<String, Integer> simplePair4 = new Pair<>("Cafe", R.drawable.cafe);
        Pair<String, Integer> simplePair5 = new Pair<>("Quick Serve", R.drawable.fastfood);
        Pair<String, Integer> simplePair6 = new Pair<>("Pub", R.drawable.pub_bar);
        Pair<String, Integer> simplePair7 = new Pair<>("Bar", R.drawable.pub_bar);
        Pair<String, Integer> simplePair8 = new Pair<>("Club", R.drawable.pub_bar);
        String []tem=c.getType().split("[,]");
        String type=tem[0];
        Integer location=R.drawable.dinein;
        if(type.equalsIgnoreCase(simplePair1.first))
            location=simplePair1.second;
        else if(type.equalsIgnoreCase(simplePair2.first))
            location=simplePair2.second;
        else if(type.equalsIgnoreCase(simplePair3.first))
            location=simplePair3.second;
        else if(type.equalsIgnoreCase(simplePair4.first))
            location=simplePair4.second;
        else if(type.equalsIgnoreCase(simplePair5.first))
            location=simplePair5.second;
        else if(type.equalsIgnoreCase(simplePair6.first))
            location=simplePair6.second;
        else if(type.equalsIgnoreCase(simplePair7.first))
            location=simplePair7.second;
        else if(type.equalsIgnoreCase(simplePair8.first))
            location=simplePair8.second;



        name.setText(c.getName());
        area.setText(c.getArea());
        Picasso.with(getActivity())
                .load(location)
                .into(Type);
        TypeText.setText(c.getType());
        CostForTwoText.setText(c.getCostfortwo()+" for two");
        String Tags = c.getOtherTags();
        if(containsIgnoreCase(Tags, "Cards Accepted"))
            CardText.setText("Cards accepted");
        else
            CardText.setText("Cards not accepted");
        if(containsIgnoreCase(Tags, "Smoking Allowed"))
            SmokingText.setText("Smoking allowed");
        else
            SmokingText.setText("Smoking not allowed");
        if(containsIgnoreCase(Tags, "Wi-Fi Available"))
            WifiText.setText("Wi-Fi available");
        else
            WifiText.setText("Wi-Fi not available");
        if(containsIgnoreCase(Tags, "AC"))
            ACText.setText("A/C");
        else
            ACText.setText("Non A/C");
        FullAddress.setText(c.getAddress());
        String[] datTime=c.getRestaurnatTimings().split("\n");
        String time="";
        for(int i =0;i<datTime.length;i++)
        {
            String[] spTime=datTime[i].split(" ",2);
            if(spTime.length>1)
            {
            if(i==0 || i==2 || i==3 || i==6)
            {time+=spTime[0]+"\t"+spTime[1]+"\n";}
            else
            {
             time+=spTime[0]+"\t\t"+spTime[1]+"\n";
            }
        }}
        Timings.setText(time);

        String HH="";
        if(c.getMondayHH().compareTo("")!=0)
        {
            HH+="Mon\t"+c.getMondayHH()+"\n";
        }
        if(c.getTuesdayHH().compareTo("")!=0)
        {
            HH+="Tue\t\t"+c.getTuesdayHH()+"\n";
        }
        if(c.getWednesdayHH().compareTo("")!=0)
        {
            HH+="Wed\t"+c.getWednesdayHH()+"\n";
        }
        if(c.getThursdayHH().compareTo("")!=0)
        {
            HH+="Thu\t"+c.getThursdayHH()+"\n";
        }

        if(c.getFridayHH().compareTo("")!=0)
        {
            HH+="Fri\t\t"+c.getFridayHH()+"\n";
        }
        if(c.getSaturdayHH().compareTo("")!=0)
        {
            HH+="Sat\t\t"+c.getSaturdayHH()+"\n";
        }
        if(c.getSundayHH().compareTo("")!=0)
        {
            HH+="Sun\t"+c.getSundayHH()+"\n";
        }
        if(c.getMondayHH().equalsIgnoreCase("")&&c.getTuesdayHH().equalsIgnoreCase("")&&c.getWednesdayHH().equalsIgnoreCase("")&&c.getThursdayHH().equalsIgnoreCase("")&&c.getFridayHH().equalsIgnoreCase("")&&c.getSaturdayHH().equalsIgnoreCase("")&&c.getSundayHH().equalsIgnoreCase(""))
        {
            HappyHoursHeading.setVisibility(View.GONE);
        }
        else
        {
            HappyHours.setText(HH);
        }
            return view;
    }
    public boolean containsIgnoreCase( String haystack, String needle ) {
        if(needle.equals(""))
            return true;
        if(haystack == null || needle == null || haystack .equals(""))
            return false;

        Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+ Pattern.LITERAL);
        Matcher m = p.matcher(haystack);
        return m.find();
    }

}
