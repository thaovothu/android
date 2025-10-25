package com.midterm.vothuthao;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddBookActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etYear, etPages, etGenre;
    private FloatingActionButton fabSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Thiết lập action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thêm sách mới");
        }

        initViews();
        dbHelper = new DatabaseHelper(this);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etYear = findViewById(R.id.et_year);
        etPages = findViewById(R.id.et_pages);
        etGenre = findViewById(R.id.et_genre);
        fabSave = findViewById(R.id.fab_save);
    }

    private void addBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String pagesStr = etPages.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || 
            pagesStr.isEmpty() || genre.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            int pages = Integer.parseInt(pagesStr);
            
            if (year < 1000 || year > 2030) {
                Toast.makeText(this, "Năm xuất bản không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (pages <= 0) {
                Toast.makeText(this, "Số trang phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Book book = new Book(title, author, year, pages, genre, Book.STATUS_AVAILABLE);
            long id = dbHelper.addBook(book);

            if (id > 0) {
                Toast.makeText(this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                finish(); // Quay về màn hình chính
            } else {
                Toast.makeText(this, "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Năm xuất bản và số trang phải là số", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}