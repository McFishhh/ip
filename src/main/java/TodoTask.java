public class TodoTask extends Task {
    
    public TodoTask(String description) {
        super(description);
    }

    public TodoTask(String description, boolean isDone) {
        super(description, isDone);
    }

    public static TodoTask initFromString(String string) {
        return new TodoTask(string);
    }

    public String getTaskIcon() {
        return "T";
    }

    public String encodeSaveFormat() {
        return this.getTaskIcon() + "," + this.isDone + "," + this.description;
    }

    public static TodoTask decodeSaveFormat(String encoded) {
        String[] encodedSplit = encoded.split(",");
        return new TodoTask(encodedSplit[2], Boolean.parseBoolean(encodedSplit[1]));
    }

    @Override
    public String toString() {
        return "[T]" + "[" + this.getStatusIcon() + "] " + this.description;
    }
}
