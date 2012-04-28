package ch.android;

import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class BridgeListActivity extends ListActivity {
	private static final String CLASSTAG = BridgeListActivity.class.getSimpleName();
	
	private TextView empty;
	private ProgressDialog progressDialog;
	private BridgeAdapter bridgeAdapter;
	private List<Bridge> bridges;
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg){
			
			if(Debug.ON) {
				Log.v(Constants.LOGTAG, " " + BridgeListActivity.CLASSTAG + " worker thread done, setup BridgeAdapter");
			}
			
			progressDialog.dismiss();
			
			if(bridges == null || bridges.size() == 0) {
				empty.setText("Could not load data");
			} else {
				bridgeAdapter = new BridgeAdapter(BridgeListActivity.this, bridges);
				setListAdapter(bridgeAdapter);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Debug.ON) {
			Log.v(Constants.LOGTAG, " " + BridgeListActivity.CLASSTAG + " onCreate");
		}
		
		setContentView(R.layout.bridge_list);
		empty = (TextView)findViewById(R.id.empty);
		
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(empty);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(Debug.ON) {
			Log.v(Constants.LOGTAG, " " + BridgeListActivity.CLASSTAG + " onResume");
		}
		
		loadBridges();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		menu.add(
				0, // group
				CanalApplication.VIEW_MENU_OPTION_SWITCH_VIEW, //item id
				0, // order
				getApplicationContext().getResources().getString(R.string.view_map_text) // title
			);
			
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case CanalApplication.VIEW_MENU_OPTION_SWITCH_VIEW:
			
			String intentName = getApplicationContext().getResources().getString(R.string.map_view_value);
			
			// save the preference here
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String key = getApplicationContext().getResources().getString(R.string.view_preference);
			prefs.edit().putString(key, intentName).commit();
			
			Intent intent = new Intent(intentName);
			startActivity(intent);
		}
		return true;
	}
	
	private void loadBridges(){
		if(Debug.ON) {
			Log.v(Constants.LOGTAG, " " + BridgeListActivity.CLASSTAG + " loadBridges");
		}
		
		progressDialog = ProgressDialog.show(this, " Working...", "Retrieving Bridge List", true, false);
		
		new Thread() {
			@Override
			public void run() {
				BridgeStatusFetcher fetcher = new BridgeStatusFetcher();
				bridges = fetcher.getBridgeStatuses();
				
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
