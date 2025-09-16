package sigmaBot;
public class EventTask extends Task {
    protected String start;
    protected String end;
        
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
     * Parses a string to create a new EventTask object.
     *
     * @param string the string to parse
     * @return a new EventTask parsed from the string
     */
    public static EventTask initFromString(String string) {
        String[] stringSplit = string.split(" /from ", 2);
        String[] stringSplit2 = stringSplit[1].split(" /to ", 2);
        return new EventTask(stringSplit[0], stringSplit2[0], stringSplit2[1]);
    }
    
    /**
     * Parses a string to create a new TodoTask object.
     *
     * @param string the string to parse
     * @param isDone whether the task is marked as done
     * @return a new TodoTask parsed from the string
     */
    public static EventTask initFromString(String string, Boolean isDone) {
        String[] stringSplit = string.split(" /from ", 2);
        String[] stringSplit2 = stringSplit[1].split(" /to ", 2);
        return new EventTask(stringSplit[0], isDone, stringSplit2[0], stringSplit2[1]);
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
     */
    public static EventTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new EventTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]), encodedSplit[3], encodedSplit[4]);
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
        return "[E]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
