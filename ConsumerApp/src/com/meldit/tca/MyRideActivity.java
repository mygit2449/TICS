package com.meldit.tca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.meldit.tca.db.DbAdapter;


public class MyRideActivity extends FragmentActivity {

	ImageView logo,user_icon;
	boolean flag;
	PopupWindow pw = null;
	MyExpandableListAdapter listAdapter;
	ListView mListView;
	ExpandableListView expListView;
	private GoogleMap mMap;
	CameraPosition CameraPosition,current_camera_position;
	LinearLayout ll_listview,ll_from_ride,ll_to_ride ;
	Button findmyride;
	FrameLayout frame,frame_map;
	RelativeLayout relative_scroll;
	ScrollView scrollview;
	String value = null;
	public ArrayAdapter<String> adapter,adapter2;
	AutoCompleteTextView autoText1,autoText2;
	int auto_seperate;
	View mapDisplay,mapFragment,route_info;
	Vector <Double> coordinates = new Vector<Double>();
	RelativeLayout popup,share,favorite,save,close;
	PopupWindow popupWindow;  
	int popup_position;
	String route_no,timinngs,date,fare;
	String [] source,destination;
	/*TextView info_source,info_destination,info_routeno,
			 info_walk_text,info_walk_time,info_timings,
			 info_busstop1,info_busstop1_text,info_busstop1_time,
			 info_busstop2,info_busstop2_text,info_busstop2_time;*/
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // remove title
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

	   setContentView(R.layout.myride);
	   	   
	   findmyride = (Button) findViewById(R.id.button_findmyride);
	   findmyride.setOnClickListener(listener_findmyride);
	   frame = (FrameLayout) findViewById(R.id.frame);
	   frame.setVisibility(View.INVISIBLE);
	   ll_from_ride = (LinearLayout) findViewById(R.id.ll_from_ride);
	   ll_to_ride = (LinearLayout) findViewById(R.id.ll_to_ride);
	   relative_scroll = (RelativeLayout) findViewById(R.id.relative_scroll);
	   relative_scroll.setVisibility(View.INVISIBLE);
	   //layout_routeinfo = (LinearLayout) findViewById(R.id.ll_rideinfo);
	   //layout_routeinfo.setVisibility(View.INVISIBLE);
	   scrollview = (ScrollView) findViewById(R.id.scrollview);
	   scrollview.setVisibility(View.INVISIBLE);
	   
	   mapDisplay  = getLayoutInflater().inflate(R.layout.mapdisplay, null);
	   frame_map   = (FrameLayout) mapDisplay.findViewById(R.id.frame_map);
	   mapFragment = (View) mapDisplay.findViewById(R.id.map);
	   
	  // route_info = getLayoutInflater().inflate(R.layout.routeinfo_view, null);
	  /* info_source       = (TextView) route_info.findViewById(R.id.info_source);
	   info_destination  = (TextView) route_info.findViewById(R.id.text_info_destination);
	   //info_routeno		 = (TextView) route_info.findViewById(R.id.text_info_routeno);
	   info_walk_text	 = (TextView) route_info.findViewById(R.id.info_source_walk);
	   info_walk_time	 = (TextView) route_info.findViewById(R.id.info_source_walk_time);
	   //info_timings		 = (TextView) route_info.findViewById(R.id.info_timings);
	   info_busstop1	 = (TextView) route_info.findViewById(R.id.info_busstop1);
	   info_busstop1_text= (TextView) route_info.findViewById(R.id.info_busstop1_text);
	   info_busstop1_time= (TextView) route_info.findViewById(R.id.info_busstop1_time);
	   info_busstop2	 = (TextView) route_info.findViewById(R.id.info_busstop2);
	   info_busstop2_text= (TextView) route_info.findViewById(R.id.info_busstop2_text);
	   info_busstop2_time= (TextView) route_info.findViewById(R.id.info_busstop2_time);*/
	   
	   adapter = new ArrayAdapter<String>(this,R.layout.list_item);
	   adapter2 = new ArrayAdapter<String>(this,R.layout.list_item);
	   adapter.setNotifyOnChange(true);
	   autoText1 = (AutoCompleteTextView) findViewById(R.id.autoComplete1);
	   autoText1.setOnItemClickListener(listener_autocomplete);
	   autoText1.setAdapter(adapter);
	   
