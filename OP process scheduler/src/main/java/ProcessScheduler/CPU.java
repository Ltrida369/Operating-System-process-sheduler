package ProcessScheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * CPU Thread. Runs a python script from a given PCB.
 *
 */
public class CPU extends Thread {

    /**
     * Instance fields used in constructor for parsing in other objects
     */
    private boolean running = true; // if CPU is actively executing a process
    private ProcessControlBlock PCB = null;

    public CPU(ProcessControlBlock PCB) {
        this.PCB = PCB;
    }

    /**
     * TO DO for Stage 0
     */
    @Override
    public void run() {

        // Print that the process is running
        System.out.println(PCB.getPID() + ": " + "Running ");

        // Build the process to run the Python script

        ProcessBuilder pb = new ProcessBuilder(Main.pythonPath, PCB.getProcessPath());

        Process process = null;

        String processOutput = "";
        try {
            process = pb.start();
            // Wait for the script to finish execution
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Process " + PCB.getPID() + " completed successfully");
            } else {
                System.out.println("Process" + PCB.getPID() + " failed with exit code: " + exitCode);
            }

            // Read the script output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line).append("\n");

            }

            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            processOutput = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to Start process");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("The current process was interrupted");
        }

        // Update the PCB with the script's output
        PCB.setState("running");
        PCB.setPCBResult(processOutput);

        // Print the output of the process
        System.out.println(PCB.getPID() + ": Output: " + processOutput);

        // Mark the CPU as finished
        running = false;

    }

    public ProcessControlBlock getPCB() {
        return this.PCB;
    }

    public boolean getRunning() {
        return this.running;
    }

}
