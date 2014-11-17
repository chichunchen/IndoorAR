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

		// ��l��AR���������ܼ�, ��ĳ�b�{���M�ݪ�Application���]�w
		SPEnv.init(this);

		// �]�w���եHportrait�覡�}��camera, �u�b�̶}�l�ϥΪ�activity���I�s�@��
		// �]�i�H�b�{���M�ݪ�Application���]�w
		SPCameraManager.getInstance().setPortraitDisplay(true);
	}

	public SPConfiguration getArDisplayConfiguration() {
		return mConfiguration;
	}
}
