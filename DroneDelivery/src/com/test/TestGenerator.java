package com.test;



import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;

import com.delivery.DroneOrder;
import com.scheduler.InputStreamLimitedTimeWithWaitScheduler;
import com.tools.ReadOrdersFromFile;

import org.junit.jupiter.api.Test;


class TestGenerator {

    @Test
    void test() {

//			Queue<DroneOrder> tasks = RandomTestGenerator.generateRandomTestOrder(100, 20, 20, 0, 60*60*24);
//			ArrayList<DroneOrder> temp = new ArrayList<>();
//			while(!tasks.isEmpty()) {
//				temp.add(tasks.poll());
//			}
//			RandomTestGenerator.writeTestCasesToFile("./random.txt", temp);



        String inputFilePath = "/Users/Owen/Desktop/testunfairlimited.txt";
    //	String outputFilePath = "./result.txt";


        // Scheduling
        List<String> order = null;
        try {
            order = ReadOrdersFromFile.readLinesFromFile(inputFilePath);
        } catch (NoSuchFileException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create tasks queue to store incoming tasks
        Queue<DroneOrder> tasks = new LinkedList<>();
        for(String cur : order) {
            tasks.offer(ReadOrdersFromFile.parseDroneOrder(cur)); // Read orders from file
        }

        // Use scheduler to schedule tasks
        InputStreamLimitedTimeWithWaitScheduler scheduler = new InputStreamLimitedTimeWithWaitScheduler(tasks, 6*60*60, 22*60*60);
        scheduler.start();  // start scheduling
        System.out.println(scheduler);
        System.out.println("finished");
        // Write schedule result to file
        //WriteOrdersToFile.writeLinesToFile(outputFilePath, scheduler.getPrintResult());
    }

}


