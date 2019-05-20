package com.scheduler;

import com.delivery.DroneOrder;
import com.tools.NPSCalculator;

import java.util.*;


/**
 * Dynamic Input Stream Scheduler has the following features:
 * 	1. Handle continuous input stream. Never know when the next order comes until it actually came
 *  2. Algorithm:
 *  	    |--When only 1 order comes
 *  			|--If it is a task longer than k, wait a certain time for potential upcoming shorter tasks
 *  				|--No shorter tasks come -- Deliver
 *  				|--Shorter tasks come -- Deliver  shorter ones
 *  			|--else, this order is a short order, deliver
 *      |--When more than 1 order is waiting, do the shortest after waiting for a while
 *     The reason when only one order comes then deliver, since you never know when will the next
 *     order come. So you need to deliver it. But what if in a short period of time a short task
 *     will come? (eg: At time 0, a 10000 order come, and At time 1, a 100 order come) In this case,
 *     this scheduler will wait for a short period of time before delivering a long task. The short
 *     period of time is set by the programmer.
 *     When many tasks come at the same time, or many tasks come in the middle of delivering
 *     an order, always deliver the shortest. The reason is if you want to make more people
 *     wait less time, you need to deliver short distance order, which can make others wait
 *     for the shortest time;
 *
 *     In this unfair scheduler, we set waiting time as follows:
 *     		Waiting Time for Promoters --> 0;
 *     		Waiting Time for Neutrals  --> 0;
 *          Waiting Time for Detractors--> infinite, as long as there is non-detractors, wait
 *     In this case, this algorithm is unfair to detractors, but benefits NPS
 *

 *
 */

public class InputWithWaitScheduler extends InputStreamScheduler{

    /**
     * Task queue, simulate continuously coming tasks
     */
    private Queue<DroneOrder> tasks;

    /**
     * Set superclass's task, later superclass method will be used
     * @param tasks
     */
    public InputWithWaitScheduler(Queue<DroneOrder> tasks) {
        super(tasks);
        this.tasks = super.getTasks();
    }

    /**
     * Rearrange the task queue, when a detractors task comes,
     * leave it aside, until certain time there is no non-detractors tasks,
     * then start delivering the detractors tasks.
     */
    @Override
    public void start() {
        // Detractors be delivered at very last
        // Edge case
        if(tasks.isEmpty())
            return;
        // Rearrange the tasks: Detractor comes last
        tasks = rearrangeTasks(tasks);
        super.setTasks(tasks);
        // Use dynamic scheduler with the rearranged tasks
        super.start();
    }

    /**
     * Calculate whether an order must be detractors.
     * Once one-way deliver time > detractorTime, it must be detractors
     * @param order
     * @return whether the order must be detractors or not
     */
    public boolean mustBeDetractor(DroneOrder order) {
        long deliveryTime = order.getEdistance() * 60; // deliveryTime = distance * 60 (sec) (one way)
        if(deliveryTime > NPSCalculator.DETRACTORS)
            return true;
        return false;
    }

    /**
     * Rearrange the task queue, put all detractors at the end of the queue
     * @param tasks
     * @return queue whose detractors always be at the end
     */
    public Queue<DroneOrder> rearrangeTasks(Queue<DroneOrder> tasks) {
        Queue<DroneOrder> detractors = new LinkedList<>();
        Queue<DroneOrder> originalSeq = new LinkedList<>();
        while(!tasks.isEmpty()) {
            DroneOrder temp = tasks.poll();
            if(mustBeDetractor(temp))
                detractors.offer(temp);
            else
                originalSeq.offer(temp);
        }
        while(!detractors.isEmpty())
            originalSeq.offer(detractors.poll());
        return originalSeq;
    }

}


