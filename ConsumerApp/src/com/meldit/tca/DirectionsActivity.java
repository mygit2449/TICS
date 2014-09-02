package com.meldit.tca;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;

import android.app.ListActivity;
import android.content.Context;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

public class DirectionsActivity extends ListActivity{
	
	AutoCompleteTextView from,to;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_layout);
		from = (AutoCompleteTextView) findViewById(R.id.from);
		to = (AutoCompleteTextView) findViewById(R.id.to);

		/*from.setText("Fisherman's Wharf, San Francisco, CA, United States");
		to.setText("The Moscone Center, Howard Street, San Francisco, CA, United States");
*/
		from.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
		to.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
	}
	
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
		private ArrayList<String> resultList;
		
		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}
		
		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
					//	resultList = autocomplete(constraint.toString());
						
						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					}
					else {
						notifyDataSetInvalidated();
					}
				}};
			return filter;
		}

		
	}/*
	static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	protected ArrayList<String> autocomplete(String string) {
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		
		try {
		
			HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
					 @Override
					 public void initialize(HttpRequest request) {
						 request.setParser(new JsonObjectParser(JSON_FACTORY));
					 }
				 }
			);
			
			GenericUrl url = new GenericUrl(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			url.put("input", input);
			url.put("key", PLACES_API_KEY);
			url.put("sensor",false);
	 
			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse httpResponse = request.execute();
			PlacesResult directionsResult = httpResponse.parseAs(PlacesResult.class);

			List<Prediction> predictions = directionsResult.predictions;
			for (Prediction prediction : predictions) {
				resultList.add(prediction.description);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultList;
	}*/
	
	

}
