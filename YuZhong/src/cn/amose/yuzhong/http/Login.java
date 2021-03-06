package cn.amose.yuzhong.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.amose.yuzhong.R;
import cn.amose.yuzhong.convert.UserJSONConvert;
import cn.amose.yuzhong.model.User;
import cn.amose.yuzhong.util.Constant;

public class Login extends HttpService {
	private static final String ACTION = Constant.SERVER + "login";
	private User mUser;

	public Login(Context context) {
		super(context);
		mRequestMethod = REQUEST_METHOD_POST;
	}

	@Override
	protected void process(String result) {
		try {
			JSONObject jsonHolder = new JSONObject(result);
			int jsonCode = jsonHolder.getInt(Constant.JSON_KEY_CODE);
			switch (jsonCode) {
			case Constant.JSON_CODE_SUCCESS:
				mErrorMessage = null;
				mUser = UserJSONConvert.convertJsonToItem(jsonHolder
						.getJSONObject("user"));
				break;
			case Constant.JSON_LOGIN_USER_PASSWORD_ERROR:
				mUser = null;
				mErrorMessage = mContext
						.getString(R.string.login_toast_namepassworderror);
				break;
			default:
				mUser = null;
				mErrorMessage = mContext.getString(R.string.common_toast_error);
				break;
			}
		} catch (JSONException e) {
			// How, my gad!!
			mErrorMessage = mContext
					.getString(R.string.common_toast_jsonparseerror);
			if (Constant.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getResult() {
		return (T) mUser;
	}

	@Override
	protected String getServiceUri() {
		return ACTION;
	}
}
