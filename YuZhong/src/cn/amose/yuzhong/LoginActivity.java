package cn.amose.yuzhong;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;
import cn.amose.yuzhong.asynctask.GetAsyncTask;
import cn.amose.yuzhong.asynctask.GetAsyncTask.OnDownloadListener;
import cn.amose.yuzhong.database.PreferenceHelper;
import cn.amose.yuzhong.http.Login;
import cn.amose.yuzhong.model.User;
import cn.amose.yuzhong.util.AppUtils;
import cn.amose.yuzhong.util.Md5Util;

public class LoginActivity extends Activity {
	private GetAsyncTask mGetAsyncTask;
	private EditText mMobileEt;
	private EditText mPasswordEt;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mMobileEt = (EditText) findViewById(R.id.et_login_mobile);
		mPasswordEt = (EditText) findViewById(R.id.et_login_password);
		mPasswordEt.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					onLoginClick(null);
					return false;
				}
				return false;
			}
		});
	}

	public void onLoginClick(View v) {
		String mobile = mMobileEt.getText().toString().trim();
		String password = mPasswordEt.getText().toString().trim();
		if (mobile.equals("")) {
			mMobileEt.requestFocus();
			mMobileEt.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.shake));
		} else if (password.equals("")) {
			mPasswordEt.requestFocus();
			mPasswordEt.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.shake));
		} else {
			login(mobile, password);
		}
	}

	public void onRegisterClick(View v) {
		startActivity(new Intent(this, RegisterActivity.class));
	}

	private void login(String mobile, String password) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setTitle(R.string.common_dg_title_info);
			mProgressDialog.setMessage(getString(R.string.login_dg_msg_login));
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					cancelAsyncTaskIfNeed();
				}
			});
		}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mobile", mobile);
			jsonObject.put("password", Md5Util.md5(password));
			cancelAsyncTaskIfNeed();
			mGetAsyncTask = new GetAsyncTask(new Login(this), jsonObject);
			mGetAsyncTask.setOnDownloadListener(new OnDownloadListener() {

				@Override
				public <T> void onDownloadComplete(T result, String errorMessage) {
					if (mProgressDialog.isShowing()) {
						mProgressDialog.dismiss();
					}
					if (errorMessage == null) {
						PreferenceHelper.initDefaultAccount((User) result);
						AppUtils.startMainActivity(LoginActivity.this);
						finish();
					} else {
						Toast.makeText(LoginActivity.this, errorMessage,
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			mGetAsyncTask.execute();
			mProgressDialog.show();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		cancelAsyncTaskIfNeed();
		super.onDestroy();
	}

	private void cancelAsyncTaskIfNeed() {
		if (mGetAsyncTask != null
				&& mGetAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			mGetAsyncTask.cancel(true);
		}
	}

}