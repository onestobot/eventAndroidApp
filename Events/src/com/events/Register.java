package com.events;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.events.custom.CustomActivity;
import com.events.model.Status;
import com.events.utils.Commons;
import com.events.utils.Const;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class Register is the Activity class that is launched when the user
 * clicks on Register button in Login screen and it allow user to register him
 * self as a new user of this app.
 */
public class Register extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		setTouchNClick(R.id.btnCancel);
		setTouchNClick(R.id.btnReg);

		getActionBar().setTitle(R.string.register);
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
			doRegister();
		}
		else
		{
			finish();
		}
	}

	/**
	 * Call the Register API and check user's Login details and based on the API
	 * response, take required action or show error message if any.
	 */
	private void doRegister()
	{
		final String name = ((EditText) findViewById(R.id.txtName)).getText()
				.toString().trim();
		final String email = ((EditText) findViewById(R.id.txtEmail)).getText()
				.toString().trim();
		final String pwd = ((EditText) findViewById(R.id.txtPwd)).getText()
				.toString().trim();

		if (Commons.isEmpty(name) || Commons.isEmpty(email)
				|| Commons.isEmpty(pwd))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (!Utils.isValidEmail(email))
		{
			Utils.showDialog(THIS, R.string.err_email);
			return;
		}

		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.doRegister(name, email, pwd);
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

}
