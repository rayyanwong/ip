package odysseus.parser;

import odysseus.OdysseusException;
import odysseus.task.Deadline;
import odysseus.task.Event;
import odysseus.task.Todo;

public class Parser {

    private Parser() {
    }

    /**
     * Validates and converts a user-entered task number into a 0-based array index.
     *
     * @param parts the whitespace-split user input; index 1 holds the number
     * @param count the current number of tasks (upper bound for the number)
     * @return the 0-based index into the task array
     * @throws OdysseusException if the number is missing, non-numeric, or out of range
     */
    public static int parseIndex(String[] parts, int count) throws OdysseusException {
        if (parts.length < 2) {
            throw new OdysseusException("Provide a task number...");
        }
        int n;
        try {
            n = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new OdysseusException("Hey! The task number must be a number...");
        }
        if (n < 1 || n > count) {
            throw new OdysseusException("There's no task numbered " + n + "...");
        }
        return n - 1;
    }

    /**
     * Builds a odysseus.task.Todo from the argument text.
     */
    public static Todo parseTodo(String rest) throws OdysseusException {
        if (rest.isEmpty()) {
            throw new OdysseusException("Hey! The description can't be empty...");
        }
        return new Todo(rest);
    }

    /**
     * Builds a odysseus.task.Deadline from "<desc> /by <date>".
     */
    public static Deadline parseDeadline(String rest) throws OdysseusException {
        String[] parts = rest.split(" /by ");
        if (parts.length < 2) {
            throw new OdysseusException("Hey! A deadline needs a /by time...");
        }
        if (parts[0].isEmpty()) {
            throw new OdysseusException("Hey! The description can't be empty...");
        }
        if (parts[1].isEmpty()) {
            throw new OdysseusException("Hey! The deadline can't be empty...");
        }
        return new Deadline(parts[0], parts[1]);
    }

    /**
     * Builds an odysseus.task.Event from "<desc> /from <start> /to <end>".
     */
    public static Event parseEvent(String rest) throws OdysseusException {
        String[] fromParts = rest.split(" /from ");
        if (fromParts.length < 2) {
            throw new OdysseusException("Hey! An event needs a /from start time...");
        }
        String[] toParts = fromParts[1].split(" /to ");
        if (fromParts[0].isEmpty()) {
            throw new OdysseusException("Hey! The description can't be empty...");
        }

        if (toParts.length < 2) {
            throw new OdysseusException("Hey! An event needs a /to end time...");
        }

        if (toParts[0].isEmpty() || toParts[1].isEmpty()) {
            throw new OdysseusException("Hey! The end time can't be empty...");
        }
        return new Event(fromParts[0], toParts[0], toParts[1]);
    }
}