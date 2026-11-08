package ProcessScheduler;

import java.util.Queue;

/**
 * Dispatcher is a thread which checks the readyQueue for a process and
 * dispatches one at a time.
 */
public class Dispatcher implements Runnable {
    /**
     * Instance fields for accessing ready queue and scheduler object
     */
    private ProcessControlBlock PCB = null;
    private Queue<ProcessControlBlock> readyQueue = null;
    private Scheduler scheduler = null;
    private ProcessCreator creator = null;
    private boolean dispatched = false; // ensures only one process is dispatched at a time.
    private JobQueue jobQueue = null;

    /**
     * Constructor to assign input parameters to instance fields.
     * 
     * @param readyQueue
     * @param scheduler
     */
    // Dispatcher.java (constructor needs creator + jobQueue)
    public Dispatcher(Queue<ProcessControlBlock> readyQueue, Scheduler scheduler,
            ProcessCreator creator, JobQueue jobQueue) {
        this.readyQueue = readyQueue;
        this.scheduler = scheduler;
        this.creator = creator;
        this.jobQueue = jobQueue;
    }

    /**
     * Continuously check the readyQueue and dispatch one process at a time until
     * all processes have finished.
     * Run in a loop while there is still process in the readyQueue
     * If readyQueue is empty:
     * Check whether ProcessCreator has finished and jobQueue is empty.
     * If both are true → stop the Dispatcher.
     * Otherwise → wait for new processes to arrive (wait() on readyQueue).
     * If readyQueue is not empty:
     * Call scheduler.runAlgorithm() to execute the scheduling algorithm
     * Use synchronisation when accessing readyQueue.
     */
    @Override
    public void run() {
        // TO DO
        System.out.println("I have entered the Dispatcher");

        while (true) {

            synchronized (readyQueue) {

                while (readyQueue.isEmpty() && !creator.isFinished()) {

                    try {
                        System.out.println("Thread has gone to sleep");
                        readyQueue.wait();
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }

                if (readyQueue.isEmpty() && jobQueue.getQueue().isEmpty() && creator.isFinished()) {

                    System.out.println("Processes finished");
                    return;
                }

            }
            scheduler.runAlgorithm();

        }

    }

    public ProcessControlBlock getPCB() {
        return this.PCB;
    }

    public void setDispatched(boolean cpuBusy) {
        this.dispatched = cpuBusy;
    }

    public boolean getDispatched() {
        return this.dispatched;
    }

}
