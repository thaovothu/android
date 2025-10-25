package com.midterm.test.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "answers")
public class Answer {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public int questionId;
    @ColumnInfo
    public boolean selectedTrue;
    @ColumnInfo
    public long timestamp;

    public Answer(int questionId, boolean selectedTrue, long timestamp) {
        this.questionId = questionId;
        this.selectedTrue = selectedTrue;
        this.timestamp = timestamp;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isSelectedTrue() {
        return selectedTrue;
    }

    public void setSelectedTrue(boolean selectedTrue) {
        this.selectedTrue = selectedTrue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
