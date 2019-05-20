package com.scheduler;

import com.delivery.DroneOrder;
import com.tools.NPSCalculator;
import com.tools.TimeConvert;

import java.util.*;


/**
 *  Input Stream Scheduler has the following features:
 * 	1. Handle continuous input stream. Never know when the next order comes until it actually came
 *  2. Algorithm:
 *  	    --When only 1 order comes, deliver; When more than 1 order is waiting, do the shortest
 *     The reason when only one order comes then deliver, is that you never know when will the next
 *     order come. So you need to deliver it.
 *     When many tasks come at the same time, or many tasks come in the middle of delivering
 *     an order, always deliver the shortest. The reason is if you want to make more people
 *     wait less time, you need to deliver short distance order, which can make others wait
 *     for the shortest time;
 *  3. Implementation:
 *  		|-- Use a queue to store the tasks (which actually simulate input task stream)
 *      |-- Use a Priority Queue to store waiting tasks, the task with the shorter distance
 *      	    comes first.
 *      |-- When PriorityQueue is empty(No task at all), poll task queue(One new task comes) and offer to PriorityQueue
 *      |-- When PriorityQueue is not empty(Currently there is task waiting to deliver),
 *          poll the task from PriorityQueue, deliver
 *      |-- When delivering, if there are other tasks comes, poll task queue and offer them to PriorityQueue
 *      		(Calculate the finish time of current delivery, poll task queue's task if they come before the
 *           finish time, then offer them to waiting queue, which is PriorityQueue)
 *  4. Graph
 *  				     --------------------                          -----------------------------
 *  		<--Deliver	   WaitingTasksQueue      <--Offer tasks               Upcoming Tasks
 *  					 ---------------------                         -----------------------------
 *                        Ascending                                Simulate Continuously Coming Tasks
 *
 *
 *
 */

public class InputStreamScheduler {

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
    private int neutral;
    private int detractors;

    /**
     * Initiate the scheduler with some upcoming tasks, which comes in time sequence
     * @param tasks
     */
    public InputStreamScheduler(Queue<DroneOrder> tasks) {
        super();
        this.tasks = tasks;
        queue = new PriorityQueue<DroneOrder>(new DroneOrderComparator()); // Comparator let shorter task goes first
        result = new ArrayList<DroneOrder>();
        printResult = new ArrayList<String>();
        totalOrder = 0;
        promoters = 0;
        neutral = 0;
        detractors = 0;
    }


    /**
     * Start scheduling
     */
    public void start() {
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
//			printResult.add(current.getOrderID().getOrderID()+" "+TimeConvert.longToStr(departureTime)
//							+" "+cal + ": "+current.geteTime()+ ", "+departureTime/60 +", "+finishTime/60 +", "+timeWait/60	);

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
     * Initiate the waiting queue(waiting queue is initially empty):
     * 	1. When only one task comes at a certain time k --> add it to waiting queue as initiation
     *  2. When several tasks comes together at a certain time k --> add the shortest one to the queue as initiation
     * @return previous tasks finish time
     */
    protected long initQueue() {
        ArrayList<DroneOrder> initHelper = new ArrayList<>(tasks);
        // if many tasks come at the init time,choose the fastest one and push into queue
        if(initHelper.size() > 1 && initHelper.get(0).geteTime() == initHelper.get(1).geteTime()) {
            DroneOrder temp = initHelper.get(0);
            long min = temp.getEdistance();
            int i = 0;
            // Find the shortest tasks
            while(i < initHelper.size() - 1 && initHelper.get(i).geteTime() == initHelper.get(i + 1).geteTime()) {
                if(initHelper.get(i + 1).getEdistance() < min) {
                    temp = initHelper.get(i + 1);
                    min = temp.getEdistance();
                }
                i++;
            }

            queue.offer(temp);
            initHelper.remove(temp);
            tasks = new LinkedList<DroneOrder>(initHelper);
            return queue.peek().geteTime();
        } else {  // if only one tasks comes at the init time, push the task into queue
            queue.offer(tasks.poll());
            return queue.peek().geteTime(); // init first finish time as it starts
        }
    }

    public List<DroneOrder> getResult() {
        return result;
    }

    public List<String> getPrintResult() {
        return printResult;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }


    public PriorityQueue<DroneOrder> getQueue() {
        return queue;
    }

    public void setQueue(PriorityQueue<DroneOrder> queue) {
        this.queue = queue;
    }


    public Queue<DroneOrder> getTasks() {
        return tasks;
    }


    public void setTasks(Queue<DroneOrder> tasks) {
        this.tasks = tasks;
    }


    public int getTotalOrder() {
        return totalOrder;
    }


    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }


    public int getPromoters() {
        return promoters;
    }


    public void setPromoters(int promoters) {
        this.promoters = promoters;
    }


    public int getDetractors() {
        return detractors;
    }


    public void setDetractors(int detractors) {
        this.detractors = detractors;
    }


    public void setResult(List<DroneOrder> result) {
        this.result = result;
    }


    public void setPrintResult(List<String> printResult) {
        this.printResult = printResult;
    }


    /**
     * You can directly print(scheduler)
     * @return formatted deliver sequence
     */
    @Override
    public String toString() {
        StringBuffer print = new StringBuffer();
        for(String cur : printResult)
            print.append(cur + "\n");
        return print.toString();
    }

}

/**
 * Comparator, takes two tasks, shorter one comes first.
 * Used in waiting tasks queue to always keep shorter tasks
 * comes first.
 *
 *
 */
class DroneOrderComparator implements Comparator<DroneOrder> {

    @Override
    public int compare(DroneOrder o1, DroneOrder o2) {
        if(o1.getEdistance() < o2.getEdistance())
            return -1;
        else if(o1.getEdistance() > o2.getEdistance())
            return 1;

        return 0;
    }

}

