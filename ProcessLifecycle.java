class ProcessLifecycle extends Thread {
   private String processName;
   private int burstTime;

   public ProcessLifecycle(String name, int burstTime) {
      this.processName = name;
      this.burstTime = burstTime;
   }

   @Override
   public void run() {
      try {
         System.out.println(processName + " is RUNNING.");
         Thread.sleep(burstTime * 1000); // Simulating execution time
         System.out.println(processName + " has TERMINATED.");
      } catch (InterruptedException e) {
         System.out.println(processName + " was INTERRUPTED.");
      }
   }

   public static void main(String[] args) {
      System.out.println("Simulation of Process Life Cycle");

      ProcessLifecycle p1 = new ProcessLifecycle("Process-1", 3);
      ProcessLifecycle p2 = new ProcessLifecycle("Process-2", 5);

      System.out.println("Starting " + p1.processName);
      p1.start();

      System.out.println("Starting " + p2.processName);
      p2.start();

      try {
         p1.join();
         p2.join();
      } catch (InterruptedException e) {
         System.out.println("Main thread interrupted.");
      }

      System.out.println("All processes have terminated.");
   }
}