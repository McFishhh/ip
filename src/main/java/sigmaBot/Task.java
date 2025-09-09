package sigmaBot;
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected String printMsg;

    /**
     * Constructs a new Task with the given description and sets it as not done by default.
     *
     * @param description the description of the task
     * @throws SigmaBotException if description is empty
     */
    public Task(String description) throws SigmaBotException {
        if (description.equals("")) {
            throw new SigmaBotException("Hey, theres no description!");
        }
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs a new Task with the given description and completion status.
     *
     * @param description the description of the task
     * @param isDone whether the task is marked as done
     * @throws SigmaBotException if description is empty
     */
    public Task(String description, boolean isDone) throws SigmaBotException {
        if (description.equals("")) {
            throw new SigmaBotException("Hey, theres no description!");
        }
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the status icon representing whether the task is done or not.
     *
     * @return the status icon ("X" if done, otherwise a space)
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the description of this task.
     *
     * @return the description of the task
     */
    public String getDescription() {
        return this.description;
    }

    public String getPrintMsg() {
        return this.printMsg;
    }

    public void setPrintMsg(String printMsg) {
        this.printMsg = printMsg;
    }

    /**
     * Marks this task as done by setting its status to true.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done by setting its status to false.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing the type of this task (e.g., Todo, Deadline, Event).
     *
     * @return the icon representing the task type
     */
    abstract public String getTaskIcon();
    
    /**
     * Returns a string encoding of this task for saving to file.
     *
     * @return the encoded string for saving this task
     */
    abstract public String encodeSaveFormat();

    // abstract public Task decodeSaveFormat(String encoded);

    abstract public String getDeleteFormat();

    /**
     * Returns a string representation of this task, including its status and description.
     *
     * @return the string representation of the task
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
