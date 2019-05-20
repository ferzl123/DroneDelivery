package com.scheduler;


import com.delivery.DroneOrder;

import java.util.*;



/**
 * Deliver in limited time + Do detractor tasks at very last.
 * In this case, we can maximize NPS as much as possible
 *
 *
 */
public class InputStreamLimitedTimeWithWaitScheduler extends InputStreamLimitedTimeScheduler {

    /**
     * Task queue, simulate continuously coming tasks
     */
    private Queue<DroneOrder> tasks;

    /**
     * Deliver in limited time + Do detractor tasks at very last
     * @param tasks Orders stream
     * @param start In seconds, the time the drone start working
     * @param end   In seconds, the time the drone end working
     */
    public InputStreamLimitedTimeWithWaitScheduler(Queue<DroneOrder> tasks, int start, int end) {
        super(tasks, start, end);
        if(start < 0 || end < 0 || start > end) {
            System.out.println("Working time not permitted");
//			throw new Exception();
            return;
        }
        this.tasks = super.getTasks();
    }

    @Override
    public void start() {
        // Detractors be delivered at very last
        // Edge case
        if(tasks.isEmpty())
            return;
        // Move the detractors to the back of the task
        tasks = new InputWithWaitScheduler(new LinkedList<DroneOrder>()).rearrangeTasks(tasks);

        super.setTasks(tasks);
        super.start();

    }
}
