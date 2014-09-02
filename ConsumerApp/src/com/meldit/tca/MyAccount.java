package com.meldit.tca;


import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccount extends TabActivity {
	
	TabHost tabHost;
	boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tabs);
		setTabs();	
	
		 ActionBar bar = getActionBar();
		// bar.setDisplayShowHomeEnabled(false);
		 bar.setDisplayShowTitleEnabled(false);  
	}
	
	public void switchTab(int tab){
		 tabHost.setCurrentTab(tab);
	}
    
		 private void setTabs()
			{
				addTab("Find My Ride", R.drawable.menu_pin_36x36, MyRideActivity.class);
				addTab("Routes", R.drawable.menu_bus_36x36,  RoutesActivity.class);
				addTab("Trip Planner", R.drawable.menu_calendar_36x36, TripPlanner.class);
				addTab("My Tickets", R.drawable.menu_ticket_36x36, MyTicketsActivity.class);
			}
			
			private void addTab(String labelId, int drawableId, Class<?> c)
			{
				tabHost = (TabHost)findViewById(android.R.id.tabhost);
				tabHost.getTabWidget().setStripEnabled(true);
				Intent intent = new Intent(this, c);
				TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
				
				View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
				TextView title = (TextView) tabIndicator.findViewById(R.id.title);
				title.setText(labelId);
				ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
				icon.setImageResource(drawableId);
				
				spec.setIndicator(tabIndicator);
				spec.setContent(intent);
				tabHost.addTab(spec);
			}
			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.main, menu);
			   
			   // setMenuBackground();
			    return true;
			}
			
			
			
			 @Override
			  public boolean onOptionsItemSelected(MenuItem item) {
				 Log.i("onOptions","========================item"+item.getItemId());
				 
			    switch (item.getItemId()) {
			    case R.id.item1:
			     Intent myProfileIntent = new Intent(MyAccount.this, ProfileActivity.class);
			     startActivity(myProfileIntent);
			      break;
			    case R.id.item2:
			    	Toast.makeText(getApplicationContext(),"Favorites List",Toast.LENGTH_SHORT).show();
			    	tabHost.setCurrentTab(2);
				      break;
			    case R.id.item3:
				     tabHost.setCurrentTab(3);
				      break;
			    case R.id.item4:
				     Intent settingsIntent = new Intent(MyAccount.this, SettingsActivity.class);
				     startActivity(settingsIntent);
				      break;
			    case R.id.item5:
			    	Utilities.signin = false;
			    	Utilities.exit = false;
				    Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
				    finish();
				      break;
			    default:
			    	 
			      break;
			    }
			    return true;
			  }
			
			
			 protected void setMenuBackground(){                     
			        // Log.d(TAG, "Enterting setMenuBackGround");  
			        getLayoutInflater().setFactory( new Factory() {  
			            public View onCreateView(String name, Context context, AttributeSet attrs) {
			                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
			                    try { // Ask our inflater to create the view  
			                        LayoutInflater f = getLayoutInflater();  
			                        final View view = f.createView( name, null, attrs );  
			                        /* The background gets refreshed each time a new item is added the options menu.  
			                        * So each time Android applies the default background we need to set our own  
			                        * background. This is done using a thread giving the background change as runnable 
			                        * object */
			                        new Handler().post( new Runnable() {  
			                            public void run () {  
			                                // sets the background color   
			                                view.setBackgroundResource(Color.parseColor("#FFA500"));
			                                // sets the text color              
			                               // ((TextView) view).setTextColor(Color.BLACK);
			                                // sets the text size              
			                               // ((TextView) view).setTextSize(18);
			                }
			                        } );  
			                    return view;
			                }
			            catch ( InflateException e ) {}
			            catch ( ClassNotFoundException e ) {}  
			        } 
			        return null;
			    }}); 
			}
}
