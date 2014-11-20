package com.cht.chihua;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		splash();
	}

	private void splash()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					startActivity(new Intent().setClass(SplashActivity.this, SecondActivity.class));
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}