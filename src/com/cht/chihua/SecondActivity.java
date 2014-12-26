/*
 * Copyright (c) 2013. Chunghwa Telecom Co., Ltd.
 * Multimedia Applications Laboratory.
 *
 * The sample code here is provided on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, whether express or implied,
 * including any implied warranty of merchantability, fitness for
 * a particular purpose or non-infringement.
 */

package com.cht.chihua;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cht.chihua.database.SDCardSQLiteOpenHelper;
import com.cht.chihua.mobileLogger.CellInfo;
import com.cht.chihua.mobileLogger.WifiInfo;
import com.cht.lab.ar.SPARDisplay;
import com.cht.lab.ar.SPCameraManager;
import com.cht.lab.ar.SPDisplayHelper;
import com.cht.lab.ar.SPFile;
import com.cht.lab.ar.adapter.SPJSONAdapter;
import com.cht.lab.ar.map.SPLatLng;
import com.google.android.maps.GeoPoint;

public class SecondActivity extends Activity implements SPCameraManager.PortraitDisplayCallback, SPDisplayHelper.EventCallback {
	private static final String TAG = SecondActivity.class.getSimpleName();
	private static final boolean LOCAL_LOGD = true;
	
	private Context mcContext;
	private SDCardSQLiteOpenHelper mcSQLiteDBHelper;

	private boolean control = false;
	private SPJSONAdapter adapter;
	private SPDisplayHelper mDisplayHelper;

	private View mPortView;

	private GeoPoint mUserPoint;
	private JSONArray mResult;

	private TextView mWatch;

	private CellInfo cellInfo;
	private WifiInfo wifiInfo;

	private boolean loop = true;
	private long sleepCycleTime = 3000;
	
	private int CellNum = 254;
	private double RSSI[] = new double[CellNum];

	final Handler UiHandler = new Handler();
	MyLocation location;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onCreate");
		}
		super.onCreate(savedInstanceState);

		mcContext = this;
		initManager();

		mDisplayHelper = new SPDisplayHelper(this, ((MyApplication) getApplication()).getArDisplayConfiguration());

		// 在setContentView之前執行，用以初始化camera及全螢幕的狀態
		mDisplayHelper.initBeforeSetContentView();

		// 在portrait模式讓UI的fill_parent完成作用
		mDisplayHelper.changeToPortrait();

		setContentView(R.layout.ar_main);

		mDisplayHelper.initAfterSetContentView(SPDisplayHelper.TYPE_SEARCH, R.layout.search);

		mPortView = findViewById(R.id.ar_port);

		// 設定事件的回傳處理的實作物件
		mDisplayHelper.setEventCallback(this);
		// 設定camera開啟失敗的回傳處理實作物件
		mDisplayHelper.setPortraitDisplayCallback(this);

		// 初始化AR所需的資料
//		mUserPoint = new SPLatLng(25.033692, 121.545202).toGeoPoint();
		mUserPoint = new SPLatLng(24.953711, 121.165493).toGeoPoint();


		JSONObject result;
		try {
			result = new JSONObject(new String(SPFile.readData(getResources().openRawResource(R.raw.test_data))));
		} catch (Exception e) {
			result = null;
		}

		mResult = result.optJSONObject("ResultSet").optJSONArray("Result");

		if (LOCAL_LOGD) {
			Log.d(TAG, "User Point:" + mUserPoint.toString());
			Log.d(TAG, "Before adapter:" + mResult.toString());
		}

		adapter = new SPJSONAdapter(mResult, mUserPoint, SPARDisplay.POI_DISTANCE, true, true, 10);

		if (LOCAL_LOGD) {
			Log.d(TAG, "After adapter (origin):" + mResult.toString());
			Log.d(TAG, "After adapter (sorted):" + adapter.getAllItems().toString());
		}

