package com.scheduler;


import com.delivery.DroneOrder;
import com.tools.NPSCalculator;
import com.tools.TimeConvert;

import java.util.*;


/**
 * The basic algorithm and rules are exactly the same as DynamicInputStreamScheduler.
 * The only thing different, is that the Drone only work between time_start and time_end.
 * In this challenge, the drone will work from 6am to 10pm, which means it strictly
 * starts waiting for orders at 6am, and it never work after 10pm. If an order
 * starts before 10pm but delivery time after 10pm, the drone doesn't deliver it.
 *
 *

 *
 */
public class InputStreamLimitedTimeScheduler extends InputStreamScheduler {

    /**
     * The start of the working time period
     */
    private int start;
    /**
     * The end of the working time period
     */
    private int end;

    /**
     * PriorityQueue to store waiting tasks.
     * Shorter tasks come first
     */
    private PriorityQueue<DroneOrder> queue;
    /**
     * Task queue, simulate continuously coming tasks
     */
    private Queue<DroneOrder> tasks;
    /**
     * Store the result DroneOrder sequence
     */
    private List<DroneOrder> result;
    /**
     * Store the result sequence in a readable way.
     * Is formatted, can be output to file
     * eg: WMXXXX 00:00:00
     */
    private List<String> printResult;
    /**
     * Total Order number, promoters number, neutral number and detractors number, in order to calculate NPS
     */
    private int totalOrder;
    private int promoters;
    @SuppressWarnings("unused")
    private int neutral;
    private int detractors;


    /**
     * Given several tasks, and the start working time and end working time,
     * only do the task which starts after starting time
     * only do the task which can be finished by the ending time
     * @param tasks Orders stream
     * @param start In seconds, the time the drone start working
     * @param end   In seconds, the time the drone end working
     */
    public InputStreamLimitedTimeScheduler(Queue<DroneOrder> tasks, int start, int end) {
        super(tasks);
        if(start < 0 || end < 0 || start > end) {
            System.out.println("Working time not permitted");
//			throw new Exception();
            return;
        }
        this.start = start;
        this.end = end;
        this.tasks = super.getTasks();
        this.queue = super.getQueue();
        this.result=  super.getResult();
        this.printResult = super.getPrintResult();
        this.totalOrder = super.getTotalOrder();
        this.promoters = super.getPromoters();
        this.neutral = super.getNeutral();
        this.detractors = super.getDetractors();
    }

    /**
     * Starting and ending time inclusive
     */
    @Override
    public void start() {
        // Edge case
        if(tasks.isEmpty())
            return;
        // Filter out the out of time range tasks
        tasks = filterTasksWithTimeRange(tasks, start, end);
        super.setTasks(tasks);

        // Edge case: if there is no tasks, return
        if(tasks.isEmpty())
            return;
        // push the first task
        long finishTime = initQueue();

        while(!queue.isEmpty() || !tasks.isEmpty()) {
            // Process current task
            DroneOrder current = queue.poll();  // get the task to be processed
            long departureTime = finishTime; // departure time = previous tasks finish time
            long deliveryTime = current.getEdistance() * 60; // deliveryTime = distance * 60 (sec)
            long flyBackTime = deliveryTime;
            finishTime = departureTime + deliveryTime + flyBackTime;

            // When out of devery time, break
            if(finishTime < this.start || finishTime > this.end) {
                //In case that:  when short task out of finish time, but longer task can be finished by that time
                if(queue.isEmpty() && !tasks.isEmpty()) {
                    finishTime = initQueue();
                }
                continue;
            }

            // Calculate NPS
            long timeWait = departureTime + deliveryTime - current.geteTime();
            totalOrder ++;
            String cal = NPSCalculator.calculateSatisfaction(timeWait);
            if("Invalid".equals(cal))
                System.out.println("Invalid waiting time");
            else if("Promoters".equals(cal))
                promoters ++;
            else if("Neutral".equals(cal))
                neutral ++;
            else if("Detractors".equals(cal))
                detractors ++;

            // Add result
            result.add(current);
            printResult.add(current.getOrderID().getOrderID()+" "+TimeConvert.longToStr(departureTime));

            // Add all tasks which starts before previous task finish
            while(true) {
                if(!tasks.isEmpty() && tasks.peek().geteTime() <= finishTime)
                    queue.offer(tasks.poll());
                else
                    break;
            }
            //if currently no task
            if(queue.isEmpty() && !tasks.isEmpty()) {
                finishTime = initQueue();
            }

        }

        String nps = "NPS " +  ((promoters - detractors)/(double)totalOrder)*100;
        printResult.add(nps);

    }

    /**
     * Filter out the tasks outside of specified time range.
     * @param orders
     * @param start time range's start
     * @param end time range's end
     * @return Queue of filtered tasks
     */
    protected Queue<DroneOrder> filterTasksWithTimeRange(Queue<DroneOrder> orders, int start, int end) {
        Queue<DroneOrder> tasks = new LinkedList<>();
        while(!orders.isEmpty()) {
            DroneOrder temp = orders.poll();
            if(start <= temp.geteTime() && temp.geteTime() <= end)
                tasks.offer(temp);
        }
        return tasks;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Queue<DroneOrder> getTasks() {
        return tasks;
    }

    public void setTasks(Queue<DroneOrder> tasks) {
        this.tasks = tasks;
    }



}

