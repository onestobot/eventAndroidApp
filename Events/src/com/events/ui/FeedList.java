package com.events.ui;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.FeedDetail;
import com.events.MapViewActivity;
import com.events.R;
import com.events.custom.CustomFragment;
import com.events.model.Feed;
import com.events.utils.Commons;
import com.events.utils.Const;
import com.events.utils.ImageLoader;
import com.events.utils.ImageLoader.ImageLoadedListener;
import com.events.utils.ImageUtils;
import com.events.utils.StaticData;
import com.events.utils.Utils;
import com.events.web.WebHelper;

/**
 * The Class FeedList is the Fragment class that is launched when the user
 * clicks on Feed option in Left navigation drawer . It simply shows a dummy
 * list of Social media Feeds. You can customize this class to display actual
 * Feed listing.
 */
public class FeedList extends CustomFragment
{

	/** The feed list. */
	private ArrayList<Feed> fList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.programs, null);
		// setHasOptionsMenu(true);

		setFeedList(v);
		return v;
	}

	/**
	 * Setup and initialize the feed list view.
	 * 
	 * @param v
	 *            the root view
	 */
	private void setFeedList(View v)
	{

		int w = StaticData.getDIP(100);
		int h = StaticData.getDIP(110);
		bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_image, w, h);

		loader = new ImageLoader(w, h, ImageUtils.SCALE_FIT_CENTER);

		final ListView list = (ListView) v.findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				startActivity(new Intent(parent, FeedDetail.class).putExtra(
						Const.EXTRA_DATA, fList.get(arg2)));
			}
		});

		final ProgressDialog dia = parent
				.showProgressDia(R.string.alert_loading);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				fList = WebHelper.getFeedList();
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();

						if (fList == null)
						{
							Utils.showDialog(parent,
									StaticData.getErrorMessage());
						}
						else
						{
							if (fList.size() == 0)
								Toast.makeText(parent, R.string.msg_no_content,
										Toast.LENGTH_SHORT).show();
							list.setAdapter(new ProgramAdapter());
						}

					}
				});

			}
		}).start();
	}

	/**
	 * The Class FeedAdapter is the adapter class for Feed ListView. The current
	 * implementation simply shows dummy contents and you can customize this
	 * class to display actual contents as per your need.
	 */
	private class ProgramAdapter extends BaseAdapter
	{

		/** The param. */
		private LayoutParams param = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return fList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Feed getItem(int position)
		{
			return fList.get(position);
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
						R.layout.feed_item, null);

			Feed d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());
			if (d.getType() == Feed.FEED_FB)
				lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_fb, 0);
			else if (d.getType() == Feed.FEED_TW)
				lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_tw, 0);
			else
				lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_ig, 0);

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(d.getMsg());

			lbl = (TextView) convertView.findViewById(R.id.lbl3);
			lbl.setText(Commons.millsToDateTime(d.getDate()));

			ImageView img;
			if (position % 2 == 0)
			{
				img = (ImageView) convertView.findViewById(R.id.img1);
				convertView.findViewById(R.id.img2).setVisibility(View.GONE);
			}
			else
			{
				img = (ImageView) convertView.findViewById(R.id.img2);
				convertView.findViewById(R.id.img1).setVisibility(View.GONE);
			}
			img.setVisibility(View.VISIBLE);
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
		inflater.inflate(R.menu.map, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_map)
			startActivity(new Intent(getActivity(), MapViewActivity.class));
		return super.onOptionsItemSelected(item);
	}

}
