import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** A task that must be done by a specific date/time */
public class Deadline extends Task {
    protected LocalDate by;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, String by) throws OdysseusException {
        super(description);
        try {
            this.by = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new OdysseusException("Invalid by input, please give in the format of YYYY-MM-DD...");
        }
    }

    @Override
    public String toSaveFormat() {
        return String.format("%s | %d | %s | %s",
                "D",
                this.isDone ? 1 : 0,
                this.description,
                this.by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() +
                " (by: " + this.by.format(DISPLAY_FORMAT) + ")";
    }
}
