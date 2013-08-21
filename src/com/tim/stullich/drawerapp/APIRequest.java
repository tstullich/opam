package com.tim.stullich.drawerapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class APIRequest extends AsyncTask<Void, Void, Void>{

	//TODO Clean up this mess
	public static final int SERVER_STATUS = 0;
	public static final int TARGET_REQUEST = 1;
	public static final int TARGET_ATTRIBUTE_REQUEST = 2;
	public static final int DEBUG_MODE = 3;

	private static String FINAL_ADDRESS;
	private static String SERVER_ADDRESS;
	private static int SERVER_PORT;
	
	private HttpClient client;
	private HttpsURLConnection conn;
	private HttpGet request;
	private URL url;
	
	//private static final String USER_PREFS_FILE = "UserPrefs";
	
	/**
	 * Builds a request to be executed at a later time
	 * @param act
	 * @param apiType
	 */
	public APIRequest(Activity act, final int apiType) {
		//SharedPreferences settings = act.getSharedPreferences(USER_PREFS_FILE, 0);
		SERVER_ADDRESS = "https://iam.vm.oracle.com";
		SERVER_PORT = 18102;	
						
		//TODO Build more cases... maybe even a better way to implement this.
		/*switch (apiType) {
			case SERVER_STATUS : 
				StringBuilder sb = new StringBuilder();
				sb.append("https://");
				sb.append(SERVER_ADDRESS + ":");
				sb.append(SERVER_PORT);
				sb.append("/opam/");
				FINAL_ADDRESS = sb.toString();
				break;
		}*/
	}
	
	private void testConn(HttpsURLConnection conn){
	    Log.i("OPAM", "Testing Connection");
	    if (conn != null){
	        try {
	            Log.i("OPAM", "Test Response Code");
	            Log.i("OPAM", "Response Code: " + conn.getResponseCode());
	            Log.i("OPAM", "Cipher: " + conn.getCipherSuite());
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}
	
	public boolean login() {
		try {
			client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

    
    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            Log.i("OPAM", "Testing Connection");
            //url = new URL("https", SERVER_ADDRESS, SERVER_PORT, "/opam");
            url = new URL("https://google.com");
            conn = (HttpsURLConnection) url.openConnection();
            
            Log.i("OPAM", "Connection Successful");
            testConn(conn);
            
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
