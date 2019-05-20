# Walmart Labs interview: Drone Delievry 

```
Author: Lei Zhang

Interviewer:
Surafel Korse - Staff Software Engineer, Walmart Retail Technology – East
Yatman Hau - Sr. Software Engineer, Walmart Retail Technology – East
Shreedhar Ramachandra - Senior Software Engineer, Walmart Retail Technology - East

Thanks for your time!

Date: 05/16/2019
```

## Assumption:

```
1. OrderDirection format strictly follows: N/S X + E/W Y. OrderID format strictly follows: WMXXXX (4 digits). 
Order TimeStamp format strictly follows: HH:MM:SS and will not exceed 24(24-hour Time Format) . All input formats are valid.

2. Assume drone only goes straight and we calculate Manhattan distance instead of Euclidean distance.

3. OrderTime always within a day, we do not consider overtime order or can't finish before 10PM.
Time is always valid and are in the same day. We can assume Input is from 6 a.m. until 10 p.m. And the drone is able to finish the work same day(clear the order in warehouse).

3.(alternative way) drone only works from 6:00 - 22:00. start to work at 6 and if the task can't be finished by 22:00, give up that delivery.

4. Orders come as a data stream, you never know if you have next order or when will you have next order.

5. OrderDistance is always within the range of the Drone(within warehouse delivery range).

6. NPS Calculation:
	 0 <= Waiting Time <= 1 hour : Promoters
	 1 < Waiting Time <= 3 hours : Neutral
	 3 < Waiting Time            : Detractors

7. When only one order comes, deliver it regardless its distance, since you never know when is the next order.

8. If and Only if one order exists, even the delivery time > 3 hours(Must be detractors). 
   the scheduler has to deliver this item because you don't know when will your next order come.
   But if delivery time > 3 hours and it's not the only item, put this item the last to deliver.

9. Drone delivers one item at a time and have to go back to warehouse to pick up the next item. That also means you have to calculate double delivery time(time to return to warehouse).

10. when dealing with multiple orders, just deliver the closet orders. Because ,literally, warehouse will be built where lots of people live nearby and also we can serve more people(to get high promoters rate) in this way. Eventually, we can get higher NPS socre.
There could be corner case people all live far away(but Actually you will not build warehouse here).
And worst case you always miss other's orders, we have to find the optimal way by processing enough data.
```

## Instruction:

### usage:
<img align="left" width="100" height="100" src ="https://github.com/ferzl123/DroneDelivery/blob/master/DroneDelivery/Project%20structure.png">

```
    java -jar DroneDelivery.jar -input input_file_path [-output output_file_path] [-scheduler scheduler_type] [-print Y/N]
Usage(.class File): 
    java iogithubowenying.test.Test -input input_file_path [-output output_file_path] [-scheduler scheduler_type] [-print Y/N]
	
Options: 
    -output    : Output file path, default as "./result.txt"   
    -scheduler : dynamic , (Shortest order first, first come first serve)(also as default)
                 unfair  , (Detractors Tasks always be delivered at the very last)
                 limited , (only deliver from 6am - 10pm + dynamic mode)
                 unfair_limited , (unfair mode + limited mode)
    -print     : print result in terminal or not. "Y" print; "N" not print, default as "N"
```

### Compile:

```

https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html#get-started
1. As Intellij Project:
		|-- Import Project into Intellij
		|-- Compile
		|-- Right click the project --> Run as --> Run Configurations --> Set main class as ./test.Test
		|-- Export the project as Runnable Jar File
			|-- Right click on the project
			|-- Click export as Runnable Jar File
		|-- Run
			|-- Follows the Usage of Jar File
		
	2. Using command line
		|-- Enter root directory of the project
			|-- src : source files
			|-- bin : binary executables
			|-- test: some test files
		|-- find ./src -name "*.java" > source.txt
			|-- find all the java file under src and export into source.txt
		|-- javac -d bin @source.txt
			|-- Compile all the java files listed in source.txt
			|-- Output the binary files into bin folder
		|-- Enter bin file
		|-- Run
			|-- Follows the Usage of .class File


```

## Core Algorithm:

    When only 1 order comes, deliver; When more than 1 order is waiting, do the shortest
     |--The reason when only one order comes then deliver, is that you never know when will the next 
       order come. So you need to deliver it. 
     |--When many tasks come at the same time, or many tasks come in the middle of delivering 
       an order, always deliver the shortest. The reason is if you want to make more people
       wait less time, you need to deliver short distance order, which can make others wait
       for the shortest time; 
       
## Implementation:

```
    |-- Use a queue to store the tasks (which actually simulate input task stream)
    |-- Use a Priority Queue to store waiting tasks, the task with the shorter distance
        	    comes first.
    |-- When PriorityQueue is empty(No task at all), poll task queue(One new task comes) and offer to PriorityQueue
    |-- When PriorityQueue is not empty(Currently there is task waiting to deliver),
            poll the task from PriorityQueue, deliver
    |-- When delivering, if there are other tasks comes, poll task queue and offer them to PriorityQueue
        		(Calculate the finish time of current delivery, poll task queue's task if they come before the 
             finish time, then offer them to waiting queue, which is PriorityQueue)
```

## Project Hierarchical Structure:

```
	Order(I)           -->      DroneOrder
	OrderDirection(I)  -->      DroneOrderDirection
	OrderID(I)         -->      DroneOrderID
	OrderTime(I)       -->      DroneOrderTime
	
	DynamicInputStreamScheduler              -->      DynamicInputWithWaitScheduler
	DynamicInputStreamLimitedTimeScheduler   -->      DynamicInputStreamLimitedTimeWithWaitScheduler
	
	RandomTestGenerator(T)
	NPSCalculator(T)
	ReadOrdersFromFile(T)
	TimeConvert(T)
	WriteOrdersToFile(T)
	
	Test(Main)
	TestGenerator(JunitTest)
	
  I: Interface, T: Tool class
```

## Future Usage

```
1. multihtread/concurrent situation, using sychronized to lock the method to make thread safe
2. Time outside of 6:00am - 10:00pm,  need to wait till the next day
3. Order distance too far away  -> corresponds to closet warehouse
4. multiple warehouse, multiple drones, carry multiple items.(real-life situations)
5. some certain/special item need to be delivered whitin a given time(flowers, vege, food and etc)
6. compare different kinds of ways of delivering: closet first, farthest first, smart wait(need to test parameters like waiting time, exist order optimal way).
7. Do a survey about how many customers living nearby and how many customers live far away. Make a HeatMap trying to figure out a optimal way.
8. add machie learning model to find the optimal way(with enough dataset, more concise model and better result)
```

some future algorithm for single drone, single item.
http://www.optimization-online.org/DB_FILE/2017/09/6206.pdf

