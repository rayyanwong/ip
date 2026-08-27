package odysseus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import odysseus.parser.Command;
import odysseus.parser.Parser;
import odysseus.storage.Storage;
import odysseus.task.Task;
import odysseus.task.TaskList;
import odysseus.ui.Ui;

/**
 * The main chatbot: reads commands, manages tasks, prints responses.
 */
public class Odysseus {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    private static final String MARK_MSG = "Nice! I've marked this task as done:%n  %s";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:%n  %s";

    /**
     * Constructs Odysseus cli backed by a given filePath.
     *
     * @param filePath string representing file path of storage
     */
    public Odysseus(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (OdysseusException e) {
            ui.showError(e);
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    /**
     * Starts the chatting interface.
     */
    public void run() {
        boolean chatting = true;
        ui.showWelcome();
        while (chatting) {
            try {
                String input = ui.readCommand();
                if (input.isBlank()) {
                    throw new OdysseusException("Hey! Please enter a command...");
                }
                String[] inputSplit = input.split(" ");
                String command = inputSplit[0];
                String rest = input.substring(command.length()).trim();

                Task toAdd = null;

                switch (Command.fromInput(command)) {

                    case Command.BYE -> {
                        chatting = false;
                        ui.showGoodbye();
                    }

                    case Command.LIST -> {
                        ui.showTasks(tasks.getTasks());
                    }

                    case Command.MARK -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task markedTask = tasks.mark(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(MARK_MSG, markedTask);
                        ui.show(msg);
                    }

                    case Command.UNMARK -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task unmarkedTask = tasks.unmark(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(UNMARK_MSG, unmarkedTask);
                        ui.show(msg);
                    }

                    case Command.TODO -> {
                        toAdd = Parser.parseTodo(rest);
                    }

                    case Command.DEADLINE -> {
                        toAdd = Parser.parseDeadline(rest);
                    }

                    case Command.EVENT -> {
                        toAdd = Parser.parseEvent(rest);
                    }

                    case Command.DELETE -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task removedTask = tasks.remove(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(
                                "Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                                removedTask, tasks.size());
                        ui.show(msg);
                    }

                    case Command.ON -> {
                        if (rest.isEmpty()) {
                            throw new OdysseusException("Hey! The date can't be empty...");
                        }
                        try {
                            LocalDate onDate = LocalDate.parse(rest);
                            ui.showTasks(tasks.on(onDate));
                        } catch (DateTimeParseException e) {
                            throw new OdysseusException("Hey! The date can't be parsed..." +
                                    "Provide in the form of yyyy-mm-dd");
                        }
                    }

                    case Command.FIND -> {
                        if (rest.isEmpty()) {
                            throw new OdysseusException("Hey! The keyword to find can't be empty...");
                        }
                        ui.showTasks(tasks.find(rest));
                    }

                    case Command.UNKNOWN -> {
                        throw new OdysseusException("Hey! That's not a valid command. Try again.");
                    }
                }
                if (toAdd != null) {
                    tasks.add(toAdd);
                    String addedMsg = String.format(
                            "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                            toAdd, tasks.size());
                    storage.save(tasks.getTasks());
                    ui.show(addedMsg);
                }
            } catch (OdysseusException e) {
                ui.showError(e);
            }
        }
    }

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments passed to the program
     */
    public static void main(String[] args) {
        new Odysseus("data/odysseus.txt").run();
    }
}
