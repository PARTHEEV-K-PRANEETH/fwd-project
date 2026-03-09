import java.util.*;

// ==========================================
// DATA STRUCTURES & ALGORITHMS (CO1, CO2, CO3, CO4)
// ==========================================

// CO2: Abstract Data Type Design
abstract class Habit {
    String name;
    
    public Habit(String name) {
        this.name = name;
    }

    abstract void trackProgress(int day, double value);
    abstract double getCompletionPercentage();
    abstract void printMonthlyProgress();
}

// Subclass for Yes/No Habits (e.g., Did you workout? 1 for Yes, 0 for No)
class BinaryHabit extends Habit {
    // CO2: Arrays used to store 30-day linear sequence
    boolean[] days = new boolean[30]; 

    public BinaryHabit(String name) {
        super(name);
    }

    @Override
    void trackProgress(int day, double value) {
        if (day >= 1 && day <= 30) {
            days[day - 1] = (value >= 1.0); 
        }
    }

    @Override
    double getCompletionPercentage() {
        int count = 0;
        for (boolean d : days) {
            if (d) count++;
        }
        return (count / 30.0) * 100.0;
    }

    @Override
    void printMonthlyProgress() {
        System.out.printf("%-15s |", name);
        for (boolean d : days) {
            System.out.print(d ? "X" : "-");
        }
        System.out.printf("| %.1f%%\n", getCompletionPercentage());
    }
}

// Subclass for Target-based Habits (e.g., Drink 3.0 Liters of water)
class NumericHabit extends Habit {
    // CO2: Arrays used to store 30-day linear sequence
    double[] days = new double[30]; 
    double dailyTarget;

    public NumericHabit(String name, double dailyTarget) {
        super(name);
        this.dailyTarget = dailyTarget;
    }

    @Override
    void trackProgress(int day, double value) {
        if (day >= 1 && day <= 30) {
            days[day - 1] = value;
        }
    }

    @Override
    double getCompletionPercentage() {
        int count = 0;
        for (double v : days) {
            if (v >= dailyTarget) count++;
        }
        return (count / 30.0) * 100.0;
    }

    @Override
    void printMonthlyProgress() {
        System.out.printf("%-15s |", name);
        for (double v : days) {
            System.out.print(v >= dailyTarget ? "X" : "-");
        }
        System.out.printf("| %.1f%%\n", getCompletionPercentage());
    }
}

// ==========================================
// MAIN ENGINE & EXECUTION
// ==========================================

public class Main {
    
    // CO4: Hash-based data structure (HashMap) for O(1) fast lookup
    static Map<String, Habit> habitsMap = new HashMap<>();
    
    // CO3: Stack to model workflow (maintaining an update history)
    static Stack<String> updateHistory = new Stack<>();

    // CO1: Merge Sort Algorithm to analyze and rank habits by success rate
    static void mergeSort(List<Habit> list) {
        if (list.size() <= 1) return;
        
        int mid = list.size() / 2;
        List<Habit> left = new ArrayList<>(list.subList(0, mid));
        List<Habit> right = new ArrayList<>(list.subList(mid, list.size()));
        
        mergeSort(left);
        mergeSort(right);
        merge(list, left, right);
    }

    static void merge(List<Habit> list, List<Habit> left, List<Habit> right) {
        int i = 0, j = 0, k = 0;
        
        // Sorting in descending order (highest % first)
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getCompletionPercentage() >= right.get(j).getCompletionPercentage()) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }

    // THE MAIN ENTRY POINT - Ensures no compiler errors
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Pre-loading some examples so the tracker isn't empty on startup
        habitsMap.put("reading", new BinaryHabit("Reading"));
        habitsMap.put("water", new NumericHabit("Water", 3.0));

        while (true) {
            System.out.println("\n========= HABIT TRACKER =========");
            System.out.println("1. Create Binary Habit (X/0)");
            System.out.println("2. Create Numeric Habit (Target)");
            System.out.println("3. Track Progress for a Day");
            System.out.println("4. View Monthly Graphical Progress");
            System.out.println("5. Analyze & Rank Habits (Merge Sort)");
            System.out.println("6. View Update History");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            // Input validation
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); 
                continue;
            }
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter habit name: ");
                    String bName = sc.nextLine();
                    habitsMap.put(bName.toLowerCase(), new BinaryHabit(bName));
                    System.out.println("Binary Habit Created!");
                    break;
                    
                case 2:
                    System.out.print("Enter habit name: ");
                    String nName = sc.nextLine();
                    System.out.print("Enter daily target number (e.g., 8.0 for sleep): ");
                    double target = sc.nextDouble();
                    habitsMap.put(nName.toLowerCase(), new NumericHabit(nName, target));
                    System.out.println("Numeric Habit Created!");
                    break;

                case 3:
                    System.out.print("Enter the name of the habit to update: ");
                    String hName = sc.nextLine().toLowerCase();
                    
                    // CO4: O(1) Search using HashMap
                    if (!habitsMap.containsKey(hName)) {
                        System.out.println("Error: Habit not found.");
                        break;
                    }
                    
                    System.out.print("Enter day number (1-30): ");
                    int day = sc.nextInt();
                    System.out.print("Enter value (1 for Done / 0 for Missed, OR numeric value): ");
                    double val = sc.nextDouble();
                    
                    habitsMap.get(hName).trackProgress(day, val);
                    updateHistory.push("Updated '" + hName + "' on Day " + day); // CO3: Stack
                    System.out.println("Progress Saved!");
                    break;

                case 4:
                    System.out.println("\n--- Monthly Graphical Progress ---");
                    System.out.println("Legend: X = Goal Met, - = Missed");
                    for (Habit h : habitsMap.values()) {
                        h.printMonthlyProgress();
                    }
                    break;

                case 5:
                    System.out.println("\n--- Habit Analysis (Ranked by Success Rate) ---");
                    List<Habit> listToRank = new ArrayList<>(habitsMap.values());
                    
                    // CO1: Executing Merge Sort
                    mergeSort(listToRank);
                    
                    int rank = 1;
                    for (Habit h : listToRank) {
                        System.out.printf("%d. %s: %.1f%% completed\n", rank++, h.name, h.getCompletionPercentage());
                    }
                    break;

                case 6:
                    System.out.println("\n--- Update History (Most Recent First) ---");
                    if (updateHistory.isEmpty()) {
                        System.out.println("No updates yet.");
                    } else {
                        // Iterating backwards to show LIFO nature of the Stack
                        for (int i = updateHistory.size() - 1; i >= 0; i--) {
                            System.out.println("- " + updateHistory.get(i));
                        }
                    }
                    break;

                case 7:
                    System.out.println("Exiting Habit Tracker. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please pick 1-7.");
            }
        }
    }
}