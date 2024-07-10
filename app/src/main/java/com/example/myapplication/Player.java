package com.example.myapplication;

public class Player {
    private int id;
    private String nickname;
    private int wins, lose, draw, points;

    // Constructors, getters, and setters
    public Player(int id, String nickname, int wins, int lose, int draw, int points) {
        this.id = id;
        this.nickname = nickname;
        this.wins= wins;
        this.lose= lose;
        this.draw= draw;
        this.points= points;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getWins() {
        return wins;
    }

    public int getLose() {
        return lose;
    }

    public int getDraw() {
        return draw;
    }

    public int getPoints() {
        return points;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
