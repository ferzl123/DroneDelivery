package com.delivery;

import java.util.*;
import com.delivery.interfaces.Order;
import com.delivery.interfaces.OrderDirection;
import com.delivery.interfaces.OrderID;
import com.delivery.interfaces.OrderTime;
/**
 * A DroneOrder is implements from Order interface.
 * It includes id, direction and time.
 *
 *
 */

public class DroneOrder implements Order {

    /**
     * A DroneOrder id is represented as: WMXXXX
     */
    private OrderID id;
    /**
     * A DroneOrder direction is represented as: N50S7
     * and distance using Manhattan Distance
     */
    private OrderDirection direction;
    /**
     * A DroneOrder time is represented as: HH:MM:SS
     * Since there is no date info, so the time
     * can also be represented in seconds using long integer
     */
    private OrderTime time;

    /**
     * An eID is an easyID, which erase the common prefix of
     * OrderID and convert the digits into integer. Using
     * eID can simplify the operations in some functions.
     * eg: OrderID = "WM1234" , eID = 1234
     * Note that eID cannot be used under different OrderID prefix,
     * which means it can only be used under the same department
     * eg: OrderID = "Game1234" or OrderID = "Food1234" cannot
     * be compared using eID
     */
    private int eID;
    /**
     * An edistance is an easy distance, which is calculated based
     * on defined distance function. For Drone Delivery problem,
     * since it is defined as Manhattan Distance, the edistance
     * is the distance of X-axis + the distance of Y-axis.
     */
    private long edistance;
    /**
     * An eTime is an easy time, which is calculated based on
     * HH:MM:SS. It is represented by seconds. Starting from 00:00:00.
     * eg: 01:00:00 --> eTime = 3600s
     */
    private long eTime;

    /**
     * Constructed using orderID, orderDirection and orderTime.
     * Make use of setter methods, to keep the variable safe,
     * since set methods contains checking mechanism.
     * @param id
     * @param direction
     * @param time
     */
    public DroneOrder(OrderID id, OrderDirection direction, OrderTime time) {
        setOrderID(id);
        setOrderTime(time);
        setDirection(direction);
    }


    @Override
    public OrderID getOrderID() {
        return this.id;
    }

    /**
     * Set DroneOrder's OrderID and set the easyID,
     * when the input OrderID is valid
     * @return whether the input OrderID is valid
     */
    @Override
    public boolean setOrderID(OrderID id) {
        if(this.isOrderIDValid(id)) {     // orderID valid check
            this.id = id;
            eID = id.geteID();            // get int eID of the ID
            return true;
        }
        return false;
    }

    @Override
    public OrderDirection getDirection() {
        return this.direction;
    }

    /**
     * Set DroneOrder's OrderDirection and set easy direction,
     * when the input Direction is valid.
     * In this task, the direction is valid if and only if it is
     * represented as : NS XX WE
     *
     * @return whether the orderDirection is valid
     */
    @Override
    public boolean setDirection(OrderDirection orderDirection) {
        if(this.isOrderDirectionValid(orderDirection)) {  // if the orderDirection is valid, then set
            this.direction = orderDirection;
            // set easy distance
            edistance = direction.getOrderDistance();
            return true;
        }
        return false;
    }

    @Override
    public OrderTime getOrderTime() {
        return this.time;
    }

    /**
     * Set DroneOrder's OrderDirection and set easy time,
     * when the input orderTime is valid.
     * In this task, the orderTime is valid only if it is
     * represented as : HH:MM:SS
     * @return whether the orderTime is valid
     */
    @Override
    public boolean setOrderTime(OrderTime orderTime) {
        if(this.isOrderTimeValid(orderTime)) {  // if the orderTime is valid, then set
            this.time = orderTime;
            eTime = orderTime.getOrderTime();
            return true;
        }
        return false;
    }

    /**
     * Check whether an orderID is valid using regex.
     * In this task, an orderID is valid if it follows the
     * format: WMXXXX
     * @return whether an orderID is valid
     */
    @Override
    public boolean isOrderIDValid(OrderID id) {
        String checkID = id.getOrderID();
        // use regex to check whether the id is WMXXXX
        if(checkID.matches("WM\\d{4}")) {
            return true;
        }
        return false;
    }

    /**
     * Check whether an orderDirection is valid using regex.
     * In this task, an orderDirection is valid if it follows
     * the format: N100S5
     * @return whether an orderDirection is valid
     */
    @Override
    public boolean isOrderDirectionValid(OrderDirection orderDirection) {
        String checkOrderDirection = orderDirection.getOrderDirection();
        // use regex to check whether the direction is N11W5
        if(checkOrderDirection.matches("[NEWS]\\d+[NEWS]\\d+")) {
            return true;
        }
        return false;
    }

    /**
     * Check whether an orderTime is valid.
     * If the second representation >= 0, means it is valid.
     * @return whether the orderTime is valid
     */
    @Override
    public boolean isOrderTimeValid(OrderTime orderTime) {
        long checkTime = orderTime.getOrderTime();
        // checkTime is in seconds
        if(checkTime >= 0)
            return true;
        return false;
    }


    public int geteID() {
        return eID;
    }

    public long getEdistance() {
        return edistance;
    }


    public long geteTime() {
        return eTime;
    }

    /**
     * Used to print a DroneOrder using its easy id,time and distance
     * The format is: "id: 1234, time: 3600, distance: 250".
     * @return String to be printed
     */
    @Override
    public String toString() {
        String print = "id: " + eID + ", time: " + eTime + ", distance: " + edistance;
        return print;
    }

}