package com.example.twonumber;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twonumber.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etNumber1, etNumber2;
    Button btnAdd, btnSub, btnMul, btnDiv;
    ListView listHistory;

    ArrayList<String> historyList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        etNumber1 = findViewById(R.id.etNumber1);
        etNumber2 = findViewById(R.id.etNumber2);
        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);
        listHistory = findViewById(R.id.listHistory);

        // Khởi tạo lịch sử
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        listHistory.setAdapter(adapter);

        // Xử lý sự kiện nút bấm
        btnAdd.setOnClickListener(v -> calculate('+'));
        btnSub.setOnClickListener(v -> calculate('-'));
        btnMul.setOnClickListener(v -> calculate('*'));
        btnDiv.setOnClickListener(v -> calculate('/'));
    }

    private void calculate(char operator) {
        String aStr = etNumber1.getText().toString().trim();
        String bStr = etNumber2.getText().toString().trim();

        if (aStr.isEmpty() || bStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cả a và b", Toast.LENGTH_SHORT).show();
            return;
        }

        double a = Double.parseDouble(aStr);
        double b = Double.parseDouble(bStr);
        double result = 0;

        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                if (b == 0) {
                    Toast.makeText(this, "Không thể chia cho 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                result = a / b;
                break;
        }

        String historyItem = a + " " + operator + " " + b + " = " + result;
        historyList.add(historyItem);
        adapter.notifyDataSetChanged();
    }
}
