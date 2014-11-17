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

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cht.lab.ar.SPARDisplay;
import com.cht.lab.ar.SPCameraManager;
import com.cht.lab.ar.SPDisplayHelper;
import com.cht.lab.ar.adapter.SPJSONAdapter;
import com.cht.lab.ar.map.SPLatLng;
import com.google.android.maps.GeoPoint;

public class ThirdActivity extends Activity implements SPCameraManager.PortraitDisplayCallback {
	private static final String TAG = ThirdActivity.class.getSimpleName();
	private static final boolean LOCAL_LOGD = true;

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

		mDisplayHelper.initAfterSetContentView(SPDisplayHelper.TYPE_DIRECTION, R.layout.direction);

		mPortView = findViewById(R.id.ar_port);

		// 設定camera開啟失敗的回傳處理實作物件
		mDisplayHelper.setPortraitDisplayCallback(this);

		// 初始化AR所需的資料
		mUserPoint = new SPLatLng(24.953711, 121.165493).toGeoPoint();

		try {
			Bundle bundle =this.getIntent().getExtras();
			String JSONdata = bundle.getString("JSONdata");
			mResult = new JSONArray("["+JSONdata+"]");
//			mResult = new JSONArray("[{\"lo\":\"121.859164\",\"d\":\"-1\",\"u1\":\"基隆山\",\"u\":\"1306d859ec7000001654\",\"la\":\"25.129018\",\"ad\":\"新北市瑞芳區基隆山\",\"aa\":\"-1\"}]");
		} catch (JSONException e) {
			mResult = null;
		}

		SPJSONAdapter adapter = new SPJSONAdapter(mResult, mUserPoint, SPARDisplay.POI_DISTANCE, true, true, 0);

		mDisplayHelper.setAdapter(adapter);

//		mWatch = (TextView) findViewById(R.id.text1);
	}

	@Override
	protected void onResume() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onResume");
		}
		super.onResume();

		// 啟動方位角監聽處理
		mDisplayHelper.startOrientationListener();
		// 啟動定位監聽處理
		mDisplayHelper.startLocationListener();
		// 將已經初始化完成的portrait UI改為landscape以便開啟camera
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
		// 關閉定位監聽處理
		mDisplayHelper.stopLocationListener();
		// 關閉照相機資源
		mDisplayHelper.stopCamera();
	}

	@Override
	public void onPortraitDisplayFail() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onPortraitDisplayFail");
		}
		// 只會發生在第一次嘗試portrait模式開啟camera失敗後呼叫
		// 這裡改用landscape模式開啟camera, 注意UI同時也會變為landscape, 但會自動轉置為看似portrait的UI
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

}
