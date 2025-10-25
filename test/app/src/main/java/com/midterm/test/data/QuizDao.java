package com.midterm.test.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.midterm.test.model.Question;
import com.midterm.test.model.Answer;

import java.util.List;

@Dao
public interface QuizDao {
    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();

    @Insert
    void insertQuestions(Question... qs);

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    Question findQuestionById(int id);

    @Insert
    long insertAnswer(Answer a);

    @Update
    void updateAnswer(Answer a);

    @Query("SELECT * FROM answers ORDER BY timestamp DESC")
    List<Answer> getAllAnswers();

    @Query("SELECT * FROM answers WHERE questionId = :qid LIMIT 1")
    Answer findAnswerByQuestionId(int qid);

    @Query("DELETE FROM answers")
    void clearAnswers();
}
