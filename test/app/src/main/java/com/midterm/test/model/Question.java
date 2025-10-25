package com.midterm.test.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo()
    public String text;
    @ColumnInfo()
    public boolean correctIsTrue;

    public Question(String text, boolean correctIsTrue) {
        this.text = text;
        this.correctIsTrue = correctIsTrue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrectIsTrue() {
        return correctIsTrue;
    }

    public void setCorrectIsTrue(boolean correctIsTrue) {
        this.correctIsTrue = correctIsTrue;
    }
}
