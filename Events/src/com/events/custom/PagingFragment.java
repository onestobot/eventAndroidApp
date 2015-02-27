package com.events.custom;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.events.R;
import com.events.utils.Const;

/**
 * The Class PagingFragment can be extended by any fragment who includes a
 * ListView which require to load contents page by page.
 */
abstract public class PagingFragment extends CustomFragment implements
		OnScrollListener
{

	/** The page. */
	protected int page = -1;

	/** The is running. */
	private boolean isRunning;

	/** The has more. */
	private boolean hasMore;

	/** The total. */
	private int total;

	/** The footer. */
	protected View footer;

	/** The adapter. */
	protected BaseAdapter adapter;

	/** The list. */
	protected ListView list;

	/**
	 * Inits the paging list.
	 * 
	 * @param v
	 *            the v
	 * @param adp
	 *            the adp
	 */
	protected void initPagingList(ListView v, BaseAdapter adp)
	{
		list = v;
		list.setOnScrollListener(this);
		footer = getLayoutInflater(null).inflate(R.layout.loading, null);
		list.addFooterView(footer);
		adapter = adp;
		list.setAdapter(adapter);
	}

	/**
	 * Reset.
	 */
	protected void reset()
	{
		page = -1;
		total = 0;
		hasMore = true;
		isRunning = false;
	}

	/**
	 * On start loading.
	 */
	protected void onStartLoading()
	{
		page++;
		isRunning = true;
	}

	/**
	 * On finish loading.
	 * 
	 * @param count
	 *            the count
	 */
	protected void onFinishLoading(int count)
	{
		isRunning = false;

		total = total + count;
		if (count < Const.PAGE_SIZE_30)
		{
			footer.setVisibility(View.GONE);
			hasMore = false;
		}
		else
			footer.setVisibility(View.VISIBLE);
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// Log.e("CVAL",
		// scrollState+"__"+listV.getLastVisiblePosition()+"__"+isRunning);
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& list.getLastVisiblePosition() >= total - 2 && !isRunning
				&& hasMore)
			loadNextPage();

	}

	/**
	 * Load next page.
	 */
	protected abstract void loadNextPage();

}
