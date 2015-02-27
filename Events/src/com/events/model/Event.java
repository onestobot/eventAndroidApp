package com.events.model;

import java.io.Serializable;

import com.events.utils.Commons;

/**
 * The Class Event is a simple Java Bean that is used to hold data related to an
 * Event like title, date etc.
 */
public class Event implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2268007184591194673L;
	/** The title. */
	private String title;
	
	/** The desc. */
	private String desc;

	/** The start date. */
	private String startDate;
	
	/** The start time. */
	private String startTime;
	
	/** The start date time. */
	private long startDateTime;
	
	/** The end date. */
	private String endDate;
	
	/** The end time. */
	private String endTime;
	
	/** The end date time. */
	private long endDateTime;

	/** The location. */
	private String location;
	
	/** The latitude. */
	private double latitude;
	
	/** The longitude. */
	private double longitude;
	
	/** The price. */
	private double price;
	
	/** The avail space. */
	private int availSpace;
	
	/** The is fav. */
	private boolean isFav;
	
	/** The is booked. */
	private boolean isBooked;

	/** The image. */
	private String image;

	/** The id. */
	private String id;

	/**
	 * Instantiates a new event.
	 * 
	 * @param title
	 *            the title
	 * @param date
	 *            the date
	 * @param time
	 *            the time
	 * @param location
	 *            the location
	 * @param image
	 *            the image
	 */
	public Event(String title, String date, String time, String location,
			int image)
	{
		startDate = date;
		this.title = title;
		startTime = time;
		this.location = location;
		this.image = "";
	}

	/**
	 * Instantiates a new event.
	 */
	public Event()
	{

	}

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
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Gets the desc.
	 *
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * Sets the desc.
	 *
	 * @param desc the new desc
	 */
	public void setDesc(String desc)
	{
		if (desc != null)
			desc = desc.replaceAll("\\[caption*.*\\[/caption\\]", "");
		this.desc = desc;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setStartDate(String date)
	{
		startDate = date;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public String getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the time.
	 * 
	 * @param time
	 *            the new time
	 */
	public void setStartTime(String time)
	{
		startTime = time;
	}

	/**
	 * Gets the start date time.
	 *
	 * @return the start date time
	 */
	public long getStartDateTime()
	{
		if (startDateTime == 0)
			startDateTime = Commons.dateTimeToMillis(startDate + " "
					+ startTime);
		return startDateTime;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public String getEndTime()
	{
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * Gets the end date time.
	 *
	 * @return the end date time
	 */
	public long getEndDateTime()
	{
		if (endDateTime == 0)
			endDateTime = Commons.dateTimeToMillis(endDate + " " + endTime);
		return endDateTime;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location)
	{
		this.location = location;
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}

	/**
	 * Gets the avail space.
	 *
	 * @return the avail space
	 */
	public int getAvailSpace()
	{
		return availSpace;
	}

	/**
	 * Sets the avail space.
	 *
	 * @param availSpace the new avail space
	 */
	public void setAvailSpace(int availSpace)
	{
		this.availSpace = availSpace;
	}

	/**
	 * Checks if is fav.
	 *
	 * @return true, if is fav
	 */
	public boolean isFav()
	{
		return isFav;
	}

	/**
	 * Sets the fav.
	 *
	 * @param isFav the new fav
	 */
	public void setFav(boolean isFav)
	{
		this.isFav = isFav;
	}

	/**
	 * Checks if is booked.
	 *
	 * @return true, if is booked
	 */
	public boolean isBooked()
	{
		return isBooked;
	}

	/**
	 * Sets the booked.
	 *
	 * @param isBooked the new booked
	 */
	public void setBooked(boolean isBooked)
	{
		this.isBooked = isBooked;
	}

}
