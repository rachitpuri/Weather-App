package com.example.weatherapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

public class WeatherActivity extends Activity {
	
	private Menu optionsMenu;
	public static final String ALERT = "City";
	CityPreference cityPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		SharedPreferences settings = getSharedPreferences(ALERT, 0);
		boolean dialogShown = settings.getBoolean("dialogShown", false);
		
		if(!dialogShown){
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
			        WeatherActivity.this);
	
			// Setting Dialog Title
			alertDialog2.setTitle("Enter Your City");
	
			final EditText input = new EditText(this);
			input.setHint("New York");
			alertDialog2.setView(input);
			
			alertDialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					  String value = input.getText().toString();					
					  changeCity(value);
					  }
					});
	
			// Showing Alert Dialog
			alertDialog2.show();
			SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean("dialogShown", true);
		    editor.commit();    
		}
		
		if (savedInstanceState == null) {
	        getFragmentManager().beginTransaction()
	                .add(R.id.container, new WeatherFragment())
	                .commit();
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather, menu);

        SearchManager searchManager = (SearchManager)
                                getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.
                                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
        {
            public boolean onQueryTextChange(String newText) 
            {
                return true;
            }

            public boolean onQueryTextSubmit(String query) 
            {
            	changeCity(query);
            	searchView.clearFocus();
            	menu.findItem(R.id.search).collapseActionView();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

	 
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if(item.getItemId() == R.id.menuRefresh){
    		 setRefreshActionButtonState(true);
    		 cityPref = new CityPreference(WeatherActivity.this);
    		 String currentCity = cityPref.getCity();
    		 changeCity(currentCity);
    		 setRefreshActionButtonState(false);
    		 return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu
	            .findItem(R.id.menuRefresh);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.actionbar_progress);
	            } else {
	                refreshItem.setActionView(null);
	            }
	        }
	    }
	}
		
    
    public void changeCity(String city){
    	WeatherFragment wf = (WeatherFragment)getFragmentManager()
    							.findFragmentById(R.id.container);
    	wf.changeCity(city);
    	CityPreference cityPref = new CityPreference(this);
    	cityPref.setCity(city);
    }
}
