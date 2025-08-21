public class EventTask extends TodoTask {
    protected String start;
    protected String end;
        
    public EventTask(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + "[" + this.getStatusIcon() + "] " + this.description + //
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
