package ch.android;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.tidy.*;

import android.util.Log;

public class BridgeStatusFetcher {
	private static final String CLASSTAG = BridgeStatusFetcher.class.getSimpleName();
	private static final String BASE_URL = "http://www.greatlakes-seaway.com/R2/jsp/NiaBrdgStatus.jsp?language=E";
	
	public BridgeStatusFetcher() {
		if(Debug.ON) {
			Log.v(Constants.LOGTAG, " " + BridgeStatusFetcher.CLASSTAG + " constructor");
		}
	}
	
	public ArrayList<Bridge> getBridgeStatuses() {
		long start = System.currentTimeMillis();
		HttpURLConnection urlConn = null; 
		
		try {
			URL url = new URL(BASE_URL);
			
			urlConn = (HttpURLConnection)url.openConnection();		
			
			if(Debug.ON) {
				long duration = System.currentTimeMillis() - start;
				Log.v(Constants.LOGTAG, " " + BridgeStatusFetcher.CLASSTAG + " retrieved html duration - " + duration);			
			
				int response = urlConn.getResponseCode();
				Log.v(Constants.LOGTAG, " Connection response: " + response);
			}
			
			InputStream stream = urlConn.getInputStream();
			ArrayList<Bridge> bridges = getBridgesFromStream(stream);				
			
			return bridges;
						
		} catch (Exception ex) {
			if(Debug.ON) {
				Log.e(Constants.LOGTAG, " " + BridgeStatusFetcher.CLASSTAG, ex);
			}
			
			return null;
			
		} finally {
			try {
 				urlConn.disconnect();
			} catch(Exception ex) { 
				return null;
			}
		}
	}
	
	private ArrayList<Bridge> getBridgesFromStream(InputStream stream) {
		ArrayList<Bridge> bridges = new ArrayList<Bridge>();
		
		try {
			Tidy tidy = new Tidy();
			Document doc = tidy.parseDOM(stream, null);
			
			Node table = doc.getElementsByTagName("table").item(1);
			NodeList rows = table.getChildNodes();
			for(int i = 1; i < rows.getLength(); i++) {
				Node row = rows.item(i);
				Bridge bridge = getBridgeFromRow(row);
				bridges.add(bridge);
			}		
			
			return bridges;
			
		} catch(Exception ex) {
			
			return bridges;
			
		} finally {
		
			
		}
	}
	
	private static Bridge getBridgeFromRow(Node row) {
		NodeList cells = row.getChildNodes();
		Node cell = cells.item(1);
		String bridgeName = cell.getChildNodes().item(0).getChildNodes().item(0).getNodeValue() +
			"\n" + cell.getChildNodes().item(cell.getChildNodes().getLength()-1).getChildNodes().item(0).getNodeValue();
		String status = cells.item(2).getChildNodes().item(0).getNodeValue().trim();
		String next = cells.item(4).getChildNodes().item(0).getNodeValue();
		String subsequent = cells.item(6).getChildNodes().item(0).getNodeValue();
		
		Bridge bridge = new Bridge(bridgeName, status, next, subsequent);
		return bridge;
	}
}
