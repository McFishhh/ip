import java.util.ArrayList;
import java.io.IOException;

public class SigmaBot {
    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";

    private ArrayList<Task> todo = new ArrayList<Task>(); 

    public SigmaBot() { 
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
        String msg = ui.nextInput();
        String[] msgSplit = msg.split(" ", 2);
        
        Task task = new TodoTask(msg);
        if (ui.isTodoTask()) {
            // added testcase for todo with empty description 
            if (msgSplit.length == 1) {
                System.out.println(SEP + "Hey! invalid description\n" + SEP);
                task = nextTask(ui);
            } else { 
                task = TodoTask.initFromString(msgSplit[1]);
            }
        } else if (ui.isDeadlineTask()) {
            task = DeadlineTask.initFromString(msgSplit[1]);
        } else if (ui.isEventTask()) {
            task = EventTask.initFromString(msgSplit[1]);
        } 

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
        
        
        Task task = bot.nextTask(ui);
        try {
            while (!ui.isBye()) {
                if (ui.isList()) {
                    bot.printTasks();
                } else if (ui.isMark()) {
                    bot.markTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (ui.isUnmark()) {
                    bot.unmarkTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (ui.isDelete()) {
                    Task deleted = bot.deleteItem(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    System.out.println(SEP + "Noted. I've removed this task:\n" + 
                            deleted + "\nNow you have " + bot.getNumTask() + 
                            " tasks in the list." + "\r\n" + SEP);
                } else {
                    bot.addItem(task);
                    System.out.println(SEP + "Got it. I've added this task:\n" + 
                            task + "\nNow you have " + bot.getNumTask() + 
                            " tasks in the list." + "\r\n" + SEP);
                }

                task = bot.nextTask(ui);
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
