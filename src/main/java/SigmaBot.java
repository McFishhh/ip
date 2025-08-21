import java.util.Scanner;

public class SigmaBot {
    public static final String GREETING = "\t Hello! I'm SigmaBot\r\n" + "\t What can I do for you?\r\n";
    public static final String GOODBYE = "\t Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "\t____________________________________________________________\r\n";

    private Task[] todo = new Task[100]; 
    private int numTask = 0;

    public SigmaBot() { 
    }

    public Task[] addItem(Task item) {
        todo[numTask] = item;
        numTask += 1;

        return todo;
    }

    public Task[] getTodo() {
        return this.todo;
    } 

    public int getNumTask() {
        return this.numTask;
    } 

    public void markTask(int i) {
        this.todo[i].mark();
    }

    public void unmarkTask(int i) {
        this.todo[i].unmark();
    }

    public Task nextTask(Scanner scanner) {
        String msg = scanner.nextLine().toLowerCase();
        String[] msg2 = msg.split(" ", 2);
        
        Task task = new Task(msg);
        if (msg2[0].equals("todo")) {
            task = new TodoTask(msg2[1]);
        } else if (msg2[0].equals("deadline")) {
            String[] msg3 = msg2[1].split(" /by ", 2);
            task = new DeadlineTask(msg3[0], msg3[1]);
        } else if (msg2[0].equals("event")) {
            String[] msg3 = msg2[1].split(" /from ", 2);
            String[] msg4 = msg3[1].split(" /to ", 2);
            task = new EventTask(msg3[0], msg4[0], msg4[1]);
        }

        return task;
    }

    public void printTasks() {
        System.out.print(SEP);
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < getNumTask(); i += 1) {
            System.out.println("\t " + String.valueOf(i + 1) + "." + this.todo[i]);
        }       
        System.out.println(SEP);
}

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SigmaBot bot = new SigmaBot();
        
        System.out.println(SEP + GREETING + SEP);
    
        Task task = bot.nextTask(scanner);
        while (!task.getDescription().equals("bye")) {
            if (task.getDescription().equals("list")) {
                bot.printTasks();
            } else if (task.getDescription().split(" ")[0].equals("mark")) {
                bot.markTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                bot.printTasks();
            } else if (task.getDescription().split(" ")[0].equals("unmark")) {
                bot.unmarkTask(Integer.parseInt(task.getDescription().split(" ")[1]) - 1);
                bot.printTasks();
            } 
            else {
                bot.addItem(task);
                System.out.println(SEP + "\t Got it. I've added this task:\n\t    " + //
                         task + "\n\t Now you have " + bot.getNumTask() + " tasks in the list." + "\r\n" + SEP);
            }

            task = bot.nextTask(scanner);
        } 

        System.out.println(SEP + GOODBYE + SEP);
    }
}
