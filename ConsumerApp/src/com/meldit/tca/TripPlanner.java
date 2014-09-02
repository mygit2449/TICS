package com.meldit.tca;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.meldit.tca.db.DbAdapter;


public class TripPlanner extends Activity {
	
	SetterAndGetters obj_setters;
	ImageView image_addtrip;
	static PopupWindow popupWindow;
	ListView myListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_planner);
		
		image_addtrip = (ImageView) findViewById(R.id.image_addtrip);
		image_addtrip.setOnClickListener(listener_addtrip);
		// DB open
		if (Utilities.dbAdapter == null)
			Utilities.dbAdapter = new DbAdapter(this);
		if (null != Utilities.dbAdapter) {
			Utilities.dbAdapter.open();	
		}
		getResultsFromDB();
		myListView =(ListView) findViewById(android.R.id.list);
		
		myListView.setAdapter(new MyEfficientAdapter(TripPlanner.this));
		
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),"Your Selected Item position", Toast.LENGTH_LONG).show();
				ExpandedpopupWindow();
			}
		});
		
		popupWindow = popupWindowDogs();
		
	}
	PopupWindow pw;
	
	public void popupWindowDisplay() {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutInflater inflater = (LayoutInflater) TripPlanner.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Inflate the view from a predefined XML layout
		View layout = inflater.inflate(R.layout.addtrip,(ViewGroup) findViewById(R.id.relative_popup));
		
	
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		pw = new PopupWindow(layout,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		pw.showAtLocation(layout, Gravity.RIGHT,-5,-10);
		pw.setOutsideTouchable(true);
		pw.setTouchable(true);
		layout.setClickable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg_exp));
		pw.setFocusable(true);
		pw.getContentView().setClickable(true);
		pw.getContentView().setFocusableInTouchMode(true);

		// pw.setAnimationStyle(R.style.Theme_Transparent);
		pw.getContentView().setVerticalFadingEdgeEnabled(true);

		layout.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.i("NewsTab","hi cusror is in back buton event occureing.");
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
	
	public void ExpandedpopupWindow(){
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutInflater inflater = (LayoutInflater) TripPlanner.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Inflate the view from a predefined XML layout
		View layout = inflater.inflate(R.layout.routeinfo_view,(ViewGroup) findViewById(R.id.relative_popup));
		
	
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		pw = new PopupWindow(layout,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
		pw.setOutsideTouchable(true);
		pw.setTouchable(true);
		layout.setClickable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg_exp));
		pw.setFocusable(true);
		pw.getContentView().setClickable(true);
		pw.getContentView().setFocusableInTouchMode(true);
		pw.showAtLocation(layout,Gravity.CENTER, -5,-10);


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
	
	
	 public PopupWindow popupWindowDogs() {

	        // initialize a pop up window type
	        popupWindow = new PopupWindow(this);
	        View layout;
	        	LayoutInflater inflater = (LayoutInflater) TripPlanner.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		       // layout = inflater.inflate(R.layout.addtrip,(ViewGroup) findViewById(R.id.relative_popup));
	        	 layout = inflater.inflate(R.layout.edittrip,(ViewGroup) findViewById(R.id.linear_routeinfo));
	        	 popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg_exp));
	       
	        // some other visual settings
	        popupWindow.setFocusable(true);
	        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
	        popupWindow.setHeight(LayoutParams.MATCH_PARENT);
	        // set the list view as pop up window content
	        popupWindow.setContentView(layout);
	        Button edit_trip = (Button) layout.findViewById(R.id.edit_trip);
	        edit_trip.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
					Utilities.dbAdapter.insert_trips("San Jose,CA","Fremont,CA","L-110","12:45-1:25(1h 45mins)","6/25/2013", "13");
				}
			});

	        return popupWindow;
	    }
	
	OnClickListener listener_addtrip = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			popupWindowDisplay();
			
			
		}
	};
	
	

	private void getResultsFromDB() {
		Cursor cursor = Utilities.dbAdapter.fetchAllTrips();
		Utilities.TripsVector = new Vector<SetterAndGetters>();
		
		if (null != cursor && cursor.getCount() > 0) {
			Toast.makeText(getApplicationContext(),"service details size = " + cursor.getCount(),Toast.LENGTH_SHORT).show();
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					obj_setters = new SetterAndGetters();
					/*String rowid = cursor.getString(cursor
							.getColumnIndex(DbAdapter.KEY_ROWID));*/
					String trip_source = cursor.getString(cursor
							.getColumnIndex(DbAdapter.KEY_SOURCE));
					obj_setters.setSource(trip_source);

					String trip_destination = cursor.getString(cursor
							.getColumnIndex(DbAdapter.KEY_DESTINATION));
					obj_setters.setDestination(trip_destination);
					String trip_route = cursor.getString(cursor
							.getColumnIndex(DbAdapter.KEY_ROUTENO));
					obj_setters.setRoute_no(trip_route);
					String trip_timings = cursor.getString(cursor
							.getColumnIndex(DbAdapter.KEY_TIMINGS));
					obj_setters.setTiminngs(trip_timings);
					
					Utilities.TripsVector.add(obj_setters);
					
					cursor.moveToNext();

				}
			}
		}
	}

	// Constant for identifying the dialog
	private static final int DIALOG_ALERT = 10;
	public static  class  MyEfficientAdapter extends BaseAdapter {
		

	LayoutInflater mInflater = null;
	
	public MyEfficientAdapter(Context context){
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
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
		if(convertView ==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.trips_planner_list,null);
			holder.text_source = (TextView) convertView.findViewById(R.id.text_plan_from);
			holder.text_destination = (TextView) convertView.findViewById(R.id.text_plan_destination);
			holder.text_routeno = (TextView) convertView.findViewById(R.id.text_routeno_plan);
			holder.text_timings = (TextView) convertView.findViewById(R.id.text_plan_timings);
			holder.image_edittrip = (ImageView) convertView.findViewById(R.id.image_edit_plan);
          //  holder.text_dispatch = (TextView) convertView.findViewById(R.id.button_buytickets);
            
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		/*if(Utilities.TripsVector.size()>0){
		holder.text_source.setText(Utilities.TripsVector.get(position).getSource());
		holder.text_destination.setText(Utilities.TripsVector.get(position).getDestination());
		holder.text_routeno.setText(Utilities.TripsVector.get(position).getRoute_no());
		holder.text_timings.setText(Utilities.TripsVector.get(position).getTiminngs());
		
		}*/
		//holder.image_edittrip.setOnClickListener(listener_edittrip);
		holder.image_edittrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.showAsDropDown(v, -5, 0);
			}
		});
		return convertView;
	}
	
	 class ViewHolder {				 
		    TextView text_source;
		    TextView text_destination;
		    TextView text_routeno;
		    TextView text_timings;
		    ImageView image_edittrip;
		    TextView text_dispatch;
  	 }
	 
	
}
	 
	MyAccount myAccount = new MyAccount();
	OnClickListener listener_edittrip = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DIALOG_ALERT);
		}
	};
	
	/*public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_signin, null))
	    // Add action buttons
	           .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   LoginDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}*/
	
}
