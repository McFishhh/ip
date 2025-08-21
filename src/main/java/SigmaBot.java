import java.util.Scanner;
import java.util.ArrayList;

public class SigmaBot {
    public static final String GREETING = "     Hello! I'm SigmaBot\r\n" + "     What can I do for you?\r\n";
    public static final String GOODBYE = "     Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "    ____________________________________________________________\r\n";

    private ArrayList<Task> todo = new ArrayList<Task>(); 
    private int numTask = 0;

    public SigmaBot() { 
    }

    public ArrayList<Task> addItem(Task item) {
        todo.add(numTask, item);
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
        String[] msg2 = msg.split(" ", 2);
        
        Task task = new Task(msg);
        if (msg2[0].equals("todo")) {
            // added testcase for todo with empty description 
            if (msg2.length == 1) {
                System.out.println(SEP + "     Hey! invalid description\n" + SEP);
                task = nextTask(scanner);
            } else { 
                task = new TodoTask(msg2[1]);
            }
        } else if (msg2[0].equals("deadline")) {
            String[] msg3 = msg2[1].split(" /by ", 2);
            task = new DeadlineTask(msg3[0], msg3[1]);
        } else if (msg2[0].equals("event")) {
            String[] msg3 = msg2[1].split(" /from ", 2);
            String[] msg4 = msg3[1].split(" /to ", 2);
            task = new EventTask(msg3[0], msg4[0], msg4[1]);
        } else if (!msg.equals("list") && !msg2[0].equals("mark") && !msg2[0].equals("unmark") && !msg2[0].equals("delete") && !msg.equals("bye")) {
            // throw new SigmaBotException("Hey, that is not a valid command!"); 
            System.out.println(SEP + "     Hey! that doesnt make any sense!\n" + SEP);
            task = nextTask(scanner);
        }

        return task;
    }

    public void printTasks() {
        System.out.print(SEP);
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < getNumTask(); i += 1) {
            System.out.println("     " + String.valueOf(i + 1) + "." + this.todo.get(i));
        }       
        System.out.println(SEP);
    }

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SigmaBot bot = new SigmaBot();
        
        System.out.println(SEP + GREETING + SEP);
    
        Task task = bot.nextTask(scanner);
        try {
            while (!task.getDescription().equals("bye")) {
                if (task.getDescription().equals("list")) {
                    bot.printTasks();
                } else if (task.getDescription().split(" ")[0].equals("mark")) {
                    bot.markTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (task.getDescription().split(" ")[0].equals("unmark")) {
                    bot.unmarkTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    bot.printTasks();
                } else if (task.getDescription().split(" ")[0].equals("delete")) {
                    Task deleted = bot.deleteItem(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                    System.out.println(SEP + "     Noted. I've removed this task:\n        " + //
                            deleted + "\n     Now you have " + bot.getNumTask() + " tasks in the list." + "\r\n" + SEP);
                } 
                else {
                    bot.addItem(task);
                    System.out.println(SEP + "     Got it. I've added this task:\n        " + //
                            task + "\n     Now you have " + bot.getNumTask() + " tasks in the list." + "\r\n" + SEP);
                }

                task = bot.nextTask(scanner);
            } 
        } catch (SigmaBotException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GOODBYE + SEP);
    }
}
