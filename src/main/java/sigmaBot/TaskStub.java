package sigmaBot;
public class TaskStub extends Task {
    // protected String description;
    // protected boolean isDone;

    // public TaskStub(String description) throws SigmaBotExceptionTest {
    //     if (description.equals("")) {
    //         throw new SigmaBotExceptionTest("Hey, theres no description!");
    //     }
    //     this.description = description;
    //     this.isDone = false;
    // }

    // public TaskStub(String description, boolean isDone) throws SigmaBotExceptionTest {
    //     if (description.equals("")) {
    //         throw new SigmaBotExceptionTest("Hey, theres no description!");
    //     }
    //     this.description = description;
    //     this.isDone = isDone;
    // }

    public TaskStub(String string) {
        super(string, false);
    }

    public String getStatusIcon() {
        return (isDone ? "S" : " "); // mark done task with X
    }

    public String getDescription() {
        return this.description;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String getTaskIcon() {
        return "S";
    }
    
    @Override
    public String encodeSaveFormat() {
        return "S,false,batheHamster";
    }

    @Override
    public String getDeleteFormat() {
        return "stub batheHamster";
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
