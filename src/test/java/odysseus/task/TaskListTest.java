package odysseus.task;

import odysseus.OdysseusException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    public void isEmpty_newList_returnsTrue() {
        TaskList taskList = new TaskList();
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void isEmpty_afterAdd_returnsFalse() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("x"));
        assertFalse(taskList.isEmpty());
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
}
