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

		// �bsetContentView���e����A�ΥH��l��camera�Υ��ù������A
		mDisplayHelper.initBeforeSetContentView();

		// �bportrait�Ҧ���UI��fill_parent�����@��
		mDisplayHelper.changeToPortrait();

		setContentView(R.layout.ar_main);

		mDisplayHelper.initAfterSetContentView(SPDisplayHelper.TYPE_SEARCH, R.layout.search);

		mPortView = findViewById(R.id.ar_port);

		// �]�w�ƥ󪺦^�ǳB�z����@����
		mDisplayHelper.setEventCallback(this);
		// �]�wcamera�}�ҥ��Ѫ��^�ǳB�z��@����
		mDisplayHelper.setPortraitDisplayCallback(this);

		// ��l��AR�һݪ����
//		mUserPoint = new SPLatLng(25.033692, 121.545202).toGeoPoint();
		mUserPoint = new SPLatLng(24.953711, 121.165493).toGeoPoint();
		
//		try {
//			mResult = new JSONArray(
//				"[{\"u\":\"10459\",\"ad\":\"��鿤�[���m�j����G�q342��\",\"u1\":\"�C�L�𶢹A��\",\"c\":\"3\",\"la\":\"24.995533\",\"lo\":\"121.133297\",\"d\":5412,\"aa\":325.28},{\"u\":\"1048271\",\"ad\":\"��鿤�s�ζm�Z�ܧ�3�F48-3��\",\"u1\":\"1&0�𶢹A��\",\"c\":\"3\",\"la\":\"24.907846\",\"lo\":\"121.183362\",\"d\":5620,\"aa\":159.32},{\"u\":\"1067192\",\"ad\":\"��鿤�������s�_���@�q21��1 ��\",\"u1\":\"���D�y�O�����]\",\"c\":\"3\",\"la\":\"24.908611\",\"lo\":\"121.141388\",\"d\":5648,\"aa\":203.54},{\"u\":\"1067097\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131��\",\"u1\":\"��a�A��\",\"c\":\"3\",\"la\":\"24.997777\",\"lo\":\"121.132055\",\"d\":5688,\"aa\":325.67},{\"u\":\"1050272\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131��\",\"u1\":\"��a���𶢹A��\",\"c\":\"3\",\"la\":\"24.997802\",\"lo\":\"121.131929\",\"d\":5698,\"aa\":325.58},{\"u\":\"1048238\",\"ad\":\"��鿤������\",\"u1\":\"������Q�s����\",\"c\":\"3\",\"la\":\"24.905270\",\"lo\":\"121.148790\",\"d\":5748,\"aa\":195.2},{\"u\":\"106945\",\"ad\":\"��鿤�[���m�jԳ��5�F175��\",\"u1\":\"�L�a�j��𶢹A��\",\"c\":\"3\",\"la\":\"24.998926\",\"lo\":\"121.129149\",\"d\":5961,\"aa\":324.03},{\"u\":\"1067098\",\"ad\":\"��鿤�[���m�jԳ���j����G�q131-1��\",\"u1\":\"�C�L�A��\",\"c\":\"3\",\"la\":\"25.001833\",\"lo\":\"121.130944\",\"d\":6125,\"aa\":327.17},{\"u\":\"1023226\",\"ad\":\"��鿤�[���m�jԳ��184��\",\"u1\":\"�d������𶢹A��\",\"c\":\"3\",\"la\":\"25.000368999999999\",\"lo\":\"121.12837\",\"d\":6137,\"aa\":324.31},{\"u\":\"1067117\",\"ad\":\"��鿤���c�����_���B�忳���f\",\"u1\":\"����ڴβy��\",\"c\":\"3\",\"la\":\"24.999444\",\"lo\":\"121.200861\",\"d\":6155,\"aa\":37.39},{\"u\":\"1021320\",\"ad\":\"��鿤�s�ζm�s�Χ�������110�Ѥ�\",\"u1\":\"�S���j��\",\"c\":\"3\",\"la\":\"24.973969\",\"lo\":\"121.104391\",\"d\":6342,\"aa\":288.93},{\"u\":\"1067089\",\"ad\":\"��鿤�s�ζm������110��\",\"u1\":\"���Ѵ��B�D\",\"c\":\"3\",\"la\":\"24.975916\",\"lo\":\"121.104805\",\"d\":6376,\"aa\":290.89}]");
//		} catch (JSONException e) {
//			mResult = null;
//		}

		// Ū���w�s�����ո��
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

		// �Ұʤ�쨤��ť�B�z
		mDisplayHelper.startOrientationListener();
		// �Ұʩw���ť�B�z
//		mDisplayHelper.startLocationListener();
		control = true;
		// �N�w�g��l�Ƨ�����portrait UI�אּlandscape�H�K�}��camera
		mDisplayHelper.changeToLandscape();
	}

	@Override
	protected void onPause() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onPause");
		}
		super.onPause();
		// ������쨤��ť�B�z
		mDisplayHelper.stopOrientationListener();
		// �����w���ť�B�z
//		mDisplayHelper.stopLocationListener();
		control = false;
		// �����Ӭ۾��귽
		mDisplayHelper.stopCamera();
	}

	@Override
	public void onPortraitDisplayFail() {
		if (LOCAL_LOGD) {
			Log.d(TAG, "onPortraitDisplayFail");
		}
		// �u�|�o�ͦb�Ĥ@������portrait�Ҧ��}��camera���ѫ�I�s
		// �o�̧��landscape�Ҧ��}��camera, �`�NUI�P�ɤ]�|�ܬ�landscape, ���|�۰���m���ݦ�portrait��UI
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
		// �קK�]portrait/landscape�����y��activity��onCreate���srestart
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