package odysseus.storage;

import odysseus.OdysseusException;
import odysseus.task.Task;
import odysseus.task.TaskFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path path;

    public Storage(String filePath) {
        this.path = Path.of(filePath);
    }

    public List<Task> load() throws OdysseusException {
       // 1. check if the file exist
        if (Files.exists(path)) {
            // 2. load the line
            try {
                List<String> lines = Files.readAllLines(path);
                List<Task> tasks = new ArrayList<>();
                for (String line : lines) {
                    Task newTask = TaskFactory.fromSaveFormat(line);
                    tasks.add(newTask);
                }
                return tasks;
            } catch (IOException e) {
                throw new OdysseusException("Hey! An error occurred while loading file: " +
                        e.getMessage());
            }
        } else {
            return new ArrayList<>();
        }
    }

    public void save(List<Task> tasks) throws OdysseusException {
        // 1. go through the task and convert each task into it's save format
        // 2. write the List<String> into file
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            String saveStr = task.toSaveFormat();
            lines.add(saveStr);
        }
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines);
        } catch (IOException e) {
            throw new OdysseusException("Error occurred while saving tasks: " +
                    e.getMessage());
        }
    }
}