//		mDisplayHelper.setAdapter(adapter);
		
		location = new MyLocation(mUserPoint);
		control = true;
		location.start();		
	}
	
	class MyLocation extends Thread{
		public MyLocation(GeoPoint point){
			mUserPoint = point;
		}
		
		public void run(){
			try {
				while(control){
					List<Double>location = getLocation();
					mUserPoint = new SPLatLng(location.get(0), location.get(1)).toGeoPoint();
					adapter = new SPJSONAdapter(mResult, mUserPoint, SPARDisplay.POI_DISTANCE, true, true, 10);
					Log.e("location", adapter.getUserLocation().toString());
					mDisplayHelper.setAdapter(adapter);
					
					Thread.sleep(sleepCycleTime);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onResume");
		}
		super.onResume();

		// 啟動方位角監聽處理
		mDisplayHelper.startOrientationListener();
		// 啟動定位監聽處理器
//		mDisplayHelper.startLocationListener();
		location = new MyLocation(mUserPoint);
		control = true;
		location.start();
		// 將已經初始化完成的portrait UI該為landscape以便開啟camera
		mDisplayHelper.changeToLandscape();
	}

	@Override
	protected void onPause() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onPause");
		}
		super.onPause();
		// 關閉方位角監聽處理
		mDisplayHelper.stopOrientationListener();
		// 關閉定位監聽處理器
//		mDisplayHelper.stopLocationListener();
		control = false;
		// 關閉照相機資源
		mDisplayHelper.stopCamera();
	}

	@Override
	public void onPortraitDisplayFail() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onPortraitDisplayFail");
		}
		// 只會發生在第一次嘗試portrait模式開啟camera失敗後呼叫
		// 這裏改用landscape模式開啟camera, 注意UI同時也會變為landscape, 但會自動轉置為看似portrait的UI
		mDisplayHelper.changeToLandscape();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 避免因portrait/landscape切換造成activity的onCreate重新restart
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onExtraItemOrientationChanged(float angle) {
	}

	@Override
	public void onVisualItemsChanged(Integer[] indices) {
	}

	@Override
	public void onInfoWindowClick(int index) {
//		mWatch.setText(String.format("%s", index));
		mDisplayHelper.refreshRealityView();
		
		try {
			String JSONdata = adapter.getItem(index).toString();
			
			 Intent intent = new Intent();
             intent.setClass(SecondActivity.this, ThirdActivity.class);
             Bundle bundle = new Bundle();
             bundle.putString("JSONdata", JSONdata);
             intent.putExtras(bundle);
             startActivity(intent);
		} catch (Exception e) {
			Log.e("error", e.getMessage());
		}
	}

	private void initManager(){
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		cellInfo = new CellInfo(telephonyManager);
		wifiInfo = new WifiInfo(wifiManager);
		
		mcSQLiteDBHelper = new SDCardSQLiteOpenHelper(mcContext);
	}

	private List<Double> getLocation() {
		initRSSI();

		return callDB();
	}
	
	private void initRSSI(){
		for(int i = 0; i < CellNum; i++){
			RSSI[i] = -120;
		}
	}

	private List<Double> callDB() {
		List<Double>locationData = new ArrayList<Double>();
		
//		String cell_temp = cellInfo.getCellInfo();
		String cell_temp = "12345\t-1";
		String cell_info[] = cell_temp.split("\t");
		String msSQL = "SELECT id FROM mapping_table WHERE ap_cell = '" + cell_info[0] + "'";
		Cursor mcCursor = mcSQLiteDBHelper.select(msSQL);
		if(mcCursor.moveToNext()){
			RSSI[mcCursor.getInt(0) - 1] = Double.parseDouble(cell_info[1]);
		}
		
		List<String> ap_temp = wifiInfo.getWifiInfo();
		for(int i = 0; i < ap_temp.size(); i++){
			String ap_info[] = ap_temp.get(i).split("\t");
			msSQL = "SELECT id FROM mapping_table WHERE ap_cell = '" + ap_info[0] + "'";
			mcCursor = mcSQLiteDBHelper.select(msSQL);
			if(mcCursor.moveToNext()){
				RSSI[mcCursor.getInt(0) - 1] = Double.parseDouble(ap_info[1]);
			}
		}
		
		msSQL = "SELECT lat, lon, ("
				+ "(cell_1 - " + RSSI[0] + ")*(cell_1 - " + RSSI[0] + ") + "
				+ "(cell_2 - " + RSSI[1] + ")*(cell_2 - " + RSSI[1] + ") + "
				+ "(cell_3 - " + RSSI[2] + ")*(cell_3 - " + RSSI[2] + ") + "
				+ "(cell_4 - " + RSSI[3] + ")*(cell_4 - " + RSSI[3] + ") + "
				+ "(cell_5 - " + RSSI[4] + ")*(cell_5 - " + RSSI[4] + ") + "
				+ "(cell_6 - " + RSSI[5] + ")*(cell_6 - " + RSSI[5] + ") + "
				+ "(cell_7 - " + RSSI[6] + ")*(cell_7 - " + RSSI[6] + ") + "
				+ "(cell_8 - " + RSSI[7] + ")*(cell_8 - " + RSSI[7] + ") + "
				+ "(cell_9 - " + RSSI[8] + ")*(cell_9 - " + RSSI[8] + ") + "
				+ "(cell_10 - " + RSSI[9] + ")*(cell_10 - " + RSSI[9] + ") + "
				+ "(cell_11 - " + RSSI[10] + ")*(cell_11 - " + RSSI[10] + ") + "
				+ "(cell_12 - " + RSSI[11] + ")*(cell_12 - " + RSSI[11] + ") + "
				+ "(cell_13 - " + RSSI[12] + ")*(cell_13 - " + RSSI[12] + ") + "
				+ "(cell_14 - " + RSSI[13] + ")*(cell_14 - " + RSSI[13] + ") + "
				+ "(cell_15 - " + RSSI[14] + ")*(cell_15 - " + RSSI[14] + ") + "
				+ "(cell_16 - " + RSSI[15] + ")*(cell_16 - " + RSSI[15] + ") + "
				+ "(cell_17 - " + RSSI[16] + ")*(cell_17 - " + RSSI[16] + ") + "
				+ "(cell_18 - " + RSSI[17] + ")*(cell_18 - " + RSSI[17] + ") + "
				+ "(cell_19 - " + RSSI[18] + ")*(cell_19 - " + RSSI[18] + ") + "
				+ "(cell_20 - " + RSSI[19] + ")*(cell_20 - " + RSSI[19] + ") + "
				+ "(cell_21 - " + RSSI[20] + ")*(cell_21 - " + RSSI[20] + ") + "
				+ "(cell_22 - " + RSSI[21] + ")*(cell_22 - " + RSSI[21] + ") + "
				+ "(cell_23 - " + RSSI[22] + ")*(cell_23 - " + RSSI[22] + ") + "
				+ "(cell_24 - " + RSSI[23] + ")*(cell_24 - " + RSSI[23] + ") + "
				+ "(cell_25 - " + RSSI[24] + ")*(cell_25 - " + RSSI[24] + ") + "
				+ "(cell_26 - " + RSSI[25] + ")*(cell_26 - " + RSSI[25] + ") + "
				+ "(cell_27 - " + RSSI[26] + ")*(cell_27 - " + RSSI[26] + ") + "
				+ "(cell_28 - " + RSSI[27] + ")*(cell_28 - " + RSSI[27] + ") + "
				+ "(cell_29 - " + RSSI[28] + ")*(cell_29 - " + RSSI[28] + ") + "
				+ "(cell_30 - " + RSSI[29] + ")*(cell_30 - " + RSSI[29] + ") + "
				+ "(cell_31 - " + RSSI[30] + ")*(cell_31 - " + RSSI[30] + ") + "
				+ "(cell_32 - " + RSSI[31] + ")*(cell_32 - " + RSSI[31] + ") + "
				+ "(cell_33 - " + RSSI[32] + ")*(cell_33 - " + RSSI[32] + ") + "
				+ "(cell_34 - " + RSSI[33] + ")*(cell_34 - " + RSSI[33] + ") + "
				+ "(cell_35 - " + RSSI[34] + ")*(cell_35 - " + RSSI[34] + ") + "
				+ "(cell_36 - " + RSSI[35] + ")*(cell_36 - " + RSSI[35] + ") + "
				+ "(cell_37 - " + RSSI[36] + ")*(cell_37 - " + RSSI[36] + ") + "
				+ "(cell_38 - " + RSSI[37] + ")*(cell_38 - " + RSSI[37] + ") + "
				+ "(cell_39 - " + RSSI[38] + ")*(cell_39 - " + RSSI[38] + ") + "
				+ "(cell_40 - " + RSSI[39] + ")*(cell_40 - " + RSSI[39] + ") + "
				+ "(cell_41 - " + RSSI[40] + ")*(cell_41 - " + RSSI[40] + ") + "
				+ "(cell_42 - " + RSSI[41] + ")*(cell_42 - " + RSSI[41] + ") + "
				+ "(cell_43 - " + RSSI[42] + ")*(cell_43 - " + RSSI[42] + ") + "
				+ "(cell_44 - " + RSSI[43] + ")*(cell_44 - " + RSSI[43] + ") + "
				+ "(cell_45 - " + RSSI[44] + ")*(cell_45 - " + RSSI[44] + ") + "
				+ "(cell_46 - " + RSSI[45] + ")*(cell_46 - " + RSSI[45] + ") + "
				+ "(cell_47 - " + RSSI[46] + ")*(cell_47 - " + RSSI[46] + ") + "
				+ "(cell_48 - " + RSSI[47] + ")*(cell_48 - " + RSSI[47] + ") + "
				+ "(cell_49 - " + RSSI[48] + ")*(cell_49 - " + RSSI[48] + ") + "
				+ "(cell_50 - " + RSSI[49] + ")*(cell_50 - " + RSSI[49] + ") + "
				+ "(cell_51 - " + RSSI[50] + ")*(cell_51 - " + RSSI[50] + ") + "
				+ "(cell_52 - " + RSSI[51] + ")*(cell_52 - " + RSSI[51] + ") + "
				+ "(cell_53 - " + RSSI[52] + ")*(cell_53 - " + RSSI[52] + ") + "
				+ "(cell_54 - " + RSSI[53] + ")*(cell_54 - " + RSSI[53] + ") + "
				+ "(cell_55 - " + RSSI[54] + ")*(cell_55 - " + RSSI[54] + ") + "
				+ "(cell_56 - " + RSSI[55] + ")*(cell_56 - " + RSSI[55] + ") + "
				+ "(cell_57 - " + RSSI[56] + ")*(cell_57 - " + RSSI[56] + ") + "
				+ "(cell_58 - " + RSSI[57] + ")*(cell_58 - " + RSSI[57] + ") + "
				+ "(cell_59 - " + RSSI[58] + ")*(cell_59 - " + RSSI[58] + ") + "
				+ "(cell_60 - " + RSSI[59] + ")*(cell_60 - " + RSSI[59] + ") + "
				+ "(cell_61 - " + RSSI[60] + ")*(cell_61 - " + RSSI[60] + ") + "
				+ "(cell_62 - " + RSSI[61] + ")*(cell_62 - " + RSSI[61] + ") + "
				+ "(cell_63 - " + RSSI[62] + ")*(cell_63 - " + RSSI[62] + ") + "
				+ "(cell_64 - " + RSSI[63] + ")*(cell_64 - " + RSSI[63] + ") + "
				+ "(cell_65 - " + RSSI[64] + ")*(cell_65 - " + RSSI[64] + ") + "
				+ "(cell_66 - " + RSSI[65] + ")*(cell_66 - " + RSSI[65] + ") + "
				+ "(cell_67 - " + RSSI[66] + ")*(cell_67 - " + RSSI[66] + ") + "
				+ "(cell_68 - " + RSSI[67] + ")*(cell_68 - " + RSSI[67] + ") + "
				+ "(cell_69 - " + RSSI[68] + ")*(cell_69 - " + RSSI[68] + ") + "
				+ "(cell_70 - " + RSSI[69] + ")*(cell_70 - " + RSSI[69] + ") + "
				+ "(cell_71 - " + RSSI[70] + ")*(cell_71 - " + RSSI[70] + ") + "
				+ "(cell_72 - " + RSSI[71] + ")*(cell_72 - " + RSSI[71] + ") + "
				+ "(cell_73 - " + RSSI[72] + ")*(cell_73 - " + RSSI[72] + ") + "
				+ "(cell_74 - " + RSSI[73] + ")*(cell_74 - " + RSSI[73] + ") + "
				+ "(cell_75 - " + RSSI[74] + ")*(cell_75 - " + RSSI[74] + ") + "
				+ "(cell_76 - " + RSSI[75] + ")*(cell_76 - " + RSSI[75] + ") + "
				+ "(cell_77 - " + RSSI[76] + ")*(cell_77 - " + RSSI[76] + ") + "
				+ "(cell_78 - " + RSSI[77] + ")*(cell_78 - " + RSSI[77] + ") + "
				+ "(cell_79 - " + RSSI[78] + ")*(cell_79 - " + RSSI[78] + ") + "
				+ "(cell_80 - " + RSSI[79] + ")*(cell_80 - " + RSSI[79] + ") + "
				+ "(cell_81 - " + RSSI[80] + ")*(cell_81 - " + RSSI[80] + ") + "
				+ "(cell_82 - " + RSSI[81] + ")*(cell_82 - " + RSSI[81] + ") + "
				+ "(cell_83 - " + RSSI[82] + ")*(cell_83 - " + RSSI[82] + ") + "
				+ "(cell_84 - " + RSSI[83] + ")*(cell_84 - " + RSSI[83] + ") + "
				+ "(cell_85 - " + RSSI[84] + ")*(cell_85 - " + RSSI[84] + ") + "
				+ "(cell_86 - " + RSSI[85] + ")*(cell_86 - " + RSSI[85] + ") + "
				+ "(cell_87 - " + RSSI[86] + ")*(cell_87 - " + RSSI[86] + ") + "
				+ "(cell_88 - " + RSSI[87] + ")*(cell_88 - " + RSSI[87] + ") + "
				+ "(cell_89 - " + RSSI[88] + ")*(cell_89 - " + RSSI[88] + ") + "
				+ "(cell_90 - " + RSSI[89] + ")*(cell_90 - " + RSSI[89] + ") + "
				+ "(cell_91 - " + RSSI[90] + ")*(cell_91 - " + RSSI[90] + ") + "
				+ "(cell_92 - " + RSSI[91] + ")*(cell_92 - " + RSSI[91] + ") + "
				+ "(cell_93 - " + RSSI[92] + ")*(cell_93 - " + RSSI[92] + ") + "
				+ "(cell_94 - " + RSSI[93] + ")*(cell_94 - " + RSSI[93] + ") + "
				+ "(cell_95 - " + RSSI[94] + ")*(cell_95 - " + RSSI[94] + ") + "
				+ "(cell_96 - " + RSSI[95] + ")*(cell_96 - " + RSSI[95] + ") + "
				+ "(cell_97 - " + RSSI[96] + ")*(cell_97 - " + RSSI[96] + ") + "
				+ "(cell_98 - " + RSSI[97] + ")*(cell_98 - " + RSSI[97] + ") + "
				+ "(cell_99 - " + RSSI[98] + ")*(cell_99 - " + RSSI[98] + ") + "
				+ "(cell_100 - " + RSSI[99] + ")*(cell_100 - " + RSSI[99] + ") + "
				+ "(cell_101 - " + RSSI[100] + ")*(cell_101 - " + RSSI[100] + ") + "
				+ "(cell_102 - " + RSSI[101] + ")*(cell_102 - " + RSSI[101] + ") + "
				+ "(cell_103 - " + RSSI[102] + ")*(cell_103 - " + RSSI[102] + ") + "
				+ "(cell_104 - " + RSSI[103] + ")*(cell_104 - " + RSSI[103] + ") + "
				+ "(cell_105 - " + RSSI[104] + ")*(cell_105 - " + RSSI[104] + ") + "
				+ "(cell_106 - " + RSSI[105] + ")*(cell_106 - " + RSSI[105] + ") + "
				+ "(cell_107 - " + RSSI[106] + ")*(cell_107 - " + RSSI[106] + ") + "
				+ "(cell_108 - " + RSSI[107] + ")*(cell_108 - " + RSSI[107] + ") + "
				+ "(cell_109 - " + RSSI[108] + ")*(cell_109 - " + RSSI[108] + ") + "
				+ "(cell_110 - " + RSSI[109] + ")*(cell_110 - " + RSSI[109] + ") + "
				+ "(cell_111 - " + RSSI[110] + ")*(cell_111 - " + RSSI[110] + ") + "
				+ "(cell_112 - " + RSSI[111] + ")*(cell_112 - " + RSSI[111] + ") + "
				+ "(cell_113 - " + RSSI[112] + ")*(cell_113 - " + RSSI[112] + ") + "
				+ "(cell_114 - " + RSSI[113] + ")*(cell_114 - " + RSSI[113] + ") + "
				+ "(cell_115 - " + RSSI[114] + ")*(cell_115 - " + RSSI[114] + ") + "
				+ "(cell_116 - " + RSSI[115] + ")*(cell_116 - " + RSSI[115] + ") + "
				+ "(cell_117 - " + RSSI[116] + ")*(cell_117 - " + RSSI[116] + ") + "
				+ "(cell_118 - " + RSSI[117] + ")*(cell_118 - " + RSSI[117] + ") + "
				+ "(cell_119 - " + RSSI[118] + ")*(cell_119 - " + RSSI[118] + ") + "
				+ "(cell_120 - " + RSSI[119] + ")*(cell_120 - " + RSSI[119] + ") + "
				+ "(cell_121 - " + RSSI[120] + ")*(cell_121 - " + RSSI[120] + ") + "
				+ "(cell_122 - " + RSSI[121] + ")*(cell_122 - " + RSSI[121] + ") + "
				+ "(cell_123 - " + RSSI[122] + ")*(cell_123 - " + RSSI[122] + ") + "
				+ "(cell_124 - " + RSSI[123] + ")*(cell_124 - " + RSSI[123] + ") + "
				+ "(cell_125 - " + RSSI[124] + ")*(cell_125 - " + RSSI[124] + ") + "
				+ "(cell_126 - " + RSSI[125] + ")*(cell_126 - " + RSSI[125] + ") + "
				+ "(cell_127 - " + RSSI[126] + ")*(cell_127 - " + RSSI[126] + ") + "
				+ "(cell_128 - " + RSSI[127] + ")*(cell_128 - " + RSSI[127] + ") + "
				+ "(cell_129 - " + RSSI[128] + ")*(cell_129 - " + RSSI[128] + ") + "
				+ "(cell_130 - " + RSSI[129] + ")*(cell_130 - " + RSSI[129] + ") + "
				+ "(cell_131 - " + RSSI[130] + ")*(cell_131 - " + RSSI[130] + ") + "
				+ "(cell_132 - " + RSSI[131] + ")*(cell_132 - " + RSSI[131] + ") + "
				+ "(cell_133 - " + RSSI[132] + ")*(cell_133 - " + RSSI[132] + ") + "
				+ "(cell_134 - " + RSSI[133] + ")*(cell_134 - " + RSSI[133] + ") + "
				+ "(cell_135 - " + RSSI[134] + ")*(cell_135 - " + RSSI[134] + ") + "
				+ "(cell_136 - " + RSSI[135] + ")*(cell_136 - " + RSSI[135] + ") + "
				+ "(cell_137 - " + RSSI[136] + ")*(cell_137 - " + RSSI[136] + ") + "
				+ "(cell_138 - " + RSSI[137] + ")*(cell_138 - " + RSSI[137] + ") + "
				+ "(cell_139 - " + RSSI[138] + ")*(cell_139 - " + RSSI[138] + ") + "
				+ "(cell_140 - " + RSSI[139] + ")*(cell_140 - " + RSSI[139] + ") + "
				+ "(cell_141 - " + RSSI[140] + ")*(cell_141 - " + RSSI[140] + ") + "
				+ "(cell_142 - " + RSSI[141] + ")*(cell_142 - " + RSSI[141] + ") + "
				+ "(cell_143 - " + RSSI[142] + ")*(cell_143 - " + RSSI[142] + ") + "
				+ "(cell_144 - " + RSSI[143] + ")*(cell_144 - " + RSSI[143] + ") + "
				+ "(cell_145 - " + RSSI[144] + ")*(cell_145 - " + RSSI[144] + ") + "
				+ "(cell_146 - " + RSSI[145] + ")*(cell_146 - " + RSSI[145] + ") + "
				+ "(cell_147 - " + RSSI[146] + ")*(cell_147 - " + RSSI[146] + ") + "
				+ "(cell_148 - " + RSSI[147] + ")*(cell_148 - " + RSSI[147] + ") + "
				+ "(cell_149 - " + RSSI[148] + ")*(cell_149 - " + RSSI[148] + ") + "
				+ "(cell_150 - " + RSSI[149] + ")*(cell_150 - " + RSSI[149] + ") + "
				+ "(cell_151 - " + RSSI[150] + ")*(cell_151 - " + RSSI[150] + ") + "
				+ "(cell_152 - " + RSSI[151] + ")*(cell_152 - " + RSSI[151] + ") + "
				+ "(cell_153 - " + RSSI[152] + ")*(cell_153 - " + RSSI[152] + ") + "
				+ "(cell_154 - " + RSSI[153] + ")*(cell_154 - " + RSSI[153] + ") + "
				+ "(cell_155 - " + RSSI[154] + ")*(cell_155 - " + RSSI[154] + ") + "
				+ "(cell_156 - " + RSSI[155] + ")*(cell_156 - " + RSSI[155] + ") + "
				+ "(cell_157 - " + RSSI[156] + ")*(cell_157 - " + RSSI[156] + ") + "
				+ "(cell_158 - " + RSSI[157] + ")*(cell_158 - " + RSSI[157] + ") + "
				+ "(cell_159 - " + RSSI[158] + ")*(cell_159 - " + RSSI[158] + ") + "
				+ "(cell_160 - " + RSSI[159] + ")*(cell_160 - " + RSSI[159] + ") + "
				+ "(cell_161 - " + RSSI[160] + ")*(cell_161 - " + RSSI[160] + ") + "
				+ "(cell_162 - " + RSSI[161] + ")*(cell_162 - " + RSSI[161] + ") + "
				+ "(cell_163 - " + RSSI[162] + ")*(cell_163 - " + RSSI[162] + ") + "
				+ "(cell_164 - " + RSSI[163] + ")*(cell_164 - " + RSSI[163] + ") + "
				+ "(cell_165 - " + RSSI[164] + ")*(cell_165 - " + RSSI[164] + ") + "
				+ "(cell_166 - " + RSSI[165] + ")*(cell_166 - " + RSSI[165] + ") + "
				+ "(cell_167 - " + RSSI[166] + ")*(cell_167 - " + RSSI[166] + ") + "
				+ "(cell_168 - " + RSSI[167] + ")*(cell_168 - " + RSSI[167] + ") + "
				+ "(cell_169 - " + RSSI[168] + ")*(cell_169 - " + RSSI[168] + ") + "
				+ "(cell_170 - " + RSSI[169] + ")*(cell_170 - " + RSSI[169] + ") + "
				+ "(cell_171 - " + RSSI[170] + ")*(cell_171 - " + RSSI[170] + ") + "
				+ "(cell_172 - " + RSSI[171] + ")*(cell_172 - " + RSSI[171] + ") + "
				+ "(cell_173 - " + RSSI[172] + ")*(cell_173 - " + RSSI[172] + ") + "
				+ "(cell_174 - " + RSSI[173] + ")*(cell_174 - " + RSSI[173] + ") + "
				+ "(cell_175 - " + RSSI[174] + ")*(cell_175 - " + RSSI[174] + ") + "
				+ "(cell_176 - " + RSSI[175] + ")*(cell_176 - " + RSSI[175] + ") + "
				+ "(cell_177 - " + RSSI[176] + ")*(cell_177 - " + RSSI[176] + ") + "
				+ "(cell_178 - " + RSSI[177] + ")*(cell_178 - " + RSSI[177] + ") + "
				+ "(cell_179 - " + RSSI[178] + ")*(cell_179 - " + RSSI[178] + ") + "
				+ "(cell_180 - " + RSSI[179] + ")*(cell_180 - " + RSSI[179] + ") + "
				+ "(cell_181 - " + RSSI[180] + ")*(cell_181 - " + RSSI[180] + ") + "
				+ "(cell_182 - " + RSSI[181] + ")*(cell_182 - " + RSSI[181] + ") + "
				+ "(cell_183 - " + RSSI[182] + ")*(cell_183 - " + RSSI[182] + ") + "
				+ "(cell_184 - " + RSSI[183] + ")*(cell_184 - " + RSSI[183] + ") + "
				+ "(cell_185 - " + RSSI[184] + ")*(cell_185 - " + RSSI[184] + ") + "
				+ "(cell_186 - " + RSSI[185] + ")*(cell_186 - " + RSSI[185] + ") + "
				+ "(cell_187 - " + RSSI[186] + ")*(cell_187 - " + RSSI[186] + ") + "
				+ "(cell_188 - " + RSSI[187] + ")*(cell_188 - " + RSSI[187] + ") + "
				+ "(cell_189 - " + RSSI[188] + ")*(cell_189 - " + RSSI[188] + ") + "
				+ "(cell_190 - " + RSSI[189] + ")*(cell_190 - " + RSSI[189] + ") + "
				+ "(cell_191 - " + RSSI[190] + ")*(cell_191 - " + RSSI[190] + ") + "
				+ "(cell_192 - " + RSSI[191] + ")*(cell_192 - " + RSSI[191] + ") + "
				+ "(cell_193 - " + RSSI[192] + ")*(cell_193 - " + RSSI[192] + ") + "
				+ "(cell_194 - " + RSSI[193] + ")*(cell_194 - " + RSSI[193] + ") + "
				+ "(cell_195 - " + RSSI[194] + ")*(cell_195 - " + RSSI[194] + ") + "
				+ "(cell_196 - " + RSSI[195] + ")*(cell_196 - " + RSSI[195] + ") + "
				+ "(cell_197 - " + RSSI[196] + ")*(cell_197 - " + RSSI[196] + ") + "
				+ "(cell_198 - " + RSSI[197] + ")*(cell_198 - " + RSSI[197] + ") + "
				+ "(cell_199 - " + RSSI[198] + ")*(cell_199 - " + RSSI[198] + ") + "
				+ "(cell_200 - " + RSSI[199] + ")*(cell_200 - " + RSSI[199] + ") + "
				+ "(cell_201 - " + RSSI[200] + ")*(cell_201 - " + RSSI[200] + ") + "
				+ "(cell_202 - " + RSSI[201] + ")*(cell_202 - " + RSSI[201] + ") + "
				+ "(cell_203 - " + RSSI[202] + ")*(cell_203 - " + RSSI[202] + ") + "
				+ "(cell_204 - " + RSSI[203] + ")*(cell_204 - " + RSSI[203] + ") + "
				+ "(cell_205 - " + RSSI[204] + ")*(cell_205 - " + RSSI[204] + ") + "
				+ "(cell_206 - " + RSSI[205] + ")*(cell_206 - " + RSSI[205] + ") + "
				+ "(cell_207 - " + RSSI[206] + ")*(cell_207 - " + RSSI[206] + ") + "
				+ "(cell_208 - " + RSSI[207] + ")*(cell_208 - " + RSSI[207] + ") + "
				+ "(cell_209 - " + RSSI[208] + ")*(cell_209 - " + RSSI[208] + ") + "
				+ "(cell_210 - " + RSSI[209] + ")*(cell_210 - " + RSSI[209] + ") + "
				+ "(cell_211 - " + RSSI[210] + ")*(cell_211 - " + RSSI[210] + ") + "
				+ "(cell_212 - " + RSSI[211] + ")*(cell_212 - " + RSSI[211] + ") + "
				+ "(cell_213 - " + RSSI[212] + ")*(cell_213 - " + RSSI[212] + ") + "
				+ "(cell_214 - " + RSSI[213] + ")*(cell_214 - " + RSSI[213] + ") + "
				+ "(cell_215 - " + RSSI[214] + ")*(cell_215 - " + RSSI[214] + ") + "
				+ "(cell_216 - " + RSSI[215] + ")*(cell_216 - " + RSSI[215] + ") + "
				+ "(cell_217 - " + RSSI[216] + ")*(cell_217 - " + RSSI[216] + ") + "
				+ "(cell_218 - " + RSSI[217] + ")*(cell_218 - " + RSSI[217] + ") + "
				+ "(cell_219 - " + RSSI[218] + ")*(cell_219 - " + RSSI[218] + ") + "
				+ "(cell_220 - " + RSSI[219] + ")*(cell_220 - " + RSSI[219] + ") + "
				+ "(cell_221 - " + RSSI[220] + ")*(cell_221 - " + RSSI[220] + ") + "
				+ "(cell_222 - " + RSSI[221] + ")*(cell_222 - " + RSSI[221] + ") + "
				+ "(cell_223 - " + RSSI[222] + ")*(cell_223 - " + RSSI[222] + ") + "
				+ "(cell_224 - " + RSSI[223] + ")*(cell_224 - " + RSSI[223] + ") + "
				+ "(cell_225 - " + RSSI[224] + ")*(cell_225 - " + RSSI[224] + ") + "
				+ "(cell_226 - " + RSSI[225] + ")*(cell_226 - " + RSSI[225] + ") + "
				+ "(cell_227 - " + RSSI[226] + ")*(cell_227 - " + RSSI[226] + ") + "
				+ "(cell_228 - " + RSSI[227] + ")*(cell_228 - " + RSSI[227] + ") + "
				+ "(cell_229 - " + RSSI[228] + ")*(cell_229 - " + RSSI[228] + ") + "
				+ "(cell_230 - " + RSSI[229] + ")*(cell_230 - " + RSSI[229] + ") + "
				+ "(cell_231 - " + RSSI[230] + ")*(cell_231 - " + RSSI[230] + ") + "
				+ "(cell_232 - " + RSSI[231] + ")*(cell_232 - " + RSSI[231] + ") + "
				+ "(cell_233 - " + RSSI[232] + ")*(cell_233 - " + RSSI[232] + ") + "
				+ "(cell_234 - " + RSSI[233] + ")*(cell_234 - " + RSSI[233] + ") + "
				+ "(cell_235 - " + RSSI[234] + ")*(cell_235 - " + RSSI[234] + ") + "
				+ "(cell_236 - " + RSSI[235] + ")*(cell_236 - " + RSSI[235] + ") + "
				+ "(cell_237 - " + RSSI[236] + ")*(cell_237 - " + RSSI[236] + ") + "
				+ "(cell_238 - " + RSSI[237] + ")*(cell_238 - " + RSSI[237] + ") + "
				+ "(cell_239 - " + RSSI[238] + ")*(cell_239 - " + RSSI[238] + ") + "
				+ "(cell_240 - " + RSSI[239] + ")*(cell_240 - " + RSSI[239] + ") + "
				+ "(cell_241 - " + RSSI[240] + ")*(cell_241 - " + RSSI[240] + ") + "
				+ "(cell_242 - " + RSSI[241] + ")*(cell_242 - " + RSSI[241] + ") + "
				+ "(cell_243 - " + RSSI[242] + ")*(cell_243 - " + RSSI[242] + ") + "
				+ "(cell_244 - " + RSSI[243] + ")*(cell_244 - " + RSSI[243] + ") + "
				+ "(cell_245 - " + RSSI[244] + ")*(cell_245 - " + RSSI[244] + ") + "
				+ "(cell_246 - " + RSSI[245] + ")*(cell_246 - " + RSSI[245] + ") + "
				+ "(cell_247 - " + RSSI[246] + ")*(cell_247 - " + RSSI[246] + ") + "
				+ "(cell_248 - " + RSSI[247] + ")*(cell_248 - " + RSSI[247] + ") + "
				+ "(cell_249 - " + RSSI[248] + ")*(cell_249 - " + RSSI[248] + ") + "
				+ "(cell_250 - " + RSSI[249] + ")*(cell_250 - " + RSSI[249] + ") + "
				+ "(cell_251 - " + RSSI[250] + ")*(cell_251 - " + RSSI[250] + ") + "
				+ "(cell_252 - " + RSSI[251] + ")*(cell_252 - " + RSSI[251] + ") + "
				+ "(cell_253 - " + RSSI[252] + ")*(cell_253 - " + RSSI[252] + ") + "
				+ "(cell_254 - " + RSSI[253] + ")*(cell_254 - " + RSSI[253] + ")"
				+ ") AS dist FROM data_table ORDER BY dist";
		mcCursor = mcSQLiteDBHelper.select(msSQL);
		if(mcCursor.moveToNext()){
			locationData.add(mcCursor.getDouble(0));
			locationData.add(mcCursor.getDouble(1));
		}
		
		return locationData;
	}
}