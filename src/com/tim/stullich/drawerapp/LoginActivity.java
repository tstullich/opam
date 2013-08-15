package com.tim.stullich.drawerapp;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {

	private Button loginButton;
	private Spinner serverList;
	private String[] serverNames;
	private static final String USER_PREFS = "UserPrefs";
	private static final String SERVER_NAMES = "serverNames";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new LoginOnClickListener());
		
		serverList = (Spinner) findViewById(R.id.servers_spinner);
		
		addServersToSpinner();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (saveServerPreferences()) {
			Log.i("OPAM", "Preferences saved successfully");
		}
		else {
			Log.i("OPAM", "Preferences did not save");
		}
	}
	
	private void addServersToSpinner() {
		loadServerPreferences();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				serverNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serverList.setAdapter(adapter);
		serverList.setOnItemSelectedListener(new SpinnerItemSelectedListener());
	}
	
	private String[] loadServerPreferences() {
		SharedPreferences prefs = getSharedPreferences(USER_PREFS, 0);
		int size = prefs.getInt(SERVER_NAMES + "_SIZE", 1);
		serverNames = new String[size];
		if (size == 1) {
			serverNames[0]= "<Add New Server>";
			return serverNames;
		}
		for (int i = 0; i < size; i++) {
			serverNames[i] = prefs.getString(SERVER_NAMES + "_" + i, null);
		}
		return serverNames;
	}
	
	private boolean saveServerPreferences() {
		SharedPreferences prefs = getSharedPreferences(USER_PREFS, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SERVER_NAMES + "_SIZE", serverNames.length);
		
		for (int i = 0; i < serverNames.length; i++) {
			editor.putString(SERVER_NAMES + "_" + i, serverNames[i]);
		}
		
		return editor.commit();
	}
	
		private class LoginOnClickListener implements Button.OnClickListener {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.login_button :
						Intent i = new Intent(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(i);
				}
			
			}
		}
		private class SpinnerItemSelectedListener implements OnItemSelectedListener {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos ,long id) {
				Toast.makeText(parent.getContext(), 
						"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
						Toast.LENGTH_SHORT).show();
				Log.i("OPAM", "Item selected: " + pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.i("OPAM", "Nothing selected");
			
			}
		}
}
