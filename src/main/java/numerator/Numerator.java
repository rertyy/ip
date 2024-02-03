package numerator;

import java.nio.file.Path;
import java.util.Scanner;

import numerator.exceptions.NumeratorException;
import numerator.exceptions.storage.LoadingException;
import numerator.task.TaskList;

/**
 * Represents the main class of the Numerator program
 */
public class Numerator {
    private final Storage storage;
    private TaskList taskList;

    /**
     * Constructs a Numerator object with the default file path "data/storage.txt"
     */
    public Numerator() {
        this("data/storage.txt");
    }

    /**
     * Constructs a Numerator object with the specified file path
     *
     * @param filePath the path to the file to be loaded and saved
     */
    public Numerator(String filePath) {
        Path path = Path.of(filePath);
        storage = new Storage(path);

        try {
            taskList = storage.load();
        } catch (LoadingException e) {
            Ui.printLoadingError(e);
            taskList = new TaskList();
        }
    }

    /**
     * Instantiates The entry point of the Numerator program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Numerator().run();
    }

    public String getResponse(String response) {
        return "TODO to be implemented";
    }

    /**
     * Runs the Numerator program
     */
    public void run() {
        Ui.printLogo();
        Scanner sc = new Scanner(System.in);

        String input;
        while (sc.hasNext()) {
            try {
                input = sc.nextLine();
                Ui.printLine();
                boolean isExiting = Parser.parseArguments(input, taskList, storage);
                storage.save(taskList);

                if (isExiting) {
                    Ui.printExit();
                    break;
                }
            } catch (NumeratorException e) {
                Ui.printError(e);
            } finally {
                Ui.printLine();
            }
        }

        sc.close();

    }
}

