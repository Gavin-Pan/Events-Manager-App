package com.example.a1_gavinpan_fit2081.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EmaViewModel extends AndroidViewModel {
    // reference to CardRepository
    private EmaRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Category>> allCategoriesLiveData;

    private LiveData<List<Event>> allEventsLiveData;

    public EmaViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EmaRepository(application);

        // get all items by calling method defined in repository class
        allCategoriesLiveData = repository.getAllCategories();
        allEventsLiveData = repository.getAllEvents();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<Category>> getAllCategories() {
        return allCategoriesLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param category object containing details of new Item to be inserted
     */
    public void insertCats(Category category) {
        repository.insertCategory(category);
    }
    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public List<Category> getCategory(String id) {
        return repository.getCategory(id);
    }

    public void updateCategory(Category existingCategory, Category newCategory) {
        repository.deleteAndInsertCategory(existingCategory, newCategory);
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param event object containing details of new Item to be inserted
     */
    public void insertEvents(Event event) {
        repository.insertEvent(event);
    }

    public void deleteCat(String id) {
        repository.deleteIndividualCat(id);
    }
    public void deleteAllEvents() {
        repository.deleteAllEvents();
    }
}

