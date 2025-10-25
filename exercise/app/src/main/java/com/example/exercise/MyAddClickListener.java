package com.example.exercise;

import android.view.View;
import android.widget.TextView;

public class MyAddClickListener implements View.OnClickListener {
    private TextView textView;
    private int delta; // +1 để cộng, -1 để trừ

    public MyAddClickListener(TextView tv, int delta) {
        this.textView = tv;
        this.delta = delta;
    }

    @Override
    public void onClick(View v) {
        int count = Integer.parseInt(textView.getText().toString());
        count += delta;
        textView.setText(String.valueOf(count));
    }
}
