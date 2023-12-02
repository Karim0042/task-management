package az.iktlab.taskmanagment.enums;

public enum TaskPriority {
    NOT_URGENT,
    MEDIUM,
    NORMAL,
    MAJOR,
    HIGH,
    URGENT,
    BLOCKER;

    public static boolean find(String name) {

        for (var task : TaskPriority.values()) {
            if (task.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static TaskPriority getByName(String name) {
        for (var task : TaskPriority.values()) {
            if (task.name().equals(name)) {
                return task;
            }
        }
        return null;
    }
}
