package com.midterm.vothuthao;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle, tvAuthor, tvYear, tvPages, tvGenre, tvStatus;
    private TextView tvBorrowInfo;
    private Button btnBorrow, btnReturn, btnExtend, btnBack;
    private DatabaseHelper dbHelper;
    private Book currentBook;
    private BorrowHistory currentBorrowHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();
        dbHelper = new DatabaseHelper(this);

        int bookId = getIntent().getIntExtra("BOOK_ID", -1);
        if (bookId != -1) {
            loadBookDetails(bookId);
        }

        setupClickListeners();
    }

    private void initViews() {
        ivBookCover = findViewById(R.id.iv_book_cover_detail);
        tvTitle = findViewById(R.id.tv_title_detail);
        tvAuthor = findViewById(R.id.tv_author_detail);
        tvYear = findViewById(R.id.tv_year_detail);
        tvPages = findViewById(R.id.tv_pages_detail);
        tvGenre = findViewById(R.id.tv_genre_detail);
        tvStatus = findViewById(R.id.tv_status_detail);
        tvBorrowInfo = findViewById(R.id.tv_borrow_info);
        btnBorrow = findViewById(R.id.btn_borrow);
        btnReturn = findViewById(R.id.btn_return);
        btnExtend = findViewById(R.id.btn_extend);
        btnBack = findViewById(R.id.btn_back);
    }

    private void loadBookDetails(int bookId) {
        currentBook = dbHelper.getBookById(bookId);
        if (currentBook != null) {
            displayBookDetails();
            updateButtonVisibility();
            loadBorrowInfo();
        }
    }

    private void displayBookDetails() {
        tvTitle.setText(currentBook.getTitle());
        tvAuthor.setText("Tác giả: " + currentBook.getAuthor());
        tvYear.setText("Năm xuất bản: " + currentBook.getPublishYear());
        tvPages.setText("Số trang: " + currentBook.getPageCount());
        tvGenre.setText("Thể loại: " + currentBook.getGenre());
        tvStatus.setText("Tình trạng: " + currentBook.getStatus());
        
        // Hiển thị ảnh bìa từ drawable
        if (currentBook.getCoverImage() != null && !currentBook.getCoverImage().isEmpty()) {
            // Lấy resource ID từ tên drawable
            int resourceId = getResources().getIdentifier(
                currentBook.getCoverImage(), 
                "drawable", 
                getPackageName()
            );
            if (resourceId != 0) {
                ivBookCover.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy resource, dùng ảnh mặc định
                ivBookCover.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            // Hiển thị ảnh mặc định nếu không có cover_image
            ivBookCover.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        
        // Set màu cho trạng thái
        switch (currentBook.getStatus()) {
            case Book.STATUS_AVAILABLE:
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case Book.STATUS_BORROWED:
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            default:
                tvStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
                break;
        }
    }

    private void updateButtonVisibility() {
        String status = currentBook.getStatus();
        
        // Chỉ hiển thị nút mượn sách khi sách có sẵn
        btnBorrow.setVisibility(status.equals(Book.STATUS_AVAILABLE) ? View.VISIBLE : View.GONE);
        
        // Ẩn nút trả sách và gia hạn (chỉ có trong lịch sử)
        btnReturn.setVisibility(View.GONE);
        btnExtend.setVisibility(View.GONE);
    }

    private void loadBorrowInfo() {
        if (currentBook.getStatus().equals(Book.STATUS_BORROWED)) {
            currentBorrowHistory = dbHelper.getCurrentBorrowInfo(currentBook.getId());
            if (currentBorrowHistory != null) {
                String borrowInfo = "Ngày mượn: " + currentBorrowHistory.getBorrowDate() + 
                                   "\nNgày phải trả: " + currentBorrowHistory.getDueDate();
                if (currentBorrowHistory.getExtensionDays() > 0) {
                    borrowInfo += "\nĐã gia hạn: " + currentBorrowHistory.getExtensionDays() + " ngày";
                }
                tvBorrowInfo.setText(borrowInfo);
                tvBorrowInfo.setVisibility(View.VISIBLE);
            }
        } else {
            tvBorrowInfo.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        btnBorrow.setOnClickListener(v -> borrowBook());
        btnBack.setOnClickListener(v -> finish());
        
        // Không cần thiết lập listener cho nút trả sách và gia hạn
        // vì chúng sẽ bị ẩn và chỉ sử dụng trong lịch sử
    }

    private void borrowBook() {
        // Giả sử user ID = 1 (trong thực tế cần có hệ thống đăng nhập)
        long result = dbHelper.borrowBook(currentBook.getId(), 1);
        
        if (result > 0) {
            Toast.makeText(this, "Mượn sách thành công!", Toast.LENGTH_SHORT).show();
            currentBook.setStatus(Book.STATUS_BORROWED);
            displayBookDetails();
            updateButtonVisibility();
            loadBorrowInfo();
        } else {
            Toast.makeText(this, "Mượn sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnBook() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn trả sách này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    boolean result = dbHelper.returnBook(currentBook.getId());
                    
                    if (result) {
                        Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        currentBook.setStatus(Book.STATUS_RETURNED);
                        displayBookDetails();
                        updateButtonVisibility();
                        loadBorrowInfo();
                    } else {
                        Toast.makeText(this, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showExtendDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_extend, null);
        EditText etDays = dialogView.findViewById(R.id.et_extend_days);
        
        new AlertDialog.Builder(this)
                .setTitle("Gia hạn mượn sách")
                .setView(dialogView)
                .setPositiveButton("Gia hạn", (dialog, which) -> {
                    String daysStr = etDays.getText().toString().trim();
                    if (!daysStr.isEmpty()) {
                        try {
                            int days = Integer.parseInt(daysStr);
                            if (days > 0 && days <= 30) {
                                boolean result = dbHelper.extendBorrowPeriod(currentBook.getId(), days);
                                
                                if (result) {
                                    Toast.makeText(this, "Gia hạn thành công " + days + " ngày!", Toast.LENGTH_SHORT).show();
                                    loadBorrowInfo();
                                } else {
                                    Toast.makeText(this, "Gia hạn thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Số ngày gia hạn phải từ 1-30 ngày!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập số ngày gia hạn!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}