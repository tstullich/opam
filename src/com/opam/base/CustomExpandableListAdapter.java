package com.opam.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opam.request.types.Account;
import com.opam.request.types.AccountToken;
import com.tim.stullich.drawerapp.R;

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

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements AsyncResponse{

	private Context _context;
	private Activity _act;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private List<Account> _listDataChild;
    private AsyncResponse delegate;
 
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
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        //        .get(childPosititon);
    	return _listDataChild.get(groupPosition);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        //final String childText = (String) getChild(groupPosition, childPosition);
    	final Account acc = _listDataChild.get(groupPosition);
    	String childText = "Target UID: " + acc.getAccount().getTargetUID();
    	Log.i("OPAM", acc.getAccount().getAccountUID());
    	
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.account_sub_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.grp_child);
        txtListChild.setText(childText);
        
        Button b = (Button) convertView.findViewById(R.id.checkout_button);
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
		AccountToken token = null;
		try {
			token = mapper.readValue(json, AccountToken.class);
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
		Log.i("OPAM", "Account Token Generated: " + token.getWrapper().getAccountName() + " " 
				+ token.getWrapper().getAccountPassword());
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setTitle("Account Checked Out Successfully!");
		builder.setMessage("Account Name: " + token.getWrapper().getAccountName() +
				"\n" + "Password: " + token.getWrapper().getAccountPassword());
		builder.setPositiveButton("OK", null);
		builder.create().show();
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        //        .size();
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
        //final String headerTitle = (String) getGroup(groupPosition);
    	Account acc = _listDataChild.get(groupPosition);
    	String headerTitle = acc.getName();
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
			builder.setTitle("Check Out This Account?");
			builder.setMessage("Account Name: " + acc.getName() + "\n"
				+ "Target Name: " + acc.getAccount().getTargetName());
			builder.setPositiveButton(R.string.checkout_dialog_positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APIRequestHandler request = new APIRequestHandler(_act, APIRequestHandler.ACCOUNT_CHECKOUT_REQUEST);
				request.setAccountID(acc.getAccount().getAccountUID());
				request.setAsyncDelegate(delegate);
				request.setLoginInfo("olaf", "welcome1");
				request.execute();
				}
			});
		}
		else {
			builder.setTitle("Check In ");
		}
		builder.setNegativeButton("NO", null);
		return builder.create();
    }
}
