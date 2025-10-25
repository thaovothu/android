package com.midterm.vothuthao;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("Tác giả: " + book.getAuthor());
        holder.tvStatus.setText("Tình trạng: " + book.getStatus());
        
        // Set màu cho trạng thái
        switch (book.getStatus()) {
            case Book.STATUS_AVAILABLE:
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case Book.STATUS_BORROWED:
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                break;
            default:
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                break;
        }
        
        // Hiển thị ảnh bìa từ drawable
        if (book.getCoverImage() != null && !book.getCoverImage().isEmpty()) {
            // Lấy resource ID từ tên drawable
            int resourceId = context.getResources().getIdentifier(
                book.getCoverImage(), 
                "drawable", 
                context.getPackageName()
            );
            if (resourceId != 0) {
                holder.ivCover.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy resource, dùng ảnh mặc định
                holder.ivCover.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            // Hiển thị ảnh mặc định nếu không có cover_image
            holder.ivCover.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBookList(List<Book> newBookList) {
        this.bookList = newBookList;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvStatus;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvStatus = itemView.findViewById(R.id.tv_book_status);
        }
    }
}