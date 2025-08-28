package sigmaBot;
import java.util.ArrayList;
import java.io.IOException;

public class SigmaBot {
    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";

    private TaskList taskList; 
    private Parser parser;
    private Storage storage;
    private Ui ui;

    public SigmaBot() { 
        this.parser = new Parser();
        this.taskList = new TaskList();
        this.storage = new Storage();
        this.ui = new Ui();
 
        try {
            this.loadTasks(this.storage); // load tasks
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Task> addItem(Task task) {
        return this.taskList.addTask(task);
    }
    
    public Task deleteItem(int i) {
        return this.taskList.deleteTask(i);
    }

    public TaskList getTodo() {
        return this.taskList;
    } 

    public void setTodo(TaskList todoToSet) {
        this.taskList = todoToSet;
    } 

    public int getNumTask() {
        return taskList.size();
    } 

    public void markTask(int i) {
        this.taskList.get(i).mark();
    }

    public void unmarkTask(int i) {
        this.taskList.get(i).unmark();
    }

    public Task nextTask(Ui ui) throws SigmaBotException{
        Task task = this.parser.parseInput(ui, this);

        return task;
    }

    public void printTasks() {
        System.out.print(SEP);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < this.taskList.size() ; i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + this.taskList.get(i));
        }       
        System.out.println(SEP);
    }

    public void printMatchingTasks(ArrayList<Task> matchingList) {
        System.out.print(SEP);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingList.size() ; i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + matchingList.get(i));
        }       
        System.out.println(SEP);
    }

    private void loadTasks(Storage storage) throws IOException {
        taskList.setTaskList(storage.loadTasks());
    }

    private boolean saveTasks(Storage storage) throws IOException {
        return storage.saveTasks(this.taskList);
    }

    public ArrayList<Task> findTasks(String keyword) {
        return storage.findTasks(this.taskList, keyword);
    }
    
    public void run() {
        SigmaBot bot = this;

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

    public static void main(String[] args) {
        new SigmaBot().run();
    }
}
