package com.example.a1_gavinpan_fit2081.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmaDAO {
    // // Specifies a database query to retrieve all items from the "items" table. (referenced A.3.4)
    @Query("select * from EventCategory")
    LiveData<List<Category>> getAllCategories(); // Returns a LiveData object containing a list of Item objects.

    // Indicates that this method is used to insert data into the database.
    @Insert
    void addCategory(Category category); // Method signature for inserting a category object into the database.

    @Query("delete from EventCategory")
    void deleteAllCategory();
    @Query("delete from EventCategory where categoryId=:id")
    void deleteCategory(String id);

    @Query("select * from EventCategory where categoryId=:id")
    List<Category> getCategory(String id);

    @Query("select * from Event")
    LiveData<List<Event>> getAllEvents();
    @Query("DELETE FROM Event")
    void deleteAllEvents();
    @Insert
    void addEvent(Event event);



}
