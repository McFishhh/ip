package sigmabot;

/**
 * A stub implementation of Task used for testing purposes.
 * Provides minimal functionality to simulate task behavior in unit tests.
 */
public class TaskStub extends Task {

    /**
     * Constructs a TaskStub with the given description and sets it as not done.
     *
     * @param string the description of the stub task
     */
    public TaskStub(String string) {
        super(string, false);
    }

    /**
     * Returns the status icon for this stub task.
     * Uses 'S' for done tasks and space for undone tasks.
     *
     * @return "S" if task is done, " " if not done
     */
    public String getStatusIcon() {
        return (isDone ? "S" : " "); // mark done task with S
    }

    /**
     * Returns the description of this stub task.
     *
     * @return the task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Marks this stub task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this stub task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing a stub task type.
     *
     * @return "S" representing a stub task
     */
    @Override
    public String getTaskIcon() {
        return "S";
    }
    
    /**
     * Returns a hardcoded string encoding for testing purposes.
     *
     * @return a fixed encoded string for stub testing
     */
    @Override
    public String encodeSaveFormat() {
        return "S,false,batheHamster";
    }

    /**
     * Returns a formatted string suitable for deletion confirmation messages.
     *
     * @return formatted string for stub task deletion display
     */
    @Override
    public String getDeleteFormat() {
        return "stub batheHamster";
    }

    /**
     * Returns a string representation of this stub task.
     *
     * @return formatted string showing task status and description
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
