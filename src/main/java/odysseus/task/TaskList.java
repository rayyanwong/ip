package odysseus.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles all TaskList operations: modifying of list, task status
 * and checks on task.
 */
public class TaskList {

    private final List<Task> tasks;

    /**
     * Creates a tasklist object with empty list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a tasklist object with an initial task list.
     *
     * @param initial the initial list of tasks to populate this list with
     */
    public TaskList(List<Task> initial) {
        this.tasks = initial;
    }

    /**
     * Adds task into tasklist.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Returns a removed task.
     *
     * @param idx index of task to remove using 0-base indexing
     * @return removed task
     */
    public Task remove(int idx) {
        return this.tasks.remove(idx);
    }

    /**
     * Returns size of tasklist.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns task marked as done.
     *
     * @param idx index of task using 0-base indexing
     * @return task marked as done
     */
    public Task mark(int idx) {
        Task task = this.tasks.get(idx);
        task.markAsDone();
        return task;
    }

    /**
     * Returns task marked as undone.
     *
     * @param idx index of task using 0-base indexing
     * @return task marked as undone
     */
    public Task unmark(int idx) {
        Task task = this.tasks.get(idx);
        task.markAsUndone();
        return task;
    }

    /**
     * Returns unmodifiable list of tasks.
     *
     * @return list of tasks
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns list of task due on a date.
     *
     * @param date the date the task is due on
     * @return list of tasks that occurs on given date
     */
    public List<Task> on(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.occursOn(date))
                .toList();
    }

    /**
     * Returns task list with keyword in description.
     *
     * @param keyword string to check description against
     * @return list of tasks with description containing string
     */
    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.descriptionContains(keyword))
                .toList();
    }

}
