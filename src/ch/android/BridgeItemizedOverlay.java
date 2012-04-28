package ch.android;

import java.util.*;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class BridgeItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private static final String CLASSTAG = BridgeItemizedOverlay.class.getSimpleName();

	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public BridgeItemizedOverlay(Drawable defaultMarker, List<Bridge> bridges, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		
		addBridges(bridges);
	}

	private void addBridges(List<Bridge> bridges) {
		for(int i = 0; i<bridges.size(); i++) {
			GeoPoint point;
			OverlayItem item;			

			Bridge bridge = bridges.get(i);
			String description = bridge.getDescription();
			String status = bridge.getStatus();
			String text = description + "\n" + status;
			Boolean isAvailable = status.equals("Available");
			
			if(Debug.ON) {
				Log.v(Constants.LOGTAG, " " + BridgeItemizedOverlay.CLASSTAG + " bridge: " + description);
			}
			
			if(description.startsWith("Bridge 19A")) { // mellanby ave, pt colborne
				point = new GeoPoint((int)(42.896513 * 1E6), (int)(-79.246509 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 19")) { // main st, pt colborne
				point = new GeoPoint((int)(42.901559 * 1E6), (int)(-79.245243 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 21")) { // clarence st, pt colborne
				point = new GeoPoint((int)(42.886373 * 1E6), (int)(-79.248955 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 11")) { // thorold
				point = new GeoPoint((int) (43.076506 * 1E6), (int) (-79.210439 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 1")) { // lakeshore
				point = new GeoPoint((int) (43.216119 * 1E6), (int) (-79.21252 * 1E6));
				item = new OverlayItem(point, text, null);				
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 3")) { // carleton
				point = new GeoPoint((int) (43.19185 * 1E6), (int) (-79.201341 * 1E6));
				item = new OverlayItem(point, text, null);				
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 4")) { // queenston
				point = new GeoPoint((int) (43.165831 * 1E6), (int) (-79.194909 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			} else if(description.startsWith("Bridge 5")) { // glendale
				point = new GeoPoint((int) (43.145258 * 1E6), (int) (-79.192312 * 1E6));
				item = new OverlayItem(point, text, null);
				addOverlayItem(item, isAvailable);
			}
		}
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		Toast.makeText(mContext, getItem(index).getTitle(), Toast.LENGTH_LONG).show();
		return true;
	}
	
	public void addOverlayItem(OverlayItem overlayItem) {
		mOverlays.add(overlayItem);
		populate();
	} 
	
	public void addOverlayItem(OverlayItem overlayItem, Drawable marker) {
		boundCenterBottom(marker);
		overlayItem.setMarker(marker);
		this.addOverlayItem(overlayItem);		
	}
	
	private void addOverlayItem(OverlayItem overlayItem, Boolean isAvailable) {
		Drawable marker = isAvailable ?
				mContext.getResources().getDrawable(R.drawable.map_icon_car) :
				mContext.getResources().getDrawable(R.drawable.map_icon_ship);
		addOverlayItem(overlayItem, marker);
	}
	
	public GeoPoint getCenter()
	{
		return new GeoPoint((int) (43.156984 * 1E6), (int) (-79.193 * 1E6));
	}
}
