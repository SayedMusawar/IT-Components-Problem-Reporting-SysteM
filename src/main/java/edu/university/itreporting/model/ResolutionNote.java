package edu.university.itreporting.model;

import java.time.LocalDate;
import java.util.Objects;

public class ResolutionNote {
    private final int noteId;
    private final String text;
    private final LocalDate date;
    private final ITStaff author;

    public ResolutionNote(int noteId, String text, LocalDate date, ITStaff author) {
        this.noteId = noteId;
        this.text = User.requireText(text, "note text");
        this.date = Objects.requireNonNull(date, "date is required.");
        this.author = author;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public ITStaff getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        String authorName = author == null ? "System" : author.getName();
        return "Note #" + noteId + " by " + authorName + " on " + date + ": " + text;
    }
}
