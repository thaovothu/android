package com.midterm.vothuthao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LibraryDB";
    private static final int DATABASE_VERSION = 5;
    
    // Books table
    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_BOOK_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_PUBLISH_YEAR = "publish_year";
    private static final String COLUMN_PAGE_COUNT = "page_count";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_COVER_IMAGE = "cover_image";
    
    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    
    // Borrow History table
    private static final String TABLE_BORROW_HISTORY = "borrow_history";
    private static final String COLUMN_HISTORY_ID = "id";
    private static final String COLUMN_HISTORY_BOOK_ID = "book_id";
    private static final String COLUMN_HISTORY_USER_ID = "user_id";
    private static final String COLUMN_BORROW_DATE = "borrow_date";
    private static final String COLUMN_RETURN_DATE = "return_date";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_EXTENSION_DAYS = "extension_days";
    private static final String COLUMN_HISTORY_STATUS = "status";
    
    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + "("
            + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_AUTHOR + " TEXT NOT NULL,"
            + COLUMN_PUBLISH_YEAR + " INTEGER NOT NULL,"
            + COLUMN_PAGE_COUNT + " INTEGER NOT NULL,"
            + COLUMN_GENRE + " TEXT NOT NULL,"
            + COLUMN_STATUS + " TEXT NOT NULL,"
            + COLUMN_COVER_IMAGE + " TEXT"
            + ")";
            
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT NOT NULL,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PHONE + " TEXT"
            + ")";
            
    private static final String CREATE_TABLE_BORROW_HISTORY = "CREATE TABLE " + TABLE_BORROW_HISTORY + "("
            + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_HISTORY_BOOK_ID + " INTEGER NOT NULL,"
            + COLUMN_HISTORY_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_BORROW_DATE + " TEXT NOT NULL,"
            + COLUMN_RETURN_DATE + " TEXT,"
            + COLUMN_DUE_DATE + " TEXT NOT NULL,"
            + COLUMN_EXTENSION_DAYS + " INTEGER DEFAULT 0,"
            + COLUMN_HISTORY_STATUS + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + COLUMN_HISTORY_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + "),"
            + "FOREIGN KEY(" + COLUMN_HISTORY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BORROW_HISTORY);
        
        // Thêm dữ liệu mẫu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROW_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Thêm sách mẫu
        ContentValues bookValues = new ContentValues();
        
        bookValues.put(COLUMN_TITLE, "Đắc Nhân Tâm");
        bookValues.put(COLUMN_AUTHOR, "Dale Carnegie");
        bookValues.put(COLUMN_PUBLISH_YEAR, 1936);
        bookValues.put(COLUMN_PAGE_COUNT, 320);
        bookValues.put(COLUMN_GENRE, "Phát triển bản thân");
        bookValues.put(COLUMN_STATUS, Book.STATUS_AVAILABLE);
        bookValues.put(COLUMN_COVER_IMAGE, "dac_nhan_tam");
        db.insert(TABLE_BOOKS, null, bookValues);
        
        bookValues.clear();
        bookValues.put(COLUMN_TITLE, "Tôi Thấy Hoa Vàng Trên Cỏ Xanh");
        bookValues.put(COLUMN_AUTHOR, "Nguyễn Nhật Ánh");
        bookValues.put(COLUMN_PUBLISH_YEAR, 2010);
        bookValues.put(COLUMN_PAGE_COUNT, 280);
        bookValues.put(COLUMN_GENRE, "Văn học");
        bookValues.put(COLUMN_STATUS, Book.STATUS_AVAILABLE);
        bookValues.put(COLUMN_COVER_IMAGE, "hoa_vang");
        db.insert(TABLE_BOOKS, null, bookValues);
        
        bookValues.clear();
        bookValues.put(COLUMN_TITLE, "Clean Code");
        bookValues.put(COLUMN_AUTHOR, "Robert C. Martin");
        bookValues.put(COLUMN_PUBLISH_YEAR, 2008);
        bookValues.put(COLUMN_PAGE_COUNT, 464);
        bookValues.put(COLUMN_GENRE, "Lập trình");
        bookValues.put(COLUMN_STATUS, Book.STATUS_AVAILABLE);
        bookValues.put(COLUMN_COVER_IMAGE, "clean_code");
        db.insert(TABLE_BOOKS, null, bookValues);
        
        // Thêm user mẫu
        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USER_NAME, "Nguyễn Văn A");
        userValues.put(COLUMN_EMAIL, "vana@email.com");
        userValues.put(COLUMN_PHONE, "0123456789");
        db.insert(TABLE_USERS, null, userValues);
    }

    // === BOOK METHODS ===
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_PUBLISH_YEAR, book.getPublishYear());
        values.put(COLUMN_PAGE_COUNT, book.getPageCount());
        values.put(COLUMN_GENRE, book.getGenre());
        values.put(COLUMN_STATUS, book.getStatus());
        values.put(COLUMN_COVER_IMAGE, book.getCoverImage());
        
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return id;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS + " ORDER BY " + COLUMN_BOOK_ID + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)));
                book.setPublishYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PUBLISH_YEAR)));
                book.setPageCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGE_COUNT)));
                book.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)));
                book.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                book.setCoverImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COVER_IMAGE)));
                
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return bookList;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, 
                null,
                COLUMN_BOOK_ID + "=?", 
                new String[]{String.valueOf(id)}, 
                null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book();
            book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)));
            book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)));
            book.setPublishYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PUBLISH_YEAR)));
            book.setPageCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGE_COUNT)));
            book.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)));
            book.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
            book.setCoverImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COVER_IMAGE)));
            
            cursor.close();
            db.close();
            return book;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public boolean updateBookStatus(int bookId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        
        int rowsAffected = db.update(TABLE_BOOKS, values, COLUMN_BOOK_ID + "=?", 
                new String[]{String.valueOf(bookId)});
        db.close();
        return rowsAffected > 0;
    }

    // === BORROW HISTORY METHODS ===
    public long borrowBook(int bookId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Tạo ngày mượn và ngày phải trả (14 ngày sau)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String borrowDate = sdf.format(new Date());
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        String dueDate = sdf.format(calendar.getTime());
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_HISTORY_BOOK_ID, bookId);
        values.put(COLUMN_HISTORY_USER_ID, userId);
        values.put(COLUMN_BORROW_DATE, borrowDate);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_HISTORY_STATUS, "BORROWED");
        values.put(COLUMN_EXTENSION_DAYS, 0);
        
        long id = db.insert(TABLE_BORROW_HISTORY, null, values);
        
        // Cập nhật trạng thái sách
        if (id > 0) {
            updateBookStatus(bookId, Book.STATUS_BORROWED);
        }
        
        db.close();
        return id;
    }

    public boolean returnBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String returnDate = sdf.format(new Date());
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_RETURN_DATE, returnDate);
        values.put(COLUMN_HISTORY_STATUS, "RETURNED");
        
        int rowsAffected = db.update(TABLE_BORROW_HISTORY, values, 
                COLUMN_HISTORY_BOOK_ID + "=? AND " + COLUMN_HISTORY_STATUS + "=?",
                new String[]{String.valueOf(bookId), "BORROWED"});
        
        // Cập nhật trạng thái sách về "Có sẵn" để có thể mượn lại
        if (rowsAffected > 0) {
            updateBookStatus(bookId, Book.STATUS_AVAILABLE);
        }
        
        db.close();
        return rowsAffected > 0;
    }

    public boolean extendBorrowPeriod(int bookId, int additionalDays) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Lấy thông tin mượn hiện tại
        Cursor cursor = db.query(TABLE_BORROW_HISTORY,
                new String[]{COLUMN_DUE_DATE, COLUMN_EXTENSION_DAYS},
                COLUMN_HISTORY_BOOK_ID + "=? AND " + COLUMN_HISTORY_STATUS + "=?",
                new String[]{String.valueOf(bookId), "BORROWED"},
                null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            String currentDueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE));
            int currentExtensionDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTENSION_DAYS));
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dueDate = sdf.parse(currentDueDate);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dueDate);
                calendar.add(Calendar.DAY_OF_MONTH, additionalDays);
                
                String newDueDate = sdf.format(calendar.getTime());
                int newExtensionDays = currentExtensionDays + additionalDays;
                
                ContentValues values = new ContentValues();
                values.put(COLUMN_DUE_DATE, newDueDate);
                values.put(COLUMN_EXTENSION_DAYS, newExtensionDays);
                
                int rowsAffected = db.update(TABLE_BORROW_HISTORY, values,
                        COLUMN_HISTORY_BOOK_ID + "=? AND " + COLUMN_HISTORY_STATUS + "=?",
                        new String[]{String.valueOf(bookId), "BORROWED"});
                
                cursor.close();
                db.close();
                return rowsAffected > 0;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public List<BorrowHistory> getBorrowHistory() {
        List<BorrowHistory> historyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BORROW_HISTORY + " ORDER BY " + COLUMN_HISTORY_ID + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                BorrowHistory history = new BorrowHistory();
                history.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_ID)));
                history.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_BOOK_ID)));
                history.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_USER_ID)));
                history.setBorrowDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_DATE)));
                history.setReturnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RETURN_DATE)));
                history.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)));
                history.setExtensionDays(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTENSION_DAYS)));
                history.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_STATUS)));
                
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return historyList;
    }

    public BorrowHistory getCurrentBorrowInfo(int bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BORROW_HISTORY,
                null,
                COLUMN_HISTORY_BOOK_ID + "=? AND " + COLUMN_HISTORY_STATUS + "=?",
                new String[]{String.valueOf(bookId), "BORROWED"},
                null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            BorrowHistory history = new BorrowHistory();
            history.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_ID)));
            history.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_BOOK_ID)));
            history.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_USER_ID)));
            history.setBorrowDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_DATE)));
            history.setReturnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RETURN_DATE)));
            history.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)));
            history.setExtensionDays(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTENSION_DAYS)));
            history.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_STATUS)));
            
            cursor.close();
            db.close();
            return history;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
}