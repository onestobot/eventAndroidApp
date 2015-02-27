package com.events;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.events.custom.CustomActivity;
import com.events.utils.Const;
import com.events.utils.Log;

/**
 * The main Browser screen, launched for user to make the payment for the Events
 * which are not Free. This screen simply shows a Webview that load the payment
 * page url which allow user to make payment.
 */
@SuppressLint("SetJavaScriptEnabled")
public class Browser extends CustomActivity
{

	/** The web. */
	private WebView web;

	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);

		getActionBar().setTitle(R.string.book_ticket);

		final ProgressBar pBar = (ProgressBar) findViewById(R.id.progress);
		web = (WebView) findViewById(R.id.web);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setDomStorageEnabled(true);
		web.getSettings().setSupportZoom(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setLoadWithOverviewMode(true);
		web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		web.getSettings().setSupportMultipleWindows(true);
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				pBar.setProgress(newProgress);
				pBar.setVisibility(newProgress == 100 ? View.GONE
						: View.VISIBLE);
			}

		});

		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				Log.e("WEB=" + url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		String url = Uri.decode(getIntent().getStringExtra(Const.EXTRA_DATA));
		Log.e(url);
		web.loadUrl(url);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		if (web.canGoBack())
			web.goBack();
		else
			super.onBackPressed();
	}
}
