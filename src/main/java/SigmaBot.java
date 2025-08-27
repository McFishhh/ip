import java.util.Scanner;
import java.util.ArrayList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SigmaBot {
    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";

    private ArrayList<Task> todo = new ArrayList<Task>(); 
    private ArrayList<Task> newTodo = new ArrayList<Task>();
    private int numTask = 0;

    private String savePath = "saves/savedTasks.txt";

    public SigmaBot() { 
    }

    public ArrayList<Task> addItemFromSave(Task item) {
        todo.add(numTask, item);
        numTask += 1;

        return todo;
    }

    public ArrayList<Task> addItem(Task item) {
        todo.add(numTask, item);
        newTodo.add(item);
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

    public Task nextTask(Scanner scanner) throws SigmaBotException{
        String msg = scanner.nextLine().toLowerCase();
        String[] msgSplit = msg.split(" ", 2);
        
        // Task task = new Task(msg);
        Task task = new TodoTask(msg);
        if (msgSplit[0].equals("todo")) {
            // added testcase for todo with empty description 
            if (msgSplit.length == 1) {
                System.out.println(SEP + "Hey! invalid description\n" + SEP);
                task = nextTask(scanner);
            } else { 
                task = new TodoTask(msgSplit[1]);
            }
        } else if (msgSplit[0].equals("deadline")) {
            String[] msg3 = msgSplit[1].split(" /by ", 2);
            task = new DeadlineTask(msg3[0], LocalDate.parse(msg3[1])); // deadline must be in LocalDate format 
        } else if (msgSplit[0].equals("event")) {
            String[] msg3 = msgSplit[1].split(" /from ", 2);
            String[] msg4 = msg3[1].split(" /to ", 2);
            task = new EventTask(msg3[0], msg4[0], msg4[1]);
        } else if (!msg.equals("list") && !msg.equals("bye") &&
                    !msgSplit[0].equals("mark") && !msgSplit[0].equals("unmark") && !msgSplit[0].equals("delete")) {
            // throw new SigmaBotException("Hey, that is not a valid command!"); 
            System.out.println(SEP + "Hey! that doesnt make any sense!\n" + SEP);
            task = nextTask(scanner);
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
                addItemFromSave(TodoTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("D")) {
                addItemFromSave(DeadlineTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("E")) {
                addItemFromSave(EventTask.decodeSaveFormat(line));
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
        FileWriter fileWriter = new FileWriter(savePath, true);

        for (Task task : newTodo) {
            fileWriter.write(task.encodeSaveFormat() + "\n");
        }

        System.out.println(SEP + "Successfully saved tasks!\n" + SEP);
        fileWriter.close();
        return true;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SigmaBot bot = new SigmaBot();

        try {
            bot.loadTasks();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GREETING + SEP);
        
        
        Task task = bot.nextTask(scanner);
        try {
            while (!task.getDescription().equals("bye")) {
                String firstWord = task.getDescription().split(" ")[0];

                if (task.getDescription().equals("list")) {
                    bot.printTasks();
                } else if (firstWord.equals("mark")) {
                    bot.markTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (firstWord.equals("unmark")) {
                    bot.unmarkTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (firstWord.equals("delete")) {
                    Task deleted = bot.deleteItem(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    System.out.println(SEP + "Noted. I've removed this task:\n" + 
                            deleted + "\nNow you have " + bot.getNumTask() + " tasks in the list." + "\r\n" + SEP);
                } else {
                    bot.addItem(task);
                    System.out.println(SEP + "Got it. I've added this task:\n" + 
                            task + "\nNow you have " + bot.getNumTask() + " tasks in the list." + "\r\n" + SEP);
                }

                task = bot.nextTask(scanner);
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
