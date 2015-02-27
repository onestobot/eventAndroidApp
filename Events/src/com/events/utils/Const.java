package com.events.utils;

import java.io.File;

import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * The Class Const.
 */
public class Const
{

	/** The Constant REQ_LOGIN. */
	public static final int REQ_LOGIN = 1;
	
	/** The Constant REQ_BOOK. */
	public static final int REQ_BOOK = 2;

	/** The Constant EXTRA_DATA. */
	public static final String EXTRA_DATA = "extraData";
	
	/** The Constant EXTRA_DATA1. */
	public static final String EXTRA_DATA1 = "extraData1";

	/** The Constant USER_ID. */
	public static final String USER_ID = "userId";

	/** The Constant PAGE_SIZE_100. */
	public static final int PAGE_SIZE_100 = 100;
	
	/** The Constant PAGE_SIZE_30. */
	public static final int PAGE_SIZE_30 = 30;

	/** The Constant TEN_MIN. */
	public static final long TEN_MIN = 10 * 60 * 1000;

	/** The Constant ROOT_DIR. */
	public static final File ROOT_DIR;
	
	/** The Constant WEB_DIR. */
	public static final File WEB_DIR;
	
	/** The Constant CACHE_DIR. */
	public static final File CACHE_DIR;

	static
	{

		ROOT_DIR = new File(Environment.getExternalStorageDirectory(), "Events");
		if (!ROOT_DIR.exists())
		{
			ROOT_DIR.mkdirs();
			Utils.createNoMediaFile(ROOT_DIR);
		}
		WEB_DIR = new File(ROOT_DIR, "web");
		if (!WEB_DIR.exists())
			WEB_DIR.mkdirs();

		CACHE_DIR = new File(ROOT_DIR, "Cache");
		if (!CACHE_DIR.exists())
			CACHE_DIR.mkdirs();

	}

}
