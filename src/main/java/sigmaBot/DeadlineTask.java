package sigmabot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadlineTask extends Task {
    protected LocalDate deadline;

    public static final Pattern DEADLINE_PATTERN = Pattern.compile(
        "^(.+?)\\s*/by\\s*(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\s*$"
    );

    /**
     * Helper method to extract deadline fields using regex.
     *
     * @param input the input string to match
     * @return a String array: [description, deadline], or null if not matched
     */
    private static String[] extractDeadlineFields(String input) {
        Matcher matcher = DEADLINE_PATTERN.matcher(input);
        if (matcher.matches()) {
            String description = matcher.group(1).trim();
            String date = matcher.group(2) + "-" + matcher.group(3) + "-" + matcher.group(4);
            return new String[] { description, date };
        }
        return null;
    }

    /**
     * Checks if the date in the given deadline description string is a valid calendar date.
     * Accounts for months with 30/31 days and February with leap year logic.
     *
     * @param description The deadline description string (e.g. "submit assignment /by 2024-02-29")
     * @return true if the date is valid, false otherwise
     */
    public static boolean isValidDate(String description) {
        String[] fields = extractDeadlineFields(description);
        if (fields == null) {
            return false;
        }
        String[] dateParts = fields[1].split("-");
        if (dateParts.length != 3) {
            return false;
        }
        try {
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            // Leap year check for February
            if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
                return day >= 1 && day <= 29;
            }
            if (month >= 1 && month <= 12) {
                return day >= 1 && day <= daysInMonth[month];
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Constructs a DeadlineTask with the given description and deadline date.
     *
     * @param description the description of the deadline task
     * @param deadline the deadline date
     * @throws SigmaBotException if description is empty or deadline is null
     */
    private DeadlineTask(String description, LocalDate deadline) {
        super(description);
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
        if (fields != null && isValidDate(string)) {
            try {
                LocalDate date = LocalDate.parse(fields[1]);
                return new DeadlineTask(fields[0], date);
            } catch (SigmaBotException e) {
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
        if (fields != null && isValidDate(string)) {
            try {
                LocalDate date = LocalDate.parse(fields[1]);
                return new DeadlineTask(fields[0], isDone, date);
            } catch (SigmaBotException e) {
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
    public static DeadlineTask decodeSaveFormat(String encoded) throws SigmaBotReadSaveException {
        String[] encodedSplit = encoded.split(",");

        if (encodedSplit.length < 4) {
            throw new SigmaBotReadSaveException("Malformed deadline task line: " + encoded);
        } 

        String boolStr = encodedSplit[1].trim();
        if (!"true".equals(boolStr) && !"false".equals(boolStr)) {
            throw new SigmaBotReadSaveException("Invalid boolean value for isDone in DeadlineTask: " + boolStr);
        }

        try {
            return new DeadlineTask(
                encodedSplit[2],
                Boolean.parseBoolean(boolStr),
                java.time.LocalDate.parse(encodedSplit[3])
            );
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | java.time.format.DateTimeParseException e) {
            throw new SigmaBotReadSaveException("Invalid date or format in deadline task: " + encoded);
        }
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
