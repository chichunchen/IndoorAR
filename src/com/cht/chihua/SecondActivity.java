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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cht.chihua.R;
import com.cht.chihua.R.id;
import com.cht.chihua.R.layout;
import com.cht.chihua.R.raw;
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

	private boolean control = false;
	private SPJSONAdapter adapter;
	private SPDisplayHelper mDisplayHelper;

	private View mPortView;

	private GeoPoint mUserPoint;
	private JSONArray mResult;

	private TextView mWatch;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onCreate");
		}
		super.onCreate(savedInstanceState);

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
		
//		try {
//			mResult = new JSONArray(
//				"[{\"u\":\"10459\",\"ad\":\"桃園縣觀音鄉大湖路二段342號\",\"u1\":\"青林休閒農場\",\"c\":\"3\",\"la\":\"24.995533\",\"lo\":\"121.133297\",\"d\":5412,\"aa\":325.28},{\"u\":\"1048271\",\"ad\":\"桃園縣楊梅鎮中山北路一段21巷1號\",\"u1\":\"雅雯魅力博覽館\",\"c\":\"3\",\"la\":\"24.907846\",\"lo\":\"121.183362\",\"d\":5620,\"aa\":159.32},{\"u\":\"1067192\",\"ad\":\"��鿤�������s�_���@�q21��1 ��\",\"u1\":\"���D�y�O�����]\",\"c\":\"3\",\"la\":\"24.908611\",\"lo\":\"121.141388\",\"d\":5648,\"aa\":203.54},{\"u\":\"1067097\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131��\",\"u1\":\"��a�A��\",\"c\":\"3\",\"la\":\"24.997777\",\"lo\":\"121.132055\",\"d\":5688,\"aa\":325.67},{\"u\":\"1050272\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131��\",\"u1\":\"��a���𶢹A��\",\"c\":\"3\",\"la\":\"24.997802\",\"lo\":\"121.131929\",\"d\":5698,\"aa\":325.58},{\"u\":\"1048238\",\"ad\":\"��鿤������\",\"u1\":\"������Q�s����\",\"c\":\"3\",\"la\":\"24.905270\",\"lo\":\"121.148790\",\"d\":5748,\"aa\":195.2},{\"u\":\"106945\",\"ad\":\"��鿤�[���m�jԳ��5�F175��\",\"u1\":\"�L�a�j��𶢹A��\",\"c\":\"3\",\"la\":\"24.998926\",\"lo\":\"121.129149\",\"d\":5961,\"aa\":324.03},{\"u\":\"1067098\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131-1��\",\"u1\":\"�C�L�A��\",\"c\":\"3\",\"la\":\"25.001833\",\"lo\":\"121.130944\",\"d\":6125,\"aa\":327.17},{\"u\":\"1023226\",\"ad\":\"��鿤�[���m�jԳ��184��\",\"u1\":\"�d������𶢹A��\",\"c\":\"3\",\"la\":\"25.000368999999999\",\"lo\":\"121.12837\",\"d\":6137,\"aa\":324.31},{\"u\":\"1067117\",\"ad\":\"��鿤���c�����_���B�忳���f\",\"u1\":\"����ڴβy��\",\"c\":\"3\",\"la\":\"24.999444\",\"lo\":\"121.200861\",\"d\":6155,\"aa\":37.39},{\"u\":\"1021320\",\"ad\":\"��鿤�s�ζm�s�Χ�������110�Ѥ�\",\"u1\":\"�S���j��\",\"c\":\"3\",\"la\":\"24.973969\",\"lo\":\"121.104391\",\"d\":6342,\"aa\":288.93},{\"u\":\"1067089\",\"ad\":\"��鿤�s�ζm������110��\",\"u1\":\"���Ѵ��B�D\",\"c\":\"3\",\"la\":\"24.975916\",\"lo\":\"121.104805\",\"d\":6376,\"aa\":290.89}]");
//		} catch (JSONException e) {
//			mResult = null;
//		}

		// 
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

		mDisplayHelper.setAdapter(adapter);
		
//		MyLocation location = new MyLocation(mUserPoint);
//		location.start();
//		control = true;

//		mPortView.post(new Runnable() {
//			@Override
//			public void run() {
//				mDisplayHelper.goToPage(3);
//			}
//		});

//		mWatch = (TextView) findViewById(R.id.text1);
//
//		findViewById(R.id.navi_btn).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
//			}
//		});
	}
	
	class MyLocation extends Thread{
		public MyLocation(GeoPoint point){
			mUserPoint = point;
		}
		
		public void run(){
			try {
				double i = 0.00001;
				while(control){
					mUserPoint = new SPLatLng(24.953707 + i, 121.165479 + i).toGeoPoint();
					adapter = new SPJSONAdapter(mResult, mUserPoint, SPARDisplay.POI_DISTANCE, true, true, 10);
					Log.e("location", adapter.getUserLocation().toString());
					mDisplayHelper.setAdapter(adapter);
					
					i += 0.00001;
					Thread.sleep(1000);
				}				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		control = true;
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
			String JSONdata = mResult.get(index).toString();
			
			 Intent intent = new Intent();
             intent.setClass(SecondActivity.this, ThirdActivity.class);
             Bundle bundle = new Bundle();
             bundle.putString("JSONdata", JSONdata);
             intent.putExtras(bundle);
             startActivity(intent);
		} catch (JSONException e) {
			Log.e("error", e.getMessage());
		}
	}

}
