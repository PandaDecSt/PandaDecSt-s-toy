package com.pandadecst.toy;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pandadecst.toy.启动器;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new 启动器(), config);
	}
}
