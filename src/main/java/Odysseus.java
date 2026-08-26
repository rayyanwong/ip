import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/** The main chatbot: reads commands, manages tasks, prints responses */
public class Odysseus {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    private static final String MARK_MSG = "Nice! I've marked this task as done:%n  %s";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:%n  %s";

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

                    case BYE -> {
                        chatting = false;
                        ui.showGoodbye();
                    }

                    case LIST -> {
                        // list out tasks
                        ui.showTasks(tasks.getTasks());
                    }

                    case MARK -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task markedTask = tasks.mark(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(MARK_MSG, markedTask);
                        ui.show(msg);
                    }

                    case UNMARK -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task unmarkedTask = tasks.unmark(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(UNMARK_MSG, unmarkedTask);
                        ui.show(msg);
                    }

                    case TODO -> {
                        toAdd = Parser.parseTodo(rest);
                    }

                    case DEADLINE -> {
                        toAdd = Parser.parseDeadline(rest);
                    }

                    case EVENT -> {
                        toAdd = Parser.parseEvent(rest);
                    }

                    case DELETE -> {
                        int idx = Parser.parseIndex(inputSplit, tasks.size());
                        Task removedTask = tasks.remove(idx);
                        storage.save(tasks.getTasks());
                        String msg = String.format(
                                "Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                                removedTask, tasks.size());
                        ui.show(msg);
                    }

                    case ON -> {
                        if (rest.isEmpty()) {
                            throw new OdysseusException("Hey! The date can't be empty...");
                        }
                        String onDateStr = rest;
                        try {
                            LocalDate onDate = LocalDate.parse(onDateStr);
                            ui.showTasks(tasks.on(onDate));
                        } catch (DateTimeParseException e) {
                            throw new OdysseusException("Hey! The date can't be parsed..." +
                                    "Provide in the form of yyyy-mm-dd");
                        }
                    }

                    case UNKNOWN -> {
                        // Unknown command
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

    public static void main(String[] args) {
        new Odysseus("data/odysseus.txt").run();
    }
}
