package com.midterm.test.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.midterm.test.R;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvSummary = findViewById(R.id.tv_summary);
        TextView tvResult = findViewById(R.id.tv_result);

        int total = getIntent().getIntExtra("total", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        String summary = getIntent().getStringExtra("summary");

        tvSummary.setText(summary);
        tvResult.setText("Score: " + correct + " / " + total);
    }
}
