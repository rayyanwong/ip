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
    public static final String DEFAULT_STORAGE = "data/odysseus.txt";
    private static final String ADD_MSG = "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.";
    private static final String UPDATE_MSG = "Updated task %d:%n  %s";

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

            return switch (Command.fromInput(command)) {
                case Command.BYE -> handleBye();
                case Command.LIST -> formatTasks(tasks.getTasks());
                case Command.MARK -> handleMark(inputSplit);
                case Command.UNMARK -> handleUnmark(inputSplit);
                case Command.TODO -> handleAdd(Parser.parseTodo(rest));
                case Command.DEADLINE -> handleAdd(Parser.parseDeadline(rest));
                case Command.EVENT -> handleAdd(Parser.parseEvent(rest));
                case Command.DELETE -> handleDelete(inputSplit);
                case Command.ON -> handleOn(rest);
                case Command.FIND -> handleFind(rest);
                case Command.UPDATE -> handleUpdate(inputSplit, rest);
                case Command.UNKNOWN -> {
                    throw new OdysseusException("Hey! That's not a valid command. Try again.");
                }
            };
        } catch (OdysseusException e) {
            return e.getMessage();
        }
    }

    private String handleBye() {
        this.isExit = true;
        return BYE_MSG;
    }

    private String handleMark(String[] inputSplit) throws OdysseusException {
        int idx = Parser.parseIndex(inputSplit, tasks.size());
        Task markedTask = tasks.mark(idx);
        storage.save(tasks.getTasks());
        return String.format(MARK_MSG, markedTask);
    }

    private String handleUnmark(String[] inputSplit) throws OdysseusException {
        int idx = Parser.parseIndex(inputSplit, tasks.size());
        Task unmarkedTask = tasks.unmark(idx);
        storage.save(tasks.getTasks());
        return String.format(UNMARK_MSG, unmarkedTask);
    }

    private String handleDelete(String[] inputSplit) throws OdysseusException {
        int idx = Parser.parseIndex(inputSplit, tasks.size());
        Task removedTask = tasks.remove(idx);
        storage.save(tasks.getTasks());
        return String.format(
                "Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                removedTask, tasks.size());
    }

    private String handleOn(String rest) throws OdysseusException {
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

    private String handleFind(String rest) throws OdysseusException {
        if (rest.isEmpty()) {
            throw new OdysseusException("Hey! The keyword to find can't be empty...");
        }
        return formatTasks(tasks.find(rest));
    }

    private String handleAdd(Task toAdd) throws OdysseusException {
        tasks.add(toAdd);
        storage.save(tasks.getTasks());
        return String.format(ADD_MSG, toAdd, tasks.size());
    }

    private String handleUpdate(String[] inputSplit, String rest) throws OdysseusException {
        int idx = Parser.parseIndex(inputSplit, tasks.size());   // reuse: validates + throws on bad index
        String[] parts = rest.split(" ", 3);       // [index, flag, value]
        if (parts.length < 3 || parts[2].isBlank()) {
            throw new OdysseusException("Hey! Usage: update INDEX /desc|/by|/from|/to NEW_VALUE...");
        }
        String flag = parts[1];
        String value = parts[2];
        Task updated = tasks.update(idx, flag, value);
        storage.save(tasks.getTasks());
        return String.format(UPDATE_MSG, idx + 1, updated);
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
        new Odysseus(DEFAULT_STORAGE).run();
    }
}
