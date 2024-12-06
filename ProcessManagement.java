import java.util.concurrent.*;
import java.util.*;

// Class representing a simulated process
class Process implements Runnable, Comparable<Process> {
    private String name;
    private int priority;
    private Semaphore resource; // For resource allocation
    private static final Random random = new Random();

    public Process(String name, int priority, Semaphore resource) {
        this.name = name;
        this.priority = priority;
        this.resource = resource;
    }

    // Process lifecycle simulation
    @Override
    public void run() {
        try {
            System.out.println(name + " is in NEW state.");

            // Transition to READY state
            Thread.sleep(random.nextInt(500));
            System.out.println(name + " is now READY.");

            // Transition to RUNNING state
            resource.acquire(); // Acquire a resource
            System.out.println(name + " is now RUNNING and using a resource.");
            Thread.sleep(random.nextInt(1000));

            // Transition to WAITING state
            if (random.nextBoolean()) {
                System.out.println(name + " is now WAITING.");
                Thread.sleep(random.nextInt(500));
                System.out.println(name + " is back to RUNNING.");
            }

            // Transition to TERMINATED state
            System.out.println(name + " is now TERMINATED.");
            resource.release(); // Release the resource
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(name + " was interrupted.");
        }
    }

    public int getPriority() {
        return priority;
    }

    @Override
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

        // Create processes with different priorities
        processQueue.add(new Process("Process1", 3, resource));
        processQueue.add(new Process("Process2", 1, resource));
        processQueue.add(new Process("Process3", 5, resource));
        processQueue.add(new Process("Process4", 2, resource));

        // Executor service to simulate process execution
        ExecutorService executor = Executors.newFixedThreadPool(2);

        System.out.println("Starting Process Management Simulation...");
        while (!processQueue.isEmpty()) {
            Process process = processQueue.poll(); // Get the highest-priority process
            executor.execute(process); // Execute the process
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
