package com.midterm.vothuthao;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<BorrowHistory> historyList;
    private DatabaseHelper dbHelper;
    private OnHistoryUpdateListener listener;

    public interface OnHistoryUpdateListener {
        void onHistoryUpdated();
    }

    public HistoryAdapter(Context context, List<BorrowHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
        this.dbHelper = new DatabaseHelper(context);
        if (context instanceof OnHistoryUpdateListener) {
            this.listener = (OnHistoryUpdateListener) context;
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BorrowHistory history = historyList.get(position);
        
        // Lấy thông tin sách
        Book book = dbHelper.getBookById(history.getBookId());
        if (book != null) {
            holder.tvBookTitle.setText(book.getTitle());
            holder.tvBookAuthor.setText("Tác giả: " + book.getAuthor());
        }
        
        holder.tvBorrowDate.setText("Ngày mượn: " + history.getBorrowDate());
        holder.tvDueDate.setText("Ngày phải trả: " + history.getDueDate());
        
        if (history.getReturnDate() != null && !history.getReturnDate().isEmpty()) {
            holder.tvReturnDate.setText("Ngày trả: " + history.getReturnDate());
            holder.tvReturnDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvReturnDate.setVisibility(View.GONE);
        }
        
        if (history.getExtensionDays() > 0) {
            holder.tvExtension.setText("Đã gia hạn: " + history.getExtensionDays() + " ngày");
            holder.tvExtension.setVisibility(View.VISIBLE);
        } else {
            holder.tvExtension.setVisibility(View.GONE);
        }
        
        holder.tvStatus.setText("Trạng thái: " + getStatusText(history.getStatus()));
        
        // Hiển thị các nút chỉ khi đang mượn
        if ("BORROWED".equals(history.getStatus())) {
            holder.layoutButtons.setVisibility(View.VISIBLE);
            holder.btnExtend.setOnClickListener(v -> showExtendDialog(history));
            holder.btnReturn.setOnClickListener(v -> showReturnDialog(history));
        } else {
            holder.layoutButtons.setVisibility(View.GONE);
        }
        
        // Set màu cho trạng thái
        switch (history.getStatus()) {
            case "BORROWED":
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "RETURNED":
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "OVERDUE":
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
        }
    }

    private void showExtendDialog(BorrowHistory history) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_extend, null);
        EditText etDays = dialogView.findViewById(R.id.et_extend_days);
        
        new AlertDialog.Builder(context)
                .setTitle("Gia hạn mượn sách")
                .setView(dialogView)
                .setPositiveButton("Gia hạn", (dialog, which) -> {
                    String daysStr = etDays.getText().toString().trim();
                    if (!daysStr.isEmpty()) {
                        try {
                            int days = Integer.parseInt(daysStr);
                            if (days > 0 && days <= 30) {
                                boolean result = dbHelper.extendBorrowPeriod(history.getBookId(), days);
                                
                                if (result) {
                                    Toast.makeText(context, "Gia hạn thành công " + days + " ngày!", Toast.LENGTH_SHORT).show();
                                    // Cập nhật lại danh sách
                                    if (listener != null) {
                                        listener.onHistoryUpdated();
                                    }
                                } else {
                                    Toast.makeText(context, "Gia hạn thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Số ngày gia hạn phải từ 1-30 ngày!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Vui lòng nhập số ngày gia hạn!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showReturnDialog(BorrowHistory history) {
        // Lấy thông tin sách để hiển thị trong dialog
        Book book = dbHelper.getBookById(history.getBookId());
        String bookTitle = book != null ? book.getTitle() : "Sách này";
        
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận trả sách")
                .setMessage("Bạn có chắc chắn muốn trả sách \"" + bookTitle + "\"?")
                .setPositiveButton("Trả sách", (dialog, which) -> {
                    boolean result = dbHelper.returnBook(history.getBookId());
                    
                    if (result) {
                        Toast.makeText(context, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        // Cập nhật lại danh sách
                        if (listener != null) {
                            listener.onHistoryUpdated();
                        }
                    } else {
                        Toast.makeText(context, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private String getStatusText(String status) {
        switch (status) {
            case "BORROWED":
                return "Đang mượn";
            case "RETURNED":
                return "Đã trả";
            case "OVERDUE":
                return "Quá hạn";
            default:
                return status;
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void updateHistoryList(List<BorrowHistory> newHistoryList) {
        this.historyList = newHistoryList;
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBookAuthor, tvBorrowDate, tvDueDate, tvReturnDate, tvExtension, tvStatus;
        Button btnExtend, btnReturn;
        View layoutButtons;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title_history);
            tvBookAuthor = itemView.findViewById(R.id.tv_book_author_history);
            tvBorrowDate = itemView.findViewById(R.id.tv_borrow_date);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            tvExtension = itemView.findViewById(R.id.tv_extension);
            tvStatus = itemView.findViewById(R.id.tv_status_history);
            btnExtend = itemView.findViewById(R.id.btn_extend_history);
            btnReturn = itemView.findViewById(R.id.btn_return_history);
            layoutButtons = itemView.findViewById(R.id.layout_buttons);
        }
    }
}