import java.util.ArrayList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class SigmaBot {
    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";

    private ArrayList<Task> todo = new ArrayList<Task>(); 
    // private ArrayList<Task> newTodo = new ArrayList<Task>();
    private int numTask = 0;

    private String savePath = "saves/savedTasks.txt";

    public SigmaBot() { 
    }

    // public ArrayList<Task> addItemFromSave(Task item) {
    //     todo.add(numTask, item);
    //     numTask += 1;

    //     return todo;
    // }

    public ArrayList<Task> addItem(Task item) {
        todo.add(numTask, item);
        // newTodo.add(item);
        numTask += 1;

        return todo;
    }
    
    public Task deleteItem(int i) {
        numTask -= 1;
        
        return todo.remove(i);
    }

    public ArrayList<Task> getTodo() {
        return this.todo;
    } 

    public int getNumTask() {
        return this.numTask;
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
        
        // Task task = new Task(msg);
        Task task = new TodoTask(msg);
        if (ui.isTodoTask()) {
            // added testcase for todo with empty description 
            if (msgSplit.length == 1) {
                System.out.println(SEP + "Hey! invalid description\n" + SEP);
                task = nextTask(ui);
            } else { 
                // task = new TodoTask(msgSplit[1]);
                task = TodoTask.initFromString(msgSplit[1]);
            }
        } else if (ui.isDeadlineTask()) {
            task = DeadlineTask.initFromString(msgSplit[1]);
        } else if (ui.isEventTask()) {
            // String[] msg3 = msgSplit[1].split(" /from ", 2);
            // String[] msg4 = msg3[1].split(" /to ", 2);
            // task = new EventTask(msg3[0], msg4[0], msg4[1]);
            task = EventTask.initFromString(msgSplit[1]);
        } 

        return task;
    }

    public void printTasks() {
        System.out.print(SEP);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < getNumTask(); i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + this.todo.get(i));
        }       
        System.out.println(SEP);
    }

    private boolean loadTasks() throws IOException {
        Path path = Paths.get(savePath);
        
        // check for existence of filepath 
        if (!Files.exists(path)) {
            System.out.println(SEP + "No existing save file :(\n" + SEP);
            return false;
        }

        // read in files one line at a time and creates Tasks to add to todo
        BufferedReader bufferReader = new BufferedReader(new FileReader(savePath));
        String line;
        while ((line = bufferReader.readLine()) != null) { 
            String[] lineSplit = line.split(",");

            if (lineSplit[0].equals("T")) {
                addItem(TodoTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("D")) {
                addItem(DeadlineTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("E")) {
                addItem(EventTask.decodeSaveFormat(line));
            }
        }

        System.out.println(SEP + "Successfully loaded tasks! :D\n" + SEP);
        bufferReader.close();
        return true;
    }

    private boolean saveTasks() throws IOException {
        File file = new File(savePath);

        // create new dir and file if it doesnt exist 
        if (!Files.exists(Paths.get(savePath))) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        // saves file to savePath
        // FileWriter fileWriter = new FileWriter(savePath, true);
        FileWriter fileWriter = new FileWriter(savePath);

        for (Task task : todo) {
            fileWriter.write(task.encodeSaveFormat() + "\n");
        }

        System.out.println(SEP + "Successfully saved tasks!\n" + SEP);
        fileWriter.close();
        return true;
    }
    
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);
        Ui ui = new Ui();
        SigmaBot bot = new SigmaBot();

        try {
            bot.loadTasks();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GREETING + SEP);
        
        
        Task task = bot.nextTask(ui);
        try {
            while (!ui.isBye()) {
                // String firstWord = task.getDescription().split(" ")[0];
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

            bot.saveTasks();
        } catch (SigmaBotException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GOODBYE + SEP);
    }
}
