package com.delivery.interfaces;

/**
 * Order interface defines 3 getter/setters and 3 checking methods
 * which associated with OrderID, Direction, OrderTime.
 *
 * For further usage:
 * An Order with new features can extend or modify the current Order interface
 *
 */
public interface Order {

    /**
     * Get OrderID, which will return an interface type -- OrderID
     * @return OrderID
     */
    OrderID getOrderID();
    /**
     * Set OrderID
     * @param id
     * @return whether set action is successful
     */
    boolean setOrderID(OrderID id);

    /**
     * Get Direction, which returns an OrderDirection interface type,
     * indicates the User's direction.
     * @return OrderDirection
     */
    OrderDirection getDirection();

    /**
     * Set OrderDirection
     * @param orderDirection
     * @return whether set action is successful
     */
    boolean setDirection(OrderDirection orderDirection);

    /**
     * Get OrderTime, which returns an OrderTime interface,
     * indicating when does the user make this order.
     * @return OrderTime
     */
    OrderTime getOrderTime();

    /**
     * Set OrderTime
     * @param orderTime
     * @return whether set action is successful
     */
    boolean setOrderTime(OrderTime orderTime);

    /**
     * When setting an OrderID, firstly need to check whether it is valid
     * @param id
     * @return whether the OrderID is valid
     */
    boolean isOrderIDValid(OrderID id);
    /**
     * When setting an Order Direction, firstly need to check whether it is valid
     * @param orderDirection
     * @return whether the OrderDirection is valid
     */
    boolean isOrderDirectionValid(OrderDirection orderDirection);
    /**
     * When setting an Order Arriving Time, firstly need to check whether it is valid
     * @param orderTime
     * @return whether the OrderTime is valid
     */
    boolean isOrderTimeValid(OrderTime orderTime);
}
