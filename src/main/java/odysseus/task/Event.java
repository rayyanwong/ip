package odysseus.task;

/** A task that runs from a start to an end time. */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs the Event task.
     *
     * @param description string description of the task
     * @param from string indicating start of the event
     * @param to string indicating end of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * {@inheritDoc}
     *
     * @return the event encoded as a save-file line
     */
    @Override
    public String toSaveFormat() {
        return String.format("%s | %d | %s | %s | %s",
                "E",
                this.isDone ? 1 : 0,
                this.description,
                this.from, this.to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
