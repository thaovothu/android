package com.midterm.vothuthao;

public class BorrowHistory {
    private int id;
    private int bookId;
    private int userId;
    private String borrowDate;
    private String returnDate;
    private String dueDate;
    private int extensionDays;
    private String status; // "BORROWED", "RETURNED", "OVERDUE"

    public BorrowHistory() {
    }

    public BorrowHistory(int bookId, int userId, String borrowDate, String dueDate, String status) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
        this.extensionDays = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getExtensionDays() {
        return extensionDays;
    }

    public void setExtensionDays(int extensionDays) {
        this.extensionDays = extensionDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}