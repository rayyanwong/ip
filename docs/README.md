# Odysseus User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Updating a task

Edits a field of an existing task in place, without deleting and re-adding it.

Format: `update INDEX /FLAG NEW_VALUE`

- `/desc` — change the description (any task type)
- `/by` — change a deadline's date (Deadline only, `YYYY-MM-DD`)
- `/from`, `/to` — change an event's start/end (Event only)

Example: `update 2 /desc buy oat milk`

```
Updated task 2:
  [D][ ] buy oat milk (by: Jun 6 2026)
```


## Feature XYZ

// Feature details