import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ProcessSJN {
   String processName;
   int burstTime;

   public ProcessSJN(String processName, int burstTime) {
      this.processName = processName;
      this.burstTime = burstTime;
   }
}

class SJNScheduler {
   public void execute(List<ProcessSJN> processList) {
      // Sort processes by burst time
      processList.sort(Comparator.comparingInt(p -> p.burstTime));

      int currentTime = 0;

      System.out.println("Executing Processes in SJN Order:");
      for (ProcessSJN process : processList) {
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
      List<ProcessSJN> processList = new ArrayList<>();
      processList.add(new ProcessSJN("P1", 6));
      processList.add(new ProcessSJN("P2", 2));
      processList.add(new ProcessSJN("P3", 8));
      processList.add(new ProcessSJN("P4", 3));

      SJNScheduler scheduler = new SJNScheduler();
      scheduler.execute(processList);

      System.out.println("All processes executed.");
   }
}
