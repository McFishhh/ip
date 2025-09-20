package sigmabot;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskList;
    
    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the Task to add
     * @param index te index to place the Task at
     * @return the updated list of tasks
     */
    public ArrayList<Task> addTask(Task task, int index) {
        this.taskList.add(index, task);
        return this.taskList;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the Task to add
     * @return the updated list of tasks
     */
    public ArrayList<Task> addTask(Task task) {
        this.taskList.add(task);
        return this.taskList;
    }

    /**
     * Deletes the task at the specified index from the task list.
     *
     * @param i the index of the task to delete
     * @return the Task that was removed
     */
    public Task deleteTask(int i) {
        return this.taskList.remove(i);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param i the index of the task to retrieve
     * @return the Task at the specified index
     */
    public Task get(int i) {
        return this.taskList.get(i);
    }

    /**
     * Returns the list of all tasks.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * Sets the task list to the given list of tasks.
     *
     * @param taskList the new list of tasks
     */
    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Checks if the given index is a valid index in the task list.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < taskList.size();
    }
}
