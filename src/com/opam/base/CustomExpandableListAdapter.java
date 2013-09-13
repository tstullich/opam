package com.opam.base;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opam.request.types.Account;
import com.opam.request.types.AccountToken;
import com.opam.request.types.CheckInStatus;
import com.tim.stullich.drawerapp.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements AsyncResponse{

	private Context _context;
	private Activity _act;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private List<Account> _listDataChild;
    private AsyncResponse delegate;
    private boolean checkIn;
    private int groupPosi;
    
    public CustomExpandableListAdapter(Activity act, Context context, List<String> listDataHeader,
            List<Account> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._act = act;
        delegate = this;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
    	return _listDataChild.get(groupPosition);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	final Account acc = _listDataChild.get(groupPosition);
    	groupPosi = groupPosition;
    	String childText = "Target UID: " + acc.getAccount().getTargetUID();
    	Log.i("OPAM", acc.getAccount().getAccountUID());
    	
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.account_sub_item, null);
        }
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.grp_child);
        txtListChild.setText(childText);
        
        Button b = (Button) convertView.findViewById(R.id.checkout_button);
        if (acc.getAccount().getStatus().equals("checkedIn")) {
        	b.setText(R.string.checkout_button_text);
        }
        else {
        	b.setText(R.string.checkin_button_text);
        }
        b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AlertDialog dialog = buildAlert(acc);
				dialog.show();
			}			        	
        });
        return convertView;
    }
    
    @Override
	public void processFinish(String json) {
    	ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if (!checkIn) {
			AccountToken token = null;
			try {
				token = mapper.readValue(json, AccountToken.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} 	catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("OPAM", "Account Token Generated: " + token.getWrapper().getAccountName() + " " 
				+ token.getWrapper().getAccountPassword());
			AlertDialog.Builder builder = new AlertDialog.Builder(_context);
			builder.setTitle("Account Checked Out Successfully!");
			builder.setMessage("Account Name: " + token.getWrapper().getAccountName() +
				"\n" + "Password: " + token.getWrapper().getAccountPassword());
			builder.setPositiveButton("OK", null);
			builder.create().show();
			_listDataChild.get(groupPosi).getAccount().setStatus("checkedOut");
			notifyDataSetChanged();
			
		}
		else {
			CheckInStatus status = null;
			try {
				status = mapper.readValue(json, CheckInStatus.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (status != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(_context);
				builder.setTitle("Account Checked In Successfully!");
				builder.setMessage("You can check out this account again at any time.");
				builder.setPositiveButton("OK", null);
				builder.create().show();
				_listDataChild.get(groupPosi).getAccount().setStatus("checkedIn");
				notifyDataSetChanged();
			}
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(_context);
				builder.setTitle("There Was A Problem!");
				builder.setMessage("The account was not checked in. Please try again or" +
						" contact your system administrator.");
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
    	return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	Account acc = _listDataChild.get(groupPosition);
    	String headerTitle = acc.getName() + " [" + acc.getAccount().getTargetName() + "]";
    	if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.account_group_row, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.accounts_row_name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    private AlertDialog buildAlert(final Account acc) {
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		if (acc.getAccount().getStatus().equals("checkedIn")) {
			checkIn = false;
			builder.setTitle("Check Out This Account?");
			builder.setMessage("Account Name: " + acc.getName() + "\n"
				+ "Target Name: " + acc.getAccount().getTargetName());
			builder.setPositiveButton(R.string.checkout_dialog_positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APIRequestHandler request = new APIRequestHandler(_act, APIRequestHandler.ACCOUNT_CHECKOUT_REQUEST);
				request.setAccountID(acc.getAccount().getAccountUID(), true);
				request.setAsyncDelegate(delegate);
				request.setLoginInfo("olaf", "welcome1");
				request.execute();
				}
			});
		}
		else {
			checkIn = true;
			builder.setTitle("Check In This Account?");
			builder.setMessage("Account Name: " + acc.getName() + "\n"
				+ "Target Name: " + acc.getAccount().getTargetName());
			builder.setPositiveButton(R.string.checkin_button_text, new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					APIRequestHandler request = new APIRequestHandler(_act, APIRequestHandler.ACCOUNT_CHECKIN_REQUEST);
					request.setAccountID(acc.getAccount().getAccountUID(), false);
					request.setAsyncDelegate(delegate);
					request.setLoginInfo("olaf", "welcome1");
					request.execute();
				}
				
			});
		}
		builder.setNegativeButton("NO", null);
		return builder.create();
    }
}