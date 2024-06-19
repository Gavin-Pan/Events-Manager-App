package com.example.a1_gavinpan_fit2081;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.a1_gavinpan_fit2081.provider.Category;
import com.example.a1_gavinpan_fit2081.provider.EmaViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class NewEventCategory extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Switch isActive;
    ArrayList<Category> listCategories = new ArrayList<>();
    MyCategoryRecyclerAdapter recyclerAdapter;

    Gson gson = new Gson();

    EditText etCategoryName, etCategoryId,etEventCount, etLocation;

    private EmaViewModel emaViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        etCategoryId = findViewById(R.id.etCategoryId);
        etCategoryName = findViewById(R.id.etCategoryName);
        etEventCount = findViewById(R.id.etEventCount);
        isActive = findViewById(R.id.isActive);
        etLocation = findViewById(R.id.etLocation);

        // initialise ViewModel
        emaViewModel = new ViewModelProvider(this).get(EmaViewModel.class);


        recyclerAdapter = new MyCategoryRecyclerAdapter();


        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        /*
         * Register the broadcast handler with the intent filter that is declared in

         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.CATEGORY_SMS_FILTER), RECEIVER_EXPORTED);

    }


    public void onSaveCategoryClick(View v){
        String randomCategoryID = generateCategoryId();
        String categoryName = etCategoryName.getText().toString();
        String eventCount = etEventCount.getText().toString();
        String eventLocation = etLocation.getText().toString();

        if (eventCount.isEmpty()) {
            eventCount = "0";
        }

        if (validateCategory(categoryName,eventCount) && validateCategoryName(categoryName)) {
            int eventCountInt = Integer.parseInt(eventCount);
            if (eventCountInt < 0){
                eventCountInt = 0;
            }
            boolean bolActive = isActive.isChecked();
            // save data to SharedPreferences
            saveDataToSharedPreference(randomCategoryID, categoryName, eventCountInt, bolActive, eventLocation);
            etCategoryId.setText(randomCategoryID); //set the text for category ID
            Toast.makeText(NewEventCategory.this, "Category saved successfully: " + randomCategoryID, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }
    }

    public boolean validateCategory(String categoryName, String eventCount) {
        // We only need to check categoryName and eventCount for when we mannually input our category details
        // This is because category ID is already auto generated and we merely use a switch to check if its active or not.
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter all category details.",Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            int parsedEventCount = Integer.parseInt(eventCount);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Event count must be a valid number.", Toast.LENGTH_LONG).show();
            return false;
        }

        int parsedEventCount = Integer.parseInt(eventCount);
        if (parsedEventCount < 0) { // Check if the entered eventCount is above 0
            Toast.makeText(this, "Event count cannot be negative.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }



    public boolean validateCategoryName(String categoryName){
        String trimmedCategoryName = categoryName.trim();

        for (char c: trimmedCategoryName.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                Toast.makeText(this, "Invalid category name",Toast.LENGTH_LONG).show();
                return false; // disallow non-alphanumeric characters
            }

        }

        boolean hasLetters = false;
        boolean hasDigits = false;
        for (int i = 0; i < trimmedCategoryName.length(); i++) {
            char ch = trimmedCategoryName.charAt(i);
            if (Character.isLetter(ch)) {
                hasLetters = true;
            } else if (Character.isDigit(ch)) {
                hasDigits = true;
            }
        }

        if (!hasLetters && hasDigits) {
            Toast.makeText(this, "Invalid category name",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void saveDataToSharedPreference(String categoryId, String categoryName, int eventCount, boolean isActive, String eventLocation) {
        // initialise shared preference class variable to access Android's persistent storage
        //SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.CATEGORY_FILE_NAME, MODE_PRIVATE);
        // use .edit function to access file using Editor variable
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
//        editor.putString(KeyStore.KEY_CATEGORY_ID, categoryId);
//        editor.putString(KeyStore.KEY_CATEGORY_NAME, categoryName);
//        editor.putInt(KeyStore.KEY_EVENT_COUNT, eventCount);
//        editor.putBoolean(KeyStore.IS_CATEGORY_ACTIVE, isActive);

        //String readArrayListString = sharedPreferences.getString("data_key", "[]");

        //Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        //listCategories = gson.fromJson(readArrayListString,type);

        Category nCategory = new Category(categoryId,categoryName, eventCount, isActive, eventLocation);

        // insert new record to database
        emaViewModel.insertCats(nCategory);

        //listCategories.add(nCategory);
        // no need of notifyDataSetChanged here as it will be done by LiveData subscription above
        //recyclerAdapter.notifyDataSetChanged();

//        String arrayListString = gson.toJson(listCategories);
//        editor.putString("data_key", arrayListString);
//        editor.apply();

    }


    private String generateCategoryId() {
        Random random = new Random();
        StringBuilder categoryId = new StringBuilder();
        categoryId.append("C");
        categoryId.append((char) (random.nextInt(26) + 'A')); // casts the result to a char
        categoryId.append((char) (random.nextInt(26) + 'A'));
        categoryId.append("-");
        for (int i = 0; i < 4; i++) {
            categoryId.append(random.nextInt(10));
        }
        return categoryId.toString();
    }


    class MyBroadCastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            myStringTokenizer(msg);
        }

        public void myStringTokenizer(String msg) {
            /*
             * String Tokenizer is used to parse the incoming message
             * The SMS starts with 'category' followed by a colon ':', followed by the three parameters of the category separated by semicolons ';'. When such SMS is received,
             * the app should extract its values and place them in the correct fields.
             * */

            try {
                // First tokenizer to split by colon
                StringTokenizer categoryTokenizer = new StringTokenizer(msg, ":");

                // Check if the first token is 'category' (case-insensitive)
                String firstToken = categoryTokenizer.nextToken().toLowerCase();
                if (!firstToken.equals("category")) {
                    Toast.makeText(NewEventCategory.this, "Unrecognized message format. Expected 'category' at the beginning.", Toast.LENGTH_SHORT).show();
                }

                // Second tokenizer to split by semicolons
                String categoryDetails = categoryTokenizer.nextToken();
                StringTokenizer detailsTokenizer = new StringTokenizer(categoryDetails, ";");

                // Ensure at least 3 tokens exist
                if (detailsTokenizer.countTokens() < 3) {
                    Toast.makeText(NewEventCategory.this, "Incomplete message. Missing parameters.", Toast.LENGTH_SHORT).show();
                }

                String categoryName = detailsTokenizer.nextToken();
                int eventCountInteger = Integer.parseInt(detailsTokenizer.nextToken());
                String isActiveStr = detailsTokenizer.nextToken();

                // validate is Active (must be TRUE or FALSE)
                if (!isActiveStr.equalsIgnoreCase("TRUE") && !isActiveStr.equalsIgnoreCase("FALSE")) {
                    Toast.makeText(NewEventCategory.this, "Invalid. Ignoring caps, need true or false for active.", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isActiveBool = isActiveStr.equalsIgnoreCase("TRUE");

                etCategoryName.setText(categoryName);
                etEventCount.setText(String.valueOf(eventCountInteger));
                isActive.setChecked(isActiveBool);

            } catch (Exception e) {
                Toast.makeText(NewEventCategory.this, "Invalid unable to read category!", Toast.LENGTH_SHORT).show();
            }

        }



    }
}