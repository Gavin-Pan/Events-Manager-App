package com.example.a1_gavinpan_fit2081;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1_gavinpan_fit2081.provider.Category;
import com.example.a1_gavinpan_fit2081.provider.EmaViewModel;
import com.example.a1_gavinpan_fit2081.provider.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Toolbar myToolbar;
    Switch isActive2;
    FloatingActionButton fab;
    EditText etEventId, etEventName, etCategoryId2, etTickets;


    ArrayList<Event> listEvents = new ArrayList<>();
    MyEventRecyclerAdapter recyclerAdapter;

    MyCategoryRecyclerAdapter categoryAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Gson gson = new Gson();
    private EmaViewModel emaViewModel;

    //TextView tvEventType;


    // help detect basic gestures like scroll, single tap, double tap, etc
    private GestureDetectorCompat mDetector;

    // help detect multi touch gesture like pinch-in, pinch-out specifically used for zoom functionality
    private ScaleGestureDetector mScaleDetector;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Assignment 2");

        // initialise ViewModel
        emaViewModel = new ViewModelProvider(this).get(EmaViewModel.class);


        getSupportFragmentManager().beginTransaction().replace(
                R.id.host_container_category_dash, new FragmentListCategory()).commit();

        drawerLayout = findViewById(R.id.drawer_layout);

        etEventId= findViewById(R.id.etEventId);
        etEventName= findViewById(R.id.etEventName);
        etCategoryId2= findViewById(R.id.etCategoryId2);
        etTickets= findViewById(R.id.etTickets);
        isActive2= findViewById(R.id.isActive2);


        // initialise new instance of CustomGestureDetector class
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();

