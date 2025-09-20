package sigmabot;

import java.util.ArrayList;

public class Parser {
    private String input = "";
    private String inputFirstWord = "";
    private String prevInput;

    public static final int DEFAULT_SPLIT_LIMIT = 2; 

    /**
     * Sets the input string and updates the first word of the input.
     * Also updates the previous input if the command is a task or mark/unmark.
     *
     * @param input The user input string.
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
     * @return The current user input string.
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Parses the user input from the UI and performs the corresponding action on the bot.
     *
     * @param ui The Ui object for user interaction.
     * @param bot The SigmaBot instance to operate on.
     * @return The Task created or affected by the input.
     * @throws IllegalArgumentException If ui or bot is null.
     */
    public Task parseInput(Ui ui, SigmaBot bot) {
        if (ui == null || bot == null) {
            throw new IllegalArgumentException("Ui and SigmaBot cannot be null");
        }
        
        return handleInput(ui.nextInput(), bot);
    }

    /**
     * Parses the user input from a string and performs the corresponding action on the bot.
     *
     * @param msg The string input message by the user.
     * @param bot The SigmaBot instance to operate on.
     * @return The Task created or affected by the input.
     * @throws IllegalArgumentException If msg or bot is null.
     */
    public Task parseInputFromString(String msg, SigmaBot bot) {
        if (msg == null || bot == null) {
            throw new IllegalArgumentException("Input message and SigmaBot cannot be null");
        }

        return handleInput(msg, bot);
    } 

    /**
     * Handles the parsed input and executes the corresponding command on the bot.
     * Processes task creation, action commands, or returns a default TodoTask.
     *
     * @param msg The input message to process.
     * @param bot The SigmaBot instance to operate on.
     * @return The Task created or affected by the input.
     * @throws IllegalArgumentException If msg or bot is null.
     */
    private Task handleInput(String msg, SigmaBot bot) {
        if (msg == null || bot == null) {
            throw new IllegalArgumentException("Input message and SigmaBot instance cannot be null");
        }

        String[] msgSplit = msg.split(" ", DEFAULT_SPLIT_LIMIT);
        String cleanedMsg = msg.toLowerCase().trim();
        setInput(cleanedMsg);

        if (isValidTask()) {
            return handleTaskCreation(msgSplit, bot);
        } else if (isActionCommand()) {
            return handleActionCommand(msgSplit, bot);
        }
        
        return new TodoTask(msg);
    }

    /**
     * Handles task creation commands by validating and creating the appropriate task.
     *
     * @param msgSplit The split message array containing the command and description.
     * @param bot The SigmaBot instance to operate on.
     * @return The created Task with a success or error message.
     */
    private Task handleTaskCreation(String[] msgSplit, SigmaBot bot) {
        Task task = new TodoTask(String.join(" ", msgSplit));
        
        boolean hasValidDescription = msgSplit.length >= 2 && !msgSplit[1].trim().isEmpty();
        
        if (!hasValidDescription) {
            task.setPrintMsg("Hey! invalid description\n");
            return task;
        }
        
        task = createTaskFromInput(msgSplit[1]);
        bot.addItem(task);
        
        String successMessage = "Got it. I've added this task:\n" +
                task + "\nNow you have " + bot.getNumTask() +
                " tasks in the list." + "\r\n";
        task.setPrintMsg(successMessage);
        
        return task;
    }

    /**
     * Handles action commands such as list, mark, unmark, delete, undo, and find.
     *
     * @param msgSplit The split message array containing the command and arguments.
     * @param bot The SigmaBot instance to operate on.
     * @return The Task with the result of the action command.
     */
    private Task handleActionCommand(String[] msgSplit, SigmaBot bot) {
        Task task = new TodoTask(String.join(" ", msgSplit));
        
        if (isList()) {
            task.setPrintMsg(bot.getPrintTasks());
        } else if (isMarkCommand()) {
            return handleMarkUnmarkCommand(task, bot);
        } else if (isDelete()) {
            return handleDeleteCommand(task, bot, msgSplit);
        } else if (isUndo()) {
            return undo(bot);
        } else if (isFind() && msgSplit.length > 1) {
            return handleFindCommand(msgSplit, bot);
        }
        
        return task;
    }

    /**
     * Undoes the most recent user action by reversing its effects.
     * Supports undoing task additions, markings, unmarkings, and deletions.
     *
     * @param bot The SigmaBot instance to operate on.
     * @return A Task representing the undo operation result.
     */
    private Task undo(SigmaBot bot) {
        if (prevInput == null) {
            return new TodoTask(" ");
        }

        System.out.println(prevInput);
        String[] prevInputSplit = prevInput.split(" ", 3);
        String prevInputFirstWord = prevInputSplit[0];

        Task task = new TodoTask(prevInput);

        if (isValidTaskFromString(prevInputFirstWord)) {
            task = undoTaskCreation(bot, task);
        } else if (prevInputFirstWord.equals("mark")) {
            task = undoMark(bot, task);
        } else if (prevInputFirstWord.equals("unmark")) {
            task = undoUnmark(bot, task);
        } else if (prevInputFirstWord.equals("delete")) {
            task = undoDelete(bot, task, prevInputSplit);
        }

        return task;
    }

