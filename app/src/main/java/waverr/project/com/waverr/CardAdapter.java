package waverr.project.com.waverr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikhil on 8/11/2015.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements View.OnClickListener {

    List<Restaurant> restaurants;
    Context context;


    public CardAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Restaurant c = restaurants.get(i);

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

        Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        String []cui=c.getCuisine().split("[,]");
        if(cui.length>2)
        {viewHolder.CuisineText.setText(cui[0]+","+cui[1]+"...");}
        else if(cui.length==2)
        {viewHolder.CuisineText.setText(cui[0]+","+cui[1]);}
        else
        {viewHolder.CuisineText.setText(cui[0]);}
        viewHolder.Name.setTypeface(face);
        viewHolder.Name.setText(c.getName());
        viewHolder.Area.setTypeface(face);
        viewHolder.Area.setText(c.getArea());
        viewHolder.HappyHours.setTypeface(face);

        viewHolder.CostfortwoText.setTypeface(face);
        viewHolder.CuisineText.setTypeface(face);
        viewHolder.TypeText.setTypeface(face);
        viewHolder.TypeText.setText(c.getType());
        Picasso.with(context)
                .load(location)
                .into(viewHolder.Type);
        viewHolder.CostfortwoText.setTypeface(face);
        if(c.getDeal1().equals("")&&c.getDeal2().equals("")&&c.getDeal3().equals("")&&c.getDeal4().equals("")&&c.getDeal5().equals(""))
        {viewHolder.Deals.setVisibility(View.INVISIBLE);}
        else
        {
            viewHolder.Deals.setVisibility(View.VISIBLE);
        }
        viewHolder.CostfortwoText.setText(c.getCostfortwo()+" for two");

        Calendar sCalendar = Calendar.getInstance();
        int dayLongName = sCalendar.get(Calendar.DAY_OF_WEEK);
        Pair<String, String> day1 = new Pair<>("Monday",c.getMondayHH() );
        Pair<String, String> day2 = new Pair<>("Tuesday",c.getTuesdayHH() );
        Pair<String, String> day3 = new Pair<>("Wednesday",c.getWednesdayHH() );
        Pair<String, String> day4 = new Pair<>("Thursday",c.getThursdayHH() );
        Pair<String, String> day5 = new Pair<>("Friday",c.getFridayHH() );
        Pair<String, String> day6 = new Pair<>("Saturday",c.getSaturdayHH() );
        Pair<String, String> day7 = new Pair<>("Sunday",c.getSundayHH() );

        if(dayLongName==(Calendar.MONDAY))
        {
            if(day1.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            else
            viewHolder.HappyHours.setText("Happy hours from "+day1.second);
        }
        else if(dayLongName==(Calendar.TUESDAY))
        {
            if(day2.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day2.second);}
        else if(dayLongName==(Calendar.WEDNESDAY))
        {
            if(day3.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day3.second);}
        else if(dayLongName==(Calendar.THURSDAY))
        {
            if(day4.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day4.second);}
        else if(dayLongName==(Calendar.FRIDAY))
        {
            if(day5.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day5.second);}
        else if(dayLongName==(Calendar.SATURDAY))
        {
            if(day6.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day6.second);}
        else if(dayLongName==(Calendar.SUNDAY))
        {
            if(day7.second.equals(""))
            {
                viewHolder.HappyHours.setVisibility(View.GONE);
                viewHolder.clock.setVisibility(View.GONE);
            }
            viewHolder.HappyHours.setText("Happy hours from "+day7.second);}

        viewHolder.RestaurantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(context,R.string.ga_cat_button_click,R.string.ga_act_restaurant_card, "ID:"+c.getRestaurantID()+" Name:"+c.getName());
                Global.filterClickedFlag=0;
                Intent i = new Intent(context,InRestaurant.class);
                Gson gson = new Gson();
                i.putExtra("Restaurant",gson.toJson(c));
                context.startActivity(i);
            }


        });
        viewHolder.Directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sendAnalyticsEventHit(context,R.string.ga_cat_button_click,R.string.ga_act_get_directions_card);
                String label = c.getName();
                String uriBegin = "geo:"+c.getGPS();
                String query = c.getGPS()+"(" + label + ")";
                String encodedQuery = Uri.encode( query );
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse( uriString );
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri );

                context.startActivity(intent);
            }


        });

    }



    @Override
    public int getItemCount() {
        return restaurants == null ? 0 : restaurants.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView Name;
        public TextView Area;
        public ImageView Deals;
        public ImageView Directions;
        public ImageView Type;
        public ImageView Costfortwo;
        public ImageView Cuisine;
        public TextView TypeText;
        public TextView CostfortwoText;
        public TextView CuisineText;
        public TextView HappyHours;
        public CardView RestaurantCard;
        public ImageView clock;
        public ViewHolder(View itemView) {
            super(itemView);

            Name = (TextView)itemView.findViewById(R.id.RestaurantName);
            Area = (TextView)itemView.findViewById(R.id.RestaurantArea);
            Deals = (ImageView)itemView.findViewById(R.id.RestaurantDeals);
            Directions = (ImageView)itemView.findViewById(R.id.RestaurantDirections);
            Type = (ImageView)itemView.findViewById(R.id.RestaurantType);
            Costfortwo = (ImageView)itemView.findViewById(R.id.RestaurantCostfortwo);
            Cuisine = (ImageView)itemView.findViewById(R.id.RestaurantCuisine);
            TypeText = (TextView)itemView.findViewById(R.id.RestaurantTypeText);
            CostfortwoText = (TextView)itemView.findViewById(R.id.RestaurantCostfortwoText);
            CuisineText = (TextView)itemView.findViewById(R.id.RestaurantCuisineText);
            HappyHours = (TextView)itemView.findViewById(R.id.RestaurantHappyHours);
            RestaurantCard = (CardView)itemView.findViewById(R.id.RestaurantCard);
            clock = (ImageView)itemView.findViewById(R.id.clock);

        }
    }
}
