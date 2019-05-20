package com.delivery.interfaces;
/**
 * OrderDirection interface has a Direction setter/getter
 * and a getDistance() function.
 * The orderDirection is represented as String.
 *
 * Note that getOrderDistance() can be implemented
 * in different ways such as Manhattan Distance or Euclidean Distance.
 *
 *
 */

public interface OrderDirection {

    /**
     * Get OrderDirection, OrderDirection is represented
     * as String, such as "N50S4"
     * @return Order Direction String representation
     */
    String getOrderDirection();
    /**
     * Set OrderDirection
     * @param orderDirection
     * @return successful or not
     */
    boolean setOrderDirection(String orderDirection);
    /**
     * Compute the distance based on the direction,
     * we choose Manhattan Distance here
     * @return
     */
    long getOrderDistance();
}