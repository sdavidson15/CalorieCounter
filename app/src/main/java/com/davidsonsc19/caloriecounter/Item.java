package com.davidsonsc19.caloriecounter;

import java.util.Date;

public class Item {
    int itemID;
    String name;
    int calories;
    String time;
    Date timestamp; // this isn't used for anything. For now, just records the item creation time.

    public Item(String itemStr) {
        String[] fields = itemStr.split("@");

        this.itemID = Integer.parseInt(fields[0]);
        this.name = fields[1];
        this.calories = Integer.parseInt(fields[2]);
        this.time = fields[3];
        this.timestamp = new Date();
    }

    public Item(String name, int calories, String time, Date timestamp) {
        this.itemID = Util.getNextItemID();
        this.name = name;
        this.calories = calories;
        this.time = time;
        this.timestamp = timestamp;
    }

    @Override
    public String toString () {
        return this.itemID + "@"
                + this.name + "@"
                + String.valueOf(this.calories) + "@"
                + this.time + "@"
                + this.timestamp.toString();
    }
}
