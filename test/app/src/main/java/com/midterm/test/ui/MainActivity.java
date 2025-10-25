package com.midterm.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.test.R;
import com.midterm.test.data.AppDatabase;
import com.midterm.test.data.QuizDao;
import com.midterm.test.model.Question;
import com.midterm.test.model.Answer;
import com.midterm.test.ui.DetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DETAILS = 1000;
    private static final int QUIZ_LIMIT = 10; // giới hạn số câu trong quiz (thay đổi được)

    private TextView tvIndex, tvQuestion;
    private Button btnTrue, btnFalse, btnPrev, btnNext, btnSubmit;


    private List<Question> allQuestions = new ArrayList<>();
    private List<Question> quizQuestions = new ArrayList<>();
    private int currentIndex = 0;

    // lưu tạm câu trả lời: questionId -> Boolean
    private final List<Answer> answersCache = new ArrayList<>();

    private QuizDao dao;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // save keys
    private static final String KEY_INDEX = "key_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        tvIndex = findViewById(R.id.tv_index);
        tvQuestion = findViewById(R.id.tv_question);
        btnTrue = findViewById(R.id.btn_true);
        btnFalse = findViewById(R.id.btn_false);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnSubmit = findViewById(R.id.btn_submit);


        dao = AppDatabase.getInstance(this).quizDao();

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        seedQuestionsIfNeededAndLoad();

        btnTrue.setOnClickListener(v -> {
            selectAnswer(true);
        });

        btnFalse.setOnClickListener(v -> {
            selectAnswer(false);
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < quizQuestions.size() - 1) {
                showQuestion(currentIndex + 1);
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                showQuestion(currentIndex - 1);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            int correctCount = 0;
            StringBuilder summary = new StringBuilder();

            for (Answer ans : answersCache) {
                Question q = findQuestionById(ans.questionId);
                if (q != null) {
                    boolean isCorrect = (q.correctIsTrue == ans.selectedTrue);
                    if (isCorrect) correctCount++;

                    summary.append("Q: ").append(q.text).append("\n")
                            .append("Your answer: ").append(ans.selectedTrue ? "True" : "False").append("\n")
                            .append("Correct answer: ").append(q.correctIsTrue ? "True" : "False").append("\n")
                            .append(isCorrect ? "✅ Correct\n\n" : "❌ Wrong\n\n");
                }
            }

            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("total", quizQuestions.size());
            intent.putExtra("correct", correctCount);
            intent.putExtra("summary", summary.toString());
            startActivity(intent);
        });




    }

    private Question findQuestionById(int qid) {
        for (Question q : quizQuestions) {
            if (q.id == qid) return q;
        }
        return null;
    }


    private void seedQuestionsIfNeededAndLoad() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Question> dbQuestions = dao.getAllQuestions();
            if (dbQuestions == null || dbQuestions.isEmpty()) {
                // seed sample questions
                dao.insertQuestions(
                        new Question("Android is based on Linux kernel.", true),
                        new Question("Activity runs on UI thread.", true),
                        new Question("You must always run Room queries on main thread.", false),
                        new Question("Intent is used for communication between components.", true),
                        new Question("Service has a UI.", false),
                        new Question("RecyclerView is used to display lists.", true),
                        new Question("You can do long network on UI thread.", false),
                        new Question("Room is an ORM for SQLite.", true),
                        new Question("ViewModel is tightly coupled to Activity lifecycle and is destroyed on rotation automatically.", false),
                        new Question("An AsyncTask runs indefinitely.", false),
                        new Question("ConstraintLayout helps build responsive UIs.", true),
                        new Question("BroadcastReceiver cannot receive system broadcasts.", false)
                );
                dbQuestions = dao.getAllQuestions();
            }

            allQuestions.clear();
            allQuestions.addAll(dbQuestions);

            // randomize and limit
            Collections.shuffle(allQuestions);
            int take = Math.min(QUIZ_LIMIT, allQuestions.size());
            quizQuestions = new ArrayList<>(allQuestions.subList(0, take));

            // load any existing answers to answersCache
            answersCache.clear();
            List<Answer> prevAnswers = dao.getAllAnswers();
            if (prevAnswers != null) answersCache.addAll(prevAnswers);

            // show first or restored index on UI thread
            mainHandler.post(() -> showQuestion(currentIndex));
        });
    }

    private void showQuestion(int index) {
        if (quizQuestions == null || quizQuestions.isEmpty()) return;
        currentIndex = index;
        Question q = quizQuestions.get(index);
        tvIndex.setText((index + 1) + " / " + quizQuestions.size());
        tvQuestion.setText(q.text);

        // reset button visuals
        btnTrue.setBackgroundColor(getResources().getColor(R.color.unselected));
        btnFalse.setBackgroundColor(getResources().getColor(R.color.unselected));

        // check if existing answer in answersCache
        Answer saved = findAnswerInCache(q.id);
        if (saved != null) {
            highlight(saved.selectedTrue);
        }
    }

    private Answer findAnswerInCache(int questionId) {
        for (Answer a : answersCache) {
            if (a.questionId == questionId) return a;
        }
        return null;
    }

    private void selectAnswer(boolean isTrue) {
        // visual feedback
        highlight(isTrue);

        // save to DB (background)
        Question q = quizQuestions.get(currentIndex);
        Executors.newSingleThreadExecutor().execute(() -> {
            Answer existing = dao.findAnswerByQuestionId(q.id);
            if (existing == null) {
                Answer a = new Answer(q.id, isTrue, System.currentTimeMillis());
                dao.insertAnswer(a);
                answersCache.add(a);
            } else {
                existing.selectedTrue = isTrue;
                existing.timestamp = System.currentTimeMillis();
                dao.updateAnswer(existing);
                // update cache
                for (int i = 0; i < answersCache.size(); i++) {
                    if (answersCache.get(i).questionId == existing.questionId) {
                        answersCache.set(i, existing);
                        break;
                    }
                }
            }
        });
    }

    private void highlight(boolean isTrue) {
        int selColor = getResources().getColor(R.color.selected);
        int unselColor = getResources().getColor(R.color.unselected);
        if (isTrue) {
            btnTrue.setBackgroundColor(selColor);
            btnFalse.setBackgroundColor(unselColor);
        } else {
            btnFalse.setBackgroundColor(selColor);
            btnTrue.setBackgroundColor(unselColor);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAILS && resultCode == Activity.RESULT_OK && data != null) {
            int jumpQid = data.getIntExtra("jumpQuestionId", -1);
            if (jumpQid != -1) {
                // find index in quizQuestions
                for (int i = 0; i < quizQuestions.size(); i++) {
                    if (quizQuestions.get(i).id == jumpQid) {
                        showQuestion(i);
                        break;
                    }
                }
            }
        }
    }
}
