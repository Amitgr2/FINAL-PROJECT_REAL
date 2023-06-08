package com.example.finalprojectamit.models;

import java.util.ArrayList;
import java.util.HashMap;

public class UserData {

    private String uid;
    private String userName;
    private int height;
    private double age;
    private String url;
    private ArrayList<Game> games;

    private final String UID_KEY = "uid";
    private final String USER_NAME_KEY = "userName";
    private final String HEIGHT_KEY = "height";
    private final String AGE_KEY = "age";
    private final String URL_KEY = "url";
    private final String GAMES_KEY = "games";
    private final String SUB_GAMES_KEY = "g";

    public UserData(String uid, String userName, int age, int height, String url) { // setting the user data with a profile picture
        this.uid = uid;
        this.userName = userName;
        this.age = age;
        this.height = height;
        this.url = url;
        this.games = new ArrayList<>();
    }

    public UserData(String uid, String userName, int age, int height) { // setting the user data without a profile picture
        this.uid = uid;
        this.userName = userName;
        this.age = age;
        this.height = height;
        this.url = "";
        this.games = new ArrayList<>();
    }

    public HashMap<String, Object> toMap() { //adding a new game to the games hashmap
        HashMap<String, Object> map = new HashMap<>();
        map.put(UID_KEY, this.uid);
        map.put(USER_NAME_KEY, this.userName);

        map.put(HEIGHT_KEY, this.height);
        map.put(AGE_KEY, this.age);
        map.put(URL_KEY, this.url);

        HashMap<String, Object> mapGames = new HashMap<>();
        for (int i = 0; i < games.size(); i++) // adding all the values
            mapGames.put(SUB_GAMES_KEY + i, games.get(i).toMap());

        map.put(GAMES_KEY, mapGames); // adding the new game hashmap to the hashmap of all the games

        return map;
    }

    public UserData(HashMap<String, Object> map) { // gets the user data from the hashmap
        this.uid = String.valueOf(map.get(UID_KEY));
        this.userName = String.valueOf(map.get(USER_NAME_KEY));
        this.height = Integer.parseInt(String.valueOf(map.get(HEIGHT_KEY)));
        this.age = Integer.parseInt(String.valueOf(map.get(AGE_KEY)));
        this.url = String.valueOf(map.get(URL_KEY));

        this.games = new ArrayList<>();
        if (map.get(GAMES_KEY) != null) {
            int i = 0;
            HashMap<String, Object> mapGames = (HashMap<String, Object>) map.get(GAMES_KEY);
            while (mapGames.get(SUB_GAMES_KEY + i) != null) { // adding a new game
                this.games.add(new Game((HashMap<String, Object>) mapGames.get(SUB_GAMES_KEY + i)));
                i++;
            }
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Game> getGamesByCity(String city) { // new array with all the games with the same city
        ArrayList<Game> filterGames = new ArrayList<>();
        for(Game g: this.games) { //moves over all the games
            if(g.getCity().contains(city.trim())) // filtering the games by city
                filterGames.add(g); // adds the game to a new array
        }
        return filterGames;
    }

    public ArrayList<Game> getGamesByDate(String date) { // new array with all the games with the same date
        ArrayList<Game> filterGames = new ArrayList<>();
        for(Game g: this.games) { //moves over all the games
            if(g.getDate().equals(date)) // filtering the games by city
                filterGames.add(g); // adds the game to a new array
        }
        return filterGames;
    }


    public void addGame(int threePoints, int twoPoints, int blocks, int assists, int ballsLoose, int foul, String city) { // adding a new game
        this.games.add(new Game(threePoints, twoPoints, blocks, assists, ballsLoose, foul, city));
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getThreePointsAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getThreePoints();
        }
        return (double)(sum/this.games.size());
    }

    public double getTwoPointsAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getTwoPoints();
        }
        return (double)(sum/this.games.size());
    }

    public double getFoulAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getFoul();
        }
        return (double)(sum/this.games.size());
    }

    public double getAssistsAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getAssists();
        }
        return (double)(sum/this.games.size());
    }

    public double getBallsLooseAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getBallsLoose();
        }
        return (double)(sum/this.games.size());
    }

    public double getBlocksAv() { // calculating the average
        if(games.size()==0) return 0;
        int sum = 0;
        for(Game game : this.games) {
            sum += game.getBlocks();
        }
        return (double)(sum/this.games.size());
    }

    public int getHeight() {
        return height;
    }

    public double getAge() {
        return age;
    }

    public String getUserName() {
        return userName;
    }
}
