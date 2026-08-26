package odysseus.task;

import odysseus.OdysseusException;

public class TaskFactory {

    private TaskFactory() {}

    /**
     * Rebuilds a odysseus.task.Task from one saved line, e.g. "D | 1 | return book | June 6th".
     * @param line one line from the save file
     * @return the reconstructed odysseus.task.Todo/odysseus.task.Deadline/odysseus.task.Event
     */
    public static Task fromSaveFormat(String line) throws OdysseusException {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new OdysseusException("Corrupted save format...Please verify line: " + line);
        }
        String marker = parts[0].strip();
        boolean isDone =  parts[1].strip().equals("1");

        Task task;
        switch (marker) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new OdysseusException("Corrupted save format for odysseus.task.Deadline..." +
                            "Please verify line: " + line);
                }
                task = new Deadline(parts[2], parts[3]);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new OdysseusException("Corrupted save format for odysseus.task.Event..." +
                            "Please verify line: " + line);
                }
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                throw new OdysseusException("Line corrupted, invalid marker");
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}