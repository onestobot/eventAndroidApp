package com.events;

import android.os.Bundle;

import com.events.custom.CustomActivity;
import com.events.ui.MapViewer;

/**
 * The MapViewActivity is the activity class that shows Map fragment. This
 * activity is only created to show Back button on ActionBar.
 */
public class MapViewActivity extends CustomActivity
{
	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle("Maps");

		addFragment();
	}

	/**
	 * Attach the appropriate MapViewer fragment with activity.
	 */
	private void addFragment()
	{
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new MapViewer()).commit();
	}

}
