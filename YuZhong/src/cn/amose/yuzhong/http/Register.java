package cn.amose.yuzhong.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.amose.yuzhong.R;
import cn.amose.yuzhong.convert.UserJSONConvert;
import cn.amose.yuzhong.model.User;
import cn.amose.yuzhong.util.Constant;

public class Register extends HttpService {
	private static final String ACTION = Constant.SERVER + "register";
	private User mUser;

	public Register(Context context) {
		super(context);
		mRequestMethod = REQUEST_METHOD_POST;
	}

	@Override
	protected void process(String result) {
		try {
			JSONObject jsonHolder = new JSONObject(result);
			int jsonCode = jsonHolder.getInt(Constant.JSON_KEY_CODE);
			if (jsonCode == Constant.JSON_CODE_SUCCESS) {
				mErrorMessage = null;
				mUser = UserJSONConvert.convertJsonToItem(jsonHolder
						.getJSONObject("user"));
			} else if (jsonCode == Constant.JSON_REGISTER_FAILED) {
				mErrorMessage = mContext
						.getString(R.string.process_user_toast_registerfailed);
			} else {
				mErrorMessage = mContext.getString(R.string.common_toast_error);
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
