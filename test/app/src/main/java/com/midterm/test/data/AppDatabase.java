package com.midterm.test.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.midterm.test.model.Question;
import com.midterm.test.model.Answer;

@Database(entities = {Question.class, Answer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuizDao quizDao();

    private static AppDatabase instance;
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "midterm_db").build();
        }
        return instance;
    }
}
