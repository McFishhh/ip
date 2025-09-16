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
    public static final String SEP = "____________________________________________________________\r\n";

    /**
     * Constructs a Storage object and initializes the todo list.
     */
    public Storage() {
        todo = new ArrayList<Task>();
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
     */
    public ArrayList<Task> loadTasks() throws IOException {
        Path path = Paths.get(savePath);
        
        // check for existence of filepath 
        if (!Files.exists(path)) {
            System.out.println(SEP + "No existing save file :(\n" + SEP);
            return todo;
        }

        // read in files one line at a time and creates Tasks to add to todo
        BufferedReader bufferReader = new BufferedReader(new FileReader(savePath));
        String line;
        while ((line = bufferReader.readLine()) != null) { 
            String[] lineSplit = line.split(",");

            if (lineSplit[0].equals("T")) {
                todo.add(TodoTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("D")) {
                todo.add(DeadlineTask.decodeSaveFormat(line));
            } else if (lineSplit[0].equals("E")) {
                todo.add(EventTask.decodeSaveFormat(line));
            }
        }

        System.out.println(SEP + "Successfully loaded tasks! :D\n" + SEP);
        bufferReader.close();
        return todo;
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

        // create new dir and file if it doesnt exist 
        if (!Files.exists(Paths.get(savePath))) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        // saves file to savePath
        FileWriter fileWriter = new FileWriter(savePath);

        for (Task task : todoToSave.getTaskList()) {
            fileWriter.write(task.encodeSaveFormat() + "\n");
        }

        System.out.println(SEP + "Successfully saved tasks!\n" + SEP);
        fileWriter.close();
        return true;
    }
}
