import java.util.LinkedList;
import java.util.Queue;

class ProcessRR {
   String processName;
   int burstTime;
   int remainingTime;

   public ProcessRR(String processName, int burstTime) {
      this.processName = processName;
      this.burstTime = burstTime;
      this.remainingTime = burstTime;
   }
}

class RRScheduler {
   public void execute(Queue<ProcessRR> processQueue, int quantum) {
      int currentTime = 0;

      System.out.println("Executing Processes in Round Robin Order:");
      while (!processQueue.isEmpty()) {
         ProcessRR process = processQueue.poll();

         System.out.println("Process " + process.processName + " started at time " + currentTime);

         if (process.remainingTime > quantum) {
               try {
                  Thread.sleep(quantum * 1000); // Simulating execution for time quantum
               } catch (InterruptedException e) {
                  System.out.println("Process " + process.processName + " interrupted.");
               }

               currentTime += quantum;
               process.remainingTime -= quantum;

               System.out.println("Process " + process.processName + " paused at time " + currentTime + " with " + process.remainingTime + " seconds remaining.");
               processQueue.add(process); // Re-add process to the queue
         } else {
               try {
                  Thread.sleep(process.remainingTime * 1000); // Simulating execution
               } catch (InterruptedException e) {
                  System.out.println("Process " + process.processName + " interrupted.");
               }

               currentTime += process.remainingTime;
               process.remainingTime = 0;
               System.out.println("Process " + process.processName + " finished at time " + currentTime);
         }
      }
   }

   public static void main(String[] args) {
      Queue<ProcessRR> processQueue = new LinkedList<>();
      processQueue.add(new ProcessRR("P1", 5));
      processQueue.add(new ProcessRR("P2", 8));
      processQueue.add(new ProcessRR("P3", 6));

      int quantum = 3; // Time quantum
      RRScheduler scheduler = new RRScheduler();
      scheduler.execute(processQueue, quantum);

      System.out.println("All processes executed.");
   }
}
