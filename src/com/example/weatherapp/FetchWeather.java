package com.example.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class FetchWeather {
	
	final static String TAG = "WeatherApp";
	
	private static final String OPEN_WEATHER_MAP_API = 
			"http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
	
	private static final String OPEN_WEATHER_FORECAST_API =
			 "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&units=metric&cnt=7";
	
	private static final String KEY = "45763b4c62b9a6cc2baad0a3214629bc";
	
	public static JSONObject getJSON(Context context, String city){
		try{
			Log.i(TAG, "getJSONCurrent");
			URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
			connection.addRequestProperty(KEY, 
					context.getString(R.string.open_weather_maps_app_id));
			
			BufferedReader reader = new BufferedReader
					(new InputStreamReader(connection.getInputStream()));
			
			StringBuffer json = new StringBuffer(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();
			
			JSONObject data = new JSONObject(json.toString());
			
			// check if request is successful
			if(data.getInt("cod") != 200)
				return null;
			Log.i(TAG,data.toString());
			return data;
		}catch(Exception e){
			return null;
		}
	}
	
	public static JSONObject getJSONForecast(Context context, String city){
		try{
			Log.i(TAG, "getJSONForecast");
			URL url = new URL(String.format(OPEN_WEATHER_FORECAST_API, city));
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.addRequestProperty(KEY,
					context.getString(R.string.open_weather_maps_app_id));
			
			BufferedReader reader = new BufferedReader
					(new InputStreamReader(connection.getInputStream()));
			
			StringBuffer json = new StringBuffer(2048);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();
			
			JSONObject data = new JSONObject(json.toString());
			Log.i(TAG,data.toString());
			// check if request is successful
			if(data.getInt("cod") != 200)
				return null;
			
			return data;
			
			
		}catch(Exception e){
			return null;
		}
	}
}
