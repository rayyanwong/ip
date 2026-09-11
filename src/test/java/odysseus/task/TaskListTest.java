package odysseus.task;

import odysseus.OdysseusException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {
    @Test
    public void add_newTask_increasesSize() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void remove_validIndex_returnsRemovedTaskAndShrinksList() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        Task removedTask = taskList.remove(0);
        assertEquals(0, taskList.getTasks().size());
        assertEquals("T | 0 | x", removedTask.toSaveFormat());
    }

    @Test
    public void mark_validIndex_marksTaskDone() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        Task markedTask = taskList.mark(0);
        assertEquals("T | 1 | x", markedTask.toSaveFormat());
    }

    @Test
    public void unmark_validIndex_marksTaskUndone() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        taskList.mark(0);
        Task unmarkedTask = taskList.unmark(0);
        assertEquals("T | 0 | x", unmarkedTask.toSaveFormat());
    }

    @Test
    public void on_matchingDate_returnsMatchingTasks() throws OdysseusException {
        TaskList taskList = new TaskList();
        taskList.add(new Deadline("x", "2026-06-06"));
        taskList.add(new Deadline("x", "2026-06-07"));
        taskList.add(new Todo("x"));
        assertEquals(1,
                taskList.on(LocalDate.of(2026, 6, 6)).size());
    }

    @Test
    public void update_desc_changesDescription() throws OdysseusException {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("old"));
        Task updated = taskList.update(0, "/desc", "new");
        assertEquals("[T][ ] new", updated.toString());
    }

    @Test
    public void update_deadlineBy_changesDate() throws OdysseusException {
        TaskList taskList = new TaskList();
        taskList.add(new Deadline("submit", "2026-06-06"));
        Task updated = taskList.update(0, "/by", "2026-12-25");
        assertEquals("D | 0 | submit | 2026-12-25", updated.toSaveFormat());
    }

    @Test
    public void update_flagNotApplicableToType_throws() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        assertThrows(OdysseusException.class, () -> taskList.update(0, "/by", "2026-01-01"));
    }

}
