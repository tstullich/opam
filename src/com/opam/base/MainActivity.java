package com.opam.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opam.request.types.Account;
import com.opam.request.types.AccountsCollection;
import com.tim.stullich.drawerapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private SideDrawer mLeftDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private Activity act;
	private SimpleExpandableListAdapter accountsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mLeftDrawer = new SideDrawer(this, MainActivity.this,
				(ListView) findViewById(R.id.left_drawer),
				(DrawerLayout) findViewById(R.id.drawer_layout));

		getActionBar().setIcon(R.drawable.oracle_icon);

		act = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.actionbar_menu, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new MainSectionFragment();
			Bundle args = new Bundle();
			args.putInt(MainSectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_accounts_section)
						.toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but simply displays
	 * dummy text.
	 */
	public static class MainSectionFragment extends Fragment implements AsyncResponse {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private ProgressBar progress;
		private SimpleExpandableListAdapter adapter;
		private ArrayList<String> accountNames;
		private ArrayList<Account> accounts;
		
		public MainSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.main_fragment, container,
					false);
			progress = (ProgressBar) rootView.findViewById(R.id.accounts_load_progress);
			progress.animate();
			
			createAccountsList();
			
			return rootView;
		}

		private void createAccountsList() {
			APIRequestHandler handler = new APIRequestHandler(getActivity(), APIRequestHandler.ACCOUNTS_REQUEST);
			//TODO Fix so credentials get fetched from SharedPrefs or Intent
			handler.setLoginInfo("olaf", "welcome1");
			handler.setAsyncDelegate(this);
			handler.execute();
		}

		@Override
		public void processFinish(String json) {
			ObjectMapper mapper = new ObjectMapper();
			AccountsCollection accounts = null;
			try {
				accounts = mapper.readValue(json, AccountsCollection.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class SideDrawer extends ListView {
		private String[] listOptions;
		private DrawerLayout drawerLayout;
		private ListView listView;

		public SideDrawer(Context context, Activity act, ListView listView,
				DrawerLayout layout) {
			super(context);

			listOptions = getResources().getStringArray(
					R.array.drawer_list_names);
			Log.i("DrawerList", "List Size " + listOptions.length);
			drawerLayout = layout;
			this.listView = listView;
			this.listView.setAdapter(new ArrayAdapter<String>(context,
					R.layout.drawer_list_item, listOptions));

			this.listView.setOnItemClickListener(new DrawerItemClickListener(
					context));

			setUpActionbar(act);
		}

		private void setUpActionbar(Activity act) {
			drawerToggle = new ActionBarDrawerToggle(act, drawerLayout,
					R.drawable.ic_drawer, R.string.open_drawer_description,
					R.string.close_drawer_description) {
				/**
				 * Called when a drawer has settled in a completely closed
				 * state.
				 */
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(R.string.app_name_short);
				}

				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(R.string.drawer_open);
				}
			};

			drawerLayout.setDrawerListener(drawerToggle);

			act.getActionBar().setDisplayHomeAsUpEnabled(true);
			act.getActionBar().setHomeButtonEnabled(true);
		}

		class DrawerItemClickListener implements ListView.OnItemClickListener {
			private Context mCtx;

			public DrawerItemClickListener(Context ctx) {
				mCtx = ctx;
			}

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == listOptions.length - 1) {
					finish();
				} else if (position == 2) {
					APIRequestHandler req = new APIRequestHandler(act,
							APIRequestHandler.DEBUG_MODE);
					req.execute();
				}
				Toast.makeText(mCtx, "Clicked Item", Toast.LENGTH_SHORT).show();
			}

		}
	}
}