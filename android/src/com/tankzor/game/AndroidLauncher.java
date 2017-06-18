package com.tankzor.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tankzor.game.main.Tankzor;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGLSurfaceView20API18 = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		initialize(new Tankzor(), config);
	}
}
