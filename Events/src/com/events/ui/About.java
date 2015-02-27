package com.events.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.events.R;
import com.events.custom.CustomFragment;

/**
 * The Class About is the Fragment class that is launched when the user clicks
 * on About option in Left navigation drawer and it simply shows a dummy text
 * for About. You can customize this to display actual contents.
 */
public class About extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.about, null);

		return v;
	}

}
