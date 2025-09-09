package sigmaBot;

import java.util.ArrayList;

public class Parser {
    private String input;
    private String inputFirstWord;

    public static final String SEP = "____________________________________________________________\r\n";
    
    /**
     * Sets the input string and updates the first word of the input.
     *
     * @param input the user input string
     */
    public void setInput(String input) {
        this.input = input;
        this.inputFirstWord = input.split(" ", 2)[0];
    }

    /**
     * Returns the current input string.
     *
     * @return the current user input string
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Parses the user input and performs the corresponding action on the bot.
     *
     * @param ui the Ui object for user interaction
     * @param bot the SigmaBot instance to operate on
     * @return the Task created or affected by the input
     */
    public Task parseInput(Ui ui, SigmaBot bot) {
        if (ui == null || bot == null) {
            throw new IllegalArgumentException("Ui and SigmaBot cannot be null");
        }
        
        return handleInput(ui.nextInput(), bot);
    }

    /**
     * Parses the user input and performs the corresponding action on the bot.
     *
     * @param msg the string input msg by the user 
     * @param bot the SigmaBot instance to operate on
     * @return the Task created or affected by the input
     */
    public Task parseInputFromString(String msg, SigmaBot bot) {
        if (msg == null || bot == null) {
            throw new IllegalArgumentException("Input message and SigmaBot cannot be null");
        }

        return handleInput(msg, bot);
    } 

    private Task handleInput(String msg, SigmaBot bot) {
        if (msg == null || bot == null) {
            throw new IllegalArgumentException("Input message and SigmaBot instance cannot be null");
        }

        String[] msgSplit = msg.split(" ", 2);
        input = msg;
        inputFirstWord = msgSplit[0];

        Task task = new TodoTask(msg);
        if (isValidTask()) {
            if (msgSplit.length < 2 || msgSplit[1].trim().isEmpty()) {
                task.setPrintMsg(SEP + "Hey! invalid description\n" + SEP);
            } else if (isTodoTask()) {
                task = TodoTask.initFromString(msgSplit[1]);
            } else if (isDeadlineTask()) {
                task = DeadlineTask.initFromString(msgSplit[1]);
            } else if (isEventTask()) {
                task = EventTask.initFromString(msgSplit[1]);
            }
            bot.addItem(task);
            task.setPrintMsg(SEP + "Got it. I've added this task:\n" +
                    task + "\nNow you have " + bot.getNumTask() +
                    " tasks in the list." + "\r\n" + SEP);
        } else if (isList()) {
            task.setPrintMsg(bot.getPrintTasks());
        } else if (isMark()) {
            String[] parts = task.getDescription().split(" ");
            bot.markTask(Integer.parseInt(parts[1]) - 1);
            task.setPrintMsg(bot.getPrintTasks());
        } else if (isUnmark()) {
            String[] parts = task.getDescription().split(" ");
            bot.unmarkTask(Integer.parseInt(parts[1]) - 1);
            task.setPrintMsg(bot.getPrintTasks());
        } else if (isDelete()) {
            String[] parts = task.getDescription().split(" ");
            Task deleted = bot.deleteItem(Integer.parseInt(parts[1]) - 1);
            task.setPrintMsg(SEP + "Noted. I've removed this task:\n" +
                    deleted + "\nNow you have " + bot.getNumTask() +
                    " tasks in the list." + "\r\n" + SEP);
        } else if (isFind()) {
            ArrayList<Task> matchingList = bot.findTasks(msgSplit[1]);
            task.setPrintMsg(bot.getPrintMatchingTasks(matchingList));
        }
        return task;
    }

    public boolean isTodoTask() {
        return inputFirstWord.equals("todo");
    }

    public boolean isDeadlineTask() {
        return inputFirstWord.equals("deadline");
    }

    public boolean isEventTask() {
        return inputFirstWord.equals("event");
    }

    public boolean isList() {
        return input.equals("list");
    }    
    
    public boolean isBye() {
        return input.equals("bye");
    }

    public boolean isMark() {
        return inputFirstWord.equals("mark");
    }

    public boolean isUnmark() {
        return inputFirstWord.equals("unmark");
    }

    public boolean isDelete() {
        return inputFirstWord.equals("delete");
    }

    public boolean isFind() {
        return inputFirstWord.equals("find");
    }

    public boolean isValidTask() {
        return isTodoTask() || isDeadlineTask() || isEventTask();
    }

    public boolean isValidAction() {
        return isList() || isBye() || isMark() || isUnmark() || isDelete() || isFind();
    }
}
