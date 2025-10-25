package com.midterm.vothuthao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnHistory;
    private BookAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Thiết lập Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thư viện sách");
        }

        initViews();
        setupRecyclerView();
        loadBooks();
        
        // Click listener cho nút lịch sử
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload danh sách khi quay lại từ màn hình khác
        loadBooks();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        btnHistory = findViewById(R.id.btn_history);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookList = dbHelper.getAllBooks();
        adapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(adapter);
    }

    private void loadBooks() {
        bookList = dbHelper.getAllBooks();
        adapter.updateBookList(bookList);
    }
}