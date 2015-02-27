package com.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.events.custom.CustomActivity;
import com.events.model.Feed;
import com.events.utils.Commons;
import com.events.utils.Const;

/**
 * The Class FeedDetail is the Activity class that is launched when the user
 * clicks on any Feed item in the Feed list section and it simply load the Feed
 * URL in the Webview.
 */
@SuppressLint("SetJavaScriptEnabled")
public class FeedDetail extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);

		WebView w = (WebView) findViewById(R.id.web);
		w.getSettings().setBuiltInZoomControls(true);
		w.getSettings().setJavaScriptEnabled(true);

		final ProgressBar pb = (ProgressBar) findViewById(R.id.progress);
		final Feed f = (Feed) getIntent()
				.getSerializableExtra(Const.EXTRA_DATA);
		if (!Commons.isEmpty(f.getLink()))
			w.loadUrl(f.getLink());
		else if (!Commons.isEmpty(f.getMsg()))
			w.loadDataWithBaseURL("http://feed-dummy", f.getMsg(), "text/html",
					"UTF-8", "http://feed-dummy");
		else
			finish();

		getActionBar().setTitle(f.getTitle());
		w.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				super.onReceivedTitle(view, title);
				if (Commons.isEmpty(f.getTitle()))
					getActionBar().setTitle(title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				pb.setProgress(newProgress);
				pb.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
			}
		});

		w.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});

	}

}
