package com.events.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.events.R;
import com.events.utils.StaticData;
import com.events.utils.Utils;

/**
 * The Class Status.
 */
public class Status implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -589955733594172904L;
	
	/** The success. */
	private boolean success = false;
	
	/** The message. */
	private String message = Utils.isOnline() ? StaticData.appContext
			.getString(R.string.err_unexpect) : StaticData.appContext
			.getString(R.string.err_internet);
	
	/** The data. */
	private String data;

	/**
	 * Instantiates a new status.
	 */
	public Status()
	{
	}

	/**
	 * Parses the json.
	 *
	 * @param obj the obj
	 * @param data the data
	 */
	private void parseJSON(JSONObject obj, String data)
	{
		try
		{
			int status;
			if (obj.has("status"))
				status = obj.optInt("status");
			else
				status = obj.optInt("success");
			success = status == 1;

			if (obj.has("display_message"))
				message = obj.getString("display_message");
			else if (obj.has("message"))
				message = obj.getString("message");

			if (data != null)
				this.data = obj.optString(data);
		} catch (Exception e)
		{
			success = false;
			message = StaticData.appContext.getString(R.string.err_unexpect);
		}
	}

	/**
	 * Instantiates a new status.
	 *
	 * @param obj the obj
	 */
	public Status(JSONObject obj)
	{
		parseJSON(obj, null);
	}

	/**
	 * Instantiates a new status.
	 *
	 * @param obj the obj
	 * @param data the data
	 */
	public Status(JSONObject obj, String data)
	{
		parseJSON(obj, data);
	}

	/**
	 * Instantiates a new status.
	 *
	 * @param res the res
	 * @param data the data
	 */
	public Status(String res, String data)
	{
		try
		{
			if (res != null)
				parseJSON(new JSONObject(res), data);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new status.
	 *
	 * @param res the res
	 */
	public Status(String res)
	{
		this(res, null);
	}

	/**
	 * Checks if is success.
	 *
	 * @return true, if is success
	 */
	public boolean isSuccess()
	{
		return success;
	}

	/**
	 * Sets the success.
	 *
	 * @param success the new success
	 */
	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getData()
	{
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(String data)
	{
		this.data = data;
	}

}
