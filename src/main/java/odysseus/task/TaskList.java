package odysseus.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {

    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = initial;
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public Task remove(int idx) {
        return this.tasks.remove(idx);
    }

    public int size() {
        return this.tasks.size();
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    public Task mark(int idx) {
        Task task = this.tasks.get(idx);
        task.markAsDone();
        return task;
    }

    public Task unmark(int idx) {
        Task task = this.tasks.get(idx);
        task.markAsUndone();
        return task;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public List<Task> on(LocalDate date) {
        List<Task> res = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                res.add(task);
            }
        }
        return res;
    }

    /**
     * Returns task list with keyword in description.
     *
     * @param keyword string to check description against
     * @return list of tasks with description containing string
     */
    public List<Task> find(String keyword) {
        List<Task> res = new ArrayList<>();
        for (Task task : tasks) {
            if (task.descriptionContains(keyword)) {
                res.add(task);
            }
        }
        return res;
    }

}
