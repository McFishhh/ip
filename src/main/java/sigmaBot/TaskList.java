package sigmaBot;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskList;
    
    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    public ArrayList<Task> addTask(Task task) {
        this.taskList.add(task);
        return this.taskList;
    }

    public Task deleteTask(int i) {
        return this.taskList.remove(i);
    }

    public int size() {
        return taskList.size();
    }

    public Task get(int i) {
        return this.taskList.get(i);
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
