package com.delivery;


import com.delivery.interfaces.OrderDirection;

/**
 * DroneOrderDirection has a String represented direction
 * such as "N50E40", and a distance which is calculated
 * based on Manhattan Distance.
 *
 * @author Owen
 *
 */

public class DroneOrderDirection implements OrderDirection {

    /**
     * String represented distance, such as "N5E10"
     */
    private String direction;
    /**
     * Calculate the Manhattan Distance
     * based on the distance
     */
    private long distance;

    /**
     * Initiate DroneOrderDirection with the String
     * format direction, such as "N5E10"
     * @param direction
     */
    public DroneOrderDirection(String direction) {
        super();
        setOrderDirection(direction);
    }

    /**
     * Get the String represented direction.
     * @return direction or null(if not found)
     */
    @Override
    public String getOrderDirection() {
        if(direction != null)
            return direction;
        return null;
    }

    /**
     * Set Order Direction and calculate the Manhattan distance.
     * Always returns true, since DroneOrder.class will validate it.
     * If needed, can also add validator there
     * @return true
     */
    @Override
    public boolean setOrderDirection(String orderDirection) {
        direction = orderDirection;
        // here using Manhattan distance
        int i = 1;
        while(true) {
            if(direction.charAt(i) == 'N' || direction.charAt(i) == 'E'
                    || direction.charAt(i) == 'W' || direction.charAt(i) == 'S') {
                break;
            }
            i++;
        }
        int xDistance = Integer.parseInt(direction.substring(1, i));
        int yDistance = Integer.parseInt(direction.substring(i+1));
        distance = xDistance + yDistance;
        return true;
    }

    @Override
    public long getOrderDistance() {

        return distance;
    }


}
