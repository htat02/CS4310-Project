import java.util.LinkedList;
import java.util.Queue;

class ProcessFCFS {
   String processName;
   int burstTime;

   public ProcessFCFS(String processName, int burstTime) {
      this.processName = processName;
      this.burstTime = burstTime;
   }
}

class FCFSScheduler {
   public void execute(Queue<ProcessFCFS> processQueue) {
      int currentTime = 0;

      System.out.println("Executing Processes in FCFS Order:");
      while (!processQueue.isEmpty()) {
         ProcessFCFS process = processQueue.poll();
         System.out.println("Process " + process.processName + " started at time " + currentTime);

         try {
               Thread.sleep(process.burstTime * 1000); // Simulating execution
         } catch (InterruptedException e) {
               System.out.println("Process " + process.processName + " interrupted.");
         }

         currentTime += process.burstTime;
         System.out.println("Process " + process.processName + " finished at time " + currentTime);
      }
   }

   public static void main(String[] args) {
      Queue<ProcessFCFS> processQueue = new LinkedList<>();
      processQueue.add(new ProcessFCFS("P1", 3));
      processQueue.add(new ProcessFCFS("P2", 2));
      processQueue.add(new ProcessFCFS("P3", 1));

      FCFSScheduler scheduler = new FCFSScheduler();
      scheduler.execute(processQueue);

      System.out.println("All processes executed.");
   }
}
