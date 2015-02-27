package com.events.ui;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.EventDetailActivity;
import com.events.R;
import com.events.custom.PagingFragment;
import com.events.model.Event;
import com.events.utils.Commons;
import com.events.utils.Const;
import com.events.utils.ImageLoader;
import com.events.utils.ImageUtils;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class MyTickets is the Fragment class that is launched when the user
 * clicks on My Tickets tab in MyProgram section and It simply shows a dummy
 * list of user's tickets . You can customize this class to display actual
 * ticket listing.
 */
public class MyTickets extends PagingFragment
{

	/** The ticket list. */
	private final ArrayList<Event> tList = new ArrayList<Event>();

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.my_tickets, null);
		setHasOptionsMenu(true);

		setTicketList(v);
		return v;
	}

	/**
	 * Setup and initialize the ticket list view.
	 * 
	 * @param v
	 *            the root view
	 */
	private void setTicketList(View v)
	{
		initPagingList((ListView) v.findViewById(R.id.list),
				new TicketAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				startActivity(new Intent(getActivity(),
						EventDetailActivity.class).putExtra(Const.EXTRA_DATA,
						tList.get(arg2)));
			}
		});

		int w = StaticData.width;
		int h = (int) (StaticData.width / 2.25);
		bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_image, w, h);

		// loader = new ImageLoader(w,h,ImageUtils.SCALE_ASPECT_WIDTH);
		loader = new ImageLoader(StaticData.width, StaticData.height,
				ImageUtils.SCALE_FIT_WIDTH);

		loadTicketList();
	}

	/**
	 * Load a dummy list of tickets. You need to write your own logic to load
	 * actual list of tickets.
	 * 
	 */
	private void loadTicketList()
	{
		reset();
		tList.clear();
		adapter.notifyDataSetChanged();
		footer.setVisibility(View.GONE);
		loadNextPage();
	}

	/* (non-Javadoc)
	 * @see com.events.custom.PagingFragment#loadNextPage()
	 */
	@Override
	protected void loadNextPage()
	{
		onStartLoading();
		final ProgressDialog dia;
		if (page == 0)
			dia = parent.showProgressDia(R.string.alert_loading);
		else
			dia = null;
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final ArrayList<Event> al = WebHelper.getBookedEvents(page,
						Const.PAGE_SIZE_30);
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						if (dia != null)
							dia.dismiss();

						if (page == 0 && al == null)
						{
							Utils.showDialog(parent,
									StaticData.getErrorMessage());
						}
						else if (page == 0 && al.size() == 0)
							Toast.makeText(parent, R.string.msg_no_content,
									Toast.LENGTH_SHORT).show();

						if (al == null || al.size() == 0)
						{
							onFinishLoading(0);
						}
						else
						{
							tList.addAll(al);

							adapter.notifyDataSetChanged();

							onFinishLoading(al.size());
						}
					}
				});

			}
		}).start();

	}

	/**
	 * The Class TicketAdapter is the adapter class for ticket ListView. The
	 * current implementation simply shows dummy contents and you can customize
	 * this class to display actual contents as per your need.
	 */
	private class TicketAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return tList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Event getItem(int position)
		{
			return tList.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.ticket_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(Commons.millsToDate(d.getStartDateTime()));

			lbl = (TextView) convertView.findViewById(R.id.lbl3);
			lbl.setText(Commons.millsToTime(d.getStartDateTime()));

			convertView.findViewById(R.id.free).setVisibility(
					d.getPrice() > 0 ? View.GONE : View.VISIBLE);

			return convertView;
		}

	}

}
