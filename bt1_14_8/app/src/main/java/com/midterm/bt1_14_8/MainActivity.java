package com.midterm.bt1_14_8;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView tvCount;
    private FloatingActionButton btAdd;
    private FloatingActionButton btSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btAdd = findViewById(R.id.bt_add);
        tvCount = findViewById(R.id.tv_count);
        btSub = findViewById (R.id.bt_sub);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int count = Integer.parseInt(tvCount.getText().toString());
                tvCount.setText("" + ++count);
            }
        });

        btSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int count = Integer.parseInt(tvCount.getText().toString());
                tvCount.setText("" + --count);
            }
        });
    }
}