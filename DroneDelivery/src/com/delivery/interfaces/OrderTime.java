package com.delivery.interfaces;
/**
 * This interface has a setter/getter of OrderTime.
 * The setter accepts an OrderTime in String representation,
 * and convert it to a long integer, represents the seconds
 * of the day, in other words, 00:00:00 is time 0sec,
 * 01:00:00 is time 3600sec and etc.
 *
 *
 */

public interface OrderTime {

    /**
     * Get the time of the day in seconds.
     * @return second representation of time
     */
    long getOrderTime();
    /**
     * Set the time represented in String format,
     * such as 00:00:00.
     * Then convert it to seconds.
     * @param orderTime String
     * @return successful or not
     */
    boolean setOrderTime(String orderTime);

}
