import java.util.Scanner;
/** The main chatbot: reads commands, manages tasks, prints responses */
public class Odysseus {
    private static final String NAME = "Odysseus";

    private static final String MSG_FORMAT = """
        ____________________________________________________________
        %s
        ____________________________________________________________
        """;

    private static final String BYE_MSG = """
        ____________________________________________________________
        Bye. Hope to see you again soon!
        ____________________________________________________________
        """;

    private static final String MARK_MSG = "Nice! I've marked this task as done:%n  %s";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:%n  %s";

    /**
     * Validates and converts a user-entered task number into a 0-based array index.
     *
     * @param parts the whitespace-split user input; index 1 holds the number
     * @param count the current number of tasks (upper bound for the number)
     * @return the 0-based index into the task array
     * @throws OdysseusException if the number is missing, non-numeric, or out of range
     */
    private static int parseIndex(String[] parts, int count) throws OdysseusException {
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean chatting = true;
        Task[] tasks = new Task[100];
        int count = 0;

        String str = String.format("""
           ___      _                             \s
          / _ \\  __| |_   _ ___ ___  ___ _   _ ___\s
         | | | |/ _` | | | / __/ __|/ _ \\ | | / __|
         | |_| | (_| | |_| \\__ \\__ \\  __/ |_| \\__ \\
          \\___/ \\__,_|\\__, |___/___/\\___|\\__,_|___/
                      |___/                       \s
        ____________________________________________________________
        Hello! I'm %s
        What can I do for you?
        ____________________________________________________________
        """, NAME);
        System.out.println(str);

        while (chatting) {
            try {
                String input = scanner.nextLine();
                String[] inputSplit = input.split(" ");
                String command = inputSplit[0];
                String rest = input.substring(command.length()).trim();

                Task toAdd = null;

                switch (command.toLowerCase()) {

                    case "bye" -> {
                        chatting = false;
                        System.out.println(BYE_MSG);
                    }

                    case "list" -> {
                        // list out tasks
                        StringBuilder sb = new StringBuilder();
                        if (count == 0) {
                            System.out.println(String.format(MSG_FORMAT, "No tasks available"));
                        } else {
                            for (int i = 0; i < count; i++) {
                                sb.append((i + 1) + ". " + tasks[i].toString() + "\n");
                            }
                            System.out.println(String.format(MSG_FORMAT, sb.toString()));
                        }
                    }

                    case "mark" -> {
                        int idx = parseIndex(inputSplit, count);
                        tasks[idx].markAsDone();
                        String msg = String.format(MARK_MSG, tasks[idx]);
                        System.out.println(String.format(MSG_FORMAT, msg));
                    }

                    case "unmark" -> {
                        int idx = parseIndex(inputSplit, count);
                        tasks[idx].markAsUndone();
                        String msg = String.format(UNMARK_MSG, tasks[idx]);
                        System.out.println(String.format(MSG_FORMAT, msg));
                    }

                    case "todo" -> {
                        if (rest.isEmpty()) {
                            throw new OdysseusException("Hey! The description can't be empty...");
                        }
                        toAdd = new Todo(rest);
                        tasks[count++] = toAdd;
                    }

                    case "deadline" -> {
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
                        toAdd = new Deadline(parts[0], parts[1]);
                        tasks[count++] = toAdd;
                    }

                    case "event" -> {
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
                        toAdd = new Event(fromParts[0], toParts[0], toParts[1]);
                        tasks[count++] = toAdd;
                    }

                    default -> {
                        // Unknown command
                        throw new OdysseusException("Hey! That's not a valid command. Try again.");
                    }
                }
                if (toAdd != null) {
                    String addedMsg = String.format(
                            "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                            toAdd, count);
                    System.out.println(String.format(MSG_FORMAT, addedMsg));
                }
            } catch (OdysseusException e) {
               System.out.println(String.format(MSG_FORMAT, e.getMessage()));
            }
        }
    }
}
