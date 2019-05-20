package com.test;

import com.delivery.DroneOrder;
import com.scheduler.InputStreamLimitedTimeScheduler;
import com.scheduler.InputStreamLimitedTimeWithWaitScheduler;
import com.scheduler.InputStreamScheduler;
import com.scheduler.InputWithWaitScheduler;
import com.tools.ReadOrdersFromFile;
import com.tools.WriteOrdersToFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;


public class Test {

    public static void main(String[] args) {

        /*
         * Arguments from command line:
         * 	1. inputFilePath : Input file path from command line, with default value
         *  2. outputFilePath : Output file path from command line, with default value
         *  3. schedulerType : Indicates the type of scheduler to use, with default as dynamic
         *  4. print : Whether print the result in command line or not. Default is N
         */
        String inputFilePath = "/DroneDelivery/testData/testfileread.txt";
        String outputFilePath = "/DroneDelivery/testData/result.txt";
        String schedulerType = "dynamic";
        String print = "N";

        // If there is args in command line, set the params
        // else print usage messages
        if(args.length > 0) {
            for(int i = 0; i < args.length; ) {
                if("-input".equals(args[i])) {
                    if(i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                        inputFilePath = args[i + 1];
                        i += 2;
                    } else {
                        printUsage();
                        return;
                    }
                } else if("-output".equals(args[i])) {
                    if(i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                        outputFilePath = args[i + 1];
                        i += 2;
                    } else {
                        printUsage();
                        return;
                    }
                } else if("-scheduler".equals(args[i])) {
                    if(i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                        schedulerType = args[i + 1];
                        i += 2;
                    } else {
                        printUsage();
                        return;
                    }
                } else if("-print".equals(args[i])) {
                    if(i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                        print = args[i + 1];
                        i += 2;
                    } else {
                        printUsage();
                        return;
                    }
                }
                else {
                    printUsage();
                    return;
                }


            }
        } else {
            printUsage();
            return;
        }


        // Scheduling
        List<String> order = null;
        try {
            order = ReadOrdersFromFile.readLinesFromFile(inputFilePath);
        } catch (NoSuchFileException e) {
            printError("Input file not found!");  // if the file not found, print error
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create tasks queue to store incoming tasks
        Queue<DroneOrder> tasks = new LinkedList<>();
        for(String cur : order) {
            tasks.offer(ReadOrdersFromFile.parseDroneOrder(cur)); // Read orders from file
        }
        // Choose Scheduler
        if("dynamic".equals(schedulerType)) {
            // Use scheduler to schedule tasks
            InputStreamScheduler scheduler = new InputStreamScheduler(tasks);
            scheduler.start();  // start scheduling
            if("Y".equals(print))  // if -print Y, then print in command line
                System.out.println(scheduler);
            // Write schedule result to file
            WriteOrdersToFile.writeLinesToFile(outputFilePath, scheduler.getPrintResult());
        } else if("unfair".equals(schedulerType)) {
            InputWithWaitScheduler scheduler = new InputWithWaitScheduler(tasks);
            scheduler.start();
            if("Y".equals(print))
                System.out.println(scheduler);
            WriteOrdersToFile.writeLinesToFile(outputFilePath, scheduler.getPrintResult());
        } else if("limited".equals(schedulerType)) {
            InputStreamLimitedTimeScheduler scheduler = new InputStreamLimitedTimeScheduler(tasks, 6*60*60, 22*60*60);
            scheduler.start();
            if("Y".equals(print))
                System.out.println(scheduler);
            WriteOrdersToFile.writeLinesToFile(outputFilePath, scheduler.getPrintResult());
        } else if("unfair_limited".equals(schedulerType)) {
            InputStreamLimitedTimeWithWaitScheduler scheduler = new InputStreamLimitedTimeWithWaitScheduler(tasks, 6*60*60, 22*60*60);
            scheduler.start();
            if("Y".equals(print))
                System.out.println(scheduler);
            WriteOrdersToFile.writeLinesToFile(outputFilePath, scheduler.getPrintResult());
        }
        else {
            printSchedulerError();
        }

        // Print the output file path in command line
        printResultFilePath(outputFilePath);

    }


    /**
     * Print Usage messages in command line
     */
    private static void printUsage() {
        System.out.println("Usage(Jar File): \n" +
                "    java -jar DroneDelivery.jar -input input_file_path [-output output_file_path] [-scheduler scheduler_type] [-print Y/N]\n" +
                "Usage(.class File): \n" +
                "    java com.test.Test -input input_file_path [-output output_file_path] [-scheduler scheduler_type] [-print Y/N]\n" +
                "	\n" +
                "Options: \n" +
                "    -output    : Output file path, default as \"./result.txt\"   \n" +
                "    -scheduler : dynamic , (Shortest order first, first come first serve)(also as default)\n" +
                "                 unfair  , (Detractors Tasks always be delivered at the very last)\n" +
                "                 limited , (only deliver from 6am - 10pm + dynamic mode)\n" +
                "                 unfair_limited , (unfair mode + limited mode)\n" +
                "    -print     : print result in terminal or not. \"Y\" print; \"N\" not print, default as \"N\"");
    }

    /**
     * Print Error message in command line
     */
    private static void printSchedulerError() {
        System.out.println("Error: Sorry, we don't have other scheduler currently.");
    }

    /**
     * Print output file Info in command line
     * @param output  output_file path
     */
    private static void printResultFilePath(String output) {
        System.out.println("output_file: " + output);
    }

    /**
     * Print error message in command line
     * @param err  error message
     */
    private static void printError(String err) {
        System.out.println("Error: " + err);
    }

}
