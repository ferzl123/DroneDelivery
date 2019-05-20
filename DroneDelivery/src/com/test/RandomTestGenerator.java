package com.test;


import com.delivery.DroneOrder;
import com.tools.ReadOrdersFromFile;
import com.tools.TimeConvert;
import com.tools.WriteOrdersToFile;

import java.util.*;


/**
 * To generate a test file, you need
 * WMXXXX: unique id
 * N5E10 : direction
 * HH:MM:SS : timestamp, sorted
 *
 *
 */
public class RandomTestGenerator {

    /**
     * Write a list of order to specific path
     * @param path, path of output file
     * @param orders, which you want to write into a file
     * @return true, indicate whether write is successful, can be implemented later
     */
    public static boolean writeTestCasesToFile(String path, List<DroneOrder> orders) {
        WriteOrdersToFile.writeOrdersToFile(path, orders);
        return true;
    }

    /**
     * Generate n random Test orders with format: WMXXXX N10S8 HH:MM:SS
     * @param n, how many random test cases you want to generate
     * @param xMax, for direction, x-axis max distance. Generator will generate
     * 		  random x-distance from 0 - xMax
     * @param yMax, for direction, y-axis max distance. Generator will generate
     * 		  random y-distance from 0 - yMax
     * @param tMin, the ground of the first time stamp
     * @param tMax, the roof of the time stamp,    tMin <= time stamp <= tMax
     * @return PriorityQueue of the random orders, sorted by its time stamp
     */
    public static PriorityQueue<DroneOrder> generateRandomTestOrder(int n, int xMax, int yMax, int tMin, int tMax) {
        PriorityQueue<DroneOrder> tasks = new PriorityQueue<>(new TimeStampComparator());
        for(int i = 0; i < n; i ++) {
            String id = generateRandomID("WM",0, 9);
            String direction = generateRandomDirection(xMax, yMax);
            String timestamp = generateRandomTimeStamp(tMin,tMax);
            if(timestamp == null || id == null || direction == null) {
                System.out.println("Unvalid generator parameters");
                continue;
            }
//			System.out.println(id+" "+direction+" "+timestamp);
            DroneOrder temp = ReadOrdersFromFile.parseDroneOrder(id+" "+direction+" "+timestamp);
//			System.out.println(temp);
            tasks.offer(temp);
        }
        return tasks;
    }

    /**
     * Generate order ID, with prefix+digits. Here we generate 4 digits
     * to match WMXXXX, in which prefix is WM
     * @param prefix The prefix of the id
     * @param minRandomDigit Each digit's ground
     * @param maxRandomDigit Each digit's roof.  minRandomDigit <= digit <= maxRandomDigit
     * @return String A randomly generated id formatted as : prefix+dddd
     */
    public static String generateRandomID(String prefix,int minRandomDigit, int maxRandomDigit) {
        if(minRandomDigit < 0 || maxRandomDigit < 0 || minRandomDigit > maxRandomDigit)
            return null;
        StringBuffer id = new StringBuffer();
        id.append(prefix);
        id.append(getRandomIntegerBetweenRange(minRandomDigit, maxRandomDigit));
        id.append(getRandomIntegerBetweenRange(minRandomDigit, maxRandomDigit));
        id.append(getRandomIntegerBetweenRange(minRandomDigit, maxRandomDigit));
        id.append(getRandomIntegerBetweenRange(minRandomDigit, maxRandomDigit));
        return id.toString();
    }

    /**
     * Generate random direction, such as N100E50.
     * Firstly, since x-axis has two choices, randomly choose one each time.
     * Secondly, since y-axis has two choices, randomly choose one each time.
     * Thirdly, randomly generate a xDistance which  0 <= xDistance <= xMax
     * Fourthly, randomly generate a yDistance which 0 <= yDistance <= yMax
     * @param xMax Roof of xDistance
     * @param yMax Roof of yDistance
     * @return String Randomly generated Direction, such as N100E50
     */
    public static String generateRandomDirection(int xMax, int yMax) {
        if(xMax < 0 || yMax < 0)
            return null;
        char[] x = new char[] {'N','S'};
        char[] y = new char[] {'E','W'};
        int randomXD = getRandomIntegerBetweenRange(0, 100);
        int randomYD = getRandomIntegerBetweenRange(0, 100);
        char xChar = x[randomXD % 2];
        char yChar = y[randomYD % 2];
        int xDistance = getRandomIntegerBetweenRange(0, xMax);
        int yDistance = getRandomIntegerBetweenRange(0, yMax);

        return "" + xChar + xDistance + yChar + yDistance;
    }

    /**
     * Generate a random time stamp in seconds, which tMin <= time stamp <= tMax
     * @param tMin Ground of time stamp, in seconds
     * @param tMax Roof of time stamp, in seconds
     * @return String Randomly generated time stamp.
     */
    public static String generateRandomTimeStamp(int tMin, int tMax) {
        if(tMax > 24*60*60)	tMax = 24*60*60;
        if(tMax < 0)		tMax = 0;
        if(tMin < 0)		tMin = 0;
        if(tMin > 24*60*60) tMin = 24*60*60;
        if(tMin > tMax)	return null;

        long timestamp = getRandomIntegerBetweenRange(tMin, tMax);
        String formattedTimeStamp = TimeConvert.longToStr(timestamp);
        return formattedTimeStamp;
    }

    /**
     * Get random integer between min and max
     * @param min
     * @param max
     * @return Integer, the random generated integer
     */
    public static int getRandomIntegerBetweenRange(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return (int)x;
    }

}

/**
 * Comparator, sort time stamp based on the
 * time line
 *
 *
 */
class TimeStampComparator implements Comparator<DroneOrder> {

    @Override
    public int compare(DroneOrder o1, DroneOrder o2) {
        if(o1.geteTime() < o2.geteTime())
            return -1;
        else if(o1.geteTime() > o2.geteTime())
            return 1;

        return 0;
    }

}

