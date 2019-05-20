package com.tools;


/**
 * TimeConvert Tool class is used to operate
 * time. Such as convert HH:MM:SS to seconds
 * and etc.
 *
 *
 */

public class TimeConvert {

    /**
     * Convert HH:MM:SS to seconds. 00:00:00 --> 0 (seconds)
     * If time is invalid, throw an exception or return -1
     * @param HHMMSS
     * @return time in seconds
     */
    public static int strToInt(String HHMMSS) {

        String[] temp = HHMMSS.split(":");
        int hour = Integer.parseInt(temp[0]);
        int minute = Integer.parseInt(temp[1]);
        int seconds = Integer.parseInt(temp[2]);

        // check whether the parts are valid
        if(hour >= 0 && hour <= 24 && minute >= 0 && minute <= 60 && seconds >=0 && seconds <= 60) {
            int time = hour * 3600 + minute * 60 + seconds;
            return time;
        } else {
            //throw new IndexOutOfBoundsException();
            return -1;
        }

    }

    /**
     * Convert seconds to HH:MM:SS format
     * @param timeInSeconds
     * @return HH:MM:SS
     */
    public static String intToStr(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;

    }

    /**
     * Convert seconds to HH:MM:SS format
     * @param timeInSeconds
     * @return HH:MM:SS
     */
    public static String longToStr(long timeInSeconds) {
        long hours = timeInSeconds / 3600;
        long secondsLeft = timeInSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;

    }

}
