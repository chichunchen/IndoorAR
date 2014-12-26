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

import android.app.Application;
import android.graphics.Rect;
import android.view.ViewGroup;

import com.cht.lab.ar.SPCameraManager;
import com.cht.lab.ar.SPConfiguration;
import com.cht.lab.ar.SPEnv;
import com.cht.lab.ar.sys.SPUtil;

public class MyApplication extends Application {

	private SPConfiguration mConfiguration = new SPConfiguration() {

		@Override
		public Rect getFrameRect() {
			return new Rect(0, 0, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		}

		@Override
		public float getMinZoom() {
			return 0.75f;
		}

		@Override
		public Rect getRadarRect() {
			return null;
		}

		@Override
		public int getRadarFanSweepAngle() {
			return 60;
		}

		@Override
		public Rect getTargetDistanceRect() {
			return new Rect(0, SPUtil.convert2Pixel(200), ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		@Override
		public Rect getTargetNameRect() {
			return new Rect(0, SPUtil.convert2Pixel(160), ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		@Override
		public Rect getPointerRect() {
			return null;
		}

		@Override
		public float getPointerZoom() {
			return 1.5f;
		}

	};

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化AR元件的環境變數, 建議在程式專屬的Application中設定
		SPEnv.init(this);

		// 設定嘗試以portrait方式開啟camera, 只在最開始使用的activity中呼叫一次
		// 也可以在程式專屬的Application中設定
		SPCameraManager.getInstance().setPortraitDisplay(true);
	}

	public SPConfiguration getArDisplayConfiguration() {
		return mConfiguration;
	}
}