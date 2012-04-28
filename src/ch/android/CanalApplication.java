package ch.android;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class CanalApplication extends Application {
	
	public final static int VIEW_MENU_OPTION_SWITCH_VIEW = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
			
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		Resources resources = getApplicationContext().getResources();
		String key = resources.getString(R.string.view_preference); 
		String def = resources.getString(R.string.view_preference_default);
		String view = prefs.getString(key, def);
				
		Intent intent = new Intent(view);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);		
	}
}
