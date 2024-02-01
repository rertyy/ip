package numerator;

import numerator.exceptions.storage.LoadingException;
import numerator.exceptions.storage.SavingException;
import numerator.task.TaskList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

public class Storage {
    Path filepath;

    public Storage(Path filepath) {
        this.filepath = filepath;
    }


    public TaskList load() throws LoadingException {
        TaskList taskList = new TaskList();
        try {
            // Solution below adapted from https://stackoverflow.com/a/41514348
            if (!Files.exists(this.filepath)) {
                Files.createDirectories(this.filepath.getParent());
                Files.createFile(this.filepath);
                throw new LoadingException("File does not exist");
            }

            // Solution below adapted from https://www.baeldung.com/reading-file-in-java
            Stream<String> lines = Files.lines(this.filepath);
            lines.forEach(line -> {
                        String[] s = line.split(" \\| ");
                        String taskType = s[0];
                        boolean isDone = s[1].equals("1");
                        String taskDesc = s[2];
                        switch (taskType) {
                        case "T":
                            taskList.addToDo(taskDesc);
                            break;
                        case "D":
                            taskList.addDeadline(taskDesc, s[3]);
                            break;
                        case "E":
                            taskList.addEvent(taskDesc, s[3], s[4]);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + taskType);
                        }

                        if (isDone) {
                            taskList.markLastAsDone();
                        }

                    }
            );

            lines.close();
            return taskList;
        } catch (IndexOutOfBoundsException | IllegalStateException | IOException | DateTimeParseException e) {
            throw new LoadingException("Error loading file");
        }

    }

    public void save(TaskList taskList) throws SavingException {
        try (BufferedWriter bw = Files.newBufferedWriter(this.filepath)) {
            bw.write(taskList.getSavedTasksString());
        } catch (IOException e) {
            throw new SavingException("Error saving file");
        }
    }

}
