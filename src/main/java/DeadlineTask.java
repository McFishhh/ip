public class DeadlineTask extends TodoTask {
    protected String deadline;

    public DeadlineTask(String description, String deadline) throws SigmaBotException{
        super(description);
        if (deadline.equals("")) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    public DeadlineTask(String description, boolean isDone, String deadline) throws SigmaBotException{
        super(description, isDone);
        if (deadline.equals("")) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    public String getTaskIcon() {
        return "D";
    }

    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description + "," + this.deadline;
    }

    public static DeadlineTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new DeadlineTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]), encodedSplit[3]);
    }

    @Override
    public String toString() {
        return "[D]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (by: " + this.deadline + ")";
    }
}
