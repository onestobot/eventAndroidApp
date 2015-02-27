package com.events;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.events.custom.CustomActivity;
import com.events.model.Event;
import com.events.model.Status;
import com.events.utils.Const;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class BookTkt is the Activity class that is launched when the user clicks
 * on Register option in Event's detail page and it shows interface to user for
 * selecting the seats with option to add his comments. Following to this page
 * is the payment screen where user need to make the payment to book his/her
 * seats, if Event is not free else it will just show success/fail response.
 */
public class BookTkt extends CustomActivity
{

	/** The event to be booked. */
	private Event e;

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_tkt);

		e = (Event) getIntent().getSerializableExtra(Const.EXTRA_DATA);

		setTouchNClick(R.id.btnSubmit);
		setTouchNClick(R.id.btnCancel);

		if (e.getPrice() > 0)
		{
			((TextView) findViewById(R.id.lblPrice))
					.setText("$" + e.getPrice());
		}
		loadSpaces();

		getActionBar().setTitle(R.string.book_ticket);
	}

	/**
	 * Load number of available seats for this Event.
	 */
	private void loadSpaces()
	{
		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final ArrayList<String> al = new ArrayList<String>();
				for (int i = 1; i <= e.getAvailSpace(); i++)
					al.add(i + "");
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						Spinner spin = (Spinner) findViewById(R.id.spinSpace);
						ArrayAdapter<String> adp = new ArrayAdapter<String>(
								THIS, android.R.layout.simple_spinner_item, al);
						adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spin.setAdapter(adp);
					}
				});
			}
		}).start();
	}

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnCancel)
		{
			finish();
		}
		else
		{
			bookTkt();
		}
	}

	/**
	 * This method calls the API to book the ticket and depending on the
	 * response if the Event is Free then it shows success/fail response else it
	 * open the Payment screen.
	 */
	private void bookTkt()
	{
		final String spaces = ((Spinner) findViewById(R.id.spinSpace))
				.getSelectedItem() + "";
		final String comment = ((EditText) findViewById(R.id.txtComment))
				.getText().toString();
		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.bookTkt(e.getId(), spaces, comment);
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						if (!st.isSuccess())
							Utils.showDialog(THIS, st.getMessage());
						else if (e.getPrice() == 0)
						{
							Toast.makeText(THIS,
									R.string.msg_event_book_success,
									Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							startActivityForResult(new Intent(THIS,
									Browser.class).putExtra(Const.EXTRA_DATA,
									st.getData()), Const.REQ_BOOK);
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
		if (requestCode == Const.REQ_BOOK)
		{
			final ProgressDialog dia = showProgressDia(R.string.alert_wait);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					final boolean book = WebHelper.isBooked(e);
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							dia.dismiss();
							if (book)
							{
								Toast.makeText(THIS,
										R.string.msg_event_book_success,
										Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					});
				}
			}).start();
		}
	}
}
