package com.opam.base;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.opam.request.types.Account;
import com.opam.request.types.AccountsCollection;
import com.opam.request.types.AccountWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class APIRequestHandler extends AsyncTask<Void, Void, Boolean> {

	// TODO Clean up this mess
	public static final int DEBUG_MODE = -1;
	public static final int SERVER_STATUS = 0;
	public static final int TARGET_REQUEST = 1;
	public static final int TARGET_ATTRIBUTE_REQUEST = 2;
	public static final int ACCOUNTS_REQUEST = 3;
	public static final int LOGIN_REQUEST = 0;

	private static String SERVER_ADDRESS;
	private static int SERVER_PORT;
	private String requestAddress;

	private HttpResponse response;
	private HttpClient client;
	private transient String userName;
	private transient String password;
	private String json;
	private ProgressDialog dialog;
	private Context ctx;
	
	private AsyncResponse delegate = null;

	// private static final String USER_PREFS_FILE = "UserPrefs";

	/**
	 * Builds a request to be executed at a later time
	 * 
	 * @param act
	 * @param apiType
	 */
	public APIRequestHandler(Activity act, final int apiRequestType) {
		// SharedPreferences settings =
		// act.getSharedPreferences(USER_PREFS_FILE, 0);
		// TODO Get Server info from Input Fields
		SERVER_ADDRESS = "192.168.0.27";
		SERVER_PORT = 18102;

		ctx = act;
		dialog = new ProgressDialog(ctx);

		// TODO Build more cases... maybe even a better way to implement this.
		StringBuilder sb = new StringBuilder();
		switch (apiRequestType) {
		case SERVER_STATUS:
			sb.append("https://" + SERVER_ADDRESS + ":" + SERVER_PORT
					+ "/opam/");
			requestAddress = sb.toString();
			break;
		case ACCOUNTS_REQUEST:
			sb.append("https://" + SERVER_ADDRESS + ":" + SERVER_PORT
					+ "/opam/ui/myaccounts/search?");
			requestAddress = sb.toString();
			break;
		case DEBUG_MODE:
			requestAddress = "https://" + SERVER_ADDRESS + ":" + SERVER_PORT
					+ "/opam/";
			break;
		}
	}

	public void setLoginInfo(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Loading...");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (dialog.isShowing() && success) {
			dialog.dismiss();
			delegate.processFinish(json);
			
		} else {
			dialog.dismiss();
			AlertDialog.Builder builder = new Builder(ctx);
			builder.setTitle("Request Unsuccessful");
			if (response != null) {
				builder.setMessage(response.getStatusLine().toString());
			} else {
				builder.setMessage("Host Unreachable. Check Your Server Status");
			}
			builder.setPositiveButton("OK", null);
			builder.create().show();
		}
	}

	@Override
	protected Boolean doInBackground(Void... param) {
		client = new OPAMHttpClient().iniHttpClient();
		HttpGet request = null;
		response = null;
		try {
			request = new HttpGet(requestAddress);
			response = client.execute(request);
			if (response != null && response.getStatusLine().toString().contains("200")) {
				json = EntityUtils.toString(response.getEntity());
				Log.i("OPAM", response.getStatusLine().toString());				
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @author Tim Stullich
	 * 
	 */
	private class OPAMHttpClient {
		public HttpClient iniHttpClient() {
			try {

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, SERVER_PORT));

				CredentialsProvider credProvider = new BasicCredentialsProvider();
				credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
						AuthScope.ANY_PORT), new UsernamePasswordCredentials(
						userName, password));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);

				DefaultHttpClient cli = new DefaultHttpClient(ccm, params);
				cli.setCredentialsProvider(credProvider);

				return cli;

			} catch (Exception e) {
				return new DefaultHttpClient();
			}
		}
	}
	
	public void setAsyncDelegate(AsyncResponse d){
		delegate = d;
	}
	
	public static <T> T fromJSON(final TypeReference<T> type,
		      final String jsonPacket) {
		   T data = null;

		   try {
		      data = new ObjectMapper().readValue(jsonPacket, type);
		   } catch (Exception e) {
		      // Handle the problem
		   }
		   return data;
		}
}