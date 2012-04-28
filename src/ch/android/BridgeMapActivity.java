package ch.android;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class BridgeMapActivity extends MapActivity {
	private static final String CLASSTAG = MapActivity.class.getSimpleName();

	private List<Bridge> bridges;
	private ProgressDialog progressDialog;
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg){
			
			if(Debug.ON) {
				Log.v(Constants.LOGTAG, " " + BridgeMapActivity.CLASSTAG + " worker thread done, setup BridgeOverlay");
			}
			
			progressDialog.dismiss();
			
			if(bridges == null || bridges.size() == 0) {
				Toast.makeText(BridgeMapActivity.this, "Could not load data", Toast.LENGTH_LONG).show();
			} else {
				Drawable ship = BridgeMapActivity.this.getResources().getDrawable(R.drawable.map_icon_ship);
				BridgeItemizedOverlay bridgeOverlay = new BridgeItemizedOverlay(ship, bridges, BridgeMapActivity.this);
				
				MapView mapview = (MapView)findViewById(R.id.mapview);
				mapview.getOverlays().add(bridgeOverlay);
				MapController mc = mapview.getController();
				
				mc.setCenter(bridgeOverlay.getCenter());
				int lat = bridgeOverlay.getLatSpanE6();
				int lon = bridgeOverlay.getLonSpanE6();
				mc.zoomToSpan(lat, lon);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
				
		MapView mapview = (MapView)findViewById(R.id.mapview);
		
		mapview.setBuiltInZoomControls(true);
		mapview.setSatellite(true);
		mapview.setEnabled(true);
		loadBridges();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		menu.add(
				0, // group
				CanalApplication.VIEW_MENU_OPTION_SWITCH_VIEW, //item id
				0, // order
				getApplicationContext().getResources().getString(R.string.view_list_text) // title
			);
			
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
		case CanalApplication.VIEW_MENU_OPTION_SWITCH_VIEW:
			
			String intentName = getApplicationContext().getResources().getString(R.string.list_view_value);
			
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
			Log.v(Constants.LOGTAG, " " + BridgeMapActivity.CLASSTAG + " loadBridges");
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
