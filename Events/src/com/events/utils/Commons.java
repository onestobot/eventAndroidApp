package com.events.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateUtils;

import com.events.R;

/**
 * The Class Commons.
 */
public class Commons
{
	
	/**
	 * Gets the time span.
	 *
	 * @param timeStr the time str
	 * @return the time span
	 */
	public static String getTimeSpan(long timeStr)
	{
		return DateUtils
				.getRelativeTimeSpanString(timeStr, System.currentTimeMillis(),
						DateUtils.SECOND_IN_MILLIS).toString()
				.replace(" ago", "").replace("in ", "")
				.replace(" seconds", "s").replace(" minutes", "m")
				.replace(" minute", "m").replace(" hours", "h")
				.replace(" hour", "h").replace(" days", "d")
				.replace(" day", "d");
	}

	/**
	 * Checks if is empty.
	 *
	 * @param str the str
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || str.equalsIgnoreCase("null")
				|| str.trim().length() == 0;
	}

	/**
	 * Gets the date time.
	 *
	 * @return the date time
	 */
	public static String getDateTime()
	{

		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		String format = "yyyy-MM-dd HH:mm:ss";
		return new SimpleDateFormat(format).format(d);
	}

	/**
	 * String to calander.
	 *
	 * @param date the date
	 * @param cal the cal
	 */
	public static void stringToCalander(String date, Calendar cal)
	{

		String[] str = date.split("-");
		if (str.length == 3)
			cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1,
					Integer.parseInt(str[2]));
	}

	/**
	 * Date time to millis.
	 *
	 * @param datetime the datetime
	 * @return the long
	 */
	public static long dateTimeToMillis(String datetime)
	{
		String format = "yyyy-MM-dd HH:mm:ss";
		try
		{
			return new SimpleDateFormat(format, Locale.US).parse(datetime)
					.getTime();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	/**
	 * Date time to millis1.
	 *
	 * @param datetime the datetime
	 * @return the long
	 */
	public static long dateTimeToMillis1(String datetime)
	{

		Calendar cal = Calendar.getInstance();
		try
		{
			String[] arr = datetime.split(" ");
			String[] str = arr[0].split("-");
			if (str.length == 3)
				cal.set(Integer.parseInt(str[2]), Integer.parseInt(str[0]) - 1,
						Integer.parseInt(str[1]));
			str = arr[1].split(":");
			if (str.length == 3)
			{
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(str[1]));
				cal.set(Calendar.SECOND, Integer.parseInt(str[2]));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return cal.getTimeInMillis();
	}

	/**
	 * Mills to date time.
	 *
	 * @param mills the mills
	 * @return the string
	 */
	public static String millsToDateTime(long mills)
	{
		Date d = new Date(mills);

		if (DateUtils.isToday(mills))
		{
			return StaticData.res.getString(R.string.today)
					+ new SimpleDateFormat(" - hh:mm a").format(d);
		}
		String format = "dd MMM yyyy - hh:mm a";
		return new SimpleDateFormat(format).format(d);
	}

	/**
	 * Mills to date.
	 *
	 * @param mills the mills
	 * @return the string
	 */
	public static String millsToDate(long mills)
	{

		Date d = new Date(mills);
		String format = "dd MMM yyyy";
		return new SimpleDateFormat(format).format(d);
	}

	/**
	 * Mills to time.
	 *
	 * @param mills the mills
	 * @return the string
	 */
	public static String millsToTime(long mills)
	{
		Date d = new Date(mills);

		String format = "hh:mm a";
		return new SimpleDateFormat(format).format(d);
	}

	/**
	 * Str to double.
	 *
	 * @param str the str
	 * @return the double
	 */
	public static double strToDouble(String str)
	{

		try
		{
			return Double.parseDouble(str);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Str to int.
	 *
	 * @param str the str
	 * @return the int
	 */
	public static int strToInt(String str)
	{

		try
		{
			return (int) strToDouble(str);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Str to long.
	 *
	 * @param str the str
	 * @return the long
	 */
	public static long strToLong(String str)
	{

		return (long) strToDouble(str);
	}

	/**
	 * Checks if is before today.
	 *
	 * @param date the date
	 * @return true, if is before today
	 */
	public static boolean isBeforeToday(String date)
	{

		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		stringToCalander(date, cal);

		return cal.compareTo(today) == -1;
	}

	/**
	 * Checks if is between today.
	 *
	 * @param start the start
	 * @param end the end
	 * @return true, if is between today
	 */
	public static boolean isBetweenToday(String start, String end)
	{

		try
		{
			Calendar today = Calendar.getInstance();
			Calendar calStart = Calendar.getInstance();
			stringToCalander(start, calStart);

			Calendar calEnd = Calendar.getInstance();
			stringToCalander(end, calEnd);

			return calStart.compareTo(today) <= 0
					&& calEnd.compareTo(today) >= 0;
		} catch (Exception e)
		{
			return false;
		}
	}
}
