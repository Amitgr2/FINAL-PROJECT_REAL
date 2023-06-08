package com.example.finalprojectamit.models;

import java.util.HashMap;

public class Game {

    private double threePoints;
    private double twoPoints;
    private double foul;
    private double assists;
    private double ballsLoose;
    private double blocks;
    private String date;
    private String city;

    private final String THREE_POINTS_KEY = "threePoints";
    private final String TWO_POINTS_KEY = "twoPoints";
    private final String FOUL_KEY = "foul";
    private final String ASSISTS_KEY = "assists";
    private final String BALLS_LOOSE_KEY = "ballsLoose";
    private final String BLOCKS_KEY = "blocks";
    private final String DATE_KEY = "date";
    private final String CITY_KEY = "city";


    public Game(double threePoints, double twoPoints, double blocks, double assists, double ballsLoose, double foul, String city) { // making a new game
        this.threePoints = threePoints;
        this.twoPoints = twoPoints;
        this.foul = foul;
        this.assists = assists;
        this.ballsLoose = ballsLoose;
        this.blocks = blocks;
        this.date = getCurrentDate();
        this.city = city;
    }

    private String getCurrentDate() { // getting the current date
        return android.text.format.DateFormat.format("yyyy/MM/dd", new java.util.Date()).toString(); // setting the format
    }

    public Game(HashMap<String, Object> map) { // setting the fields with the current values
        this.threePoints = Double.parseDouble(String.valueOf(map.get(THREE_POINTS_KEY)));
        this.twoPoints = Double.parseDouble(String.valueOf(map.get(TWO_POINTS_KEY)));
        this.assists = Double.parseDouble(String.valueOf(map.get(ASSISTS_KEY)));
        this.ballsLoose = Double.parseDouble(String.valueOf(map.get(BALLS_LOOSE_KEY)));
        this.blocks = Double.parseDouble(String.valueOf(map.get(BLOCKS_KEY)));
        this.date = String.valueOf(map.get(DATE_KEY));
        this.city = String.valueOf(map.get(CITY_KEY));
    }

    public HashMap<String, Object> toMap() { //creating an hashmap with all the game info
        HashMap<String, Object> map = new HashMap<>(); // creating a new hashmap
        map.put(THREE_POINTS_KEY,this.threePoints ); // putting a new value with its value_key
        map.put(TWO_POINTS_KEY,this.twoPoints); // putting a new value with its value_key
        map.put(FOUL_KEY,this.foul); // putting a new value with its value_key
        map.put(ASSISTS_KEY,this.assists); // putting a new value with its value_key
        map.put(BALLS_LOOSE_KEY,this.ballsLoose); // putting a new value with its value_key
        map.put(BLOCKS_KEY,this.blocks); // putting a new value with its value_key
        map.put(DATE_KEY,this.date); // putting a new value with its value_key
        map.put(CITY_KEY,this.city); // putting a new value with its value_key
        return map; //returning the hashmap
    }

    public double getThreePoints() {
        return threePoints;
    }

    public void setThreePoints(double threePoints) {
        this.threePoints = threePoints;
    }

    public double getTwoPoints() {
        return twoPoints;
    }

    public void setTwoPoints(double twoPoints) {
        this.twoPoints = twoPoints;
    }

    public double getFoul() {
        return foul;
    }

    public void setFoul(double foul) {
        this.foul = foul;
    }

    public double getAssists() {
        return assists;
    }

    public void setAssists(double assists) {
        this.assists = assists;
    }

    public double getBallsLoose() {
        return ballsLoose;
    }

    public void setBallsLoose(double ballsLoose) {
        this.ballsLoose = ballsLoose;
    }

    public double getBlocks() {
        return blocks;
    }

    public void setBlocks(double blocks) {
        this.blocks = blocks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
