package com.example.exercise;

import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView tvCount, tvCount2,tvCount3;

    private FloatingActionButton btnAdd, btnAdd2, btnAdd3;

    private FloatingActionButton btnSub, btnSub2,btnSub3;

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

        tvCount = findViewById(R.id.tv_count);
        tvCount2 = findViewById(R.id.tv_count2);
        tvCount3 = findViewById(R.id.tv_count3);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd2 = findViewById(R.id.btn_add2);
        btnAdd3 = findViewById(R.id.btn_add3);
        btnSub = findViewById(R.id.btn_sub);
        btnSub2 = findViewById(R.id.btn_sub2);
        btnSub3 = findViewById(R.id.btn_sub3);


        btnAdd3.setOnClickListener(new MyAddClickListener(tvCount3, 1));
        btnSub3.setOnClickListener(new MyAddClickListener(tvCount3, -1));

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = Integer.parseInt(tvCount.getText().toString());
                tvCount.setText("" + ++count);
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int count = Integer.parseInt(tvCount.getText().toString());
                tvCount.setText("" + --count);
            }
        });
    }


    public void addCount2(View view) {
        int count = Integer.parseInt(tvCount2.getText().toString());
        tvCount2.setText("" + ++count);
    }

    public void subCount2(View view) {
        int count = Integer.parseInt(tvCount2.getText().toString());
        tvCount2.setText("" + --count);
    }




}
