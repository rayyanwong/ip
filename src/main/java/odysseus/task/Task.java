package odysseus.task;

import java.time.LocalDate;

/**
 * A task with a description and done status (the base type)
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    public abstract String toSaveFormat();

    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns whether task description contains keyword.
     *
     * @param keyword string to check description against
     * @return whether string in task description
     */
    public boolean descriptionContains(String keyword) {
        return this.description.toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",
                this.getStatusIcon(),
                this.description);
    }
}
