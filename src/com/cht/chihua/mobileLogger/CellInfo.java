package com.cht.chihua.mobileLogger;

import java.util.List;

import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellInfo {
	private TelephonyManager telephonyManager;

	public GsmCellLocation cellLocation;

	public String networkOperator = "", mcc = "-1", mnc = "-1";
	
	public CellInfo(TelephonyManager telephonyManager){
		this.telephonyManager = telephonyManager;

		try{
			networkOperator = telephonyManager.getNetworkOperator();
			mcc = networkOperator.substring(0, 3);
			mnc = networkOperator.substring(3);
		}
		catch(Exception e){
			networkOperator = "";
			mcc = "-1";
			mnc = "-1";
		}
	}
	
	public String getCellInfo(){
		cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

		String cid = String.valueOf(cellLocation.getCid());
		String lac = String.valueOf(cellLocation.getLac());

		return lac+"_"+cid+"\t"+0;
	}
}
