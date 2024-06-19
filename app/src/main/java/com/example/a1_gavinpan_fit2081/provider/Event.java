package com.example.a1_gavinpan_fit2081.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Event")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "eventId")
    private String eventId;
    @ColumnInfo(name = "CategoryId")
    private String CategoryId;
    @ColumnInfo(name = "eventName")
    private String eventName;
    @ColumnInfo(name = "ticketsAvaliable")
    private Integer ticketsAvaliable;
    @ColumnInfo(name = "isEventActive")
    private boolean isEventActive;

    // Default constructor
    public Event() {
    }

    public Event(String eventId, String categoryId, String eventName, int ticketsAvaliable, boolean isEventActive) {
        this.eventId = eventId;
        CategoryId = categoryId;
        this.eventName = eventName;
        this.ticketsAvaliable = ticketsAvaliable;
        this.isEventActive = isEventActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getTicketsAvaliable() {
        return ticketsAvaliable;
    }

    public void setTicketsAvaliable(int ticketsAvaliable) {
        this.ticketsAvaliable = ticketsAvaliable;
    }

    public boolean isEventActive() {
        return isEventActive;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }
}
