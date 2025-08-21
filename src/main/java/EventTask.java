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

    @Override
    public String toString() {
        return "[E]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
