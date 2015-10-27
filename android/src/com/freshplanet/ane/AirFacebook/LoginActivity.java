package com.freshplanet.ane.AirFacebook;

import java.util.List;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity
{	
	public static String extraPrefix = "com.freshplanet.ane.AirFacebook.LoginActivity";

	private CallbackManager callbackManager;
	
	private AirFacebookExtensionContext _context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Retrieve context
		_context = AirFacebookExtension.context;
		if (_context == null) {
			AirFacebookExtension.log("Extension context is null");
			finish();
			return;
		}

		// Get extra values
		Bundle extras = this.getIntent().getExtras();
		List<String> permissions = extras.getStringArrayList(extraPrefix+".permissions");
		String type = extras.getString(extraPrefix+".type");

		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().setLoginBehavior(_context.getLoginBehavior());
		LoginManager.getInstance().setDefaultAudience(_context.getDefaultAudience());
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						String result = null;
						try {
							result = new JSONObject().put("result", "success").toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						AirFacebookExtension.log("Login success! grantedPermissions:" + loginResult.getRecentlyGrantedPermissions() +
							" declinedPermissions:" + loginResult.getRecentlyDeniedPermissions());

						_context.dispatchStatusEventAsync("LOGIN", result);
						finish();
					}

					@Override
					public void onCancel() {
						String result = null;
						try {
							result = new JSONObject().put("result", "cancel").toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						AirFacebookExtension.log("Login failed! User cancelled!");
						_context.dispatchStatusEventAsync("LOGIN", result);
						finish();
					}

					@Override
					public void onError(FacebookException exception) {
						String result = null;
						try {
							result = new JSONObject().put("result", "error").put("error", exception.toString()).toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						exception.printStackTrace();
						AirFacebookExtension.log("Login failed! error:" + exception.toString());
						_context.dispatchStatusEventAsync("LOGIN", result);
						finish();
					}
				});

		try {
			if ("read".equals(type)) {
				LoginManager.getInstance().logInWithReadPermissions(this, permissions);
			} else {
				LoginManager.getInstance().logInWithPublishPermissions(this, permissions);
			}
		}
		catch (Exception e)
		{
			AirFacebookExtension.log("OPEN_SESSION_ERROR " + e.toString());
			e.printStackTrace();
			_context.dispatchStatusEventAsync("OPEN_SESSION_ERROR", e.getMessage());
			finish();
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed()
	{
		AirFacebookExtension.log("OPEN_SESSION_ERROR " + "BACK_BUTTON");
		_context.dispatchStatusEventAsync("OPEN_SESSION_ERROR", "BACK_BUTTON");
		finish();
	}
}
