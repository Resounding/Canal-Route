package ch.android;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BridgeAdapter extends BaseAdapter {
	
	private static final String CLASSTAG = BridgeAdapter.class.getSimpleName();
	private final Context context;
	private final List<Bridge> bridges;
	
	public BridgeAdapter(Context context, List<Bridge> bridges) {
		this.context = context;
		this.bridges = bridges;
		
		if(Debug.ON) {
			Log.v(Constants.LOGTAG, " " + BridgeAdapter.CLASSTAG + " bridge count - " + bridges.size());
		}
	}

	@Override
	public int getCount() {
		return bridges.size();
	}

	@Override
	public Object getItem(int position) {
		return bridges.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Bridge bridge = bridges.get(position);
		return new BridgeListView(this.context, bridge);
	}

	private final class BridgeListView extends LinearLayout {
		
		private TextView txtBridgeName;
		private TextView txtStatus;
		ImageView imgIcon;
		
		public BridgeListView(Context context, Bridge bridge){
			
			super(context);
			Boolean isAvailable = bridge.getStatus().equals("Available");
		
			setOrientation(LinearLayout.VERTICAL);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
			);
			params.setMargins(5, 3, 5, 0);
			
			txtBridgeName = new TextView(context);
			txtBridgeName.setText(bridge.getDescription());
			txtBridgeName.setTextSize(16f);
			txtBridgeName.setTextColor(Color.WHITE);
			addView(txtBridgeName, params);
			
			txtStatus = new TextView(context);
			txtStatus.setText(bridge.getStatus());
			txtStatus.setTextSize(16f);
			if(isAvailable) {
				txtStatus.setTextColor(Color.GREEN);
			} else {
				txtStatus.setTextColor(Color.RED);
			}
			addView(txtStatus, params);
			
			imgIcon = new ImageView(context);
			imgIcon.setAdjustViewBounds(true);
			imgIcon.setLayoutParams(params);
			if(isAvailable) {
				imgIcon.setImageResource(R.drawable.available);
			} else {
				imgIcon.setImageResource(R.drawable.unavailable);
			}
			addView(imgIcon, params);
		}
	}
}
