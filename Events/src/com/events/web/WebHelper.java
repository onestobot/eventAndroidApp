package com.events.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.events.database.DbHelper;
import com.events.model.Event;
import com.events.model.Feed;
import com.events.model.Status;
import com.events.utils.Commons;

/**
 * The Class WebHelper class holds all the methods that communicate with server
 * API and parses the results in required Java Bean classes.
 */
public class WebHelper extends WebAccess
{

	/**
	 * Gets the events.
	 * 
	 * @param page
	 *            the page
	 * @param pageSize
	 *            the page size
	 * @return the events
	 */
	public static ArrayList<Event> getEvents(int page, int pageSize)
	{
		try
		{

			String url = EVENT_LIST_URL;
			url = getPageParams(url, page, pageSize);
			// url=getUserParams(url);
			String res = executeGetRequest(url, true);
			return parseEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Parses the events.
	 * 
	 * @param res
	 *            the res
	 * @return the array list
	 * @throws Exception
	 *             the exception
	 */
	private static ArrayList<Event> parseEvents(String res) throws Exception
	{
		JSONArray obj = new JSONArray(res);

		TreeSet<Event> al = new TreeSet<Event>(new Comparator<Event>() {

			@Override
			public int compare(Event lhs, Event rhs)
			{
				if (lhs.getStartDateTime() == rhs.getStartDateTime())
					return rhs.getTitle().compareTo(lhs.getTitle());
				return rhs.getStartDateTime() > lhs.getStartDateTime() ? -1 : 1;
			}
		});
		for (int i = 0; i < obj.length(); i++)
		{
			JSONObject js = obj.getJSONObject(i);
			if (js.has("status") && js.has("message") && obj.length() == 1)
				return new ArrayList<Event>();
			al.add(parseEvent(js));
		}
		return new ArrayList<Event>(al);
	}

	/**
	 * Parses the event.
	 * 
	 * @param js
	 *            the js
	 * @return the event
	 * @throws Exception
	 *             the exception
	 */
	private static Event parseEvent(JSONObject js) throws Exception
	{
		Event e = new Event();
		e.setTitle(js.getString("event_name"));
		e.setId(js.getString("event_id"));
		e.setStartDate(js.getString("event_start_date"));
		e.setStartTime(js.getString("event_start_time"));
		e.setImage(js.getString("event_image_url"));
		e.setDesc(js.getString("event_content"));
		e.setEndDate(js.getString("event_end_date"));
		e.setEndTime(js.getString("event_end_time"));
		e.setLocation(js.getString("location_address") + ", "
				+ js.getString("location_town") + ", "
				+ js.getString("location_state") + ", "
				+ js.getString("location_region") + ", "
				+ js.getString("location_country") + "- "
				+ js.getString("location_postcode"));
		e.setLocation(e.getLocation().replaceAll(", null", ""));
		e.setLocation(e.getLocation().replaceAll("- null", ""));

		// e.setFav("1".equalsIgnoreCase(js.optString("is_favorite")));
		e.setFav(DbHelper.isEventFavorite(e.getId()));
		e.setLatitude(Commons.strToDouble(js.getString("location_latitude")));
		e.setLongitude(Commons.strToDouble(js.getString("location_longitude")));
		try
		{
			e.setPrice(Commons.strToDouble(js.getJSONObject("ticket")
					.getString("ticket_price")));
			e.setAvailSpace(Commons.strToInt(js.getJSONObject("ticket")
					.getString("avail_spaces")));
		} catch (Exception e2)
		{
			e2.printStackTrace();
		}

		/*while(e.getDesc().startsWith("/n")||e.getDesc().startsWith("/r"))
			e.setDesc(e.getDesc().substring(1));
		while(e.getDesc().endsWith("/n")||e.getDesc().endsWith("/r"))
			e.setDesc(e.getDesc().substring(0,e.getDesc().length()-1));*/
		return e;
	}

	/**
	 * Parses the date.
	 * 
	 * @param tw
	 *            the tw
	 * @param str
	 *            the str
	 * @return the long
	 */
	private static long parseDate(boolean tw, String str)
	{
		String format = tw ? "EEE MMM dd kk:mm:ss Z yyyy"
				: "yyyy-MM-dd'T'kk:mm:ssZ";
		try
		{
			return new SimpleDateFormat(format, Locale.US).parse(str).getTime();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	/**
	 * Gets the feed list.
	 * 
	 * @return the feed list
	 */
	public static ArrayList<Feed> getFeedList()
	{
		TreeSet<Feed> al = new TreeSet<Feed>(new Comparator<Feed>() {

			@Override
			public int compare(Feed lhs, Feed rhs)
			{
				if (lhs.getDate() == rhs.getDate())
					return 1;
				return rhs.getDate() > lhs.getDate() ? 1 : -1;
			}
		});
		try
		{
			String res = executePostRequest(FEED_URL, null, true);
			JSONObject obj = new JSONObject(res);

			JSONArray arr = obj.getJSONArray("twitter_feeds");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_TW);
				f.setDate(parseDate(true, js.getString("created_at")));
				f.setImage(js.getJSONObject("user").optString(
						"profile_image_url"));
				f.setLink(js.optString("tweet_url"));
				f.setMsg(js.optString("text"));
				f.setTitle(js.getJSONObject("user").optString("name"));

				al.add(f);
			}

			arr = obj.getJSONArray("fb_feeds");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_FB);
				f.setDate(parseDate(false, js.getString("created_time")));
				f.setImage(js.optString("picture"));
				f.setLink(js.optString("link"));
				f.setMsg(js.optString("message"));
				f.setTitle(js.optString(""));

				al.add(f);
			}

			arr = obj.getJSONArray("instagram_feeds");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_IG);
				f.setDate(Long.parseLong(js.getString("created_time")) * 1000);
				f.setImage(js.optJSONObject("images")
						.getJSONObject("low_resolution").getString("url"));
				f.setLink(js.optString("link"));
				f.setMsg(js.optString("text"));
				if (Commons.isEmpty(f.getMsg()))
					f.setMsg("@"
							+ js.optJSONObject("user").getString("username"));
				f.setTitle(js.optJSONObject("user").getString("full_name"));

				al.add(f);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Feed>(al);
	}

