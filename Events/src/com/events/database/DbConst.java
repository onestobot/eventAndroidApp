package com.events.database;

/**
 * The Class DbConst holds all the Database constants like database name, table
 * names and field names and It also includes the strings for creating tables in
 * Sqlite db.
 */
public class DbConst
{

	/** The Constant DATABASE_VERSION. */
	public static final int DATABASE_VERSION = 3;

	/** The Constant DATABASE_NAME. */
	public static final String DATABASE_NAME = "EventsWP.db";

	/** The Constant TBL_EVENT. */
	public static final String TBL_EVENT = "eventTbl";
	/** The Constant ID. */
	public static final String ID = "_id";

	/** The Constant OBJ_EVENT. */
	public static final String OBJ_EVENT = "objEvent";

	/** The Constant EVENT_ID. */
	public static final String EVENT_ID = "eventId";

	/** The Constant TIME. */
	public static final String TIME = "timeSaved";

	/** The Constant FIELDS_TBL_DIARY. */
	public static final Fields FIELDS_TBL_EVENT[] = { get2(OBJ_EVENT),
			get1(EVENT_ID), get1(TIME) };

	/** The Constant CREATE_TABLES. */
	public static final String CREATE_TABLES[] = { getCreateString(TBL_EVENT,
			FIELDS_TBL_EVENT) };

	/** The Constant TABLES. */
	public static final String TABLES[] = { TBL_EVENT };

	/**
	 * Gets the creates the string.
	 * 
	 * @param tblName
	 *            the tbl name
	 * @param fields
	 *            the fields
	 * @return the creates the string
	 */
	private static String getCreateString(String tblName, Fields[] fields)
	{

		StringBuffer create = new StringBuffer("create table " + tblName + "("
				+ ID + " integer primary key autoincrement");
		for (Fields f : fields)
			create.append(", " + f.name + " " + f.type);
		create.append(")");

		return create.toString();
	}

	/**
	 * Gets the 1.
	 * 
	 * @param name
	 *            the name
	 * @return the 1
	 */
	static Fields get1(String name)
	{

		return new Fields(name, "text");
	}

	/**
	 * Gets the 2.
	 * 
	 * @param name
	 *            the name
	 * @return the 2
	 */
	static Fields get2(String name)
	{

		return new Fields(name, "blob");
	}

	/**
	 * The Class Fields.
	 */
	private static class Fields
	{

		/** The name. */
		String name;

		/** The type. */
		String type;

		/**
		 * Instantiates a new fields.
		 * 
		 * @param name
		 *            the name
		 * @param type
		 *            the type
		 */
		Fields(String name, String type)
		{

			this.name = name;
			this.type = type;
		}
	}
}
