import java.util.ArrayList;
import java.io.IOException;

public class SigmaBot {
    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";

    private ArrayList<Task> todo = new ArrayList<Task>(); 
    private Parser parser;

    public SigmaBot() { 
        parser = new Parser();
    }

    public ArrayList<Task> addItem(Task item) {
        todo.add(item);

        return todo;
    }
    
    public Task deleteItem(int i) {
        return todo.remove(i);
    }

    public ArrayList<Task> getTodo() {
        return this.todo;
    } 

    public void setTodo(ArrayList<Task> todoToSet) {
        this.todo = todoToSet;
    } 

    public int getNumTask() {
        return todo.size();
    } 

    public void markTask(int i) {
        this.todo.get(i).mark();
    }

    public void unmarkTask(int i) {
        this.todo.get(i).unmark();
    }

    public Task nextTask(Ui ui) throws SigmaBotException{
        Task task = this.parser.parseInput(ui, this);

        return task;
    }

    public void printTasks() {
        System.out.print(SEP);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < this.todo.size() ; i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + this.todo.get(i));
        }       
        System.out.println(SEP);
    }

    private void loadTasks(Storage storage) throws IOException {
        setTodo(storage.loadTasks());
    }

    private boolean saveTasks(Storage storage) throws IOException {
        return storage.saveTasks(this.todo);
    }
    
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        SigmaBot bot = new SigmaBot();

        try {
            bot.loadTasks(storage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GREETING + SEP);
        
        
        bot.nextTask(ui);
        try {
            while (!bot.parser.isBye()) {
                bot.nextTask(ui);
            } 

            bot.saveTasks(storage);
        } catch (SigmaBotException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GOODBYE + SEP);
    }
}
