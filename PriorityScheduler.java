import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ProcessPriority {
   String processName;
   int burstTime;
   int priority;

   public ProcessPriority(String processName, int burstTime, int priority) {
      this.processName = processName;
      this.burstTime = burstTime;
      this.priority = priority;
   }
}

class PriorityScheduler {
   public void execute(List<ProcessPriority> processList) {
      // Sort processes by priority (higher priority = smaller number)
      processList.sort(Comparator.comparingInt(p -> p.priority));

      int currentTime = 0;

      System.out.println("Executing Processes in Priority Order:");
      for (ProcessPriority process : processList) {
         System.out.println("Process " + process.processName + " (Priority: " + process.priority + ") started at time " + currentTime);

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
      List<ProcessPriority> processList = new ArrayList<>();
      processList.add(new ProcessPriority("P1", 5, 2));
      processList.add(new ProcessPriority("P2", 3, 1));
      processList.add(new ProcessPriority("P3", 8, 3));
      processList.add(new ProcessPriority("P4", 6, 2));

      PriorityScheduler scheduler = new PriorityScheduler();
      scheduler.execute(processList);

      System.out.println("All processes executed.");
   }
}
