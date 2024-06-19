package com.example.a1_gavinpan_fit2081.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EmaRepository {
    // private class variable to hold reference to DAO
    private EmaDAO emaDAO;
    // private class variable to temporary hold all the categoies retrieved and pass outside of this class
    private LiveData<List<Category>> allCategoriesLiveData;

    private LiveData<List<Event>> allEventsLiveData;

    // constructor to initialise the repository class
    EmaRepository(Application application) {
        // get reference/instance of the database
        EmaDatabase db = EmaDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        emaDAO = db.emaDAO();

        // once the class is initialised get all the categoies in the form of LiveData
        allCategoriesLiveData = emaDAO.getAllCategories();

        allEventsLiveData = emaDAO.getAllEvents();
    }


    /**
     * Repository method to get all cards
     * @return LiveData of type List<Category>
     */
    LiveData<List<Category>> getAllCategories() {
        return allCategoriesLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param category object containing details of new Category to be inserted
     */
    void insertCategory(Category category) {
        // Executes the database operation to insert the item in a background thread.
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.addCategory(category));
    }

    List<Category> getCategory(String id) {
        return emaDAO.getCategory(id);
    }

    void deleteAllCategories() {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllCategory());

    }

    public void deleteAndInsertCategory(Category existingCategory, Category newCategory) {
        EmaDatabase.databaseWriteExecutor.execute(() -> {
            emaDAO.deleteCategory(existingCategory.getCategoryId());
            emaDAO.addCategory(newCategory);
        });
    }

    void deleteIndividualCat(String id) {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteCategory(id));
    }

    LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Item to be inserted
     */
    void insertEvent(Event event) {
        // Executes the database operation to insert the item in a background thread.
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.addEvent(event));
    }



    void deleteAllEvents() {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllEvents());
    }


}