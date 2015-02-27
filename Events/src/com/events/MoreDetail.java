package com.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.events.custom.CustomActivity;
import com.events.utils.Const;

/**
 * The Class MoreDetail is the Activity class that is launched when the user
 * clicks on any options in More screen and this will simply show TextView like
 * for Help Center, it will show Help center text.
 */
public class MoreDetail extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		getActionBar().setTitle(
				getIntent().getIntExtra(Const.EXTRA_DATA1, R.string.app_name));

		((TextView) findViewById(R.id.lbl)).setText(getIntent().getIntExtra(
				Const.EXTRA_DATA, 0));

		if (getIntent().getIntExtra(Const.EXTRA_DATA1, R.string.app_name) == R.string.help_center)
			setTouchNClick(R.id.btn).setVisibility(View.VISIBLE);
	}

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/html");
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help_email_subject));
		i.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "contact@myapptemplates.com" });
		startActivity(Intent.createChooser(i, getString(R.string.send_email)));
	}
}
