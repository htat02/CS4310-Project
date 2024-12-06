import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.*;

public class MainGUI {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Process Scheduler and Lifecycle Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton fcfsButton = new JButton("Run FCFS Scheduler");
        JButton sjnButton = new JButton("Run SJN Scheduler");
        JButton priorityButton = new JButton("Run Priority Scheduler");
        JButton rrButton = new JButton("Run Round Robin Scheduler");
        JButton lifecycleButton = new JButton("Run Process Lifecycle");
        JButton processManagementButton = new JButton("Process Management");

        // Add action listeners for each button
        fcfsButton.addActionListener(e -> openExecutionWindow(frame, "FCFS Scheduler", () -> {
            Queue<ProcessFCFS> processQueue = new LinkedList<>();
            processQueue.add(new ProcessFCFS("P1", 3));
            processQueue.add(new ProcessFCFS("P2", 2));
            processQueue.add(new ProcessFCFS("P3", 1));

            // Show the process input details
            System.out.println("Input Processes for FCFS:");
            processQueue.forEach(p -> System.out.println("Process Name: " + p.processName + ", Burst Time: " + p.burstTime));

            FCFSScheduler fcfs = new FCFSScheduler();
            fcfs.execute(processQueue);
        }));

        sjnButton.addActionListener(e -> openExecutionWindow(frame, "SJN Scheduler", () -> {
            List<ProcessSJN> processList = new LinkedList<>();
            processList.add(new ProcessSJN("P1", 6));
            processList.add(new ProcessSJN("P2", 2));
            processList.add(new ProcessSJN("P3", 8));

            // Show the process input details
            System.out.println("Input Processes for SJN:");
            processList.forEach(p -> System.out.println("Process Name: " + p.processName + ", Burst Time: " + p.burstTime));

            SJNScheduler sjn = new SJNScheduler();
            sjn.execute(processList);
        }));

        priorityButton.addActionListener(e -> openExecutionWindow(frame, "Priority Scheduler", () -> {
            List<ProcessPriority> processList = new LinkedList<>();
            processList.add(new ProcessPriority("P1", 5, 2));
            processList.add(new ProcessPriority("P2", 3, 1));
            processList.add(new ProcessPriority("P3", 8, 3));

            // Show the process input details
            System.out.println("Input Processes for Priority Scheduler:");
            processList.forEach(p -> System.out.println("Process Name: " + p.processName + ", Burst Time: " + p.burstTime + ", Priority: " + p.priority));

            PriorityScheduler priority = new PriorityScheduler();
            priority.execute(processList);
        }));

        rrButton.addActionListener(e -> openExecutionWindow(frame, "Round Robin Scheduler", () -> {
            Queue<ProcessRR> processQueue = new LinkedList<>();
            processQueue.add(new ProcessRR("P1", 5));
            processQueue.add(new ProcessRR("P2", 8));
            processQueue.add(new ProcessRR("P3", 6));
            int quantum = 3;

            // Show the process input details
            System.out.println("Input Processes for Round Robin:");
            processQueue.forEach(p -> System.out.println("Process Name: " + p.processName + ", Burst Time: " + p.burstTime));
            System.out.println("Time Quantum: " + quantum);

            RRScheduler rr = new RRScheduler();
            rr.execute(processQueue, quantum);
        }));

        lifecycleButton.addActionListener(e -> openExecutionWindow(frame, "Process Lifecycle", () -> {
            ProcessLifecycle p1 = new ProcessLifecycle("Process-1", 3);
            ProcessLifecycle p2 = new ProcessLifecycle("Process-2", 5);

            // Show the process input details
            System.out.println("Input Processes for Lifecycle:");
            System.out.println("Process Name: " + p1.getName() + ", Burst Time: " + 3);
            System.out.println("Process Name: " + p2.getName() + ", Burst Time: " + 5);

            p1.start();
            p2.start();

            try {
                p1.join();
                p2.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));

        processManagementButton.addActionListener(e -> openExecutionWindow(frame, "Process Management", () -> {
            try {
                // Call the main method of ProcessManagement
                ProcessManagement.main(new String[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }));

        panel.add(fcfsButton);
        panel.add(sjnButton);
        panel.add(priorityButton);
        panel.add(rrButton);
        panel.add(lifecycleButton);
        panel.add(processManagementButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void openExecutionWindow(JFrame parentFrame, String title, Runnable executionLogic) {
        // Hide the main frame
        parentFrame.setVisible(false);

        // Create a new frame for the execution window
        JFrame executionFrame = new JFrame(title);
        executionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        executionFrame.setSize(600, 400);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            executionFrame.dispose();
            parentFrame.setVisible(true);
        });

        // Redirect System.out to JTextArea
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                outputArea.append(String.valueOf((char) b));
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }
        });

        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // Execute the provided logic in a separate thread
        new Thread(() -> {
            try {
                outputArea.append("Starting " + title + "...\n");
                executionLogic.run();
                outputArea.append("\n" + title + " execution complete.\n");
            } catch (Exception ex) {
                outputArea.append("\nError during execution: " + ex.getMessage() + "\n");
            } finally {
                System.setOut(originalOut);
            }
        }).start();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(backButton, BorderLayout.WEST);

        executionFrame.setLayout(new BorderLayout());
        executionFrame.add(scrollPane, BorderLayout.CENTER);
        executionFrame.add(bottomPanel, BorderLayout.SOUTH);

        executionFrame.setVisible(true);
    }
}
