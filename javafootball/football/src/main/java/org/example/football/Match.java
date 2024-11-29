package org.example.football;

public class Match {
    private String homeTeam;
    private String awayTeam;
    private String date;

    public Match(String homeTeam, String awayTeam, String date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getDate() {
        return date;
    }
}