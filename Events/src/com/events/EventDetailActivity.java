package com.events;

import android.os.Bundle;
import android.view.MenuItem;

import com.events.custom.CustomActivity;
import com.events.model.Event;
import com.events.ui.EventDetail;
import com.events.utils.Const;

/**
 * The EventDetailActivity is the activity class that shows the details about an
 * Event. This is launched when ever user select an Event from the Event listing
 * or from Events on Map. It also show a Map with a marker on map for showing
 * the location of that event. You need to write your own logic for loading any
 * other contents related to Events.
 */
public class EventDetailActivity extends CustomActivity
{
	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(
				((Event) getIntent().getSerializableExtra(Const.EXTRA_DATA))
						.getTitle());

		addFragment();
	}

	/**
	 * Attach the appropriate fragment with this activity.
	 */
	private void addFragment()
	{
		EventDetail ed = new EventDetail();
		Bundle b = new Bundle();
		b.putSerializable(Const.EXTRA_DATA,
				getIntent().getSerializableExtra(Const.EXTRA_DATA));
		ed.setArg(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, ed).commit();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
