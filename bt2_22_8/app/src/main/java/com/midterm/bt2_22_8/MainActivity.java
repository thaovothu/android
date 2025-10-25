package com.midterm.bt2_22_8;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editA;
    private EditText editB;
    private ListView listResult;
    private ArrayList<String> results;
    private ArrayAdapter<String> adapter;

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

        editA = findViewById(R.id.edit_a);
        editB = findViewById(R.id.edit_b);
        Button btnAdd = findViewById(R.id.btn_add);
        Button btnSub = findViewById(R.id.btn_sub);
        Button btnMul = findViewById(R.id.btn_mul);
        Button btnDiv = findViewById(R.id.btn_div);
        listResult = findViewById(R.id.list_result);

        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listResult.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> calculate('+'));
        btnSub.setOnClickListener(v -> calculate('-'));
        btnMul.setOnClickListener(v -> calculate('*'));
        btnDiv.setOnClickListener(v -> calculate('/'));
    }

    private void calculate(char operator) {
        try {
            int a = Integer.parseInt(editA.getText().toString());
            int b = Integer.parseInt(editB.getText().toString());
            int result = 0;
            String operation = "";

            switch (operator) {
                case '+':
                    result = a + b;
                    operation = a + " + " + b + " = " + result;
                    break;
                case '-':
                    result = a - b;
                    operation = a + " - " + b + " = " + result;
                    break;
                case '*':
                    result = a * b;
                    operation = a + " * " + b + " = " + result;
                    break;
                case '/':
                    if (b == 0) {
                        Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    result = a / b;
                    operation = a + " / " + b + " = " + result;
                    break;
            }
            results.add(0, operation);
            adapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
