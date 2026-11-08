package ProcessScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Scheduler selects which process runs next based on the chosen algorithm.
 * - FCFS
 * - RR (Round Robin)
 * - Non-preemptive priority
 */
public class Scheduler {

    protected final Queue<ProcessControlBlock> readyQueue;
    private final EventLog log;

    private String schedulerAlgorithm;
    private long timeQuantum;

    public Scheduler(Queue<ProcessControlBlock> readyQueue,
            String schedulerAlgorithm,
            long timeQuantum,
            EventLog log) {
        this.readyQueue = readyQueue;
        this.schedulerAlgorithm = schedulerAlgorithm;
        this.timeQuantum = timeQuantum;
        this.log = log;
    }

    public String getAlgorithm() {
        return schedulerAlgorithm;
    }

    public long getQuantum() {
        return timeQuantum;
    }

    /**
     * Executes exactly ONE scheduling decision per call.
     * 
     * @return stage number for convenience (1=FCFS, 2=RR, 3=PRIORITY), or -1 if
     *         unknown.
     */
    public int runAlgorithm() {
        if (schedulerAlgorithm == null)
            return -1;

        if (schedulerAlgorithm.equalsIgnoreCase("FCFS")) {
            FCFS();
            return 1;
        }

        if (schedulerAlgorithm.equalsIgnoreCase("RR")) {
            RR();
            return 2;
        }

        if (schedulerAlgorithm.equalsIgnoreCase("PRIORITY")
                || schedulerAlgorithm.equalsIgnoreCase("NPPRIORITY")
                || schedulerAlgorithm.equalsIgnoreCase("NPP")
                || schedulerAlgorithm.equalsIgnoreCase("Priority")) {
            nonPreemptivePriority();
            return 3;
        }

        return -1;
    }

    /**
     * Stage 1 — FCFS
     * Runs the first process in the ready queue to completion.
     * Remove the first PCB
     * Run it using CPU
     * Wait for completion
     * Set state to "terminated"
     * Update its execution time
     * Set the remaining burst time to be zero
     * Add PCB to EventLog
     * print the details of each completed PCB. For example,e.g., P01: terminated,
     * Context Switches: 0, Output: Sum is 9, Execution Time: 30ms
     */
    public void FCFS() {
        // TO DO
        if (readyQueue.isEmpty()){
            return;
        }

        System.out.println("Went through FCFS");
        
            ProcessControlBlock pcb = readyQueue.poll();
            CPU cpu = new CPU(pcb);
            cpu.start();
            try {
                cpu.join();
            } catch (InterruptedException e) {

                e.printStackTrace();
                System.out.println("Interrupt error " + e.getMessage());

            }
            pcb.setState("terminated");
            pcb.setRemainingBurstTime(0);
            pcb.addExecutedTime(pcb.getCPUBurstTime());
            log.addPCB(pcb);
            pcb.toString();
        }


    /**
     * Stage 2 — Round Robin (simulated CPU burst; script executes once at
     * completion)
     * - Remove head of readyQueue
     * - Run for min(remainingBurstTime, timeQuantum) (simulation)
     * - If unfinished: contextSwitches++, state="ready", re-add to END
     * - If finished: run Python once, terminate and log
     */
    public void RR() {

        if (readyQueue.isEmpty()) {
            return;
        }

        final ProcessControlBlock pcb = readyQueue.poll();
        boolean finished = false;
        long remainingTime = pcb.getRemainingBurstTime();

        // TO DO

        long runTime = Math.min(remainingTime, timeQuantum);
        long temp;

        // pcb.setRemainingBurstTime(remainingTime);

        CPU cpu = new CPU(pcb);
        cpu.start();
        try {
            cpu.join();
        } catch (InterruptedException e) {

            e.printStackTrace();
            System.out.println("Interrupt error " + e.getMessage());
        }

        pcb.addExecutedTime(runTime);
        temp = remainingTime - runTime;
        pcb.setRemainingBurstTime(temp);

        if (pcb.getRemainingBurstTime() == 0) {
            finished = true;
        }

        if (!finished) {

            pcb.addContextSwitch();
            pcb.setState("Ready");
            readyQueue.add(pcb);
        } else {
            pcb.setState("terminated");
            log.addPCB(pcb);
            pcb.toString();
        }

    }

    /**
     * Stage 3 — Non-Preemptive Priority Scheduling
     *
     * Picks the highest-priority process from readyQueue (higher number = higher
     * priority).
     * Tie-break: earlier arrivalTime first.
     * Non-preemptive: once selected, runs to completion.
     */
    public void nonPreemptivePriority() {



        System.out.println("Entered priority");

        long now = Main.getCurrentTime();
            int maxPriority = 0;
            //long lowestArrivalTime = 10000;
            ProcessControlBlock chosen = null;
            List<ProcessControlBlock> temp = new ArrayList<>();

            for (ProcessControlBlock pcb: readyQueue){

                //get pcbs with arrival less or equal to current time
                if (pcb.getArrivalTime() <= now){
                    temp.add(pcb);
                }
            }
            for (ProcessControlBlock pcb: temp){

                if (pcb.getPriority() == maxPriority){
                    if (chosen.getArrivalTime() > pcb.getPriority()){
                        chosen = pcb;
                    }
                }
                if (pcb.getPriority() > maxPriority){
                    
                    maxPriority = pcb.getPriority();
                    chosen = pcb;
                }
            }

            //returns pcb highest with highest priority
        //     for (ProcessControlBlock pcb : readyQueue) {
        //         if (pcb.getArrivalTime() < lowestArrivalTime){
        //             lowestArrivalTime = pcb.getArrivalTime();
        //         if (pcb.getPriority() > maxPriority) {
                    
        //             maxPriority = pcb.getPriority();
        //             chosen = pcb;

        //         }

        //     }
        // }

            readyQueue.remove(chosen);
            CPU cpu = new CPU(chosen);
            cpu.start();
            try {
                cpu.join();
            } catch (InterruptedException e) {

                e.printStackTrace();
                System.out.println("Interrupt error " + e.getMessage());
            }
            //updates simulated time
            Main.advanceTime(chosen.getRemainingBurstTime());

            chosen.setState("terminated");
            chosen.setRemainingBurstTime(0);
            chosen.addExecutedTime(chosen.getCPUBurstTime());
            log.addPCB(chosen);
            chosen.toString();
        }
    }


