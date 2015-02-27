package com.events.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The Class DbAdapter is the adapter class for our Sqlite Database.
 */
public class DbAdapter
{

	/** The db helper. */
	private DatabaseHelper dbHelper;

	/** The Context. */
	private final Context ctx;

	/**
	 * Instantiates a new db adapter.
	 * 
	 * @param ctx
	 *            the ctx
	 */
	public DbAdapter(Context ctx)
	{

		this.ctx = ctx;
	}

	/**
	 * The Class DatabaseHelper is the SQLiteOpenHelper.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		/**
		 * Instantiates a new database helper.
		 * 
		 * @param context
		 *            the context
		 */
		DatabaseHelper(Context context)
		{

			super(context, DbConst.DATABASE_NAME, null,
					DbConst.DATABASE_VERSION);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
		 * .sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{

			for (String create : DbConst.CREATE_TABLES)
				db.execSQL(create);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{

			for (String tbl : DbConst.TABLES)
				db.execSQL("DROP TABLE IF EXISTS " + tbl);
			onCreate(db);
		}
	}

	/**
	 * Open database
	 * 
	 * @return the sQ lite database
	 * @throws SQLException
	 *             the sQL exception
	 */
	public SQLiteDatabase open() throws SQLException
	{

		dbHelper = new DatabaseHelper(ctx);
		return dbHelper.getWritableDatabase();
	}

	/**
	 * Close database
	 */
	public void close()
	{

		Log.e("CLOSE", "CLOSEDB");
		dbHelper.close();
	}

}
