package sigmaBot;
public class TodoTask extends Task {
    
    /**
     * Constructs a TodoTask with the given description.
     *
     * @param description the description of the todo task
     */
    public TodoTask(String description) {
        super(description);
    }

    /**
     * Constructs a TodoTask with the given description and completion status.
     *
     * @param description the description of the todo task
     * @param isDone whether the task is marked as done
     */
    public TodoTask(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Parses a string to create a new TodoTask object.
     *
     * @param string the string to parse
     * @return a new TodoTask parsed from the string
     */
    public static TodoTask initFromString(String string) {
        return new TodoTask(string);
    }

    public static TodoTask initFromString(String string, Boolean isDone) {
        return new TodoTask(string, isDone);
    }

    /**
     * Returns the icon representing a todo task.
     *
     * @return the icon representing a todo task
     */
    public String getTaskIcon() {
        return "T";
    }

    /**
     * Returns a string encoding of this todo task for saving to file.
     *
     * @return the encoded string for saving this task
     */
    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description;
    }

    /**
     * Decodes an encoded string to create a new TodoTask object.
     *
     * @param encoded the encoded string to decode
     * @return a new TodoTask decoded from the string
     */
    public static TodoTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new TodoTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]));
    }

    public String getDeleteFormat() {
        return this.isDone + " todo " + this.description;
    }

    /**
     * Returns a string representation of this todo task, including its status and description.
     *
     * @return the string representation of the todo task
     */
    @Override
    public String toString() {
        return "[T]" + "[" + this.getStatusIcon() + "] " + this.description;
    }
}
