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

    /**
     * Constructs a new SigmaBot instance and initializes all components.
     * Automatically loads existing tasks from storage during initialization.
     */
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

    /**
     * Adds a task at the specified index in the task list.
     *
     * @param task the Task to add
     * @param i the index at which to insert the task
     * @return the updated list of tasks
     */
    public ArrayList<Task> addItem(Task task, int i) {
        assert task != null : "Task to add cannot be null for SigmaBot::addItem";
        return this.taskList.addTask(task, i);
    }

    /**
     * Adds a task to the end of the task list.
     *
     * @param task the Task to add
     * @return the updated list of tasks
     */
    public ArrayList<Task> addItem(Task task) {
        assert task != null : "Task to add cannot be null for SigmaBot::addItem";
        return this.taskList.addTask(task);
    }

    /**
     * Adds a task at the second-to-last position in the task list.
     *
     * @param task the Task to add
     * @return the updated list of tasks
     */
    public ArrayList<Task> addItemLast(Task task) {
        assert task != null : "Task to add cannot be null for SigmaBot::addItem";
        return this.taskList.addTask(task, this.taskList.size() - 1);
    }
    
    /**
     * Removes and returns the task at the specified index.
     *
     * @param i the index of the task to delete
     * @return the Task that was removed
     */
    public Task deleteItem(int i) {
        assert i >= 0 && i < this.taskList.size() : "Index out of bounds for SigmaBot::deleteItem";
        return this.taskList.deleteTask(i);
    }

    /**
     * Removes and returns the last task in the task list.
     *
     * @return the Task that was removed from the end of the list
     */
    public Task deleteLastItem() {
        return this.taskList.deleteTask(this.taskList.size() - 1);
    }

    /**
     * Returns the current task list.
     *
     * @return the TaskList containing all tasks
     */
    public TaskList getTodo() {
        return this.taskList;
    } 

    /**
     * Sets the task list to the specified TaskList.
     *
     * @param todoToSet the new TaskList to use
     */
    public void setTodo(TaskList todoToSet) {
        assert todoToSet != null : "TaskList to set cannot be null for SigmaBot::setTodo";
        this.taskList = todoToSet;
    } 

    /**
     * Returns the number of tasks currently in the task list.
     *
     * @return the total number of tasks
     */
    public int getNumTask() {
        return taskList.size();
    } 

    /**
     * Marks the task at the specified index as completed.
     *
     * @param i the index of the task to mark as done
     */
    public void markTask(int i) {
        assert i >= 0 && i < this.taskList.size() : "Index out of bounds for SigmaBot::markTask";
        this.taskList.get(i).mark();
    }

    /**
     * Marks the task at the specified index as not completed.
     *
     * @param i the index of the task to unmark
     */
    public void unmarkTask(int i) {
        assert i >= 0 && i < this.taskList.size() : "Index out of bounds for SigmaBot::unmarkTask";
        this.taskList.get(i).unmark();
    }

    /**
     * Processes the next user input and returns the resulting task or action.
     *
     * @return the Task created or affected by the user input
     * @throws SigmaBotException if input parsing fails
     */
    public Task nextTask() throws SigmaBotException{
        Task task = this.parser.parseInput(ui, this);
        return task;
    }

    /**
     * Processes a string input directly and returns the resulting task or action.
     *
     * @param msg the input message to process
     * @return the Task created or affected by the input
     * @throws SigmaBotException if input parsing fails
     */
    public Task nextTaskfromString(String msg) throws SigmaBotException{
        Task task = this.parser.parseInputFromString(msg, this);
        return task;
    }

    /**
     * Prints all tasks in the current task list to the console.
     */
    public void printTasks() {
        System.out.print(SEP);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < this.taskList.size() ; i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + this.taskList.get(i));
        }       
        System.out.println(SEP);
    }

    /**
     * Returns a formatted string containing all tasks in the current task list.
     *
     * @return formatted string representation of all tasks
     */
    public String getPrintTasks() {
        String result = "";
        result += SEP + "\n";
        result += "Here are the tasks in your list:\n";
        for (int i = 0; i < this.taskList.size() ; i += 1) {
            result += String.valueOf(i + 1) + "." + this.taskList.get(i) + "\n";
        }       
        result += SEP;

        return result;
    }

    /**
     * Prints the specified list of matching tasks to the console.
     *
     * @param matchingList the list of tasks that matched a search query
     */
    public void printMatchingTasks(ArrayList<Task> matchingList) {
        assert matchingList != null : "Matching list cannot be null for SigmaBot::printMatchingTasks";
        System.out.print(SEP);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingList.size() ; i += 1) {
            System.out.println(String.valueOf(i + 1) + "." + matchingList.get(i));
        }       
        System.out.println(SEP);
    }

    /**
     * Returns a formatted string containing all matching tasks from a search.
     *
     * @param matchingList the list of tasks that matched a search query
     * @return formatted string representation of matching tasks
     */
    public String getPrintMatchingTasks(ArrayList<Task> matchingList) {
        assert matchingList != null : "Matching list cannot be null for SigmaBot::getPrintMatchingTasks";
        String result = "";
        result += SEP + "\n";
        result += "Here are the matching tasks in your list:" + "\n";
        for (int i = 0; i < matchingList.size() ; i += 1) {
            result += String.valueOf(i + 1) + "." + matchingList.get(i) + "\n";
        }       
        result += SEP;

        return result;
    }

    /**
     * Loads tasks from storage into the current task list.
     *
     * @param storage the Storage instance to load tasks from
     * @throws IOException if there's an error reading from storage
     */
    private void loadTasks(Storage storage) throws IOException {
        taskList.setTaskList(storage.loadTasks());
    }

    /**
     * Saves the current task list to storage.
     *
     * @param storage the Storage instance to save tasks to
     * @return true if save was successful, false otherwise
     * @throws IOException if there's an error writing to storage
     */
    private boolean saveTasks(Storage storage) throws IOException {
        return storage.saveTasks(this.taskList);
    }

    /**
     * Searches for tasks containing the specified keyword.
     *
     * @param keyword the search term to look for in task descriptions
     * @return a list of tasks that contain the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        return storage.findTasks(this.taskList, keyword);
    }

    /**
     * Checks if the input is a goodbye command and handles shutdown if so.
     *
     * @param input the user input string to check
     * @return true if input was "bye" and shutdown was initiated, false otherwise
     */
    public boolean isBye(String input) {
        if (input.equals("bye")) {
            try {
                this.bye();
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            return true;
        }

        return false;
    }

    /**
     * Handles the shutdown process by saving tasks to storage.
     *
     * @throws IOException if there's an error saving tasks
     */
    public void bye() throws IOException {
        this.saveTasks(storage);
    }
    
    /**
     * Runs the main interactive loop of the SigmaBot application.
     * Displays greeting, processes user input until "bye" command, then saves and exits.
     */
    public void run() {
        SigmaBot bot = this;

        System.out.println(SEP + GREETING + SEP);
        
        bot.nextTask();
        try {
            while (!bot.parser.isBye()) {
                bot.nextTask();
            } 

            bot.saveTasks(storage);
        } catch (SigmaBotException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(SEP + GOODBYE + SEP);
    }

    /**
     * Generates a response for the user's chat message in GUI mode.
     *
     * @param input the user's input message
     * @return the bot's response string
     */
    public String getResponse(String input) {
        return "SigmaBot heard: " + input;
    }

    /**
     * Main method that creates and runs a new SigmaBot instance.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new SigmaBot().run();
    }
}
