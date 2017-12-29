package com.example.memo;

/**
 * Created by James on 16/11/2016.
 */

// James Michael 788456

/* This class is used to define a Memo object.
 * Each memo consists of an id, a subject, description and importance level.
 */
public class Memo {
    private int memoNumber;
    private String subject;
    private String description;
    private int importance;
    private final int IMPORTANCE_NORMAL = 1;
    private final int IMPORTANCE_IMPORTANT = 2;
    private final int IMPORTANCE_URGENT = 3;

    // The constructor takes four parameters for each piece of Memo data.
    public Memo(int id, String subject, String description, int importance) {
        memoNumber = id;
        this.subject = subject;
        this.description = description;
        this.importance = importance;
    }

    /* Accessor methods for getting memo number, subject, description and
     * importance level.
     */
    public int getMemoNumber() {
        return memoNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    /* This method determines the importance level based on the number that was
     * passed through, and returns a String which states the importance.
     */
    public String getImportance() {
        if (importance == IMPORTANCE_NORMAL) {
            return "Normal";
        }
        else if (importance == IMPORTANCE_IMPORTANT) {
            return "Important";
        }
        else if (importance == IMPORTANCE_URGENT) {
            return "Urgent";
        }
        return "Error";
    }

}
