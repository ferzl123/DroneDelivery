package com.tools;


/**
 * A Tool class, used to calculate NPS based on
 * the given table.
 * Waiting Time = Time user get the goods - Time when user make an order
 * Waiting Time < 0 : Invalid
 * 0 <= Waiting Time <= 1 hour : Promoters
 * 1 < Waiting Time <= 3 hours : Neutral
 * 3 < Waiting Time            : Detractors
 *
 *
 */

public class NPSCalculator {
    public final static int  PROMOTERS = 60 * 60;
    public final static int  NEUTRAL = 60 * 60 * 3;
    public final static int  DETRACTORS = 60 * 60 * 3;

    /**
     * Calculate which category the watingTime belongs to.
     * All the calculations are second based.
     * @param waitingTime in seconds
     * @return category, including "Invalid", "Promoters", "Neutral", "Detractors".
     */
    public static String calculateSatisfaction(long waitingTime) {
        if(waitingTime < 0)
            return "Invalid";
        else if(waitingTime <= PROMOTERS)   // shorter than 1 hour
            return "Promoters";
        else if(waitingTime <= NEUTRAL) // shorter than 3 hours
            return "Neutral";
        else
            return "Detractors";
    }
}
