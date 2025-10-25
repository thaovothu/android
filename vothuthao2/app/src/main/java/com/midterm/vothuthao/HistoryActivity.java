package com.midterm.vothuthao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryUpdateListener {
    private RecyclerView recyclerView;
    private Button btnBack;
    private HistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<BorrowHistory> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Thiết lập action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử mượn sách");
        }

        initViews();
        setupRecyclerView();
        loadHistory();
        
        // Click listener cho nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onHistoryUpdated() {
        // Reload lại danh sách khi có thay đổi
        loadHistory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_history);
        btnBack = findViewById(R.id.btn_back);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = dbHelper.getBorrowHistory();
        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);
    }

    private void loadHistory() {
        historyList = dbHelper.getBorrowHistory();
        adapter.updateHistoryList(historyList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}