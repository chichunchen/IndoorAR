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

		// �bsetContentView���e����A�ΥH��l��camera�Υ��ù������A
		mDisplayHelper.initBeforeSetContentView();

		// �bportrait�Ҧ���UI��fill_parent�����@��
		mDisplayHelper.changeToPortrait();

		setContentView(R.layout.ar_main);

		mDisplayHelper.initAfterSetContentView(SPDisplayHelper.TYPE_DIRECTION, R.layout.direction);

		mPortView = findViewById(R.id.ar_port);

		// �]�wcamera�}�ҥ��Ѫ��^�ǳB�z��@����
		mDisplayHelper.setPortraitDisplayCallback(this);

		// ��l��AR�һݪ����
		mUserPoint = new SPLatLng(24.953711, 121.165493).toGeoPoint();

		try {
			Bundle bundle =this.getIntent().getExtras();
			String JSONdata = bundle.getString("JSONdata");
			mResult = new JSONArray("["+JSONdata+"]");
//			mResult = new JSONArray("[{\"lo\":\"121.859164\",\"d\":\"-1\",\"u1\":\"�򶩤s\",\"u\":\"1306d859ec7000001654\",\"la\":\"25.129018\",\"ad\":\"�s�_����ڰϰ򶩤s\",\"aa\":\"-1\"}]");
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

		// �Ұʤ�쨤��ť�B�z
		mDisplayHelper.startOrientationListener();
		// �Ұʩw���ť�B�z
		mDisplayHelper.startLocationListener();
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
		mDisplayHelper.stopLocationListener();
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

}
