package com.events.calendar;

/**
 * The listener interface for receiving dateChange events. The class that is
 * interested in processing a dateChange event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addDateChangeListener<code> method. When
 * the dateChange event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DateChangeEvent
 */
public interface DateChangeListener
{

	/**
	 * On date change.
	 *
	 * @param position the position
	 * @param sel the sel
	 */
	public void onDateChange(int position, long sel);

}
