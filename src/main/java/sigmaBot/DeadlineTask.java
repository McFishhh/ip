package sigmabot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeadlineTask extends Task {
    protected LocalDate deadline;

    /**
     * Constructs a DeadlineTask with the given description and deadline date.
     *
     * @param description the description of the deadline task
     * @param deadline the deadline date
     * @throws SigmaBotException if description is empty or deadline is null
     */
    public DeadlineTask(String description, LocalDate deadline) throws SigmaBotException{
        super(description);
        if (deadline == null) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    /**
     * Constructs a DeadlineTask with the given description, completion status, and deadline date.
     *
     * @param description the description of the deadline task
     * @param isDone whether the task is marked as done
     * @param deadline the deadline date
     * @throws SigmaBotException if description is empty or deadline is null
     */
    public DeadlineTask(String description, boolean isDone, LocalDate deadline) throws SigmaBotException{
        super(description, isDone);
        if (deadline.equals("")) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    /**
     * Parses a string to create a new DeadlineTask object.
     *
     * @param string the string to parse
     * @return a new DeadlineTask parsed from the string
     */
    public static DeadlineTask initFromString(String string) {
        String[] stringSplit = string.split(" /by ", 2);
        return new DeadlineTask(stringSplit[0], LocalDate.parse(stringSplit[1]));
    }

    /**
     * Parses a string to create a new TodoTask object.
     *
     * @param string the string to parse
     * @param isDone whether the task is marked as done
     * @return a new TodoTask parsed from the string
     */
    public static DeadlineTask initFromString(String string, Boolean isDone) {
        String[] stringSplit = string.split(" /by ", 2);
        return new DeadlineTask(stringSplit[0], isDone, LocalDate.parse(stringSplit[1]));
    }


    /**
     * Returns the icon representing a deadline task.
     *
     * @return the icon representing a deadline task
     */
    public String getTaskIcon() {
        return "D";
    }

    /**
     * Returns a string encoding of this deadline task for saving to file.
     *
     * @return the encoded string for saving this task
     */
    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description + "," + this.deadline;
    }

    /**
     * Decodes an encoded string to create a new DeadlineTask object.
     *
     * @param encoded the encoded string to decode
     * @return a new DeadlineTask decoded from the string
     */
    public static DeadlineTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new DeadlineTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]), LocalDate.parse(encodedSplit[3]));
    }

    /**
     * Encodes the string as a delete format, to be decoded upon an undo 
     * call, to reverse the action 
     * 
     * @return encoded delete format of the task
     */
    public String getDeleteFormat() {
        return this.isDone +  " deadline " + this.description + " /by " + this.deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Returns a string representation of this deadline task, including its status, description, and deadline date.
     *
     * @return the string representation of the deadline task
     */
    @Override
    public String toString() {
        return "[D]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (by: " + this.deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }
}
