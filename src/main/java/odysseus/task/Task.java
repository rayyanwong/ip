package odysseus.task;

import java.time.LocalDate;

/** A task with a description and done status (the base type). */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task, initially marked as undone.
     *
     * @param description string representing description of task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns status of task depending on isDone. */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Marks task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks task as undone. */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Returns the string of a task in format to be saved in.
     *
     * @return string of task to be written to file
     */
    public abstract String toSaveFormat();

    /**
     * Returns whether a task occurs on a given date.
     *
     * @param date LocalDate of the event to be checked against
     * @return whether task's deadline occurs on given date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",
                this.getStatusIcon(),
                this.description);
    }
}
