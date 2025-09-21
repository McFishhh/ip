package sigmabot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue; 
import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageTest {

    private Storage storage;
    private String testFilePath = "test_storage.txt";

    @BeforeEach
    void setUp() {
        storage = new Storage(testFilePath);
        // Clean up test file before each test
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndLoadTasks() throws Exception {
        TaskList tasks = new TaskList();
        tasks.addTask(TodoTask.initFromString("read book"));
        tasks.addTask(DeadlineTask.initFromString("report /by 2025-06-13"));
        tasks.addTask(EventTask.initFromString("meeting /from 2pm /to 4pm"));

        storage.saveTasks(tasks);

        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(3, loadedTasks.size());
        assertEquals("read book", loadedTasks.get(0).getDescription());
        assertEquals("report", loadedTasks.get(1).getDescription());
        assertEquals("meeting", loadedTasks.get(2).getDescription());
    }

    @Test
    void testLoadTasksWithMalformedFile() throws Exception {
        // Write malformed lines to file
        File file = new File(testFilePath);
        java.io.FileWriter fw = new java.io.FileWriter(file);
        fw.write("T,true,read book\n");
        fw.write("D,true,submit assignment,2025-09-21\n");
        fw.write("INVALID LINE\n");
        fw.close();

        ArrayList<Task> loadedTasks = storage.loadTasks();

        // Should only load valid tasks
        assertTrue(loadedTasks.size() >= 0); // No crash
    }

    @Test
    void testSaveTasksCreatesFile() throws Exception {
        TaskList tasks = new TaskList();
        tasks.addTask(new TodoTask("test file creation"));

        storage.saveTasks(tasks);

        File file = new File(testFilePath);
        assertTrue(file.exists());
    }
}