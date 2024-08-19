package com.example.food4you.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.example.food4you.Models.Foods;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class TinyDB {
    private static volatile TinyDB instance = null;
    private static final String SP_FILE ="SP_FILE";
    private SharedPreferences preferences;

    private TinyDB(Context appContext) {
        preferences = appContext.getSharedPreferences(SP_FILE , Context.MODE_PRIVATE);
    }

    public static synchronized TinyDB getInstance(Context context) {
        if (instance == null) {
            instance = new TinyDB(context.getApplicationContext()); // Use application context to prevent leaks
        }
        return instance;
    }

    public static TinyDB getInstance(){
        return instance;
    }

    public static TinyDB init(Context context){
        if (instance == null){
            synchronized (TinyDB.class){
                if (instance == null){
                    instance = new TinyDB(context);
                }
            }
        }
        return getInstance();
    }


    public void remove(String key) {
        checkForNullKey(key);
        preferences.edit().remove(key).apply();
    }

    // Getters
    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public ArrayList<Foods> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Foods> playerList =  new ArrayList<Foods>();

        for(String jObjString : objStrings){
            Foods player  = gson.fromJson(jObjString,  Foods.class);
            playerList.add(player);
        }
        return playerList;
    }

    public ArrayList<Integer> getIntList(String key) {
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
        ArrayList<Integer> intList = new ArrayList<>();
        for (String str : stringList) {
            if (!str.isEmpty()) {
                intList.add(Integer.parseInt(str));
            }
        }
        return intList;
    }

    public ArrayList<ArrayList<Foods>> getListOfLists(String key) {
        ArrayList<String> jsonListOfLists = getListString(key); // Retrieve the list of JSON strings
        Gson gson = new Gson();

        ArrayList<ArrayList<Foods>> listOfLists = new ArrayList<>();

        for (String jsonSublist : jsonListOfLists) {
            Type listType = new TypeToken<ArrayList<Foods>>() {}.getType();
            ArrayList<Foods> sublist = gson.fromJson(jsonSublist, listType);
            listOfLists.add(sublist);
        }
        return listOfLists;
    }

    // Put methods

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void putListObject(String key, ArrayList<Foods> playerList){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Foods player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

    public void putListOfLists(String key, ArrayList<ArrayList<Foods>> newListOfLists, int totalPrice) {
        checkForNullKey(key); // Ensure the key is not null
        Gson gson = new Gson();

        // Read existing data
        ArrayList<ArrayList<Foods>> existingListOfLists = getListOfLists(key);

        // Merge new data with existing data
        existingListOfLists.addAll(newListOfLists);

        // Convert the merged list of lists to JSON strings
        ArrayList<String> jsonListOfLists = new ArrayList<>();
        for (ArrayList<Foods> sublist : existingListOfLists) {
            String jsonSublist = gson.toJson(sublist);
            jsonListOfLists.add(jsonSublist);
        }

        // Store the list of JSON strings
        putListString(key, jsonListOfLists);

        // Save the totalPrice associated with this list
        ArrayList<Integer> totalPrices = TinyDB.getInstance().getIntList("TotalPrice");

        // Add the new totalPrice to the list
        totalPrices.add(totalPrice);

        // Store the updated list of total prices
        TinyDB.getInstance().putIntList("TotalPrice", totalPrices);
    }

    public void putIntList(String key, ArrayList<Integer> intList) {
        checkForNullKey(key); // Ensure the key is not null
        String[] myIntList = new String[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            myIntList[i] = String.valueOf(intList.get(i));
        }
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply(); // Store as a single string
    }

    public void addIntToList(String key, int value) {
        checkForNullKey(key); // Ensure the key is not null
        ArrayList<Integer> intList = getIntList(key); // Retrieve the existing list
        // Add a check to avoid IndexOutOfBoundsException
        if (intList == null) {
            intList = new ArrayList<>(); // Initialize if it's null
        }
        intList.add(value); // Add the new integer to the list
        putIntList(key, intList); // Save the updated list back to TinyDB
    }

    public void putString(String key, String value) {
        checkForNullKey(key); checkForNullValue(value);
        preferences.edit().putString(key, value).apply();
    }

    private void checkForNullValue(String value){
        if (value == null){
            throw new NullPointerException();
        }
    }

    private void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
}