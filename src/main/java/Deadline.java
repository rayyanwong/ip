/** A task that must be done by a specific date/time */
public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String toSaveFormat() {
        return String.format("%s | %d | %s | %s",
                "D",
                this.isDone ? 1 : 0,
                this.description,
                this.by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}
