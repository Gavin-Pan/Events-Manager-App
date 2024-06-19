package com.example.a1_gavinpan_fit2081;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1_gavinpan_fit2081.provider.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;

public class NewEvent extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Switch isActive2;

    EditText etEventId, etEventName, etCategoryId2, etTickets;
    ArrayList<Event> listEvents = new ArrayList<>();
    MyEventRecyclerAdapter recyclerAdapter;

    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        etEventId = findViewById(R.id.etEventId);
        etEventName = findViewById(R.id.etEventName);
        etCategoryId2 = findViewById(R.id.etCategoryId2);
        etTickets = findViewById(R.id.etTickets);
        isActive2 = findViewById(R.id.isActive2);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.EVENT_SMS_FILTER), RECEIVER_EXPORTED);
    }

    public void onSaveEventClick(View v) {
        String randomEventID = generateEventId();
        String categoryID = etCategoryId2.getText().toString();
        String eventName = etEventName.getText().toString();
        String ticketsAvaliable = etTickets.getText().toString();

        if (validateEvent(categoryID, eventName, ticketsAvaliable)) {
            int ticketsInteger = Integer.parseInt(ticketsAvaliable);
            boolean bolActive = isActive2.isChecked();
            // save data to SharedPreferences
            //saveDataToSharedPreference(randomEventID, categoryID, eventName, ticketsInteger, bolActive);
            etEventId.setText(randomEventID); //set the text for event ID
            Toast.makeText(this, "Event saved: " + randomEventID + " to " + categoryID, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateEvent(String categoryId, String eventName, String ticketsAvailable) {
        if (categoryId.isEmpty() || eventName.isEmpty()) {
            Toast.makeText(this, "Please enter all event details.", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            int ticketsAvailableInteger = Integer.parseInt(ticketsAvailable);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tickets available must be a valid number.", Toast.LENGTH_LONG).show();
            return false;
        }
        int parsedTicketsAvailable = Integer.parseInt(ticketsAvailable);
        if (parsedTicketsAvailable < 0) {
            Toast.makeText(this, "Tickets available cannot be negative.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

//    private void saveDataToSharedPreference(String eventId, String categoryID, String eventName, int ticketsAvaliable, boolean isActive) {
//        // initialise shared preference class variable to access Android's persistent storage
//        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.EVENT_FILE_NAME, MODE_PRIVATE);
//        // use .edit function to access file using Editor variable
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        String readArrayListString = sharedPreferences.getString("data_key", "[]");
//
//        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
//        listEvents = gson.fromJson(readArrayListString,type);
//
//        Event nEvent = new Event(eventId,categoryID, eventName, ticketsAvaliable,isActive);
//        listEvents.add(nEvent);
//        recyclerAdapter.notifyDataSetChanged();
//
//        String arrayListString = gson.toJson(listEvents);
//        editor.putString("data_key", arrayListString);
//        editor.apply();        // save key-value pairs to the shared preference file
//        editor.putString(KeyStore.KEY_EVENT_ID, eventId);
//        editor.putString(KeyStore.KEY_EVENT_CATEGORY_ID, categoryID);
//        editor.putString(KeyStore.KEY_EVENT_NAME, eventName);
//        editor.putInt(KeyStore.KEY_TICKETS, ticketsAvaliable);
//        editor.putBoolean(KeyStore.IS_EVENT_ACTIVE, isActive);

//    }

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


    class MyBroadCastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            myEventTokenizer(msg);
        }

        public void myEventTokenizer(String msg) {
            /*
             * String Tokenizer is used to parse the incoming message
             * The SMS starts with 'category' followed by a colon ':', followed by the 4 parameters of the category separated by semicolons ';'. When such SMS is received,
             * the app should extract its values and place them in the correct fields.
             * */

            try {
                // First tokenizer to split by colon (:)
                StringTokenizer msgTokenizer = new StringTokenizer(msg, ":");

                // Check if the first token is 'event'
                String eventID = msgTokenizer.nextToken();
                if (!eventID.equals("event")) {
                    Toast.makeText(NewEvent.this, "Unrecognized message format. Expected 'event' at the start.", Toast.LENGTH_LONG).show();
                }

                // Second tokenizer to split by semicolons
                String eventDetails = msgTokenizer.nextToken();
                StringTokenizer detailsTokenizer = new StringTokenizer(eventDetails, ";");

                // Ensure at least 4 tokens exist
                if (detailsTokenizer.countTokens() < 4) {
                    Toast.makeText(NewEvent.this, "Incomplete message. Missing parameters.", Toast.LENGTH_SHORT).show();
                }

                String eventName = detailsTokenizer.nextToken();
                String categoryId = detailsTokenizer.nextToken();
                String ticketsString = detailsTokenizer.nextToken();
                String isActiveString = detailsTokenizer.nextToken();

                // Validate isActive (must be TRUE or FALSE)
                if (!isActiveString.equalsIgnoreCase("TRUE") && !isActiveString.equalsIgnoreCase("FALSE")) {
                    Toast.makeText(NewEvent.this, "Please enter true of false. Ignoring caps lock.", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isActiveBool = isActiveString.equalsIgnoreCase("TRUE");

                // Convert tickets to integer (you might need error handling here)
                int ticketsAvailable = Integer.parseInt(ticketsString);

                etEventName.setText(eventName);
                etCategoryId2.setText(categoryId);
                etTickets.setText(ticketsString);
                isActive2.setChecked(isActiveBool);

            } catch (Exception e) {
                Toast.makeText(NewEvent.this, "Invalid unable to read event details!", Toast.LENGTH_SHORT).show();
            }
        }



    }
    }