README.md — Structured Reflection Template
--------------------------------------------

The badge below indicates if your submission compiled correctly. It may take some time to update. You can click the badge to see the jobs page and failure traces.

You must ensure your project compiles correctly.

[![Pipeline status](https://gitlab.surrey.ac.uk/csee/com1032/2025-26/com1032lg01277/badges/main/pipeline.svg)](https://gitlab.surrey.ac.uk/csee/com1032/2025-26/com1032lg01277/-/jobs/)

For each stage (0–3), complete the table below.
* 50 to 100 words per stage.
* Bullet points only.

Stage 0 (5%)
--------------
Item	                    Your Answer (Bullet Points Only)
Key Implementation

- I implemented ProcessBuilder and updated the stdout into PCB result in the run() method.
- Added PCB creation and CPU execution (CPU.start()) in runStage0SingleScript method in Main.java.

One Bug Encountered

- The process output was returning null.

How You Diagnosed It

- waitFor() exit code from process returned 9009, meaning command not found.
- The most likely problem was the Python interpreter.

What You Changed

- I installed python in my Computer.



Stage 1 — FCFS (5%)
----------------------
Item	                    Your Answer (Bullet Points Only)
Key Implementation

- Implemented JobQueue readFile() which creates a PCB from a script and adds it to a queue.
- Implemented ProcessCreator run() which removes a pcb from jobQueue, sets state to "ready" and adds it to the readyQueue.
- Implemented Dispatcher run() which constanly checks if a pcb arrived in the readyQueue, and runs the scheduling algorithm (FCFS).
- Implemented the FCFS algorithm which runs the processes in readyQueue in order and then adds them to Eventlog.

One Bug Encountered

- Some processes from test class did not have a priotity.
- this means that the processes would not be added to jobQueue because of their different format.

How You Diagnosed It

- By using the VS Code debugger, I went through line by line until I found the problem.

What You Changed

- I added an if condition in JobQueue.java that can differentiate the different formats.
- A process with no priotity is set to a priority of 0.


Stage 2 — Round Robin (5%)
-----------------------------
Item	                    Your Answer (Bullet Points Only)
Key Implementation

- Implemented the Round Robin algorithm method in Scheduler.java.
- A process can only run for as long as the time quantum is.
- if the process is not finished context switch is incremented and the process is added to the back of readyQueue.
- Next process comes in
- The process repeats

One Bug Encountered

- There would be no finished processes at the end of the EventLog list
- I discovered that if there was one pcb in readyQueue the RR algorithm wouldn't run.
- This is because removing a pcb leaves the readyQueue empty, which exits the algorithm because of the condition if readyQueue.isEmpty() --> return


How You Diagnosed It

- By using the VS Code debugger, I went through line by line until I found the problem.


What You Changed

- I just changed the order of the code.
- This means checking if the queue is empty before removing pcb from readyQueue.

Stage 3 — Priority (5%)
-------------------------------
Item	                    Your Answer (Bullet Points Only)
Key Implementation

- Implemented the Priority algorithm in Scheduler, where the process with the highest priority is executed from readyQueue.
- Re-engineered the Dispatcher/ ProcessCreator logic to fix synchronisation.

One Bug Encountered

- The highest priority process was always executed first, even if a lower priority process would arrive first.

How You Diagnosed It

- Multiple print statements exposed the flaws in my Distpatcher and JobQueue synchronisation.
- Reading the discussion forum in Surreylearn, I found the need to use CURRENT_TIME and arrivalTime to ensure that the processes are executed in correct order/time.


What You Changed

Dispatcher
- I re-made the logic conditions of the dispatcher.
- scheduler.algorithm is called outside of the synchronisation.

Scheduler: nonPreemptivePriority() 
- I put all processes that had an arrivalTime <= CURRENT_TIME in a separate list.
- Then I checked for the highest priority process to be executed.

ProcessCreator
- I Added synchronize and notifyAll() when finish == true to avoid Scheduler thread to starve.
