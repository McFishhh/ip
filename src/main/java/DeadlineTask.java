import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeadlineTask extends TodoTask {
    protected LocalDate deadline;

    public DeadlineTask(String description, LocalDate deadline) throws SigmaBotException{
        super(description);
        if (deadline == null) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    public DeadlineTask(String description, boolean isDone, LocalDate deadline) throws SigmaBotException{
        super(description, isDone);
        if (deadline.equals("")) {
            throw new SigmaBotException("Hey, theres no deadline!");
        }
        this.deadline = deadline;
    }

    public static DeadlineTask initFromString(String string) {
        String[] stringSplit = string.split(" /by ", 2);
        return new DeadlineTask(stringSplit[0], LocalDate.parse(stringSplit[1]));
    }

    public String getTaskIcon() {
        return "D";
    }

    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description + "," + this.deadline;
    }

    public static DeadlineTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new DeadlineTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]), LocalDate.parse(encodedSplit[3]));
    }

    @Override
    public String toString() {

        return "[D]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (by: " + this.deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }
}
