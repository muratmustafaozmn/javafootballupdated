package org.example.football;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController {

    @FXML
    private TableView<Match> matchesTable;

    @FXML
    private TableColumn<Match, String> homeTeamColumn;

    @FXML
    private TableColumn<Match, String> awayTeamColumn;

    @FXML
    private TableColumn<Match, String> dateColumn;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        homeTeamColumn.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
        awayTeamColumn.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @FXML
    public void fetchMatches() {
        statusLabel.setText("Fetching matches...");
        // Run API call in a separate thread to prevent UI blocking
        new Thread(() -> {
            try {
                List<Match> matches = fetchMatchesFromAPI();
                Platform.runLater(() -> {
                    matchesTable.getItems().setAll(matches);
                    statusLabel.setText("Matches loaded successfully.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> statusLabel.setText("Failed to fetch matches."));
            }
        }).start();
    }

    private List<Match> fetchMatchesFromAPI() throws Exception {
        String url = "https://api.football-data.org/v4/matches";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("X-Auth-Token", "7c58047dcdc245bb8c9d6165090cc4f1");
        connection.setRequestMethod("GET");

        // Check for successful response code or throw error
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to fetch matches: HTTP error code : " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return parseMatches(response.toString());
    }

    private List<Match> parseMatches(String json) {
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(json, ApiResponse.class);
        List<Match> matches = new ArrayList<>();
        for (MatchData matchData : apiResponse.getMatches()) {
            String homeTeamName = matchData.getHomeTeam().getName();
            String awayTeamName = matchData.getAwayTeam().getName();
            String date = formatDate(matchData.getUtcDate());
            matches.add(new Match(homeTeamName, awayTeamName, date));
        }
        return matches;
    }

    private String formatDate(String utcDate) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime zonedDateTime = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
            return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            e.printStackTrace();
            return utcDate;
        }
    }

    @FXML
    public void clearFields() {
        if (matchesTable != null) {
            matchesTable.getItems().clear();
            statusLabel.setText("Table cleared.");
        }
    }

    @FXML
    public void exitApplication() {
        Platform.exit();
    }

    // Inner classes for parsing JSON
    private class ApiResponse {
        private List<MatchData> matches;

        public List<MatchData> getMatches() {
            return matches;
        }
    }

    private class MatchData {
        private Team homeTeam;
        private Team awayTeam;
        private String utcDate;

        public Team getHomeTeam() {
            return homeTeam;
        }

        public Team getAwayTeam() {
            return awayTeam;
        }

        public String getUtcDate() {
            return utcDate;
        }
    }

    private class Team {
        private String name;

        public String getName() {
            return name;
        }
    }
}