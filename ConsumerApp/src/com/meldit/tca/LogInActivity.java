package com.meldit.tca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.meldit.tca.db.DbAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LogInActivity extends Activity implements OnClickListener{
	
	

	DbAdapter mDbHelper = new DbAdapter(LogInActivity.this);		
	
	EditText editText1,editText2;
	Button login,password,account;
	Switch mySwitch;
	String result;
	StringEntity entity;
	HttpResponse response;
	
	String uname,pword;

    @SuppressLint("NewApi") 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
               
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        
        login     = (Button) findViewById(R.id.button1);
        login.setOnClickListener(this);
        password  = (Button) findViewById(R.id.button2);
        password.setOnClickListener(this);
        account   = (Button) findViewById(R.id.button3);
        account.setOnClickListener(this);
        
        mySwitch 	  = (Switch) findViewById(R.id.toggleButton1);
        mySwitch.setOnCheckedChangeListener(listener_switch);
        //set the switch to ON 
       // mySwitch.setChecked(false);
        Utilities.exit = false;
       if (Utilities.signin){
    	    // new Activity
			Intent intent_account = new Intent(LogInActivity.this,MyAccount.class);
			startActivity(intent_account);
			editText1.setText("");
			editText2.setText("");  
       }
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if(Utilities.exit)
    	finish();
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
			uname = editText1.getText().toString();
			pword = editText2.getText().toString();
			Toast.makeText(LogInActivity.this,"onclick event", Toast.LENGTH_SHORT).show();
			
			if(uname.length()>0 && pword.length() >0)
			new LoginService().execute();
			else
				Toast.makeText(LogInActivity.this,"user name and password both are empty", Toast.LENGTH_SHORT).show();			
		 break;
		case R.id.button2:
			Intent intent_forgetpassword = new Intent(LogInActivity.this,ForgetPassword.class);
			startActivity(intent_forgetpassword);
			 break;
		case R.id.button3:
			Intent intent = new Intent(LogInActivity.this,RegistrationActivity.class);
			startActivity(intent);
			 break;
		}
	}
	
	private class  LoginService extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
			try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Utilities.consumer_login_url);
		        StringBuilder sb = new StringBuilder();
		       		        
		        sb.append("<MD_CONSUMER_DETAILS>");
		        
		        sb.append("<ticsUserName>");
		        sb.append(uname);
		        sb.append("</ticsUserName>");
		        sb.append("<ticsPassword>");
		        sb.append(pword);
		        sb.append("</ticsPassword>");
		        
		       
		        
		        sb.append("</MD_CONSUMER_DETAILS>");
	            
		        entity = new StringEntity(sb.toString());
		        httpPost.setEntity(entity);  
		        httpPost.setHeader("Accept", "application/xml");
		        httpPost.setHeader("Content-Type", "application/xml");
		      
	           response = httpClient.execute(httpPost);
	           Log.i("response","================================"+response.getStatusLine());
	           String status = response.getStatusLine().toString();
	           if(status.contains("200")){
		       result = EntityUtils.toString(response.getEntity());
		       Log.i("result","================================"+result);
	           }else{
	        	   result = "Unauthorized User";
	           }
		         

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
			Toast.makeText(LogInActivity.this,"result : "+result, Toast.LENGTH_SHORT).show();
			if(null!= result){
				if(result.contains("Unauthorized User")){
					Toast.makeText(LogInActivity.this,"Unauthorized User", Toast.LENGTH_SHORT).show();
				}else{
				InputStream is = new ByteArrayInputStream(result.getBytes());
				String [] credentials= new String [5];
				credentials = new WebServiceParsers().parseResult(is);
				if(credentials[0].equalsIgnoreCase(uname) && credentials[1].equalsIgnoreCase(pword)){
					String userName = credentials[0].toString();
					String password = credentials[1].toString();					
					String email = credentials[2].toString();
					String phoneNumber = credentials[3].toString();
					
					mDbHelper.storeProfileDetails("INSERT INTO ProfileDetails (User_Name, Password, Email_Id, Phone_Number) values('"+userName+"', '"+password+"','"+email+"', '"+phoneNumber+"')");
					Toast.makeText(LogInActivity.this,"Log in Succesfully", Toast.LENGTH_SHORT).show();
					// new Activity
					Intent intent_account = new Intent(LogInActivity.this,MyAccount.class);//.putExtra("user_name", userName).putExtra("password", password)
							//.putExtra("first_name", firstname).putExtra("email_id", email).putExtra("phone_number", phoneNumber);
					startActivity(intent_account);
					editText1.setText("");
					editText2.setText("");
					
				}else if(credentials[0].equalsIgnoreCase(uname) || credentials[1].equalsIgnoreCase(pword)){
					Toast.makeText(LogInActivity.this,"wrong username or password", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(LogInActivity.this,"username and possword are wrong", Toast.LENGTH_SHORT).show();
				}
				}
			  }else{
				  Toast.makeText(LogInActivity.this,"No Services Available", Toast.LENGTH_SHORT).show();
			  }
		}
		
	}
	
	
	
	OnCheckedChangeListener listener_switch = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			Log.i("listener_switch","=================================="+isChecked);
			Log.i("listener_switch","=========git=========================");

			Utilities.signin = true; 
		}
	};
    
}
