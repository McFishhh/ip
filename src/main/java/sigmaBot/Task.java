package sigmabot;

/**
 * Represents an abstract Task with a description, completion status, and print message.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected String printMsg;

    /**
     * Constructs a new Task with the given description and sets it as not done by default.
     *
     * @param description The description of the task.
     * @throws SigmaBotException If description is empty.
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
     * @param description The description of the task.
     * @param isDone Whether the task is marked as done.
     * @throws SigmaBotException If description is empty.
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
     * @return "X" if the task is done, otherwise a space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the description of this task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the print message associated with this task.
     *
     * @return The print message for this task.
     */
    public String getPrintMsg() {
        return this.printMsg;
    }

    /**
     * Sets the print message for this task.
     *
     * @param printMsg The print message to set for this task.
     */
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
     * Returns whether this task is marked as done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean getisDone() {
        return this.isDone;
    }

    /**
     * Returns the icon representing the type of this task (e.g., Todo, Deadline, Event).
     *
     * @return The icon representing the task type.
     */
    abstract public String getTaskIcon();
    
    /**
     * Returns a string encoding of this task for saving to file.
     *
     * @return The encoded string for saving this task.
     */
    abstract public String encodeSaveFormat();

    /**
     * Returns a formatted string suitable for deletion confirmation messages.
     *
     * @return Formatted string containing task details for deletion display.
     */
    abstract public String getDeleteFormat();

    /**
     * Returns a string representation of this task, including its status and description.
     *
     * @return The string representation of the task.
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
