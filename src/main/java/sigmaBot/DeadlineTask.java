package sigmabot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadlineTask extends Task {
    protected LocalDate deadline;

    public static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*) /by (.*)$");

    /**
     * Helper method to extract deadline fields using regex.
     *
     * @param input the input string to match
     * @return a String array: [description, deadline], or null if not matched
     */
    private static String[] extractDeadlineFields(String input) {
        Matcher matcher = DEADLINE_PATTERN.matcher(input);
        if (matcher.matches()) {
            return new String[] {
                matcher.group(1).trim(),
                matcher.group(2).trim()
            };
        }
        return null;
    }

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
        if (deadline == null) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    /**
     * Parses a string to create a new DeadlineTask object using regex validation.
     *
     * @param string the string to parse
     * @return a new DeadlineTask parsed from the string, or an empty reply if invalid
     */
    public static DeadlineTask initFromString(String string) {
        String[] fields = extractDeadlineFields(string);
        if (fields != null) {
            try {
                return new DeadlineTask(fields[0], LocalDate.parse(fields[1]));
            } catch (Exception e) {
                DeadlineTask errorTask = new DeadlineTask(fields[0], null);
                errorTask.setPrintMsg(""); // Empty reply
                return errorTask;
            }
        } else {
            DeadlineTask errorTask = new DeadlineTask(string, null);
            errorTask.setPrintMsg(""); // Empty reply
            return errorTask;
        }
    }

    /**
     * Parses a string to create a new DeadlineTask object using regex validation.
     *
     * @param string the string to parse
     * @param isDone whether the task is marked as done
     * @return a new DeadlineTask parsed from the string, or an empty reply if invalid
     */
    public static DeadlineTask initFromString(String string, Boolean isDone) {
        String[] fields = extractDeadlineFields(string);
        if (fields != null) {
            try {
                return new DeadlineTask(fields[0], isDone, LocalDate.parse(fields[1]));
            } catch (Exception e) {
                DeadlineTask errorTask = new DeadlineTask(fields[0], isDone, null);
                errorTask.setPrintMsg(""); // Empty reply
                return errorTask;
            }
        } else {
            DeadlineTask errorTask = new DeadlineTask(string, isDone, null);
            errorTask.setPrintMsg(""); // Empty reply
            return errorTask;
        }
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
