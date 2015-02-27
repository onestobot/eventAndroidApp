package com.events;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.events.custom.CustomActivity;
import com.events.model.Status;
import com.events.utils.Const;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class Login is the Activity class that is launched when the app require
 * user to Login to perform next operations like in this current we ask user to
 * Login before he/she is going to Book Event ticket and once the user is logged
 * in, it will never ask to loin again for the lifetime of this app.
 */
public class Login extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		setTouchNClick(R.id.btnLogin);
		setTouchNClick(R.id.btnReg);

		getActionBar().setTitle(R.string.login);
	}

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnReg)
		{
			startActivityForResult(new Intent(THIS, Register.class),
					Const.REQ_LOGIN);
		}
		else
		{
			doLogin();
		}
	}

	/**
	 * Call the Login API and check user's Login details and based on the API
	 * response, take required action or show error message if any.
	 */
	private void doLogin()
	{
		final String email = ((EditText) findViewById(R.id.txtEmail)).getText()
				.toString();
		final String pwd = ((EditText) findViewById(R.id.txtPwd)).getText()
				.toString();
		final ProgressDialog dia = showProgressDia(R.string.alert_login);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.doLogin(email, pwd);
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						if (!st.isSuccess())
							Utils.showDialog(THIS, st.getMessage());
						else
						{
							StaticData.pref.edit()
									.putString(Const.USER_ID, st.getData())
									.commit();
							setResult(RESULT_OK);
							finish();
						}
					}
				});
			}
		}).start();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Const.REQ_LOGIN && resultCode == Activity.RESULT_OK)
		{
			setResult(RESULT_OK);
			finish();
		}
	}

}
