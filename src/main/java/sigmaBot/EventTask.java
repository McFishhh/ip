package sigmabot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventTask extends Task {
    protected String start;
    protected String end;

    public static final Pattern EVENT_PATTERN = Pattern.compile(
        "^(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+?)\\s*$"
    );

    /**
     * Helper method to extract event fields using regex.
     *
     * @param input the input string to match
     * @return a String array: [description, start, end], or null if not matched
     */
    private static String[] extractEventFields(String input) {
        Matcher matcher = EVENT_PATTERN.matcher(input);
        if (matcher.matches()) {
            return new String[] {
                matcher.group(1).trim(),
                matcher.group(2).trim(),
                matcher.group(3).trim()
            };
        }
        return null;
    }

    /**
     * Constructs an EventTask with the given description, start time, and end time.
     *
     * @param description the description of the event task
     * @param start the start time of the event
     * @param end the end time of the event
     * @throws SigmaBotException if description, start, or end is empty
     */
    public EventTask(String description, String start, String end) throws SigmaBotException{
        super(description);
        if (start.equals("") || end.equals("")) {
            throw new SigmaBotException("Hey, invalid start and end inputs!");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs an EventTask with the given description, completion status, start time, and end time.
     *
     * @param description the description of the event task
     * @param isDone whether the task is marked as done
     * @param start the start time of the event
     * @param end the end time of the event
     * @throws SigmaBotException if description, start, or end is empty
     */
    public EventTask(String description, boolean isDone, String start, String end) throws SigmaBotException{
        super(description, isDone);
        if (start.equals("") || end.equals("")) {
            throw new SigmaBotException("Hey, invalid start and end inputs!");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Parses a string to create a new EventTask object using regex validation.
     *
     * @param string the string to parse
     * @return a new EventTask parsed from the string, or an empty reply if invalid
     */
    public static EventTask initFromString(String string) {
        String[] fields = extractEventFields(string);
        if (fields != null) {
            try {
                return new EventTask(fields[0], fields[1], fields[2]);
            } catch (SigmaBotException e) {
                EventTask errorTask = new EventTask(fields[0], "", "");
                errorTask.setPrintMsg(""); // Empty reply
                return errorTask;
            }
        } else {
            EventTask errorTask = new EventTask(string, "", "");
            errorTask.setPrintMsg(""); // Empty reply
            return errorTask;
        }
    }

    /**
     * Parses a string to create a new EventTask object using regex validation.
     *
     * @param string the string to parse
     * @param isDone whether the task is marked as done
     * @return a new EventTask parsed from the string, or an empty reply if invalid
     */
    public static EventTask initFromString(String string, Boolean isDone) {
        String[] fields = extractEventFields(string);
        if (fields != null) {
            try {
                return new EventTask(fields[0], isDone, fields[1], fields[2]);
            } catch (SigmaBotException e) {
                EventTask errorTask = new EventTask(fields[0], isDone, "", "");
                errorTask.setPrintMsg(""); // Empty reply
                return errorTask;
            }
        } else {
            EventTask errorTask = new EventTask(string, isDone, "", "");
            errorTask.setPrintMsg(""); // Empty reply
            return errorTask;
        }
    }

    /**
     * Returns the icon representing an event task.
     *
     * @return the icon representing an event task
     */
    public String getTaskIcon() {
        return "E";
    }

    /**
     * Returns a string encoding of this event task for saving to file.
     *
     * @return the encoded string for saving this task
     */
    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description + "," + this.start + "," + this.end;
    }

    /**
     * Decodes an encoded string to create a new EventTask object.
     *
     * @param encoded the encoded string to decode
     * @return a new EventTask decoded from the string
     * @throws SigmaBotReadSaveException if the encoded string is malformed
     */
    public static EventTask decodeSaveFormat(String encoded) throws SigmaBotReadSaveException {
        String[] encodedSplit = encoded.split(",");
        if (encodedSplit.length < 5) {
            throw new SigmaBotReadSaveException("Malformed event task line: " + encoded);
        } 
        
        String boolStr = encodedSplit[1].trim();
        if (!"true".equals(boolStr) && !"false".equals(boolStr)) {
            throw new SigmaBotReadSaveException("Invalid boolean value for isDone in EventTask: " + boolStr);
        }

        return new EventTask(
            encodedSplit[2],
            Boolean.parseBoolean(boolStr),
            encodedSplit[3],
            encodedSplit[4]
        );
    }

    /**
     * Encodes the string as a delete format, to be decoded upon an undo 
     * call, to reverse the action 
     * 
     * @return encoded delete format of the task
     */
    public String getDeleteFormat() {
        return this.isDone + " event " + this.description + " /from " + this.start + " /to " + this.end;
    }

    /**
     * Returns a string representation of this event task, including its status, description, and event period.
     *
     * @return the string representation of the event task
     */
    @Override
    public String toString() {
        return "[E]" + "[" + this.getStatusIcon() + "] " + this.description + 
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
