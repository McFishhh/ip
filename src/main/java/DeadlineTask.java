public class DeadlineTask extends TodoTask {
    protected String deadline;

    public DeadlineTask(String description, String deadline) throws SigmaBotException{
        super(description);
        if (deadline.equals("")) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (by: " + this.deadline + ")";
    }
}
