package com.events.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.events.model.Event;
import com.events.utils.Const;
import com.events.utils.Utils;

/**
 * The Class DbHelper holds all the helper methods that query to sqlite
 * database.
 */
public class DbHelper
{

	/** The db adapter. */
	private static DbAdapter dbAdapter;

	/** The db. */
	private static SQLiteDatabase db;

	/**
	 * Inits the.
	 * 
	 * @param ctx
	 *            the ctx
	 */
	public static void init(Context ctx)
	{

		dbAdapter = new DbAdapter(ctx);
		db = dbAdapter.open();
		Log.e("OPEN", "OPENDB" + db);
	}

	/**
	 * Checks if is open.
	 * 
	 * @return true, if is open
	 */
	public static boolean isOpen()
	{

		if (db != null && db.isOpen())
			return true;
		return false;
	}

	/**
	 * Close.
	 */
	public static void close()
	{

		try
		{
			dbAdapter.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sets the event favorite.
	 * 
	 * @param a
	 *            the a
	 * @param isFav
	 *            the is fav
	 */
	public static void setEventFavorite(Event a, boolean isFav)
	{
		if (isFav)
		{
			ContentValues cv = new ContentValues();
			cv.put(DbConst.EVENT_ID, a.getId());
			cv.put(DbConst.OBJ_EVENT, Utils.serialiseObj(a));
			cv.put(DbConst.TIME, System.currentTimeMillis());

			db.insert(DbConst.TBL_EVENT, null, cv);
		}
		else
		{
			String where = DbConst.EVENT_ID + "=?";
			String arg[] = { a.getId() + "" };
			db.delete(DbConst.TBL_EVENT, where, arg);
		}
	}

	/**
	 * Checks if is event favorite.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is event favorite
	 */
	public static boolean isEventFavorite(String id)
	{
		String where = DbConst.EVENT_ID + "=?";
		String arg[] = { id + "" };
		Cursor c = db.rawQuery("select count(*) from " + DbConst.TBL_EVENT
				+ " where " + where, arg);
		c.moveToNext();
		int count = c.getInt(0);
		c.close();
		return count > 0;
	}

	/**
	 * Gets the favourite events.
	 * 
	 * @param page
	 *            the page
	 * @return the favourite events
	 */
	public static ArrayList<Event> getFavouriteEvents(int page)
	{
		ArrayList<Event> al = new ArrayList<Event>();
		Cursor c = db.query(DbConst.TBL_EVENT,
				new String[] { DbConst.OBJ_EVENT }, null, null, null, null,
				DbConst.ID + " DESC", page * Const.PAGE_SIZE_30 + ","
						+ Const.PAGE_SIZE_30);

		while (c.moveToNext())
		{
			byte b[] = c.getBlob(0);
			Event a = (Event) Utils.deSerialiseObj(b);
			a.setFav(true);
			al.add(a);
		}
		c.close();
		return al;
	}

}
