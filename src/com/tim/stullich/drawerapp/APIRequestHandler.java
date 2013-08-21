package com.tim.stullich.drawerapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class APIRequestHandler extends AsyncTask<Void, Void, Void>{

	//TODO Clean up this mess
	public static final int SERVER_STATUS = 0;
	public static final int TARGET_REQUEST = 1;
	public static final int TARGET_ATTRIBUTE_REQUEST = 2;
	public static final int DEBUG_MODE = 3;

	private static String FINAL_ADDRESS;
	private static String SERVER_ADDRESS;
	private static int SERVER_PORT;
	
	private HttpClient client;
	private HttpGet request;
	
	//private static final String USER_PREFS_FILE = "UserPrefs";
	
	/**
	 * Builds a request to be executed at a later time
	 * @param act
	 * @param apiType
	 */
	public APIRequestHandler(Activity act, final int apiRequestType) {
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
        client = new MyHttpClient().iniHttpClient();
        HttpGet get = null;
        HttpResponse response = null;
        
        try {
            get = new HttpGet("https://www.google.com");
            response = client.execute(get);
            if (response != null) {
                Log.i("OPAM", response.getStatusLine().toString());
            }
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * @author Tim Stullich
     *
     */
    private class MyHttpClient {
        public HttpClient iniHttpClient() {
            try {
                
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                
                DefaultHttpClient cli = new DefaultHttpClient(ccm, params);
                Log.i("OPAM", "Client returned");
                return cli;
                
            } catch (Exception e) {
                return new DefaultHttpClient();
            }
        }
    }
}
