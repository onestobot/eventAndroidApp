package com.events.ui;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.EventDetailActivity;
import com.events.MainActivity;
import com.events.R;
import com.events.custom.PagingFragment;
import com.events.database.DbHelper;
import com.events.model.Event;
import com.events.utils.Commons;
import com.events.utils.Const;
import com.events.utils.ImageLoader;
import com.events.utils.ImageLoader.ImageLoadedListener;
import com.events.utils.ImageUtils;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class Programs is the Fragment class that is launched when the user
 * clicks on Programs or on MyPrograms option in Left navigation drawer and this
 * is also used as a default fragment for MainActivity. It simply shows a dummy
 * list of Events/Programs. . You can customize this class to display actual
 * Feed listing.
 */
public class Programs extends PagingFragment
{

	/** The Programs list. */
	private final ArrayList<Event> pList = new ArrayList<Event>();

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.programs, null);
		setHasOptionsMenu(true);

		setProgramList(v);
		return v;
	}

	/**
	 * Setup and initialize the Program list view.
	 * 
	 * @param v
	 *            the root view
	 */
	private void setProgramList(View v)
	{
		initPagingList((ListView) v.findViewById(R.id.list),
				new ProgramAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				startActivity(new Intent(getActivity(),
						EventDetailActivity.class).putExtra(Const.EXTRA_DATA,
						pList.get(arg2)));
			}
		});

		int w = StaticData.width;
		int h = (int) (StaticData.width / 2.25);
		bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_image, w, h);

		// loader = new ImageLoader(w,h,ImageUtils.SCALE_ASPECT_WIDTH);
		loader = new ImageLoader(StaticData.width, StaticData.height,
				ImageUtils.SCALE_FIT_WIDTH);

		loadEventList();
	}

	/**
	 * This method currently loads a dummy list of Recipes. You can write the
	 * actual implementation of loading Recipes.
	 */
	private void loadEventList()
	{
		reset();
		pList.clear();
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
				final ArrayList<Event> al;
				if (getArg() == null)
					al = WebHelper.getEvents(page, Const.PAGE_SIZE_30);
				else
					al = DbHelper.getFavouriteEvents(page);
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
							pList.addAll(al);

							adapter.notifyDataSetChanged();

							onFinishLoading(al.size());
						}
					}
				});

			}
		}).start();

	}

	/**
	 * The Class ProgramAdapter is the adapter class for Feed ListView. The
	 * current implementation simply shows dummy contents and you can customize
	 * this class to display actual contents as per your need.
	 */
	private class ProgramAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return pList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Event getItem(int position)
		{
			return pList.get(position);
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
						R.layout.program_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(Html.fromHtml(d.getDesc()));

			lbl = (TextView) convertView.findViewById(R.id.lbl3);

			lbl.setText(Commons.millsToDateTime(d.getStartDateTime()));

			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			Bitmap bm = loader.loadImage(d.getImage(),
					new ImageLoadedListener() {

						@Override
						public void imageLoaded(Bitmap bm)
						{
							if (bm != null)
								notifyDataSetChanged();
						}
					});
			if (bm == null)
				img.setImageBitmap(bmNoImg);
			else
				img.setImageBitmap(bm);

			return convertView;
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		if (getArg() == null)
			inflater.inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_fav)
		{
			((MainActivity) getActivity()).launchFragment(21);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
