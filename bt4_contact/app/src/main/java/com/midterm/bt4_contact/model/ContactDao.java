package com.midterm.bt4_contact.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact ORDER BY name")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contact where name LIKE :s ORDER BY name")
    List<Contact> findByName(String s);

    @Query("SELECT * FROM contact where id = :id")
    List<Contact> findById(int id);

    @Insert
    void insertAll (Contact... contacts);

    @Update
    void updateAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
}