	/**
	 * Gets the events by month.
	 * 
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @return the events by month
	 */
	public static ArrayList<Event> getEventsByMonth(String month, String year)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("month", month));
			param.add(new BasicNameValuePair("year", year));
			String res = executePostRequest(EVENT_BY_MONTH_URL, param, true);
			return parseEvents(res);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Checks if is booked.
	 * 
	 * @param e
	 *            the e
	 * @return true, if is booked
	 */
	public static boolean isBooked(Event e)
	{
		try
		{
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("event_id", e.getId()));
			String res = executePostRequest(CHECK_BOOK_URL, param, true);
			e.setBooked(new Status(new JSONArray(res).getJSONObject(0))
					.isSuccess());
			return e.isBooked();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Do login.
	 * 
	 * @param email
	 *            the email
	 * @param pwd
	 *            the pwd
	 * @return the status
	 */
	public static Status doLogin(String email, String pwd)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("email", email));
			param.add(new BasicNameValuePair("pwd", pwd));
			String res = executePostRequest(LOGIN_URL, param, false);
			return new Status(res, "user_id");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return new Status();
	}

	/**
	 * Do register.
	 * 
	 * @param name
	 *            the name
	 * @param email
	 *            the email
	 * @param pwd
	 *            the pwd
	 * @return the status
	 */
	public static Status doRegister(String name, String email, String pwd)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("name", name));
			param.add(new BasicNameValuePair("email", email));
			param.add(new BasicNameValuePair("pwd", pwd));
			String res = executePostRequest(REGISTER_URL, param, false);
			return new Status(res, "user_id");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return new Status();
	}

	/**
	 * Book tkt.
	 * 
	 * @param event_id
	 *            the event_id
	 * @param space
	 *            the space
	 * @param comment
	 *            the comment
	 * @return the status
	 */
	public static Status bookTkt(String event_id, String space, String comment)
	{
		try
		{
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("event_id", event_id));
			param.add(new BasicNameValuePair("booking_spaces", space));
			param.add(new BasicNameValuePair("booking_comment", comment));
			String res = executePostRequest(BOOK_TKT_URL, param, false);
			return new Status(res, "payment_page_link");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return new Status();
	}

	/**
	 * Gets the booked events.
	 * 
	 * @param page
	 *            the page
	 * @param pageSize
	 *            the page size
	 * @return the booked events
	 */
	public static ArrayList<Event> getBookedEvents(int page, int pageSize)
	{
		try
		{
			page++;
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("page", page + ""));
			param.add(new BasicNameValuePair("page_size", pageSize + ""));
			String res = executePostRequest(TKT_LIST_URL, param, true);
			return parseEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
