package com.midterm.vothuthao;

public class Book {
    public static final String STATUS_AVAILABLE = "Có sẵn";
    public static final String STATUS_BORROWED = "Đang mượn";
    public static final String STATUS_RETURNED = "Đã trả"; // Chỉ dùng cho lịch sử, không dùng cho trạng thái sách

    private int id;
    private String title;
    private String author;
    private int publishYear;
    private int pageCount;
    private String genre;
    private String status;
    private String coverImage; // URL hoặc path của ảnh bìa

    public Book() {
    }

    public Book(String title, String author, int publishYear, int pageCount, String genre, String status) {
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.pageCount = pageCount;
        this.genre = genre;
        this.status = status;
    }

    public Book(int id, String title, String author, int publishYear, int pageCount, String genre, String status, String coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.pageCount = pageCount;
        this.genre = genre;
        this.status = status;
        this.coverImage = coverImage;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}