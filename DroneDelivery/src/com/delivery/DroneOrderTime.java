package com.delivery;


import com.delivery.interfaces.OrderTime;
import com.tools.TimeConvert;

/**
 * Since there is no date information,
 * DroneOrderTime is represented as seconds of the day,
 * starting at 00:00:00.
 * eg: 01:00:00 --> 3600
 *
 *
 */

public class DroneOrderTime implements OrderTime {

    /**
     * Time in seconds
     */
    private long time;  // time in seconds

    /**
     * Initiate DroneOrderTime with String formatted as "HH:MM:SS".
     * Use setter to check the input orderTime
     * @param orderTime
     */
    public DroneOrderTime(String orderTime) {
        setOrderTime(orderTime);
    }

    /**
     * Get order's coming time, in seconds
     * @return time in seconds; if time < 0, return -1;
     */
    @Override
    public long getOrderTime() {
        if(this.time >= 0)
            return this.time;
        return -1;
    }

    /**
     * Set time only the String matches "HH:MM:SS" format.
     * @return true, if it matches "HH:MM:SS"; false, if it doesn't match "HH:MM:SS"
     */
    @Override
    public boolean setOrderTime(String orderTime) {
        if(orderTime.matches("\\d{2}:\\d{2}:\\d{2}")) {
            time = TimeConvert.strToInt(orderTime);
            return true;
        }

        return false;
    }

}