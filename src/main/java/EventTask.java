import java.time.LocalDate;

public class EventTask extends TodoTask {
    protected String start;
    protected String end;
        
    public EventTask(String description, String start, String end) throws SigmaBotException{
        super(description);
        if (start.equals("") || end.equals("")) {
            throw new SigmaBotException("Hey, invalid start and end inputs!");
        }
        this.start = start;
        this.end = end;
    }

    public EventTask(String description, boolean isDone, String start, String end) throws SigmaBotException{
        super(description, isDone);
        if (start.equals("") || end.equals("")) {
            throw new SigmaBotException("Hey, invalid start and end inputs!");
        }
        this.start = start;
        this.end = end;
    }

    public static EventTask initFromString(String string) {
        String[] stringSplit = string.split(" /from ", 2);
        String[] stringSplit2 = stringSplit[1].split(" /to ", 2);
        return new EventTask(stringSplit[0], stringSplit2[0], stringSplit2[1]);
    }

    public String getTaskIcon() {
        return "E";
    }

    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description + "," + this.start + "," + this.end;
    }

    public static EventTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new EventTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]), encodedSplit[3], encodedSplit[4]);
    }

    @Override
    public String toString() {
        return "[E]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
