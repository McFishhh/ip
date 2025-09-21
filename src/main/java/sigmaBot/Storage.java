package sigmabot;
import java.util.ArrayList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Storage {
    private ArrayList<Task> todo;
    private String savePath = "saves/savedTasks.txt";

    /**
     * Constructs a Storage object and initializes the todo list.
     */
    public Storage() {
        this.todo = new ArrayList<Task>();
    }

    /**
     * Constructs a Storage object and initializes the todo list.
     */
    public Storage(String savePath) {
        this.todo = new ArrayList<Task>();
        this.savePath = savePath;
    }

    public ArrayList<Task> findTasks(TaskList taskList, String keyword) {
        assert taskList != null : "TaskList cannot be null";
        assert keyword != null : "Keyword cannot be null";
        ArrayList<Task> matchedList = new ArrayList<Task>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : taskList.getTaskList()) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchedList.add(task);
            }
        }
        return matchedList;
    }

    /**
     * Loads tasks from the save file into the todo list.
     *
     * @return the list of loaded tasks
     * @throws IOException if an I/O error occurs while reading the file
     * @throws SigmaBotReadSaveException if a line in the save file cannot be decoded
     */
    public ArrayList<Task> loadTasks() throws IOException {
        Path path = Paths.get(savePath);

        if (!Files.exists(path)) {
            System.out.println("No existing save file :(\n");
            return todo;
        }

        int lineNumber = 0;
        BufferedReader bufferReader = new BufferedReader(new FileReader(savePath));
        String line;
        
        while ((line = bufferReader.readLine()) != null) {
            lineNumber++;
            tryAddTaskFromLine(line, lineNumber);
        }

        System.out.println("Successfully loaded tasks! :D\n");
        bufferReader.close();
        return todo;
    }

    /**
     * Attempts to decode and add a task from a line. Returns true if successful.
     */
    private boolean tryAddTaskFromLine(String line, int lineNumber)  {
        String[] lineSplit = line.split(",");
        if (lineSplit.length == 0) return false;
        String taskSymbol = lineSplit[0];
        try {
            switch (taskSymbol) {
                case "T":
                    todo.add(TodoTask.decodeSaveFormat(line));
                    break;
                case "D":
                    todo.add(DeadlineTask.decodeSaveFormat(line));
                    break;
                case "E":
                    todo.add(EventTask.decodeSaveFormat(line));
                    break;
                default:
                    throw new SigmaBotReadSaveException("Unknown task type at line " + lineNumber + ": " + line);
            }
        } catch (SigmaBotReadSaveException e) {
            System.out.println("Line: " + lineNumber + "\n" + e.getMessage() + "\n");
            return false;
        } 

        return true;
    }

    /**
     * Saves the given TaskList to the save file.
     *
     * @param todoToSave the TaskList to save
     * @return true if the tasks were saved successfully, false otherwise
     * @throws IOException if an I/O error occurs while writing the file
     */
    public boolean saveTasks(TaskList todoToSave) throws IOException {
        File file = new File(savePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        FileWriter fileWriter = new FileWriter(savePath);

        for (Task task : todoToSave.getTaskList()) {
            fileWriter.write(task.encodeSaveFormat() + "\n");
        }

        System.out.println("Successfully saved tasks!\n");
        fileWriter.close();
        return true;
    }
}