    /**
     * Undoes a task creation by deleting the last item in the task list.
     *
     * @param bot The SigmaBot instance to operate on.
     * @param task The Task object representing the undo operation.
     * @return The Task with a message about the undone creation.
     */
    private Task undoTaskCreation(SigmaBot bot, Task task) {
        Task deleted = bot.deleteLastItem();
        task.setPrintMsg("I've undone the previous command,\n" +
                "and removed this task:\n" +
                deleted + "\nNow you have " + bot.getNumTask() +
                " tasks in the list." + "\r\n");
        return task;
    }

    /**
     * Undoes a mark command by unmarking the specified task if it was previously not done.
     *
     * @param bot The SigmaBot instance to operate on.
     * @param task The Task object representing the undo operation.
     * @return The Task with a message about the undone mark.
     */
    private Task undoMark(SigmaBot bot, Task task) {
        String[] parts = task.getDescription().split(" ");
        boolean prevTaskisDone = Boolean.parseBoolean(this.prevInput.split(" ", 3)[2]);

        if (!prevTaskisDone) {
            bot.unmarkTask(Integer.parseInt(parts[1]) - 1);
        }

        task.setPrintMsg("I've undone the previous command,\n" + bot.getPrintTasks());
        return task;
    }

    /**
     * Undoes an unmark command by marking the specified task if it was previously done.
     *
     * @param bot The SigmaBot instance to operate on.
     * @param task The Task object representing the undo operation.
     * @return The Task with a message about the undone unmark.
     */
    private Task undoUnmark(SigmaBot bot, Task task) {
        String[] parts = task.getDescription().split(" ");
        boolean prevTaskisDone = Boolean.parseBoolean(this.prevInput.split(" ", 3)[2]);

        if (prevTaskisDone) {
            bot.markTask(Integer.parseInt(parts[1]) - 1);
        }

        task.setPrintMsg("I've undone the previous command,\n" + bot.getPrintTasks());
        return task;
    }

    /**
     * Undoes a delete command by reconstructing and re-adding the deleted task.
     *
     * @param bot The SigmaBot instance to operate on.
     * @param task The Task object representing the undo operation.
     * @param prevInputSplit The split previous input array containing delete details.
     * @return The Task with a message about the undone deletion.
     */
    private Task undoDelete(SigmaBot bot, Task task, String[] prevInputSplit) {
        String[] prevInputSplitAgain = prevInputSplit[2].split(" ", 3);

        int deleteIndex = Integer.valueOf(prevInputSplit[1]);
        Boolean isDone = Boolean.parseBoolean(prevInputSplitAgain[0]);
        String taskType = prevInputSplitAgain[1];
        String taskDescription = prevInputSplitAgain[2];

        if (prevInputSplit.length < 2 || prevInputSplit[1].trim().isEmpty()) {
            task.setPrintMsg("Hey! invalid description\n");
        } else if (taskType.equals("todo")) {
            task = TodoTask.initFromString(taskDescription, isDone);
        } else if (taskType.equals("deadline")) {
            task = DeadlineTask.initFromString(taskDescription, isDone);
        } else if (taskType.equals("event")) {
            task = EventTask.initFromString(taskDescription, isDone);
        }
        bot.addItem(task, deleteIndex);
        task.setPrintMsg("I've undone the previous command!\n" +
                "I've and added this task:\n" +
                task + "\nNow you have " + bot.getNumTask() +
                " tasks in the list." + "\r\n");
        return task;
    }

    /**
     * Handles the delete command by removing the specified task and updating previous input.
     *
     * @param task The Task object containing the command.
     * @param bot The SigmaBot instance to operate on.
     * @param msgSplit The split message array containing the delete command and index.
     * @return The Task with updated print message after deletion.
     */
    private Task handleDeleteCommand(Task task, SigmaBot bot, String[] msgSplit) {
        Task deletedTask = bot.deleteItem(Integer.parseInt(msgSplit[1]) - 1);
        
        int taskNumber = Integer.valueOf(msgSplit[1]);
        this.prevInput = "delete " + (taskNumber - 1) + " ";
        this.prevInput += deletedTask.getDeleteFormat();
        
        String deletionMessage = "Noted. I've removed this task:\n" +
                deletedTask + "\nNow you have " + bot.getNumTask() +
                " tasks in the list." + "\r\n";
        task.setPrintMsg(deletionMessage);
        
        return task;
    }

    /**
     * Creates a task from the given input description based on the current command type.
     *
     * @param description The task description to create the task from.
     * @return The created Task object (TodoTask, DeadlineTask, or EventTask).
     */
    private Task createTaskFromInput(String description) {
        if (isTodoTask()) {
            return TodoTask.initFromString(description);
        } else if (isDeadlineTask()) {
            return DeadlineTask.initFromString(description);
        } else if (isEventTask()) {
            return EventTask.initFromString(description);
        }
        return new TodoTask(description);
    }

