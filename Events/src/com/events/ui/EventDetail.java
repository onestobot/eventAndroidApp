package com.events.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.events.BookTkt;
import com.events.Login;
import com.events.R;
import com.events.custom.CustomFragment;
import com.events.database.DbHelper;
import com.events.model.Event;
import com.events.utils.Commons;
import com.events.utils.Const;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The Class EventDetail is the Fragment class that shows the details about an
 * Event. This Fragment is used inside the EventDetailActivity class. It also
 * show a Map with a marker on map for showing the location of that event. You
 * need to write your own logic for loading actual contents related to Events
 * and also need to show actual location for Event.
 */
public class EventDetail extends CustomFragment
{

	/** The map view. */
	private MapView mMapView;

	/** The Google map. */
	private GoogleMap mMap;

	/** The e. */
	private Event e;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.event_detail, null);
		e = (Event) getArg().getSerializable(Const.EXTRA_DATA);
		setHasOptionsMenu(true);

		setTouchNClick(v.findViewById(R.id.btnReg));

		showDetails(v);
		setupMap(v, savedInstanceState);

		return v;
	}

	/**
	 * Show details.
	 *
	 * @param v the v
	 */
	private void showDetails(View v)
	{
		TextView lbl = (TextView) v.findViewById(R.id.lblTitle);
		lbl.setText(e.getTitle());

		lbl = (TextView) v.findViewById(R.id.lblAddress);
		lbl.setText(e.getLocation());

		lbl = (TextView) v.findViewById(R.id.lblDesc);
		lbl.setText(Html.fromHtml(e.getDesc()));

		lbl = (TextView) v.findViewById(R.id.lblDate);
		if (e.getStartDate().equalsIgnoreCase(e.getEndDate()))
			lbl.setText(Commons.millsToDate(e.getStartDateTime()) + " "
					+ Commons.millsToTime(e.getStartDateTime()) + " - "
					+ Commons.millsToTime(e.getEndDateTime()));
		else
			lbl.setText(Commons.millsToDateTime(e.getStartDateTime()) + " to "
					+ Commons.millsToDateTime(e.getEndDateTime()));

		lbl = (TextView) v.findViewById(R.id.lblCost);
		lbl.setText(e.getPrice() == 0 ? getString(R.string.free_event) : "$"
				+ e.getPrice());

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause()
	{
		mMapView.onPause();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		mMapView.onDestroy();
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		mMapView.onResume();

		mMap = mMapView.getMap();
		if (mMap != null)
		{
			mMap.setMyLocationEnabled(true);
			mMap.setInfoWindowAdapter(null);
			setupMarker();
		}
	}

	/**
	 * Setup and initialize the Google map view.
	 * 
	 * @param v
	 *            the root view
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	private void setupMap(View v, Bundle savedInstanceState)
	{
		try
		{
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e)
		{
			e.printStackTrace();
		}
		mMapView = (MapView) v.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);

	}

	/**
	 * This method simply place a few dummy location markers on Map View. You
	 * can write your own logic for loading the locations and placing the marker
	 * for each location as per your need.
	 */
	private void setupMarker()
	{
		mMap.clear();
		LatLng ll = new LatLng(e.getLatitude(), e.getLongitude());
		MarkerOptions opt = new MarkerOptions();
		opt.position(ll).title(e.getTitle()).snippet(e.getLocation());
		opt.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

		mMap.addMarker(opt);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.share_fav, menu);
		if (e.isFav())
		{
			menu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_fav_orange);
			menu.findItem(R.id.menu_fav).setTitle(R.string.remove_fav);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_share)
		{
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/html");
			i.putExtra(Intent.EXTRA_TEXT, e.getDesc());
			i.putExtra(Intent.EXTRA_SUBJECT, e.getTitle());
			startActivity(Intent.createChooser(i, getString(R.string.share)));
		}
		else if (item.getItemId() == R.id.menu_fav)
		{
			e.setFav(!e.isFav());
			if (e.isFav())
			{
				item.setIcon(R.drawable.ic_fav_orange);
				item.setTitle(R.string.remove_fav);
				Toast.makeText(parent, R.string.msg_add_fav, Toast.LENGTH_SHORT)
						.show();
			}
			else
			{
				item.setIcon(R.drawable.ic_fav);
				item.setTitle(R.string.add_to_fav);
				Toast.makeText(parent, R.string.msg_rem_fav, Toast.LENGTH_SHORT)
						.show();
			}
			DbHelper.setEventFavorite(e, e.isFav());
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see com.events.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnReg)
		{
			bookTicket();
		}
	}

	/**
	 * Book ticket.
	 */
	private void bookTicket()
	{
		if (e.isBooked())
			Utils.showDialog(parent, R.string.err_event_booked);
		else if (e.getAvailSpace() <= 0)
			Utils.showDialog(parent, R.string.err_no_space);
		else if (e.getEndDateTime() < System.currentTimeMillis())
			Utils.showDialog(parent, R.string.err_past_event);
		else if (!StaticData.pref.contains(Const.USER_ID))
			startActivityForResult(new Intent(parent, Login.class),
					Const.REQ_LOGIN);
		else
		{
			final ProgressDialog dia = parent
					.showProgressDia(R.string.alert_wait);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					final boolean book = WebHelper.isBooked(e);
					parent.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							dia.dismiss();
							if (book)
								Utils.showDialog(parent,
										R.string.err_event_booked);
							else
								startActivity(new Intent(parent, BookTkt.class)
										.putExtra(Const.EXTRA_DATA, e));
						}
					});
				}
			}).start();
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Const.REQ_LOGIN && resultCode == Activity.RESULT_OK)
			bookTicket();
	}
}