	   autoText2 = (AutoCompleteTextView) findViewById(R.id.autoComplete2);
	   autoText2.setOnItemClickListener(listener_autocomplete2);
	   autoText2.setAdapter(adapter2);
	   
	    ll_listview = (LinearLayout) findViewById(R.id.ll_listview);
	    expListView = (ExpandableListView) findViewById(R.id.exp_list);
		listAdapter = new MyExpandableListAdapter(this/*, listDataHeader, listDataChild*/);
        
		ll_listview.setVisibility(View.INVISIBLE);
		
		autoText1.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count%3 == 1) {
				auto_seperate =1;
				availableLocations(count,auto_seperate);
				}
			
			}
			
			

			public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
			// TODO Auto-generated method stub
			
			}
			
			public void afterTextChanged(Editable s) {
			
			}
			});
		
			autoText2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int count) {
				// TODO Auto-generated method stub
				if (count%3 == 1) {
				auto_seperate =2;
				availableLocations(count,auto_seperate);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			
			popupWindow = popupWindowDogs();
			
	}
	 String popUpContents[];
	 MyAccount myAccount = (MyAccount) this.getParent();

	 public PopupWindow popupWindowDogs() {

	        // initialize a pop up window type
	        popupWindow = new PopupWindow(this);
	        LayoutInflater inflater = (LayoutInflater) MyRideActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View layout = inflater.inflate(R.layout.popupwindow,(ViewGroup) findViewById(R.id.relative_popup));
			
	        // some other visual settings
	       // popupWindow = new PopupWindow(layout,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
	        popupWindow.setFocusable(true);
	        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
	        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	        
	        
	        // set the list view as pop up window content
	        popupWindow.setContentView(layout);
	        
	        share    = (RelativeLayout) layout.findViewById(R.id.relative_share);
			favorite = (RelativeLayout) layout.findViewById(R.id.relative_favorite);
			save     = (RelativeLayout) layout.findViewById(R.id.relative_save);
			close    = (RelativeLayout) layout.findViewById(R.id.relative_close);
			//share.setOnClickListener(new );
			favorite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					saveinDB();
				}
			});
			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					saveinDB();
					((MyAccount) getParent()).switchTab(2);
					popupWindow.dismiss();
				}
			});
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});

	        return popupWindow;
	    }
	 
	 public void saveinDB(){
		 int i = popup_position; 
		 if (Utilities.dbAdapter == null)
				Utilities.dbAdapter = new DbAdapter(this);
			if (null != Utilities.dbAdapter) {
				Utilities.dbAdapter.open();
				
			}
		 Utilities.dbAdapter.insert_trips(source[0],destination[0],Utilities.TripsVector.get(0).getRoute_no(),
				 Utilities.TripsVector.get(0).getTiminngs(),"11/27/2013","13.00");
	 }
	 

	private void availableLocations(int count,int i) {
		// TODO Auto-generated method stub
		if(i==1){
			if(null != adapter)adapter.clear();
			//now pass the argument in the textview to the task
			Log.i("GetPlaces", "=============================char "+autoText1.getText().toString());
			GetPlaces task = new GetPlaces();
			value = autoText1.getText().toString();
			task.execute();
		}else{
			if(null != adapter2)adapter2.clear();
			//now pass the argument in the textview to the task
			Log.i("GetPlaces", "=============================char "+autoText2.getText().toString());
			GetPlaces task = new GetPlaces();
			value = autoText2.getText().toString();
			task.execute();
		
		}
	}
	
	ArrayList<String> predictionsArr ; 
	String latlong;
	
	class GetPlaces extends AsyncTask<Void, Void,Void>
	{
	
	@Override
	 // three dots is java for an array of strings
	protected Void doInBackground(Void... arg0)
	{
	Log.i("doInBackground", "=============================args.toString() "+value.toString());
	try
	{
		predictionsArr = new ArrayList<String>(); 
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
	//attach the adapter to textview
	if(predictionsArr.size()>0){
	if(auto_seperate ==1){
		Log.i("YourApp", "onPostExecute : =========================adapter 1");
		adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item);
		adapter.setNotifyOnChange(true);
		autoText1.setAdapter(adapter);
	}
	if(auto_seperate == 2){
		Log.i("YourApp", "onPostExecute : =========================adapter 2");
		adapter2 = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item);
		adapter2.setNotifyOnChange(true);
		autoText2.setAdapter(adapter2);	
	}
	for (String string : predictionsArr)
	{
	if(auto_seperate ==1){
	Log.i("YourApp", "onPostExecute : result = " + string);
	adapter.add(string);
	adapter.notifyDataSetChanged();
	}
	if (auto_seperate == 2) {
		Log.i("YourApp", "onPostExecute : result = " + string);
		adapter2.add(string);
		adapter2.notifyDataSetChanged();
	}
	
	}
  }else{
	 // Toast.makeText(getApplicationContext(), "NO Results", Toast.LENGTH_SHORT).show();
  }
 }
}
	
	OnItemClickListener listener_autocomplete = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View parent1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			value = (String) parent.getItemAtPosition(position);
			new getPlacesTask().execute(value);
		}
	};
	
	OnItemClickListener listener_autocomplete2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View parent1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			value = (String) parent.getItemAtPosition(position);
			new getPlacesTask().execute(value);
			
		}

		
	};
	
	OnClickListener listener_findmyride = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			count = 1;
			hidesoftKeyBoard();
			if(autoText1.getText().toString().length()> 0 && autoText2.getText().toString().length() > 0){
			callWebService();
			} else
				Toast.makeText(MyRideActivity.this,"Don't Leave any of above fields empty", Toast.LENGTH_SHORT).show();
			
		}

		private void callWebService() {
			// TODO Auto-generated method stub
			source = autoText1.getText().toString().split(",");
			destination = autoText2.getText().toString().split(",");
			new AvailableServices().execute(source[0],destination[0]);
			
		}
		String result;
		HttpResponse response;
		class  AvailableServices extends AsyncTask<String, Void, Void>{

			@Override
			protected Void doInBackground(String... params) {
					// TODO Auto-generated method stub
				try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(Utilities.availble_trips_url+params[0]+"&destinationname="+params[1]);
			        
		           response = httpClient.execute(httpGet);
			           
			       result = EntityUtils.toString(response.getEntity());
			       Log.i("result","================================"+result);
			         

				} catch (ClientProtocolException cpe) {
						System.out.println("First Exception becaz of HttpResponese :" + cpe);
						cpe.printStackTrace();
					} catch (IOException ioe) {
						System.out.println("Second Exception becaz of HttpResponse :" + ioe);
						ioe.printStackTrace();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void info_result) {
				// TODO Auto-generated method stub
				super.onPostExecute(info_result);
				if(null!= result){
				if(result.contains("No Services")){
					Toast.makeText(MyRideActivity.this,"No Services Available", Toast.LENGTH_SHORT).show();
				}else{
				InputStream is = new ByteArrayInputStream(result.getBytes());
				new WebServiceParsers(is);
				
				findmyride.setVisibility(View.INVISIBLE);
				ll_listview.setVisibility(View.VISIBLE);
				//myListView.setAdapter(myAdapter);
				// setting list adapter
		        expListView.setAdapter(listAdapter);
		        mapVisibility();
				//myListView.setOnItemClickListener(listener_list);
				}
			  }else{
				  Toast.makeText(MyRideActivity.this,"No Services Available", Toast.LENGTH_SHORT).show();
			  }
			}
						
		}
		
		private void mapVisibility() {
			// TODO Auto-generated method stub
			ll_from_ride.setVisibility(View.INVISIBLE);
			ll_to_ride.setVisibility(View.INVISIBLE);
			//ll_listview.setVisibility(View.INVISIBLE);
			//layout_routeinfo.setVisibility(View.VISIBLE);
			//layout_routeinfo.removeAllViews();
			//layout_routeinfo.addView(route_info);
			//setInfoData(position);
			
			//Display Map 
			frame.setVisibility(View.VISIBLE);
			//relative_scroll.setVisibility(View.VISIBLE);
			//scrollview.setVisibility(View.VISIBLE);
			frame.removeAllViews();
			frame.addView(mapDisplay);
			hidesoftKeyBoard();
			displayMap();
		}

		OnItemClickListener listener_list = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> Adapter, View view, int position,
					long value) {
				// TODO Auto-generated method stub
				count = 2;
				ll_from_ride.setVisibility(View.INVISIBLE);
				ll_to_ride.setVisibility(View.INVISIBLE);
				//ll_listview.setVisibility(View.INVISIBLE);
				//layout_routeinfo.setVisibility(View.VISIBLE);
				//layout_routeinfo.removeAllViews();
				//layout_routeinfo.addView(route_info);
				//setInfoData(position);
				
				//Display Map 
				frame.setVisibility(View.VISIBLE);
				//relative_scroll.setVisibility(View.VISIBLE);
				//scrollview.setVisibility(View.VISIBLE);
				frame.removeAllViews();
				frame.addView(mapDisplay);
				hidesoftKeyBoard();
				displayMap();
			}

			
		};
		private void setInfoData(int position) {/*
			// TODO Auto-generated method stub
			 info_source.setText(source[0]);
			 info_destination.setText(destination[0]);
			 
			 info_routeno.setText(Utilities.TripsVector.get(position).getService_id());
			 info_timings.setText(Utilities.TripsVector.get(position).getTiminngs());
			 
			 info_walk_text.setText("Walk to MGBS Bus stop");
			 info_walk_time.setText("10mins");
			 
			 info_busstop1.setText("MGBS Bus Stop");
			 info_busstop1_text.setText("Service NO 5678 towards suryapet Super Luxury bus");
			 info_busstop1_time.setText("5h 30mins");
			 
			 info_busstop2.setText("Nehru Bus Stop");
			 info_busstop2_text.setText("Walk to Local bus stop");
			 info_busstop2_time.setText("5mins");
		*/}

		private void hidesoftKeyBoard() {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findmyride.getWindowToken(), 0);
		}
	};
	
	class getPlacesTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			Log.i("doInBackground", "============================value="+params[0]);
			try
			{
				predictionsArr = new ArrayList<String>(); 
			            URL googlePlaces = new URL(
			                    "http://maps.googleapis.com/maps/api/geocode/json?address="+ 
			            		URLEncoder.encode(params[0], "UTF-8") +
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
			
			              //  for (int i = 0; i < ja.length(); i++) {
			                    JSONObject jo = (JSONObject) ja.get(0);
			                     //add each entry to our array
			                    predictionsArr.add(jo.getString("formatted_address"));
			                    JSONObject geoJson=jo.getJSONObject("geometry");
			                    JSONObject locJson=geoJson.getJSONObject("location");
			                    if(null!= locJson && locJson.length() != 0){
			                    coordinates.add(Double.parseDouble(locJson.getString("lat")));
			                    coordinates.add(Double.parseDouble(locJson.getString("lng")));
			                    Log.i("getPlacesTask", "::::::::: lat : "+locJson.getString("lat"));
			                    Log.i("getPlacesTask", "::::::::: lng : "+locJson.getString("lng"));
			                    }else{
			                    	Log.i("getPlacesTask", "::::::::: doInBackground location object is null");
			                    }
			                //}
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
		
	}
		private void displayMap() {
			// TODO Auto-generated method stub
			/*mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				        .getMap();*/
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
			if(status == ConnectionResult.SUCCESS) {
			    //Success! Do what you want
				if (mMap == null) {
					mMap = ((SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.map)).getMap();
				}
				setMarkers();
			}
			
	}
		
		Marker source_marker;
		LatLng userLocation,destinatio_loc;
		float bearing;
		private void setMarkers() {
			if (source_marker != null)
				source_marker.remove();
			getScreenWidthHeightinDp();
			userLocation = new LatLng(coordinates.get(0),coordinates.get(1));
			destinatio_loc = new LatLng(coordinates.get(2),coordinates.get(3));
			Location l1 = new Location("la");
			Location l2 = new Location("lb");
			l1.setLatitude(coordinates.get(0));
			l1.setLongitude(coordinates.get(1));
			l2.setLatitude(coordinates.get(2));
			l2.setLongitude(coordinates.get(3));
			bearing = l1.bearingTo(l2);
			source_marker = mMap.addMarker(new MarkerOptions()
					.position(userLocation)
					.title(userLocation.toString())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.source_map)));
			current_camera_position = new CameraPosition.Builder()
					.target(userLocation).zoom(7.5f).bearing(bearing).tilt(25).build();
			// myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
			mMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(current_camera_position));

			mMap.addMarker(new MarkerOptions()
					.position(destinatio_loc)
					.title(destinatio_loc.toString())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.destination_map)));
			setRouteMap();
			//moveCameraDown();
			/*SimpleGeofence fence = new SimpleGeofence("test",
					userLocation.latitude, userLocation.longitude, 600, 20000, 2);
			setEmergencyAlertView(fence);*/

		}
		
		CameraPosition camera_position ;
		
		private void downtheMarker_Side(LatLng l,float zoom,float bearing,float tilt) {
			FrameLayout mapContainer = (FrameLayout) mapDisplay.findViewById(R.id.frame_map);
			int container_height = mapContainer.getHeight();
			Projection projection = mMap.getProjection();

			Point markerScreenPosition = projection.toScreenLocation(l);
			Point pointHalfScreenAbove = new Point(markerScreenPosition.x,
					markerScreenPosition.y - (container_height / 3));
			LatLng aboveMarkerLatLng = projection
					.fromScreenLocation(pointHalfScreenAbove);
			camera_position = new CameraPosition.Builder()
					.target(aboveMarkerLatLng)
					.zoom(zoom)
					.bearing(bearing)
					.tilt(tilt).build();
			//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aboveMarkerLatLng,11.5f));
			// mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera_position));
		   //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera_position));
			mMap.animateCamera(CameraUpdateFactory.newLatLng(aboveMarkerLatLng));

		}
		
		private void setRouteMap() {
			new Thread(r).start();
		}
		String json;
		Handler uiHandler = new Handler();
		ArrayList<LatLng> ll_list = new ArrayList<LatLng>();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONParser jp = new JSONParser();
				// String url = makeURL(17.4018, 78.5602 ,17.4800, 78.3300);
				String url = makeURL(userLocation.latitude, userLocation.longitude,
						destinatio_loc.latitude, destinatio_loc.longitude);
				json = jp.getJSONFromUrl(url);
				uiHandler.post(p);
			}
		};
		Runnable p = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (json != null) {
					drawPath(json);
				}
				setPolyLine(list);
				//setCustomizeMarkers();
				downtheMarker_Side(userLocation, 5, bearing, 25);
			}
		};
		
		private void setPolyLine(ArrayList<LatLng> gp) {
			PolylineOptions rectOptions = new PolylineOptions();
			for (LatLng geoPoint : gp) {
				rectOptions.add(geoPoint);
			}
			rectOptions.color(Color.CYAN);
			// Get back the mutable Polyline
			Polyline polyline = mMap.addPolyline(rectOptions);
			polyline.getPoints();
			// Toast.makeText(MyLocationDemoActivity.this, "poly line = "+gp.size(),
			// Toast.LENGTH_SHORT).show();

		}
		
		private ArrayList<LatLng> decodePoly(String encoded) {

			ArrayList<LatLng> poly = new ArrayList<LatLng>();
			int index = 0, len = encoded.length();
			int lat = 0, lng = 0;

			while (index < len) {
				int b, shift = 0, result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lat += dlat;

				shift = 0;
				result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lng += dlng;

				LatLng p = new LatLng((((double) lat / 1E5)),
						(((double) lng / 1E5)));
				// Toast.makeText(RouteMapJson.this,"Json ll values = "+p.latitude+"\n"+p.longitude,
				// Toast.LENGTH_SHORT).show();
				poly.add(p);
			}

			return poly;
		}
		
		public String makeURL(double sourcelat, double sourcelog, double destlat,
				double destlog) {
			    StringBuilder urlString = new StringBuilder();
				urlString.append("http://maps.googleapis.com/maps/api/directions/json");
				urlString.append("?origin=");// from
				urlString.append(Double.toString(sourcelat));
				urlString.append(",");
				urlString.append(Double.toString(sourcelog));
				urlString.append("&destination=");// to
				urlString.append(Double.toString(destlat));
				urlString.append(",");
				urlString.append(Double.toString(destlog));
				urlString.append("&sensor=false&mode=TRANSIT&alternatives=true");
				// http://maps.googleapis.com/maps/api/directions/json?origin=17.408883,78.470196&destination=17.428743,78.450783&waypoints=Khairatabad,%20Hyderabad,OK&sensor=false
				//String rt_map = "http://maps.googleapis.com/maps/api/directions/json?origin=Hyderabad,Andhrapradesh,India&destination=Secunderabad,Andhrapradesh&sensor=false";
				//rt_map = "http://maps.googleapis.com/maps/api/directions/json?origin=17.408883,78.470196&destination=17.428743,78.450783&&sensor=false&mode=TRANSIT&alternatives=true";
				// "http://maps.googleapis.com/maps/api/directions/json?origin=Secretariat,%20Hyderabad&destination=Panjagutta,%20Hyderabad,&waypoints=Khairatabad,%20Hyderabad,OK&sensor=false";
				return urlString.toString();
				// http://maps.googleapis.com/maps/api/directions/json?origin=17.408883,78.470196&destination=17.428743,78.450783&waypoints=NTR%20Ghat,%20Hyderabad|Khairatabad,%20Hyderabad,OK&sensor=false";

				// return rt_map;
				}
		
		ArrayList<LatLng> list = new ArrayList<LatLng>();

		public void drawPath(String result) {

			try {
				// Tranform the string into a json object
				final JSONObject json = new JSONObject(result);
				JSONArray routeArray = json.getJSONArray("routes");
				JSONObject routes = routeArray.getJSONObject(0);
				JSONObject overviewPolylines = routes
						.getJSONObject("overview_polyline");
				String encodedString = overviewPolylines.getString("points");
				list = decodePoly(encodedString);
				

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public void onBackPressed() {
			// TODO Auto-generated method stub
			 Log.i("CDA", "login========================= onKeyDown Called");
			 if(count ==2){
				 count = 1;
					ll_from_ride.setVisibility(View.VISIBLE);
					ll_to_ride.setVisibility(View.VISIBLE);
					ll_listview.setVisibility(View.VISIBLE);
					//layout_routeinfo.setVisibility(View.INVISIBLE);
					//layout_routeinfo.addView(route_info);
					
					//Display Map 
					frame.setVisibility(View.INVISIBLE);
					relative_scroll.setVisibility(View.INVISIBLE);
					scrollview.setVisibility(View.INVISIBLE);
					//frame.addView(mapDisplay);
					//hidesoftKeyBoard();
					//displayMap(); 
			 }else if(count == 1){
				 count = 0;
				 findmyride.setVisibility(View.VISIBLE);

					//ll_listview.setVisibility(View.INVISIBLE);
			 }else{
			 Utilities.exit = true;
			 finish();
			 }
			   /* Intent intent = new Intent();
			            intent.setAction(Intent.ACTION_MAIN);
			            intent.addCategory(Intent.CATEGORY_HOME);

			            startActivity(intent);

			        return;*/
			    }
		
		Display display;
		DisplayMetrics outMetrics;
		int req_width, req_height;
		float dpHeight, dpWidth;
		int maxX, maxY;
		
		private void getScreenWidthHeightinDp() {
			display = getWindowManager().getDefaultDisplay();
			outMetrics = new DisplayMetrics();
			display.getMetrics(outMetrics);
			maxX = outMetrics.widthPixels;
			maxY = outMetrics.heightPixels;
			float density = getResources().getDisplayMetrics().density;
			dpHeight = outMetrics.heightPixels / density;
			dpWidth = outMetrics.widthPixels / density;
			// req_width = (int) (dpWidth * 0.75);
			// req_height = (int) (dpHeight * 0.75);
			Log.i(" width in dp  = " + dpWidth, "height in dp = " + dpHeight);
			Toast.makeText(getApplicationContext()," width in dp  = " + dpWidth+ "height in dp = " + dpHeight ,
					Toast.LENGTH_SHORT).show();
		}
		
		//menu options display
	
	OnClickListener listener_user = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			buttonFunctionality();
			popupWindowDisplay();
		}
	};

	public void buttonFunctionality(){
		/*if(flag){
			user_icon.setImageResource(R.drawable.account_32x32);
			flag=false;
		}else{
			user_icon.setImageResource(R.drawable.user_icon);
			flag=true;
		}*/
	
	}
	
		public void popupWindowDisplay() {
			// TODO Auto-generated method stub
			// *****************************************************************************************************
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			LayoutInflater inflater = (LayoutInflater) MyRideActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.popupwindow,(ViewGroup) findViewById(R.id.relative_popup));
			
		
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);

			pw = new PopupWindow(layout,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
			pw.showAtLocation(layout, Gravity.RIGHT,0,0);

			share    = (RelativeLayout) layout.findViewById(R.id.relative_share);
			favorite = (RelativeLayout) layout.findViewById(R.id.relative_favorite);
			save     = (RelativeLayout) layout.findViewById(R.id.relative_save);
			close    = (RelativeLayout) layout.findViewById(R.id.relative_close);
			/*share.setOnClickListener(share_listener);
			favorite.setOnClickListener(favorite_listener);
			save.setOnClickListener(save_listener);
			close.setOnClickListener(close_listener);*/

			pw.setOutsideTouchable(true);
			pw.setTouchable(true);
			layout.setClickable(true);

			pw.setFocusable(true);
			pw.getContentView().setClickable(true);
			pw.getContentView().setFocusableInTouchMode(true);

			// pw.setAnimationStyle(R.style.Theme_Transparent);
			pw.getContentView().setVerticalFadingEdgeEnabled(true);

			layout.setOnKeyListener(new View.OnKeyListener() {

				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					Log.i("NewsTab","hi cusror is in back buton event occureing.##########");
					boolean res = false;
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& event.getRepeatCount() == 0) {
						// do something on back key.
						// Log.e("keydown","back key");
						if (pw.isShowing()) {
							// Log.e("keydown","pw showing");
							pw.dismiss();
							getWindow().setFlags(
											WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
											WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
							res = true;
						}
					} else {
						res = false;
					}
					return res;
				}
			});

			pw.getContentView().setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					pw.dismiss();
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
							WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

				}
			});
		}
		
		public class EfficientAdapter extends BaseAdapter{
			String [] Data1 = {"My Profile",
					"My Favorites",
					"Purchase History",
					"Settings",
					"Logout"		
			};
		
		LayoutInflater mInflater=null;
		
		public EfficientAdapter(Context context){
			
			mInflater=LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Utilities.TripsVector.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null){
				holder=new ViewHolder();
				convertView		    = mInflater.inflate(R.layout.rides_list,null);
				holder.text_routeno = (TextView) convertView.findViewById(R.id.route_no);
				holder.text_timings = (TextView) convertView.findViewById(R.id.text_timings);
				holder.image 		= (ImageView)  convertView.findViewById(R.id.image_popup);
		          
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder)convertView.getTag();
			}
			//Toast.makeText(getApplicationContext(), "position : "+ position, Toast.LENGTH_SHORT).show();
			holder.text_routeno.setText(Utilities.TripsVector.get(position).getService_id());
			holder.text_timings.setText(Utilities.TripsVector.get(position).getTiminngs());
			holder.image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				 // TODO Auto-generated method stub
				// popupWindowDisplay();
				Toast.makeText(getApplicationContext(), "position : "+ position, Toast.LENGTH_SHORT).show();	
				popup_position = position;
				popupWindow.showAsDropDown(v, -5, 0);
				}
			});
			return convertView;
		}
		
		 class ViewHolder {				 
			    TextView text_routeno;
			    TextView text_timings;
			    ImageView image;
			 }
	}
	
	String [] Data_list = {"My Profile",
			"My Favorites",
			"Purchase History",
			"Settings",
			"Logout"
			
	};	
	
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private Context _context;
		public MyExpandableListAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this._context = context;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			 
	       // final String childText = (String) getChild(groupPosition, childPosition);
	 
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.routeinfo_view, null);
	        }
	 
	        TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.info_source);
	 
	       // txtListChild.setText(SanJose,CA);
	        return convertView;
	    }

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return Utilities.TripsVector.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return Utilities.TripsVector.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
		            LayoutInflater infalInflater = (LayoutInflater) this._context
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            convertView = infalInflater.inflate(R.layout.rides_list, null);
		        }
		 
		        TextView lblListHeader = (TextView) convertView
		                .findViewById(R.id.route_no);
		        lblListHeader.setTypeface(null, Typeface.BOLD);
		       // lblListHeader.setText(headerTitle);
		        ImageView plus = (ImageView) convertView.findViewById(R.id.image_popup);
		        plus.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popup_position = groupPosition;
						popupWindow.showAsDropDown(v, -5, 0);
					}
				});
		        return convertView;
		   }
		
		public boolean onGroupClick (ExpandableListView parent, View v, int groupPosition, long id){
			Toast.makeText(getApplicationContext(), "Group clicked", Toast.LENGTH_SHORT).show();
			Log.i("Group clicked","================= GroupClicked =======");
			return true;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	ExpandableListView.OnGroupClickListener onGroupClick = new OnGroupClickListener() {
		
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			// TODO Auto-generated method stub
			Log.i("onGroupClick","================= onGroupClick ");
			Toast.makeText(getApplicationContext(), "Group clicked", Toast.LENGTH_SHORT).show();
			
			return false;
		}
	};
}