    /**
     * Handles mark and unmark commands by parsing the task index and updating task status.
     *
     * @param task The Task object containing the command.
     * @param bot The SigmaBot instance to operate on.
     * @return The Task with updated print message after marking or unmarking.
     */
    private Task handleMarkUnmarkCommand(Task task, SigmaBot bot) {
        String[] commandParts = task.getDescription().split(" ");

        
        if (commandParts.length < 2) {
            task.setPrintMsg("Hey! Please specify a task number\n");
            return task;
        }
        
        try {
            int taskIndex = Integer.parseInt(commandParts[1]) - 1;
            boolean isDone = bot.getTaskisDone(taskIndex);

            this.input += " " +  Boolean.toString(isDone);

            if (isMark()) {
                bot.markTask(taskIndex);
            } else if (isUnmark()) {
                bot.unmarkTask(taskIndex);
            }
            
            task.setPrintMsg(bot.getPrintTasks());
            
        } catch (NumberFormatException e) {
            task.setPrintMsg("Hey! Please enter a valid task number\n");
        } catch (IndexOutOfBoundsException e) {
            task.setPrintMsg("Hey! Task number is out of range\n");
        }
        
        return task;
    }

    /**
     * Handles find commands by searching for tasks containing the specified keyword.
     *
     * @param msgSplit The split message array containing the find command and keyword.
     * @param bot The SigmaBot instance to operate on.
     * @return A Task with the search results as print message.
     */
    private Task handleFindCommand(String[] msgSplit, SigmaBot bot) {
        Task task = new TodoTask(String.join(" ", msgSplit));
        
        String keyword = msgSplit[1];
        ArrayList<Task> matchingList = bot.findTasks(keyword);
        task.setPrintMsg(bot.getPrintMatchingTasks(matchingList));
        
        return task;
    }

    /**
     * Checks if the current input represents a todo task command.
     *
     * @return True if the input starts with "todo", false otherwise.
     */
    public boolean isTodoTask() {
        return inputFirstWord.equals("todo");
    }

    /**
     * Checks if the current input represents a deadline task command.
     *
     * @return True if the input starts with "deadline", false otherwise.
     */
    public boolean isDeadlineTask() {
        return inputFirstWord.equals("deadline");
    }

    /**
     * Checks if the current input represents an event task command.
     *
     * @return True if the input starts with "event", false otherwise.
     */
    public boolean isEventTask() {
        return inputFirstWord.equals("event");
    }

    /**
     * Checks if the current input is a list command.
     *
     * @return True if the input equals "list", false otherwise.
     */
    public boolean isList() {
        return input.equals("list");
    }   
    
    /**
     * Checks if the current input is an undo command.
     *
     * @return True if the input equals "undo", false otherwise.
     */
    public boolean isUndo() {
        return input.equals("undo");
    }    
    
    /**
     * Checks if the current input is a goodbye command.
     *
     * @return True if the input equals "bye", false otherwise.
     */
    public boolean isBye() {
        return input.equals("bye");
    }

    /**
     * Checks if the current input is a mark command.
     *
     * @return True if the input starts with "mark", false otherwise.
     */
    public boolean isMark() {
        return inputFirstWord.equals("mark");
    }

    /**
     * Checks if the current input is an unmark command.
     *
     * @return True if the input starts with "unmark", false otherwise.
     */
    public boolean isUnmark() {
        return inputFirstWord.equals("unmark");
    }

    /**
     * Checks if the current input is a delete command.
     *
     * @return True if the input starts with "delete", false otherwise.
     */
    public boolean isDelete() {
        return inputFirstWord.equals("delete");
    }

    /**
     * Checks if the current input is a find command.
     *
     * @return True if the input starts with "find", false otherwise.
     */
    public boolean isFind() {
        return inputFirstWord.equals("find");
    }

    /**
     * Checks if the current input represents a valid task creation command.
     *
     * @return True if the input is a todo, deadline, or event command, false otherwise.
     */
    public boolean isValidTask() {
        return isTodoTask() || isDeadlineTask() || isEventTask();
    }

    /**
     * Checks if the given string is a valid task command ("todo", "deadline", or "event").
     *
     * @param string The command string to check.
     * @return True if the string is a valid task command, false otherwise.
     */
    public boolean isValidTaskFromString(String string) {
        return string.equals("todo") || string.equals("deadline") || string.equals("event");
    }

    /**
     * Checks if the current input represents a valid action command.
     *
     * @return True if the input is a recognized action command, false otherwise.
     */
    public boolean isValidAction() {
        return isList() || isBye() || isMark() || isUnmark() || isDelete() || isFind() || isUndo();
    }

    /**
     * Checks if the current input is a valid action command.
     *
     * @return True if the input is a recognized action command, false otherwise.
     */
    private boolean isActionCommand() {
        return isList() || isMarkCommand() || isDelete() || isUndo() || isFind();
    }

    /**
     * Checks if the current input is a mark or unmark command.
     *
     * @return True if the input is a mark or unmark command, false otherwise.
     */
    private boolean isMarkCommand() {
        return isMark() || isUnmark();
    }

}
