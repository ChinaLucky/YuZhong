package cn.amose.yuzhong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import cn.amose.yuzhong.database.PreferenceHelper;
import cn.amose.yuzhong.util.AppUtils;

public class SplashActivity extends Activity {
	private static final long DELAY_MILLIS = 1 * 1000;
	private static final int MSG_WHAT_STARTUP = 0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (PreferenceHelper.getAccountId() == 0) {
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
			} else {
				AppUtils.startMainActivity(SplashActivity.this);
			}
			finish();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		mHandler.sendEmptyMessageDelayed(MSG_WHAT_STARTUP, DELAY_MILLIS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			mHandler.removeMessages(MSG_WHAT_STARTUP);
		}
		return super.onKeyDown(keyCode, event);
	}
}
