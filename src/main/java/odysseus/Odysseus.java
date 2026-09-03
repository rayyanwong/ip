package odysseus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import odysseus.parser.Command;
import odysseus.parser.Parser;
import odysseus.storage.Storage;
import odysseus.task.Task;
import odysseus.task.TaskList;
import odysseus.ui.Ui;

/**
 * The main chatbot: reads commands, manages tasks, produces responses.
 */
public class Odysseus {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private boolean isExit = false;

    private static final String MARK_MSG = "Nice! I've marked this task as done:%n  %s";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:%n  %s";
    private static final String BYE_MSG = "Bye. Hope to see you again soon!";

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
        ui.showWelcome();
        while (!this.isExit) {
            String input = ui.readCommand();
            String response = this.getResponse(input);
            ui.show(response);
        }
    }

    /**
     * Processes a single user command and returns Odysseus's textual response.
     * This is the seam the GUI calls: it hands over a raw input line and
     * receives back the text to display, instead of anything being printed.
     *
     * @param input the raw command line entered by the user
     * @return the response text to show
     */
    public String getResponse(String input) {
        try {
            if (input.isBlank()) {
                throw new OdysseusException("Hey! Please enter a command...");
            }
            String[] inputSplit = input.split(" ");
            String command = inputSplit[0];
            String rest = input.substring(command.length()).trim();

            Task toAdd = null;

            switch (Command.fromInput(command)) {

                case Command.BYE -> {
                    this.isExit = true;
                    return BYE_MSG;
                }

                case Command.LIST -> {
                    return formatTasks(tasks.getTasks());
                }

                case Command.MARK -> {
                    int idx = Parser.parseIndex(inputSplit, tasks.size());
                    Task markedTask = tasks.mark(idx);
                    storage.save(tasks.getTasks());
                    String msg = String.format(MARK_MSG, markedTask);
                    return msg;
                }

                case Command.UNMARK -> {
                    int idx = Parser.parseIndex(inputSplit, tasks.size());
                    Task unmarkedTask = tasks.unmark(idx);
                    storage.save(tasks.getTasks());
                    String msg = String.format(UNMARK_MSG, unmarkedTask);
                    return msg;
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
                    return msg;
                }

                case Command.ON -> {
                    if (rest.isEmpty()) {
                        throw new OdysseusException("Hey! The date can't be empty...");
                    }
                    try {
                        LocalDate onDate = LocalDate.parse(rest);
                        return formatTasks(tasks.on(onDate));
                    } catch (DateTimeParseException e) {
                        throw new OdysseusException("Hey! The date can't be parsed..." +
                                "Provide in the form of yyyy-mm-dd");
                    }
                }

                case Command.FIND -> {
                    if (rest.isEmpty()) {
                        throw new OdysseusException("Hey! The keyword to find can't be empty...");
                    }
                    return formatTasks(tasks.find(rest));
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
                return addedMsg;
            }
        } catch (OdysseusException e) {
            return e.getMessage();
        }
        return "";
    }

    /**
     * Returns whether the user has ended the conversation (typed {@code bye}).
     *
     * @return true if the chat session should close
     */
    public boolean isExit() {
        return isExit;
    }

    private String formatTasks(List<Task> list) {
        if (list.isEmpty()) {
            return "No tasks available";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append((i + 1) + ". " + list.get(i) + "\n");
        }
        return sb.toString();
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