// register GestureDetector and set listener as CustomGestureDetector
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        mDetector.setOnDoubleTapListener(customGestureDetector);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveEventClick(null);
            }
        });


        recyclerAdapter = new MyEventRecyclerAdapter();

        categoryAdapter = new MyCategoryRecyclerAdapter();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View touchpad = findViewById(R.id.touchpad);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass stream of events to GestureDetectorCompat, which translate events into gestures
                mDetector.onTouchEvent(event);
                // get the type of Motion Event detected which is represented by a pre-defined integer value
                int action = event.getAction();
                return true;
            }
        });
    }



    /**
     * A convenience class to extend when you only want to listen for a subset of all the gestures.
     */
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            //tvEventType.setText("onLongPress");
            clear_events();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            //tvEventType.setText("onDoubleTap");
            onSaveEventClick(null);
            return super.onDoubleTap(e);
        }

    }


    private String generateEventId() {
        Random random = new Random();
        StringBuilder eventId = new StringBuilder();
        eventId.append("E");
        eventId.append((char) (random.nextInt(26) + 'A'));
        eventId.append((char) (random.nextInt(26) + 'A'));
        eventId.append("-");
        for (int i = 0; i < 5; i++) {
            eventId.append(random.nextInt(10));
        }
        return eventId.toString();
    }

    public void onSaveEventClick(View v) {
        String randomEventID = generateEventId();
        String categoryID = etCategoryId2.getText().toString();
        String eventName = etEventName.getText().toString();
        String ticketsAvaliable = etTickets.getText().toString();
        boolean bolActive = isActive2.isChecked();

        int intTickets = 0;
        if (ticketsAvaliable.isEmpty()) {
            ticketsAvaliable = "0";
        } else {
                intTickets= Integer.parseInt(ticketsAvaliable);
            if (intTickets < 0) {
                Toast.makeText(this, "Tickets avaialble invalid ", Toast.LENGTH_SHORT).show();
                intTickets = 0;
            }
        }
        int fTickets = intTickets;


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler uiHandler=new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<Category> categoryList = emaViewModel.getCategory(categoryID);
            //Now to update the UI elements
            uiHandler.post(() -> {
                if (categoryList == null || categoryList.isEmpty()) {
                    Toast.makeText(this, "Category not found!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!eventName.isEmpty() && validateEventName(eventName)) {
                        Event newEvent = new Event(randomEventID, categoryID, eventName, fTickets, bolActive);
                        emaViewModel.insertEvents(newEvent);

                        for (Category c: categoryList) {
                            String categoryIDStr = etCategoryId2.getText().toString();
                            if (c.getCategoryId().equals(categoryIDStr)) {
                                Category cat = new Category(c.getCategoryId(), c.getCategoryName(), c.getEventCount() + 1, c.isCategoryActive(), c.getEventLocation());

                                emaViewModel.updateCategory(c,cat);
//                                emaViewModel.deleteCat(c.getCategoryId());
//                                emaViewModel.insertCats(cat);


                            }
                        }
                        etEventId.setText(randomEventID);
                        snackBarDisplay();
                        Toast.makeText(this, "Event saved: " + randomEventID + " to " + categoryID, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this,"Not a valid event name", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

    }

    private void eventCountDecrement(String categoryId){
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.CATEGORY_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String readArrayListString = sharedPreferences.getString("data_key", "[]");

        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        ArrayList<Category>listCategories = gson.fromJson(readArrayListString,type);

        for (Category category : listCategories) {
            if (category.getCategoryId().equals(categoryId)) {
                category.setEventCount(category.getEventCount() - 1);
                break;
            }
        }

        String arrListString = gson.toJson(listCategories);
        editor.putString("data_key", arrListString);
        editor.apply();

    }


    public boolean validateEventName(String eventName){
        String trimmedEventName = eventName.trim();

        for (char c: trimmedEventName.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ') {
                Toast.makeText(this, "Invalid event name",Toast.LENGTH_LONG).show();
                return false; // disallow non-alphanumeric characters
            }
        }

        boolean hasLetters = false;
        boolean hasDigits = false;
        for (int i = 0; i < trimmedEventName.length(); i++) {
            char ch = trimmedEventName.charAt(i);
            if (Character.isLetter(ch)) {
                hasLetters = true;
            } else if (Character.isDigit(ch)) {
                hasDigits = true;
            }
        }

        if (!hasLetters && hasDigits) {
            Toast.makeText(this, "Invalid event name",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // first param references to the menu resource file
        //second parameter is the menu where we would like the resource fil to be inflated
        getMenuInflater().inflate(R.menu.options_menu,menu);

        //return true so inform Android that our activity code is implementing the options menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // match the id of selected item and do something
        if (item.getItemId() == R.id.option_clear_event) {
            clear_events();

        } else if (item.getItemId() == R.id.option_refresh) {
            finish();
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.option_delete_categories) {
            emaViewModel.deleteAllCategories();
//            SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.CATEGORY_FILE_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();

        } else if (item.getItemId() == R.id.option_delete_events) {
            emaViewModel.deleteAllEvents();
//            SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.EVENT_FILE_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();

        }
        return true;
    }

    public void clear_events(){
        etEventId.setText("");
        etEventName.setText("");
        etCategoryId2.setText("");
        etTickets.setText("");
        isActive2.setChecked(false);
    }


    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.nav_view_categories) {
                onClickViewCategories(null);
            } else if (id == R.id.option_add_category) {
                onClickAddEventCategory(null);
            } else if (id == R.id.nav_view_events) {
                onClickListEvent(null);
            } else if (id == R.id.nav_logout) {
                finish();
                onClickLogout(null);
            }
            drawerLayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    public class MyListerToUndo implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.EVENT_FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String readArrayListString = sharedPreferences.getString("data_key", "[]");

            Type type = new TypeToken<ArrayList<Event>>() {}.getType();
            listEvents = gson.fromJson(readArrayListString,type);

            int lastEventIndex = listEvents.size() -1;
            Event eventLast = listEvents.get(lastEventIndex);
            eventCountDecrement(eventLast.getCategoryId());

            listEvents.remove(lastEventIndex);

            String arrListString = gson.toJson(listEvents);
            editor.putString("data_key", arrListString);
            editor.apply();

            recyclerAdapter.notifyDataSetChanged();

        }

    }

    private void snackBarDisplay() {
        Snackbar sb = Snackbar.make(drawerLayout, "Event Saved", Snackbar.LENGTH_LONG);
        sb.setAction("UNDO", new MyListerToUndo());
        sb.show();
    }

    public void onClickViewCategories(View view) {
        Intent intent = new Intent(this, ListCategory.class);
        startActivity(intent);
    }

    public void onClickAddEventCategory(View view) {
        Intent intent = new Intent(this, NewEventCategory.class);
        startActivity(intent);
    }


    public void onClickListEvent(View view) {
        Intent intent = new Intent(this, ListEvent.class);
        startActivity(intent);
    }

    public void onClickLogout(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


}


