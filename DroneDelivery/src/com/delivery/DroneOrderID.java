package com.delivery;

import com.delivery.interfaces.OrderID;

/**
 * A DroneID contains an actual ID such as: WM1234
 * and an easyID corresponding to the actual iD such as: 1234
 *
 *
 */

public class DroneOrderID implements OrderID {

    /**
     * OrderID represented as String
     */
    private String id;
    /**
     * Easy ID, erase the common prefix of orderiD
     */
    private int eid;

    /**
     * When creating an order, you need to
     * set its ID using a String
     * @param id
     */
    public DroneOrderID(String id) {
        setOrderID(id);
    }

    /**
     * If there is an OrderID, then return.
     * else return null.
     * @return OrderID
     */
    @Override
    public String getOrderID() {
        if(id != null) {
            return this.id;
        }
        return null; // or throw an exception
    }

    /**
     * Set OrderID and compute the easy ID.
     * An easyID erases the prefix of the original ID
     * Always return true, since it will be checked by DroneOrder.class
     * But if needed, you can add a validator here too.
     * @return true
     */
    @Override
    public boolean setOrderID(String id) {
        this.id = id;
        this.eid = Integer.parseInt(id.substring(2));
        return true;
    }

    public int geteID() {
        return eid;
    }

}