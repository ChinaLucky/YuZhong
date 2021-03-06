package cn.amose.yuzhong;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class ImageCropActivity extends Activity {
	public static final String INTENT_ACTION_CROPIMAGE = "cn.amose.yuzhong.cropimage";

	private Uri mUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUri = getIntent().getData();
		if (mUri == null) {
			Toast.makeText(this, R.string.common_toast_error,
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		setContentView(R.layout.image_crop);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		}
		return super.onKeyDown(keyCode, event);
	}
}
