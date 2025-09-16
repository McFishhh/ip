package sigmabot;

import org.junit.jupiter.api.Test;

import sigmabot.Task;
import sigmabot.TaskList;
import sigmabot.TaskStub;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {

    @Test
    public void addTaskTest() {
        TaskList list = new TaskList();
        TaskStub t = new TaskStub("batheHamster");
        
        list.addTask(t);
        
        assertEquals(1, list.size());
        assertEquals("batheHamster", list.get(0).getDescription());
    }

    @Test
    public void deleteTaskTest() {
        TaskList list = new TaskList();
        TaskStub t1 = new TaskStub("batheHamster");
        TaskStub t2 = new TaskStub("batheHamsterAgain");

        list.addTask(t1);
        list.addTask(t2);
        
        TaskStub removed = (TaskStub) list.deleteTask(0);
        
        assertEquals("batheHamster", removed.getDescription());
        assertEquals(1, list.size());
        assertEquals("batheHamsterAgain", list.get(0).getDescription());
    }

    @Test
    public void sizeTest() {
        TaskList list = new TaskList();
        assertEquals(0, list.size());
        
        TaskStub t = new TaskStub("batheHamster");
        list.addTask(t);
        assertEquals(1, list.size());
    }

    @Test
    public void getTest() {
        TaskList list = new TaskList();
        TaskStub t = new TaskStub("batheHamster");
        
        list.addTask(t);
        
        assertEquals(t, list.get(0));
    }

    @Test
    public void getTaskListTest() {
        TaskList list = new TaskList();
        TaskStub t = new TaskStub("batheHamster");
        
        list.addTask(t);
        
        assertEquals(1, list.getTaskList().size());
        assertEquals(t, list.getTaskList().get(0));
    }

    @Test
    public void setTaskListTest() {
        TaskList list = new TaskList();
        java.util.ArrayList<Task> newList = new java.util.ArrayList<>();
        TaskStub t = new TaskStub("batheHamster");
        
        newList.add(t);
        list.setTaskList(newList);
        
        assertEquals(1, list.size());
        assertEquals(t, list.get(0));
    }
}
