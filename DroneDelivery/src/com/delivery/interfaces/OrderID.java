package com.delivery.interfaces;

/**
        * This interface defines a basic OrderID interface.
        * This interface includes set/get OrderID and
        * geteID, which indicates get easy ID. An easy
        * ID is defined as shorter ID of Order ID.
        * eg: OrderID = WM1234, easyID = 1234.
        * With easy ID, it could be easier to compare ID under
        * same system, eg:  compare WM1234 and WM2345.
        * But cannot used to compare IDs under different system,
        * eg: Game1234, Food1111.
        * Overall, easy ID is used to simplify some operations
        * associated with OrderID
        *
        *
        *
        */

public interface OrderID {

    /**
     * Get OrderID, represented as String
     * @return String, representing the OrderID
     */
    String getOrderID();
    /**
     * Set OrderID, represented as String
     * @param id
     * @return whether set operation is successful
     */
    boolean setOrderID(String id);
    /**
     * Get easyID, represented as integer.
     * For example:  OrderID = "WM1234", easyID = 1234
     * Attention: Only use easy ID under same department(Has same ID prefix)
     * @return easyID
     */
    int geteID();
}
