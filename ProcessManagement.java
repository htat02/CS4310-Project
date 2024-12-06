import java.util.concurrent.*;
import java.util.*;

// Class representing a simulated process
class Process implements Runnable, Comparable<Process> {
    private String name;
    private int priority;
    private Semaphore resource; // For resource allocation
    private static final Random random = new Random();
    private String status;

    public Process(String name, int priority, Semaphore resource, String status) {
        this.name = name;
        this.priority = priority;
        this.resource = resource;
        this.status = status;
    }

    // Process lifecycle simulation
    @Override
    public void run() {
        try {   

            if (status.equals("READY")) {
                System.out.println(name + " is now in " + status + " state.");
                Thread.sleep(random.nextInt(500));
            }
            else {
                // Starting state
                System.out.println(name + " is now in " + status + " state.");
                
                
                // Transition to READY state
                Thread.sleep(random.nextInt(500));
                status = "READY";
                System.out.println(name + " is now in " + status + " state.");
            }

            // Transition to RUNNING state
            resource.acquire(); // Acquire a resource
            status = "RUNNING";
            System.out.println(name + " is now " + status + " state.");
            Thread.sleep(random.nextInt(1000));


            // Transition to WAITING state
            if (random.nextBoolean()) {
                System.out.println(name + " is now WAITING.");
                Thread.sleep(random.nextInt(500));
                status = "WAITING";
                resource.release();
                // Limited by the demonstration
                // System.out.println(name + " is back to RUNNING");
                // Thread.sleep(random.nextInt(1000));
                // status = "RUNNING";
                return;
            }

            // Transition to TERMINATED state
            System.out.println(name + " is now TERMINATED.");
            this.status = "Terminated state";
            resource.release(); // Release the resource
            return;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(name + " was interrupted.");
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void changeStatus(String newStatus) {
        this.status = newStatus;
    }


    public int compareTo(Process other) {
        // Higher priority processes are scheduled first
        return Integer.compare(other.priority, this.priority);
    }
}

// Main class for process management
public class ProcessManagement {
    public static void main(String[] args) {
        // Resource allocation: A semaphore with 2 resources
        Semaphore resource = new Semaphore(2);

        // Priority queue for scheduling (higher priority first)
        PriorityQueue<Process> processQueue = new PriorityQueue<>();

        // Executor service to simulate process execution
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create processes with different priorities
        String initialStatus = "NEW";
        processQueue.add(new Process("Process1", 3, resource, initialStatus));
        processQueue.add(new Process("Process2", 1, resource, initialStatus));
        processQueue.add(new Process("Process3", 7, resource, initialStatus));
        processQueue.add(new Process("Process4", 2, resource, initialStatus));
        processQueue.add(new Process("Process5", 5, resource, initialStatus));

        System.out.println("Starting Process Management Simulation...");
        while (!processQueue.isEmpty()) {
            Process process = processQueue.poll();
            executor.execute(process);
            // Re-adding the process if it was transitioned to the waiting status
                // A way to keep synchronization and efficient use of the "CPU"
            if (process.getStatus().equals("WAITING")) {
                process.changeStatus("READY");
                processQueue.add(process);
            }
        }

        // Shutdown the executor
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Executor was interrupted.");
        }

        System.out.println("All processes completed.");
    }
}