package sigmabot;

import java.util.ArrayList;

public class Parser {
    private String input = "";
    private String inputFirstWord = "";
    private String prevInput;

    public static final String SEP = "____________________________________________________________\r\n";
    
    /**
     * Sets the input string and updates the first word of the input.
     *
     * @param input the user input string
     */
    public void setInput(String input) {
        if (isValidTask() || isMark() || isUnmark()) {
            this.prevInput = this.input;
        } 

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

    /**
     * Handles the parsed input and executes the corresponding command on the bot.
     * This method processes various command types including task creation, marking, deletion, and searching.
     *
     * @param msg the input message to process
     * @param bot the SigmaBot instance to operate on
     * @return the Task created or affected by the input
     * @throws IllegalArgumentException if msg or bot is null
     */
    private Task handleInput(String msg, SigmaBot bot) {
        if (msg == null || bot == null) {
            throw new IllegalArgumentException("Input message and SigmaBot instance cannot be null");
        }

        String[] msgSplit = msg.split(" ", 2);
        setInput(msg);

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
            bot.addItemLast(task);
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
            
            int taskNo = Integer.valueOf(msg.split(" ")[1]);
            this.prevInput = "delete " + (taskNo - 1) + " ";
            this.prevInput += deleted.getDeleteFormat();
            
            task.setPrintMsg(SEP + "Noted. I've removed this task:\n" +
                    deleted + "\nNow you have " + bot.getNumTask() +
                    " tasks in the list." + "\r\n" + SEP);
        } else if (isUndo()) {
            task = undo(bot);
        } else if (isFind()) {
            ArrayList<Task> matchingList = bot.findTasks(msgSplit[1]);
            task.setPrintMsg(bot.getPrintMatchingTasks(matchingList));
        }
        return task;
    }


    /**
     * Undoes the most recent user action by reversing its effects.
     * Supports undoing task additions, markings, unmarkings, and deletions.
     *
     * @param bot the SigmaBot instance to operate on
     * @return a Task representing the undo operation result
     */
    private Task undo(SigmaBot bot) {
        if (prevInput == null) {
            return new TodoTask(" ");
        }

        System.out.println("unmark: " + prevInput);
        String[] prevInputSplit = prevInput.split(" ", 3);
        String prevInputFirstWord = prevInputSplit[0];

        Task task = new TodoTask(prevInput);
        if (prevInputFirstWord.equals("todo") 
        || prevInputFirstWord.equals("deadline") 
        || prevInputFirstWord.equals("event")) {
            Task deleted = bot.deleteLastItem();
            task.setPrintMsg(SEP + "I've undone the previous task!\n" + 
                    "Noted. I've removed this task:\n" +
                    deleted + "\nNow you have " + bot.getNumTask() +
                    " tasks in the list." + "\r\n" + SEP);
        } else if (prevInputFirstWord.equals("mark")) {
            String[] parts = task.getDescription().split(" ");
            bot.unmarkTask(Integer.parseInt(parts[1]) - 1);
            task.setPrintMsg(bot.getPrintTasks());
        } else if (prevInputFirstWord.equals("unmark")) {
            String[] parts = task.getDescription().split(" ");
            bot.markTask(Integer.parseInt(parts[1]) - 1);
            task.setPrintMsg(bot.getPrintTasks());
        } else if (prevInputFirstWord.equals("delete")) {
            String[] prevInputSplitAgain = prevInputSplit[2].split(" ", 3);

            int deleteIndex = Integer.valueOf(prevInputSplit[1]);
            Boolean isDone = Boolean.parseBoolean(prevInputSplitAgain[0]);
            String taskType = prevInputSplitAgain[1];
            String taskDescription = prevInputSplitAgain[2];

            if (prevInputSplit.length < 2 || prevInputSplit[1].trim().isEmpty()) {
                task.setPrintMsg(SEP + "Hey! invalid description\n" + SEP);
            } else if (taskType.equals("todo")) {
                task = TodoTask.initFromString(taskDescription, isDone);
            } else if (taskType.equals("deadline")) {
                task = DeadlineTask.initFromString(taskDescription, isDone);
            } else if (taskType.equals("event")) {
                task = EventTask.initFromString(taskDescription, isDone);
            }
            bot.addItem(task, deleteIndex);
            task.setPrintMsg(SEP + "I've undone the previous task!\n" + 
                    "I've and added this task:\n" +
                    task + "\nNow you have " + bot.getNumTask() +
                    " tasks in the list." + "\r\n" + SEP);
        } 

        return task;
    }

    /**
     * Checks if the current input represents a todo task command.
     *
     * @return true if the input starts with "todo", false otherwise
     */
    public boolean isTodoTask() {
        return inputFirstWord.equals("todo");
    }

    /**
     * Checks if the current input represents a deadline task command.
     *
     * @return true if the input starts with "deadline", false otherwise
     */
    public boolean isDeadlineTask() {
        return inputFirstWord.equals("deadline");
    }

    /**
     * Checks if the current input represents an event task command.
     *
     * @return true if the input starts with "event", false otherwise
     */
    public boolean isEventTask() {
        return inputFirstWord.equals("event");
    }

    /**
     * Checks if the current input is a list command.
     *
     * @return true if the input equals "list", false otherwise
     */
    public boolean isList() {
        return input.equals("list");
    }   
    
    /**
     * Checks if the current input is an undo command.
     *
     * @return true if the input equals "undo", false otherwise
     */
    public boolean isUndo() {
        return input.equals("undo");
    }    
    
    /**
     * Checks if the current input is a goodbye command.
     *
     * @return true if the input equals "bye", false otherwise
     */
    public boolean isBye() {
        return input.equals("bye");
    }

    /**
     * Checks if the current input is a mark command.
     *
     * @return true if the input starts with "mark", false otherwise
     */
    public boolean isMark() {
        return inputFirstWord.equals("mark");
    }

    /**
     * Checks if the current input is an unmark command.
     *
     * @return true if the input starts with "unmark", false otherwise
     */
    public boolean isUnmark() {
        return inputFirstWord.equals("unmark");
    }

    /**
     * Checks if the current input is a delete command.
     *
     * @return true if the input starts with "delete", false otherwise
     */
    public boolean isDelete() {
        return inputFirstWord.equals("delete");
    }

    /**
     * Checks if the current input is a find command.
     *
     * @return true if the input starts with "find", false otherwise
     */
    public boolean isFind() {
        return inputFirstWord.equals("find");
    }

    /**
     * Checks if the current input represents a valid task creation command.
     *
     * @return true if the input is a todo, deadline, or event command, false otherwise
     */
    public boolean isValidTask() {
        return isTodoTask() || isDeadlineTask() || isEventTask();
    }

    /**
     * Checks if the current input represents a valid action command.
     *
     * @return true if the input is a recognized action command, false otherwise
     */
    public boolean isValidAction() {
        return isList() || isBye() || isMark() || isUnmark() || isDelete() || isFind() || isUndo();
    }
}
