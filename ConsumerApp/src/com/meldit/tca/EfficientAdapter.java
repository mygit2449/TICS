package com.meldit.tca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		return Data1.length;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView ==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.rides_list,null);
			holder.text = (TextView) convertView.findViewById(R.id.text_timings);
			holder.image = (ImageView)  convertView.findViewById(R.id.image_popup);
	          
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		//holder.text.setText(Data1[position]);
		holder.image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			 new MyRideActivity().popupWindowDisplay();
			}
		});
		return convertView;
	}
	
	 class ViewHolder {				 
		    TextView text;
		    ImageView image;
		 }
}
