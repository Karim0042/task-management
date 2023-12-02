package az.iktlab.taskmanagment.enums;

public enum TaskStatus {
    TO_DO,
    IN_PROGRESS,
    IN_REVIEW,
    DONE;
    public static boolean find(String name) {

        for (var task : TaskPriority.values()) {
            if (task.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static TaskStatus getByName(String name){
        for (var task : TaskStatus.values()) {
            if (task.name().equals(name)) {
                return task;
            }
        }
        return  null;
    }
}
