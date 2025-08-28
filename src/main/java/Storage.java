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

    public Storage() {
        todo = new ArrayList<Task>();
    }

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

    public boolean saveTasks(ArrayList<Task> todoToSave) throws IOException {
        File file = new File(savePath);

        // create new dir and file if it doesnt exist 
        if (!Files.exists(Paths.get(savePath))) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }

        // saves file to savePath
        // FileWriter fileWriter = new FileWriter(savePath, true);
        FileWriter fileWriter = new FileWriter(savePath);

        for (Task task : todoToSave) {
            fileWriter.write(task.encodeSaveFormat() + "\n");
        }

        System.out.println(SEP + "Successfully saved tasks!\n" + SEP);
        fileWriter.close();
        return true;
    }
}