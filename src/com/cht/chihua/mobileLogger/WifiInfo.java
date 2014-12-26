package com.cht.chihua.mobileLogger;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfo {
	private WifiManager wifiManager;

	public List<ScanResult> scanReslutList;
	public List<String> wifiResult;
	
	public WifiInfo(WifiManager wifiManager){
		this.wifiManager = wifiManager;
		wifiResult = new ArrayList<String>();
	}

	public List<String> getWifiInfo(){
		wifiManager.startScan();
		scanReslutList = wifiManager.getScanResults();

		parseWifiScanResult();

		return wifiResult;
	}
	
	private void parseWifiScanResult(){
		if (scanReslutList != null) {
			final int size = scanReslutList.size();
			for(int i=0; i < size; i++){
				ScanResult result = scanReslutList.get(i);
				
				// result level returns the received signal strength of 802.11 network
				wifiResult.add(result.BSSID+"\t" + result.level);
			}
		}
	}
}
