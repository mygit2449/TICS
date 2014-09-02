package com.meldit.tca;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
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

public class RoutesActivity extends FragmentActivity {
	
	//RelativeLayout relativeMap;
	LinearLayout linearLayout,ll_nextdeparture,ll_departures;
	ExpandableListView explistview;
	ExpandableListAdapter listAdapter;
	GoogleMap mMap;
	//View mapDisplay;
	//View mapFragment;
	//FrameLayout frame,frame_map;
	int count=0;
	
	 /** Called when the activity is first created. */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.routes);
	  
	 // relativeMap = (RelativeLayout) findViewById(R.id.relative_map);
	  //relativeMap.setVisibility(View.INVISIBLE);
	  
	  linearLayout = (LinearLayout) findViewById(R.id.ll_list);
	 // frame = (FrameLayout) findViewById(R.id.frame_main);
	 // ll_departures = (LinearLayout) findViewById(R.id.nextdeparture);
	  //mapDisplay = getLayoutInflater().inflate(R.layout.mapdisplay, null);
	 // next_departures = getLayoutInflater().inflate(R.layout.next_departures, null);
	 // frame_map = (FrameLayout) mapDisplay.findViewById(R.id.frame_map);
	  //mapFragment = (View) mapDisplay.findViewById(R.id.map);
	  
	  explistview = (ExpandableListView) findViewById(R.id.explist);
	  listAdapter = new ExpandableListAdapter(this);
      //setting list adapter
	  explistview.setAdapter(listAdapter);
	  
	 }
	 
	
	public void onBackPressed() {
		 Log.i("CDA", "login=========================onKeyDown Called");
		 
		 Utilities.exit = true;
		 finish();
		 
		    /*Intent intent = new Intent();
		            intent.setAction(Intent.ACTION_MAIN);
		            intent.addCategory(Intent.CATEGORY_HOME);

		            startActivity(intent);

		        return;*/
		    }
	
	
	private void displayMap() {
		// TODO Auto-generated method stub
		/*mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();*/
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if(status == ConnectionResult.SUCCESS) {
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
	CameraPosition current_camera_position;
	private void setMarkers() {
		if (source_marker != null)
			source_marker.remove();
		getScreenWidthHeightinDp();
		userLocation = new LatLng(17.40834618,78.47246552);
		destinatio_loc = new LatLng(17.428743,78.450783);
		Location l1 = new Location("la");
		Location l2 = new Location("lb");
		l1.setLatitude(17.40834618);
		l1.setLongitude(78.47246552);
		l2.setLatitude(17.428743);
		l2.setLongitude(78.450783);
		bearing = l1.bearingTo(l2);
		source_marker = mMap.addMarker(new MarkerOptions()
				.position(userLocation)
				.title(userLocation.toString())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.source_map)));
		current_camera_position = new CameraPosition.Builder()
				.target(userLocation).zoom(13.5f).bearing(bearing).tilt(25).build();
		// myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		mMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(current_camera_position));

		mMap.addMarker(new MarkerOptions()
				.position(destinatio_loc)
				.title(destinatio_loc.toString())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.destination_map)));
		setRouteMap();
		

	}
	
	
	
	CameraPosition camera_position ;
	
	private void downtheMarker_Side(LatLng l,float zoom,float bearing,float tilt) {
		FrameLayout mapContainer = (FrameLayout) findViewById(R.id.frame_map);
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

	
	 public class ExpandableListAdapter extends BaseExpandableListAdapter {
		 
		    private Context _context;
		   
		    public ExpandableListAdapter(Context context) {
		        this._context = context;
		       
		    }
		 
		    @Override
		    public Object getChild(int groupPosition, int childPosititon) {
		        return null;
		    }
		 
		    @Override
		    public long getChildId(int groupPosition, int childPosition) {
		        return 0;
		    }
		 
		    @Override
		    public View getChildView(int groupPosition, final int childPosition,
		            boolean isLastChild, View convertView, ViewGroup parent) {
		        if (convertView == null) {
		            LayoutInflater infalInflater = (LayoutInflater) this._context
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            /*frame.removeAllViews();
		            frame_map.removeAllViews();*/
		            convertView = infalInflater.inflate(R.layout.next_departures, null);
		            
		        }
		 
		        /*TextView txtListChild = (TextView) convertView
		                .findViewById(R.id.info_source);*/
		 
		       // txtListChild.setText(SanJose,CA);
		        return convertView;
		    }
		 
		    @Override
		    public int getChildrenCount(int groupPosition) {
		        return 1;
		    }
		 
		    @Override
		    public Object getGroup(int groupPosition) {
		        return null;
		    }
		 
		    @Override
		    public int getGroupCount() {
		        return 5;
		    }
		 
		    @Override
		    public long getGroupId(int groupPosition) {
		        return groupPosition;
		    }
		 
		    @Override
		    public View getGroupView(int groupPosition, boolean isExpanded,
		            View convertView, ViewGroup parent) {
		        if (convertView == null) {
		            LayoutInflater infalInflater = (LayoutInflater) this._context
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            convertView = infalInflater.inflate(R.layout.route_list, null);
		        }
		 
		        return convertView;
		    }
		 
		    @Override
		    public boolean hasStableIds() {
		        return false;
		    }
		 
		    @Override
		    public boolean isChildSelectable(int groupPosition, int childPosition) {
		        return true;
		    }
		}
	 
	
	}
