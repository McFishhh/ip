public class DeadlineTask extends TodoTask {
    protected String deadline;

    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (by: " + this.deadline + ")";
    }
}
