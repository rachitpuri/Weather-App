package com.example.weatherapp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WeatherFragment extends Fragment {

	private final String TAG = "WeatherApp";
	
	TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    ImageView weatherIcon;
    
    //forecast textviews day
    TextView day1 ,day2, day3, day4, day5, day6, day7;
    
    // forecast textviews temperature
    TextView mintempDay1, mintempDay2, mintempDay3, mintempDay4, mintempDay5,
    		 mintempDay6, mintempDay7;
   
    TextView maxtempDay1, maxtempDay2, maxtempDay3, maxtempDay4, maxtempDay5,
             maxtempDay6, maxtempDay7;
    
    //forecast imageview weather
    ImageView weatherDay1, weatherDay2, weatherDay3, weatherDay4, weatherDay5,
              weatherDay6, weatherDay7;
    
    Handler handler;
    
    public WeatherFragment(){   
        handler = new Handler();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (ImageView)rootView.findViewById(R.id.weather_icon);
        
        day1 = (TextView)rootView.findViewById(R.id.day1);
        day2 = (TextView)rootView.findViewById(R.id.day2);
        day3 = (TextView)rootView.findViewById(R.id.day3);
        day4 = (TextView)rootView.findViewById(R.id.day4);
        day5 = (TextView)rootView.findViewById(R.id.day5);
        day6 = (TextView)rootView.findViewById(R.id.day6);
        day7 = (TextView)rootView.findViewById(R.id.day7);
        
        mintempDay1 = (TextView)rootView.findViewById(R.id.min_day1);
        mintempDay2 = (TextView)rootView.findViewById(R.id.min_day2);
        mintempDay3 = (TextView)rootView.findViewById(R.id.min_day3);
        mintempDay4 = (TextView)rootView.findViewById(R.id.min_day4);
        mintempDay5 = (TextView)rootView.findViewById(R.id.min_day5);
        mintempDay6 = (TextView)rootView.findViewById(R.id.min_day6);
        mintempDay7 = (TextView)rootView.findViewById(R.id.min_day7);
        
        maxtempDay1 = (TextView)rootView.findViewById(R.id.max_day1);
        maxtempDay2 = (TextView)rootView.findViewById(R.id.max_day2);
        maxtempDay3 = (TextView)rootView.findViewById(R.id.max_day3);
        maxtempDay4 = (TextView)rootView.findViewById(R.id.max_day4);
        maxtempDay5 = (TextView)rootView.findViewById(R.id.max_day5);
        maxtempDay6 = (TextView)rootView.findViewById(R.id.max_day6);
        maxtempDay7 = (TextView)rootView.findViewById(R.id.max_day7);
        
        weatherDay1 = (ImageView)rootView.findViewById(R.id.image_day1);
        weatherDay2 = (ImageView)rootView.findViewById(R.id.image_day2);
        weatherDay3 = (ImageView)rootView.findViewById(R.id.image_day3);
        weatherDay4 = (ImageView)rootView.findViewById(R.id.image_day4);
        weatherDay5 = (ImageView)rootView.findViewById(R.id.image_day5);
        weatherDay6 = (ImageView)rootView.findViewById(R.id.image_day6);
        weatherDay7 = (ImageView)rootView.findViewById(R.id.image_day7);
       
        return rootView; 
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);  
    	updateWeather(new CityPreference(getActivity()).getCity());
    }
    
    private void updateWeather(final String city){
    	new Thread(){
    		public void run(){
    			final JSONObject json = FetchWeather.getJSON(getActivity(), city);
    			final JSONObject json_forecast = FetchWeather.getJSONForecast(getActivity(), city);
    			if(json == null || json_forecast == null){
    				handler.post(new Runnable(){
    					public void run(){
    						Log.i(TAG,"City not found");
    						Toast.makeText(getActivity(), 
    								getActivity().getString(R.string.place_not_found), 
    								Toast.LENGTH_LONG).show(); 
    					}
    				});
    			} else {
    				handler.post(new Runnable(){
    					public void run(){
    						renderWeather(json, json_forecast);
    					}
    				});
    			}    			
    		}
    	}.start();
    }
 
    
    private void renderWeather(JSONObject json, JSONObject json_forecast){
    	try {
	    	cityField.setText(json.getString("name").toUpperCase(Locale.US) + 
	    			", " + 
	    			json.getJSONObject("sys").getString("country"));
	    	
	    	JSONObject details = json.getJSONArray("weather").getJSONObject(0);
	    	JSONObject main = json.getJSONObject("main");
	    	JSONObject wind = json.getJSONObject("wind");
	    	// forecast weather
	    	JSONObject temp_MinMax = null;
	    	int[] image_ids = new int[7];
	    	String[] min_temp = new String[7];
	    	String[] max_temp = new String[7];
	    	
	    	for(int i=0; i<7; i++){
		    	temp_MinMax = json_forecast.getJSONArray("list").getJSONObject(i).getJSONObject("temp");
		    	min_temp[i] = String.format("%d", (temp_MinMax.getInt("min")));
		    	max_temp[i] = String.format("%d", (temp_MinMax.getInt("max")));
		    	image_ids[i] = json_forecast.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
		    }
	    	
	    	mintempDay1.setText(min_temp[0]);
	    	mintempDay2.setText(min_temp[1]);
	    	mintempDay3.setText(min_temp[2]);
	    	mintempDay4.setText(min_temp[3]);
	    	mintempDay5.setText(min_temp[4]);
	    	mintempDay6.setText(min_temp[5]);
	    	mintempDay7.setText(min_temp[6]);
	    	
	    	maxtempDay1.setText(max_temp[0]);
	    	maxtempDay2.setText(max_temp[1]);
	    	maxtempDay3.setText(max_temp[2]);
	    	maxtempDay4.setText(max_temp[3]);
	    	maxtempDay5.setText(max_temp[4]);
	    	maxtempDay6.setText(max_temp[5]);
	    	maxtempDay7.setText(max_temp[6]);
	    	
	    	setWeatherIconForecast(image_ids[0], weatherDay1);
	    	setWeatherIconForecast(image_ids[1], weatherDay2);
	    	setWeatherIconForecast(image_ids[2], weatherDay3);
	    	setWeatherIconForecast(image_ids[3], weatherDay4);
	    	setWeatherIconForecast(image_ids[4], weatherDay5);
	    	setWeatherIconForecast(image_ids[5], weatherDay6);
	    	setWeatherIconForecast(image_ids[6], weatherDay7);
	    	
	    	detailsField.setText(
	    			"Humidity: " + main.getString("humidity") + "%" +
	    			"\n" + "Pressure: " + main.getString("pressure") + " hPa" +
	    			"\n" + "Wind Speed: " + wind.get("speed") +
	    			"\n" + "Wind deg: " + wind.get("deg"));
	    	
	    	currentTemperatureField.setText(
	    			details.getString("description").toUpperCase(Locale.US) +
	    			"\n" + "Temp: " + String.format("%.2f", main.getDouble("temp"))+ " ℃");
	    			
	    	DateFormat df = DateFormat.getDateTimeInstance();
	    	String updatedOn = df.format(new Date(json.getLong("dt")*1000));
	    	updatedField.setText("Last update: " + updatedOn);

	    	setWeatherIcon(details.getInt("id"),
	    			json.getJSONObject("sys").getLong("sunrise") * 1000,
	    			json.getJSONObject("sys").getLong("sunset") * 1000);
	    	
    	}catch(Exception e){
    		Log.e(TAG, "Some JSON data not found");
    	}
    }
    
    private String getNextDay(int day)
    {
    	String str = "";
    	if(day > 7)
    		day = day - 7;
    	
    	switch(day) {
	    	case 1 : str = "Sun";
	    			 break;     	
	    	case 2 : str = "Mon";
	    			 break;    	
	    	case 3 : str = "Tue";
	    			 break;
	    	case 4 : str = "Wed";
	    			 break;
	    	case 5 : str = "Thu";
	    			 break;
	    	case 6 : str = "Fri";
	    			 break;
	    	case 7 : str = "Sat";
			 		 break;
	    	}
    	return str;
    }
    
    private void setWeatherIconForecast(int actualId, ImageView image){
    	
    	Calendar cal = Calendar.getInstance();
    	int day = cal.get(Calendar.DAY_OF_WEEK);

    	day1.setText(getNextDay(day+1));
    	day2.setText(getNextDay(day+2));
    	day3.setText(getNextDay(day+3));
    	day4.setText(getNextDay(day+4));
    	day5.setText(getNextDay(day+5));
    	day6.setText(getNextDay(day+6));
    	day7.setText(getNextDay(day+7));
    	
    	int id = actualId / 100;
    	if(actualId == 800)
    		image.setImageResource(R.drawable.weather_sunny);
    	else
    	{
    		switch(id) {
	    	case 2 : image.setImageResource(R.drawable.weather_thunder); 
	    			 break;     	
	    	case 3 : image.setImageResource(R.drawable.weather_drizzle);
	    			 break;    	
	    	case 7 : image.setImageResource(R.drawable.weather_foggy);
	    			 break;
	    	case 8 : image.setImageResource(R.drawable.weather_cloudy);
	    			 break;
	    	case 6 : image.setImageResource(R.drawable.weather_snowy);
	    			 break;
	    	case 5 : image.setImageResource(R.drawable.weather_rainy);
	    			 break;
	    	default : image.setImageResource(R.drawable.weather_sunny);
	    			 break;
	    	}
    	}
    }
    
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
    	int id = actualId / 100;
    	if(actualId == 800){
    		long currentTime = new Date().getTime();
    		if(currentTime>=sunrise && currentTime<sunset) {
    			weatherIcon.setImageResource(R.drawable.weather_sunny);
    		} else {
    			weatherIcon.setImageResource(R.drawable.weather_clear_night);
    		}
    	} else {
	    	switch(id) {
	    	case 2 : weatherIcon.setImageResource(R.drawable.weather_thunder); 
	    			 break;     	
	    	case 3 : weatherIcon.setImageResource(R.drawable.weather_drizzle);
	    			 break;    	
	    	case 7 : weatherIcon.setImageResource(R.drawable.weather_foggy);
	    			 break;
	    	case 8 : weatherIcon.setImageResource(R.drawable.weather_cloudy);
	    			 break;
	    	case 6 : weatherIcon.setImageResource(R.drawable.weather_snowy);
	    			 break;
	    	case 5 : weatherIcon.setImageResource(R.drawable.weather_rainy);
	    			 break;
	    	default : weatherIcon.setImageResource(R.drawable.weather_sunny);
	    			 break;
	    	}
    	}
    }
    
    public void changeCity(String city){
    	updateWeather(city);
    }
    
}
