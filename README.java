public class PartTimeStudent extends Student {
    private static int count = 0; // Static counter for PartTimeStudent instances
    private int minHour;
    private int maxHour;

    public PartTimeStudent() {
        count++; // Increment counter in the constructor
    }

    public PartTimeStudent(String name, String major, String id) {
        super(name, major, id);
        count++;
    }

    public PartTimeStudent(String name, String major, String id, int minHour, int maxHour) {
        super(name, major, id);
        this.minHour = minHour;
        this.maxHour = maxHour;
        count++;
    }

    public void registerHour(int hour) {
        // Implementation for registering hours
    }

    public static int count() {
        return count; // Return the number of PartTimeStudent objects created
    }
}
