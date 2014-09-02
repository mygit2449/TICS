	package com.meldit.tca;
	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
	
	public class Main extends Activity {
	    /** Called when the activity is first created. */
	public ArrayAdapter<String> adapter;
	public AutoCompleteTextView autoComplete1,autocomplete2;
	String value;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item);
	        autoComplete1 = (AutoCompleteTextView)
	                findViewById(R.id.autoCompleteTextView1);
	        autoComplete1.setOnItemClickListener(listener_autocomplete);
	        adapter.setNotifyOnChange(true);
	        autoComplete1.setAdapter(adapter);
	
	        autoComplete1.addTextChangedListener(new TextWatcher() {
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	if (count%3 == 1) {
	adapter.clear();
	//now pass the argument in the textview to the task
	Log.i("GetPlaces", "============================= char "+autoComplete1.getText().toString());
	GetPlaces task = new GetPlaces();
	value = autoComplete1.getText().toString();
	task.execute();
	}
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count,
	int after) {
	// TODO Auto-generated method stub
	
	}
	
	public void afterTextChanged(Editable s) {
	
	}
	});
	}
	
	
	OnItemClickListener listener_autocomplete = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View parent1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			String selection = (String) parent.getItemAtPosition(position);
			//getLatLong(selection);
		}

		
	};
	
	private void getLatLong(String selection) {
		// TODO Auto-generated method stub
		try{
		URL googlePlaces = new URL(
                "http://maps.googleapis.com/maps/api/geocode/json?address="+ 
        		URLEncoder.encode(selection, "UTF-8") +
        		"&sensor=true");
        URLConnection tc = googlePlaces.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                tc.getInputStream()));

        String line;
        StringBuffer sb = new StringBuffer();
        //take Google's legible JSON and turn it into one big string.
        while ((line = in.readLine()) != null) {
        sb.append(line);
        }
                        //turn that string into a JSON object
        JSONObject predictions = new JSONObject(sb.toString());	
                       //now get the JSON array that's inside that object            
        JSONArray ja = new JSONArray(predictions.getString("results"));
        JSONObject jo = (JSONObject) ja.get(0);
        String s = jo.getString("location");
		Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();

            /*for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                                //add each entry to our array
                predictionsArr.add(jo.getString("formatted_address"));
            }*/
} catch (IOException e)
{

Log.i("YourApp", "GetPlaces : doInBackground", e);

} catch (JSONException e)
{

Log.i("YourApp", "GetPlaces : doInBackground", e);

}
	}
	
	ArrayList<String> predictionsArr = new ArrayList<String>(); 
	String latlong;
	class GetPlaces extends AsyncTask<Void, Void,Void>
	{
	
	@Override
	               // three dots is java for an array of strings
	protected Void doInBackground(Void... arg0)
	{
	Log.i("gottaGo", "=============================args.toString() "+value.toString());
	try
	{
	
	            URL googlePlaces = new URL(
	                    "http://maps.googleapis.com/maps/api/geocode/json?address="+ 
	            		URLEncoder.encode(value.toString(), "UTF-8") +
	            		"&sensor=true");
	            URLConnection tc = googlePlaces.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    tc.getInputStream()));
	
	            String line;
	            StringBuffer sb = new StringBuffer();
	            //take Google's legible JSON and turn it into one big string.
	            while ((line = in.readLine()) != null) {
	            sb.append(line);
	            }
	                            //turn that string into a JSON object
	            JSONObject predictions = new JSONObject(sb.toString());	
	                           //now get the JSON array that's inside that object            
	            JSONArray ja = new JSONArray(predictions.getString("results"));
	
	                for (int i = 0; i < ja.length(); i++) {
	                    JSONObject jo = (JSONObject) ja.get(i);
	                                    //add each entry to our array
	                    predictionsArr.add(jo.getString("formatted_address"));
	                    JSONObject geoJson=jo.getJSONObject("geometry");
	                    JSONObject locJson=geoJson.getJSONObject("location");
	                   double lat =  Double.parseDouble(locJson.getString("lat"));
	                   double lng = Double.parseDouble(locJson.getString("lng"));
	                   Log.i("doInBackground", "==========lat "+lat+"=============lng "+lng);
	                   
	                    
	                }
	} catch (IOException e)
	{
	
	Log.i("YourApp", "GetPlaces : doInBackground", e);
	
	} catch (JSONException e)
	{
	
	Log.i("YourApp", "GetPlaces : doInBackground", e);
	
	}
	return null;
	
	//return predictionsArr;
	
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
	
	Log.i("YourApp", "onPostExecute : " + predictionsArr.size());
	//update the adapter
	adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item);
	adapter.setNotifyOnChange(true);
	//attach the adapter to textview
	autoComplete1.setAdapter(adapter);
	
	for (String string : predictionsArr)
	{
	
	Log.i("YourApp", "onPostExecute : result = " + string);
	adapter.add(string);
	adapter.notifyDataSetChanged();
	
	}
	
	Log.i("YourApp", "onPostExecute : =========================" + latlong);
	
	}
	}
	}
	
	
