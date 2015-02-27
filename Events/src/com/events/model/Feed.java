package com.events.model;

import java.io.Serializable;

/**
 * The Class Feed is a simple Java Bean that is used to hold the data of a
 * particular Feed item like title, date etc.
 */
public class Feed implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1572509523380762055L;
	
	/** The Constant FEED_TW. */
	public static final int FEED_TW = 1;
	
	/** The Constant FEED_FB. */
	public static final int FEED_FB = 2;
	
	/** The Constant FEED_IG. */
	public static final int FEED_IG = 3;
	/** The title. */
	private String title;

	/** The msg. */
	private String msg;

	/** The link. */
	private String link;

	/** The date. */
	private long date;

	/** The image. */
	private String image;

	/** The type. */
	private int type;

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Gets the msg.
	 *
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * Sets the msg.
	 *
	 * @param msg the new msg
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink()
	{
		return link;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the new link
	 */
	public void setLink(String link)
	{
		this.link = link;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public long getDate()
	{
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(long date)
	{
		this.date = date;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage()
	{
		return image;
	}

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(String image)
	{
		this.image = image;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type)
	{
		this.type = type;
	}

}
