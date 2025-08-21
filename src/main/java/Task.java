public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) throws SigmaBotException {
        if (description.equals("")) {
            throw new SigmaBotException("Hey, theres no description!");
        }
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return this.description;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
